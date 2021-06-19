package net.suncaper.tags.model

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.execution.datasources.hbase.HBaseTableCatalog
import org.apache.spark.sql.functions._

object RecentlyLoginModel {
  def main(args: Array[String]): Unit = {
    def catalog =
      s"""{
         |  "table":{"namespace":"default", "name":"tbl_logs"},
         |  "rowkey":"id",
         |  "columns":{
         |    "id":{"cf":"rowkey", "col":"id", "type":"string"},
         |    "global_user_id":{"cf":"cf", "col":"global_user_id", "type":"string"},
         |    "log_time":{"cf":"cf", "col":"log_time", "type":"string"}
         |  }
         |}""".stripMargin

    val spark = SparkSession.builder()
      .appName("shc test")
      .master("local[10]")
      .getOrCreate()

    import spark.implicits._


    val source: DataFrame = spark.read
      .option(HBaseTableCatalog.tableCatalog, catalog)
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .load()

    //2019-08-22 = =2021-6-14
    val days_range = 662

    // 2. 得到id和最近登录时间
    val daysDF: DataFrame = source
      .groupBy("global_user_id") //按照会员ID：global_user_id分组
      .agg(
        max("log_time").as("recently_login_time") //获取最近一次订单完成时间 recently_login_time
      )
      .filter("global_user_id<951") //过滤无效用户数据
      .select(
        $"global_user_id".as("id"),
        datediff(date_sub(current_date(), days_range), $"recently_login_time").as("recently_login_time")
      )
    //    daysDF.orderBy(daysDF("recently_login_time").desc).show(10,false)
    //    daysDF.printSchema()

    val result = daysDF.select('id,
      when('recently_login_time <= "1", "1天内")
        .when('recently_login_time <= "7", "7天内")
        .when('recently_login_time <= "14", "14天内")
        .when('recently_login_time <= "30", "30天内")
        .otherwise("其他")
        .as("recently_login_time")
    )
    //    result.orderBy(result("recently_login_time").asc).show(10,false)
    //    result.printSchema()


    def catalogWrite =
      s"""{
         |"table":{"namespace":"default", "name":"user_profile"},
         |"rowkey":"id",
         |"columns":{
         |  "id":{"cf":"rowkey", "col":"id", "type":"string"},
         |  "recently_login_time":{"cf":"cf", "col":"recently_login_time", "type":"string"}
         |}
         |}""".stripMargin


    result.write
      .option(HBaseTableCatalog.tableCatalog, catalogWrite)
      .option(HBaseTableCatalog.newTable, "5")
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .save()
  }
}