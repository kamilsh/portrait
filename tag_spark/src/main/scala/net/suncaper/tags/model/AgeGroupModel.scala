package net.suncaper.tags.model

import java.text.SimpleDateFormat

import org.apache.spark.sql.execution.datasources.hbase.HBaseTableCatalog
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SparkSession}

import scala.language.postfixOps

object AgeGroupModel {
  def main(args: Array[String]): Unit = {
    def catalog =
      s"""{
         |"table":{"namespace":"default", "name":"tbl_users"},
         |"rowkey":"id",
         |"columns":{
         |"id":{"cf":"rowkey", "col":"id", "type":"string"},
         |"birthday":{"cf":"cf", "col":"birthday", "type":"string"}
         |}
         |}""".stripMargin

    val spark = SparkSession.builder()
      .appName("shc test")
      .master("local[10]")
      .getOrCreate()

    import spark.implicits._

    val readDF: DataFrame = spark.read
      .option(HBaseTableCatalog.tableCatalog, catalog)
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .load()


    val result = readDF.select('id,
      when('birthday >= "2020", "20后")
        .when('birthday >= "2010", "10后")
        .when('birthday >= "2000", "00后")
        .when('birthday >= "1990", "90后")
        .when('birthday >= "1980", "80后")
        .when('birthday >= "1970", "70后")
        .when('birthday >= "1960", "60后")
        .when('birthday >= "1950", "50后")
        .otherwise("其他")
        .as("AgeGroup")
    )
    //val result = readDF.select('id,
    //  when('birthday >= "1950", "50后")
    //    .when('birthday >= "1960", "60后")
    //    .when('birthday >= "1970", "70后")
    //    .when('birthday >= "1980", "80后")
    //    .when('birthday >= "1990", "90后")
    //    .when('birthday >= "2000", "00后")
    //    .when('birthday >= "2010", "10后")
    //    .when('birthday    >= "2020", "20后")
    //    .otherwise("其他")
    //    .as("AgeGroup")
    //)

    def catalogWrite =
      s"""{
         |"table":{"namespace":"default", "name":"user_profile"},
         |"rowkey":"id",
         |"columns":{
         |"id":{"cf":"rowkey", "col":"id", "type":"string"},
         |"AgeGroup":{"cf":"cf", "col":"AgeGroup", "type":"string"}
         |}
         |}""".stripMargin

    result.write
      .option(HBaseTableCatalog.tableCatalog, catalogWrite)
      .option(HBaseTableCatalog.newTable, "5")
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .save()
  }
}