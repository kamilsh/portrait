package net.suncaper.tags.model

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.execution.datasources.hbase.HBaseTableCatalog
import org.apache.spark.sql.functions._

object ConsumeCycleModel {
  def main(args: Array[String]): Unit = {
    def catalog =
      s"""{
         |  "table":{"namespace":"default", "name":"tbl_orders"},
         |  "rowkey":"id",
         |  "columns":{
         |    "id":{"cf":"rowkey", "col":"id", "type":"string"},
         |    "memberId":{"cf":"cf", "col":"memberId", "type":"string"},
         |    "finishTime":{"cf":"cf", "col":"finishTime", "type":"string"}
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
    //    val colRencency = "rencency"
    //    val recencyCol = datediff(date_sub(current_timestamp(), days_range), from_unixtime(max('finishTime)))

    // 2. 订单数据按照会员ID：memberid分组，获取最近一次订单完成时间 finishtime
    val daysDF: DataFrame = source
      // 2.1. 分组，获取最新订单时间，并转换格式
      .groupBy("memberId") //
      .agg(
        from_unixtime(max("finishTime")).as("consume_cycle")
      )
      .filter("memberId<951")
    //      daysDF.printSchema()
    //.orderBy(daysDF("memberId").asc).show(10,false)

    // 2.2. 计算用户最新订单距今天数
    val result = daysDF.select(
      $"memberId".as("id"), //
      datediff(date_sub(current_timestamp(), days_range),
        $"consume_cycle")
        .as("consume_cycle")
    )
      //    .orderBy(daysDF("finish_time").asc).show(10,false)
      // 3. 关联属性标签数据和消费天数数据，加上判断条件，进行打标签
      .select('id,
        when('consume_cycle <= "7", "7日")
          .when('consume_cycle <= "14", "2周")
          .when('consume_cycle <= "30", "1月")
          .when('consume_cycle <= "61", "2月")
          .when('consume_cycle <= "91", "3月")
          .when('consume_cycle <= "122", "4月")
          .when('consume_cycle <= "152", "5月")
          .when('consume_cycle <= "183", "6月")
          .otherwise("其他")
          .as("consume_cycle")
      )
    //    result.orderBy(daysDF("consume_cycle").asc).show(10,false)
    //    result.printSchema()


    def catalogWrite =
      s"""{
         |"table":{"namespace":"default", "name":"user_profile"},
         |"rowkey":"id",
         |"columns":{
         |  "id":{"cf":"rowkey", "col":"id", "type":"string"},
         |  "consume_cycle":{"cf":"cf", "col":"consume_cycle", "type":"string"}
         |}
         |}""".stripMargin

    //    result.orderBy(result("consume_cycle").asc).show(10,false)

    result.write
      .option(HBaseTableCatalog.tableCatalog, catalogWrite)
      .option(HBaseTableCatalog.newTable, "5")
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .save()
  }
}