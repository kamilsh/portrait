package net.suncaper.tags.model

import org.apache.spark.sql.execution.datasources.hbase.HBaseTableCatalog
import org.apache.spark.sql.expressions.{Window, WindowSpec}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SparkSession}


object BrowseProductsModel {
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

    def catalog2 =
      s"""{
         |"table":{"namespace":"default", "name":"tbl_goods"},
         |"rowkey":"id",
         |"columns":{
         |"id":{"cf":"rowkey", "col":"id", "type":"string"},
         |"productId":{"cf":"cf", "col":"productId", "type":"string"},
         |"productName":{"cf":"cf", "col":"productName", "type":"string"}
         |}
         |}""".stripMargin

    val spark = SparkSession.builder()
      .appName("shc test")
      .master("local[10]")
      .getOrCreate()

    import spark.implicits._

    val readDF01: DataFrame = spark.read
      .option(HBaseTableCatalog.tableCatalog, catalog)
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .load()
      val readDF=readDF01.select('global_user_id,'loc_url)

    val temp1 = readDF.select('global_user_id,'loc_url,
      when('loc_url like "%login%", "登录页")
        .when('loc_url === "http://www.eshop.com/" or ('loc_url === "http://m.eshop.com/"), "首页")
        .when('loc_url like "%item%", "分类页")
        .when('loc_url like "%product%", "商品页")
        .when('loc_url like "%order%", "我的订单页")
        .as("页面")
    ).select('global_user_id,'loc_url)
      .where('页面==="商品页")
      .withColumn("productid",regexp_extract('loc_url,"\\d+",0))
      .orderBy('global_user_id)

//        temp1.filter("global_user_id=99").show(false)
//        temp1.printSchema()

    val readDF02: DataFrame = spark.read
      .option(HBaseTableCatalog.tableCatalog, catalog2)
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .load()
    val readDF2=readDF02.select('productId,'productName)
      .distinct()

    val window: WindowSpec = Window.partitionBy('productId)
      .orderBy('productName.desc)

    val temp2 = readDF2.select('productId,'productName,rank() over window as "rank")
      .where('rank <=1)

    val temp3 = temp1.join(temp2,temp1.col("productid")===temp2.col("productId"),"inner")
      .select('global_user_id.as("id"),'productName.as("browse_products"))
      .orderBy('id.desc)

//    temp3.filter("global_user_id=99").show(false)
//    temp3.printSchema()

    temp3.createOrReplaceTempView("temp3")
    val result = spark.sql(
      """
        |select
        |   id,concat_ws(',',collect_set(browse_products)) as browse_products
        |from temp3
        |group by id
      """.stripMargin)
//    result.show(20)
//        temp3.filter("id=99").show(1000,false)
//        temp3.printSchema()

    def catalogWrite =
      s"""{
         |"table":{"namespace":"default", "name":"user_profile"},
         |"rowkey":"id",
         |"columns":{
         |"id":{"cf":"rowkey", "col":"id", "type":"string"},
         |"browse_products":{"cf":"cf", "col":"browse_products", "type":"string"}
         |}
         |}""".stripMargin

    result.write
      .option(HBaseTableCatalog.tableCatalog, catalogWrite)
      .option(HBaseTableCatalog.newTable, "5")
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .save()

    spark.stop()
  }
}
