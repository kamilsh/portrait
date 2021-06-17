package net.suncaper.tags.model

import org.apache.spark.sql.execution.datasources.hbase.HBaseTableCatalog
import org.apache.spark.sql.expressions.{Window, WindowSpec}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SparkSession}

//登录频率标签
object LoginFrequencyModel {
  def main(args: Array[String]): Unit = {
    def catalog =
      s"""{
         |"table":{"namespace":"default", "name":"tbl_logs"},
         |"rowkey":"id",
         |"columns":{
         |"id":{"cf":"rowkey", "col":"id", "type":"string"},
         |"global_user_id":{"cf":"cf", "col":"global_user_id", "type":"string"},
         |"loc_url":{"cf":"cf", "col":"loc_url", "type":"string"}
         |}
         |}""".stripMargin


    val spark = SparkSession.builder()
      .appName("shc test")
      .master("local[10]")
      .getOrCreate()

    import spark.implicits._


    val readTempDF: DataFrame = spark.read
      .option(HBaseTableCatalog.tableCatalog, catalog)
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .load()
    val readDF=readTempDF.na.drop(List("loc_url"))


    val temp1 = readDF
      .where('loc_url like "%login%")
      .groupBy('global_user_id)
      .agg(count("loc_url") as "count")

//    temp1.orderBy(asc("count")).show(50,false)
//    temp1.printSchema()

    //10<=count<=30 1-10很少 10-20一般 20-30经常
    val result = temp1.select(col("global_user_id").as("id"),
      when('count < 1,"无")
        .when('count < 10 and('count >=1),"很少")
        .when('count < 20 and('count >=10),"一般")
        .otherwise("经常")
        .as("login_frequency")
    )
//    result.filter("id = 99")show(10,false)
//    result.printSchema()

    def catalogWrite =
      s"""{
         |"table":{"namespace":"default", "name":"user_profile"},
         |"rowkey":"id",
         |"columns":{
         |"id":{"cf":"rowkey", "col":"id", "type":"string"},
         |"login_frequency":{"cf":"cf", "col":"login_frequency", "type":"string"}
         |}
         |}""".stripMargin

    result.write
      .option(HBaseTableCatalog.tableCatalog, catalogWrite)
      .option(HBaseTableCatalog.newTable, "5")
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .save()
  }
}
