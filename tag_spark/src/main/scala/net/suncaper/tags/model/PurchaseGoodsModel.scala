package net.suncaper.tags.model

import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.execution.datasources.hbase.HBaseTableCatalog
import org.apache.spark.sql.functions._

object PurchaseGoodsModel {
  def main(args: Array[String]): Unit = {
    def catalog =
      s"""{
         |  "table":{"namespace":"default", "name":"tbl_orders"},
         |  "rowkey":"id",
         |  "columns":{
         |    "id":{"cf":"rowkey", "col":"id", "type":"string"},
         |    "memberId":{"cf":"cf", "col":"memberId", "type":"string"},
         |    "orderSn":{"cf":"cf", "col":"orderSn", "type":"string"}
         |  }
         |}""".stripMargin

    def catalog2 =
      s"""{
         |  "table":{"namespace":"default", "name":"tbl_goods"},
         |  "rowkey":"id",
         |  "columns":{
         |    "id":{"cf":"rowkey", "col":"id", "type":"string"},
         |    "cOrderSn":{"cf":"cf", "col":"cOrderSn", "type":"string"},
         |    "productName":{"cf":"cf", "col":"productName", "type":"string"}
         |  }
         |}""".stripMargin

    val spark = SparkSession.builder()
      .appName("shc test")
      .master("local[10]")
      .getOrCreate()

    import spark.implicits._


    val sourceTemp1: DataFrame = spark.read
      .option(HBaseTableCatalog.tableCatalog, catalog)
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .load()

      val source1=sourceTemp1.select(col("memberId").as("id"),col("orderSn"))


    val source2: DataFrame = spark.read
      .option(HBaseTableCatalog.tableCatalog, catalog2)
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .load()
      .select('cOrderSn,col("productName").as("purchase_goods"))

    val source3 = source1.join(source2,source1.col("orderSn")===source2.col("cOrderSn"),"inner")
      .drop("cOrderSn")
      .drop("orderSn")

    source3.createOrReplaceTempView("source3")
    val result = spark.sql(
      """
        |select
        |   id,concat_ws(',',collect_set(purchase_goods)) as purchase_goods
        |from source3
        |group by id
      """.stripMargin)


    def catalogWrite =
      s"""{
         |"table":{"namespace":"default", "name":"user_profile"},
         |"rowkey":"id",
         |"columns":{
         |  "id":{"cf":"rowkey", "col":"id", "type":"string"},
         |  "purchase_goods":{"cf":"cf", "col":"purchase_goods", "type":"string"}
         |}
         |}""".stripMargin


//    result.filter("id=99").show(1000,false)
//    result.printSchema()


    result.write
      .option(HBaseTableCatalog.tableCatalog, catalogWrite)
      .option(HBaseTableCatalog.newTable, "5")
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .save()

    spark.stop()
  }
}
