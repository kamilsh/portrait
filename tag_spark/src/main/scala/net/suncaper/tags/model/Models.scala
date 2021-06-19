package net.suncaper

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.execution.datasources.hbase.HBaseTableCatalog
import org.apache.spark.sql.functions._
import org.apache.spark.sql.{DataFrame, SparkSession}


object Models {
  val spark: SparkSession = SparkSession.builder()
    .appName("shc test")
    .master("local")
    .getOrCreate()

  def log_order =
    s"""{
       |  "table":{"namespace":"default", "name":"tbl_orders"},
       |  "rowkey":"id",
       |  "columns":{
       |    "id":{"cf":"rowkey", "col":"id", "type":"string"},
       |    "memberId":{"cf":"cf", "col":"memberId", "type":"string"},
       |    "finishTime":{"cf":"cf", "col":"finishTime", "type":"string"}
       |  }
       |}""".stripMargin

  def log_order_2 =
    s"""{
       |  "table":{"namespace":"default", "name":"tbl_orders"},
       |  "rowkey":"id",
       |  "columns":{
       |    "id":{"cf":"rowkey", "col":"id", "type":"string"},
       |    "memberId":{"cf":"cf", "col":"memberId", "type":"string"},
       |    "orderAmount":{"cf":"cf", "col":"orderAmount", "type":"string"},
       |    "finishTime":{"cf":"cf", "col":"finishTime", "type":"string"}
       |  }
       |}""".stripMargin

  def log_user =
    s"""{
       |  "table":{"namespace":"default", "name":"tbl_users"},
       |  "rowkey":"id",
       |  "columns":{
       |    "id":{"cf":"rowkey", "col":"id", "type":"string"}
       |  }
       |}""".stripMargin

  def log_user_2 =
    s"""{
       |  "table":{"namespace":"default", "name":"tbl_users"},
       |  "rowkey":"id",
       |  "columns":{
       |    "id":{"cf":"rowkey", "col":"id", "type":"string"},
       |    "birthday":{"cf":"cf","col":"birthday","type":"string"}
       |  }
       |}""".stripMargin

  def log_log_1 =
    s"""
       |{
       |  "table":{"namespace":"default", "name":"tbl_logs"},
       |  "rowkey":"id",
       |  "columns":{
       |    "id":{"cf":"rowkey", "col":"id", "type":"string"},
       |    "global_user_id":{"cf":"cf","col":"global_user_id","type":"string"},
       |    "url":{"cf":"cf","col":"loc_url","type":"string"}
       |  }
       |}""".stripMargin

  def log_log_2 =
    s"""
       |{
       |  "table":{"namespace":"default", "name":"tbl_logs"},
       |  "rowkey":"id",
       |  "columns":{
       |    "id":{"cf":"rowkey", "col":"id", "type":"string"},
       |    "global_user_id":{"cf":"cf","col":"global_user_id","type":"string"},
       |    "user_agent":{"cf":"cf","col":"user_agent","type":"string"}
       |  }
       |}""".stripMargin

  def log_log_3 =
    s"""{
       |  "table":{"namespace":"default", "name":"tbl_logs"},
       |  "rowkey":"id",
       |  "columns":{
       |    "id":{"cf":"rowkey", "col":"id", "type":"string"},
       |    "global_user_id":{"cf":"cf", "col":"global_user_id", "type":"string"},
       |    "log_time":{"cf":"cf", "col":"log_time", "type":"string"}
       |  }
       |}""".stripMargin

  val source_user: DataFrame = spark.read
    .option(HBaseTableCatalog.tableCatalog, log_user)
    .format("org.apache.spark.sql.execution.datasources.hbase")
    .load()
  val source_user_2: DataFrame = spark.read
    .option(HBaseTableCatalog.tableCatalog, log_user_2)
    .format("org.apache.spark.sql.execution.datasources.hbase")
    .load()
  val source_order: DataFrame = spark.read
    .option(HBaseTableCatalog.tableCatalog, log_order)
    .format("org.apache.spark.sql.execution.datasources.hbase")
    .load()
  val source_order_2: DataFrame = spark.read
    .option(HBaseTableCatalog.tableCatalog, log_order_2)
    .format("org.apache.spark.sql.execution.datasources.hbase")
    .load()
  val source_log_1: DataFrame = spark.read
    .option(HBaseTableCatalog.tableCatalog, log_log_1)
    .format("org.apache.spark.sql.execution.datasources.hbase")
    .load()
  val source_log_2: DataFrame = spark.read
    .option(HBaseTableCatalog.tableCatalog, log_log_2)
    .format("org.apache.spark.sql.execution.datasources.hbase")
    .load()
  val source_log_3: DataFrame = spark.read
    .option(HBaseTableCatalog.tableCatalog, log_log_3)
    .format("org.apache.spark.sql.execution.datasources.hbase")
    .load()
  //  source_order.createOrReplaceTempView("source_order")
  source_user.createOrReplaceTempView("source_user")
  source_user_2.createOrReplaceTempView("source_user_2")
  source_log_1.createOrReplaceTempView("source_log_1")
  source_log_2.createOrReplaceTempView("source_log_2")
  source_log_3.createOrReplaceTempView("source_log_3")
  source_order_2.createOrReplaceTempView("source_order_2")

  def main(args: Array[String]): Unit = {
    Logger.getLogger("org.apache.spark").setLevel(Level.WARN)

    ViewPages()
    // DeviceType()
    // Constellation()
    //  ViewFrequency()
    // ViewInterval()
    // MaxOrderAmount()
    // AvgOrderAmount()
  }

  /*
+---+--------------------------------------------------------+
|id |view_page                                               |
+---+--------------------------------------------------------+
|296|其它:222,登录页:6,订单页:22,商品页:47,分类页:17,主页:66 |
  * */
  def ViewPages() = {
    val df_ = spark.sql(
      """
        |select
        |   source_user.id as id,
        |   (case when url like '%order%' then 1
        |     when url like '%itemlist%' then 2
        |     when url like '%item%' then 3
        |     when url like '%login%' then 4
        |     when url like 'http://www.eshop.com/' then 5
        |     when url like 'http://m.eshop.com/' then 5
        |     else 6 end) as view_page,
        |    url
        |from source_user join source_log_1
        |on source_user.id = source_log_1.global_user_id
        |
      """.stripMargin)
    df_.show(40, false)
    val df_2 = df_.groupBy("id", "view_page")
      .agg(count("view_page") as "count_page")
      .select("id", "view_page", "count_page")
    df_2.createOrReplaceTempView("df_2")

    val df_3 = spark.sql(
      """
        |select id,
        |(case when view_page = 1 then '订单页'
        |   when view_page = 2 then '分类页'
        |   when view_page = 3 then '商品页'
        |   when view_page = 4 then '登录页'
        |   when view_page = 5 then '主页'
        |   when view_page = 6 then '其它' end) as view_page,
        |   count_page
        |from df_2
      """.stripMargin)
    df_3.createOrReplaceTempView("df_3")
    val result = spark.sql(
      """
        |select id, concat_ws(',',collect_set(view_page)) as view_page
        |from
        |   (select id, concat_ws(':',view_page,count_page) as view_page
        |   from df_3) tmp
        |group by id
      """.stripMargin)
    //    val result = spark.sql(
    //      """
    //        |select
    //        |   id,concat_ws(',',collect_set(count_page)) as view_page
    //        |from df_2
    //        |group by id
    //      """.stripMargin)
    result.show(40, false)

    def catalogWrite =
      s"""{
         |"table":{"namespace":"default", "name":"user_profile"},
         |"rowkey":"id",
         |"columns":{
         |  "id":{"cf":"rowkey", "col":"id", "type":"string"},
         |  "view_page":{"cf":"cf", "col":"view_page", "type":"string"}
         |}
         |}""".stripMargin

    result.write
      .option(HBaseTableCatalog.tableCatalog, catalogWrite)
      .option(HBaseTableCatalog.newTable, "5")
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .save()
  }

  /*
+---+--------------------+
| id|         device_type|
+---+--------------------+
|829|iOS:49,Linux:3,Ma...|
 */
  def DeviceType() = {
    val df_ = spark.sql(
      """
        |select
        |   source_user.id as id,
        |   (case when user_agent like '%Windows%' then 1
        |     when user_agent like '%iPhone%' then 2
        |     when user_agent like '%iPad%' then 2
        |     when user_agent like '%Mac%' then 3
        |     when user_agent like '%Android%' then 4
        |     when user_agent like '%Linux%' then 5
        |     else 6 end) as device_type,
        |    user_agent
        |from source_user join source_log_2
        |on source_user.id = source_log_2.global_user_id
        |
      """.stripMargin)
    df_.show(5, false)
    val df_2 = df_.groupBy("id", "device_type")
      .agg(count("device_type") as "count_type")
      .select("id", "device_type", "count_type")
      .orderBy("id", "device_type")
    df_2.show(40)
    df_2.createOrReplaceTempView("df_2")

    val df_3 = spark.sql(
      """
        |select id,
        |   (case when device_type = 1 then 'Windows'
        |   when device_type = 2 then 'iOS'
        |   when device_type = 3 then 'Mac'
        |   when device_type = 4 then 'Android'
        |   when device_type = 5 then 'Linux'
        |   when device_type = 6 then 'other' end) as device_type,
        |   count_type
        |from df_2
      """.stripMargin)
    df_3.createOrReplaceTempView("df_3")

    val result = spark.sql(
      """
        |select id, concat_ws(',',collect_set(device_type)) as device_type
        |from
        |   (select id, concat_ws(':',device_type,count_type) as device_type
        |   from df_3) tmp
        |group by id
      """.stripMargin)

    result.show(40)

    //    val result = spark.sql(
    //      """
    //        |select
    //        |   id,concat_ws(',',collect_set(count_type)) as device_type
    //        |from df_2
    //        |group by id
    //      """.stripMargin)
    //    result.show(40,false)
    def catalogWrite =
      s"""{
         |"table":{"namespace":"default", "name":"user_profile"},
         |"rowkey":"id",
         |"columns":{
         |  "id":{"cf":"rowkey", "col":"id", "type":"string"},
         |  "device_type":{"cf":"cf", "col":"device_type", "type":"string"}
         |}
         |}""".stripMargin

    result.write
      .option(HBaseTableCatalog.tableCatalog, catalogWrite)
      .option(HBaseTableCatalog.newTable, "5")
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .save()
  }

  /*
+---+-------------+
|id |constellation|
+---+-------------+
|1  |双子座       |
   */
  def Constellation() = {
    val df_ = spark.sql(
      """
        |select id,dayofyear(birthday) as dayofyear
        |from source_user_2
      """.stripMargin)
    df_.createOrReplaceTempView("df_")

    val result = spark.sql(
      """
        |select
        |   id,
        |   (case when dayofyear < 20 then "魔羯座"
        |   when dayofyear < 50 then "水瓶座"
        |   when dayofyear < 80 then "双鱼座"
        |   when dayofyear < 110 then "白羊座"
        |   when dayofyear < 140 then "金牛座"
        |   when dayofyear < 170 then "双子座"
        |   when dayofyear < 200 then "巨蟹座"
        |   when dayofyear < 230 then "狮子座"
        |   when dayofyear < 260 then "处女座"
        |   when dayofyear < 290 then "天秤座"
        |   when dayofyear < 320 then "天蝎座"
        |   when dayofyear < 350 then "射手座"
        |   else "魔羯座" end) as constellation
        |from df_
      """.stripMargin)
    result.show(20, false)

    def catalogWrite =
      s"""{
         |"table":{"namespace":"default", "name":"user_profile"},
         |"rowkey":"id",
         |"columns":{
         |  "id":{"cf":"rowkey", "col":"id", "type":"string"},
         |  "constellation":{"cf":"cf", "col":"constellation", "type":"string"}
         |}
         |}""".stripMargin


    result.write
      .option(HBaseTableCatalog.tableCatalog, catalogWrite)
      .option(HBaseTableCatalog.newTable, "5")
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .save()
  }

  /*
+---+--------------+
|id |view_frequency|
+---+--------------+
|829|4             |
0:从不,1:很少,2:偶尔,3:经常,4:频繁
   */
  def ViewFrequency() = {
    //2019-07-01 ~ 2019-08-01
    val df_ = spark.sql(
      """
        |select global_user_id as id,count(log_time) as count
        |from source_log_3
        |where year(log_time)=2019 and dayofyear(log_time)>180 and dayofyear(log_time)<210
        |group by global_user_id
    """.stripMargin)
    df_.createOrReplaceTempView("df_")
    val result = spark.sql(
      """
        |select id ,
        |(case
        | when count=0 then '从不'
        | when count<50 then '很少'
        | when count<100 then '偶尔'
        | when count<150 then '经常'
        | else '频繁' end) as view_frequency
        |from df_
    """.stripMargin)

    result.show(30, false)
    import spark.implicits._
    val result_2 = result.select($"id", $"view_frequency".cast("string"))

    def catalogWrite =
      s"""{
         |"table":{"namespace":"default", "name":"user_profile"},
         |"rowkey":"id",
         |"columns":{
         |  "id":{"cf":"rowkey", "col":"id", "type":"string"},
         |  "view_frequency":{"cf":"cf", "col":"view_frequency", "type":"string"}
         |}
         |}""".stripMargin


    result_2.write
      .option(HBaseTableCatalog.tableCatalog, catalogWrite)
      .option(HBaseTableCatalog.newTable, "5")
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .save()
  }

  /*
+---+-------------+
|id |view_interval|
+---+-------------+
|510|1            |
1:1点-7点
2:8点-12点
3:13点-17点
4:18点-21点
5:22点-24点
   */
  def ViewInterval() = {
    //2019-07-01 ~ 2019-08-01
    val df_ = spark.sql(
      """
        |select global_user_id as id,hour(log_time) as log_hour
        |from source_log_3
        |where year(log_time)=2019 and dayofyear(log_time)>180 and dayofyear(log_time)<210
    """.stripMargin)
    df_.createOrReplaceTempView("df_")

    val df_2 = spark.sql(
      """
        |select id ,
        |(case
        | when log_hour<8 then '1:00-7:00'
        | when log_hour<13 then '8:00-12:00'
        | when log_hour<18 then '13:00-17:00'
        | when log_hour<22 then '18:00-21:00'
        | else '22:00-24:00' end) as view_interval
        |from df_
    """.stripMargin)
    df_2.createOrReplaceTempView("df_2")

    val df_3 = spark.sql(
      """
        |select id,view_interval, count(view_interval) as c
        | from df_2
        | group by id, view_interval
    """.stripMargin)
    df_3.createOrReplaceTempView("df_3")

    val df_4 = spark.sql(
      """
        |select id,max(c) as m
        |from df_3
        |group by id
    """.stripMargin)
    df_4.createOrReplaceTempView("df_4")

    val result = spark.sql(
      """
        |select df_3.id,view_interval
        |from df_3 join df_4
        |on df_3.id=df_4.id and df_3.c =df_4.m
    """.stripMargin)

    result.show(30, false)
    import spark.implicits._
    val result_2 = result.select($"id", $"view_interval".cast("string"))

    def catalogWrite =
      s"""{
         |"table":{"namespace":"default", "name":"user_profile"},
         |"rowkey":"id",
         |"columns":{
         |  "id":{"cf":"rowkey", "col":"id", "type":"string"},
         |  "view_interval":{"cf":"cf", "col":"view_interval", "type":"string"}
         |}
         |}""".stripMargin


    result_2.write
      .option(HBaseTableCatalog.tableCatalog, catalogWrite)
      .option(HBaseTableCatalog.newTable, "5")
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .save()
  }

  /*
+---+----------------+
| id|max_order_amount|
+---+----------------+
| 51|            5248|
   */
  def MaxOrderAmount() = {
    val df_ = spark.sql(
      """
        |select memberid as id, cast(orderamount as decimal) as orderamount,from_unixtime(finishtime, 'yyyy-MM-dd HH:mm:ss') as finishtime
        |from source_order_2
        |where memberid<951
    """.stripMargin)
    df_.createOrReplaceTempView("df_")
    df_.show(20)

    val result = spark.sql(
      """
        |select id, max(orderamount) as max_order_amount
        |from df_
        |where year(finishtime)=2019 and dayofyear(finishtime)>180 and dayofyear(finishtime)<210
        |group by id
    """.stripMargin)
    result.show(20)

    import spark.implicits._
    val result_2 = result.select($"id", $"max_order_amount".cast("string"))

    def catalogWrite =
      s"""{
         |"table":{"namespace":"default", "name":"user_profile"},
         |"rowkey":"id",
         |"columns":{
         |  "id":{"cf":"rowkey", "col":"id", "type":"string"},
         |  "max_order_amount":{"cf":"cf", "col":"max_order_amount", "type":"string"}
         |}
         |}""".stripMargin


    result_2.write
      .option(HBaseTableCatalog.tableCatalog, catalogWrite)
      .option(HBaseTableCatalog.newTable, "5")
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .save()
  }

  /*
+---+----------------+
| id|avg_order_amount|
+---+----------------+
| 51|       1658.8286|
   */
  def AvgOrderAmount() = {
    val df_ = spark.sql(
      """
        |select memberid as id, cast(orderamount as decimal) as orderamount,from_unixtime(finishtime, 'yyyy-MM-dd HH:mm:ss') as finishtime
        |from source_order_2
        |where memberid<951
      """.stripMargin)
    df_.createOrReplaceTempView("df_")
    df_.show(20)

    val result = spark.sql(
      """
        |select id, avg(orderamount) as avg_order_amount
        |from df_
        |where year(finishtime)=2019 and dayofyear(finishtime)>180 and dayofyear(finishtime)<210
        |group by id
      """.stripMargin)
    result.show(20)

    def catalogWrite =
      s"""{
         |"table":{"namespace":"default", "name":"user_profile"},
         |"rowkey":"id",
         |"columns":{
         |  "id":{"cf":"rowkey", "col":"id", "type":"string"},
         |  "avg_order_amount":{"cf":"cf", "col":"avg_order_amount", "type":"string"}
         |}
         |}""".stripMargin
    import spark.implicits._
    val result_2 = result.select($"id", $"avg_order_amount".cast("string"))

    result_2.write
      .option(HBaseTableCatalog.tableCatalog, catalogWrite)
      .option(HBaseTableCatalog.newTable, "5")
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .save()
  }


  //未使用
  def ConsumptionCycleModel(user_id: Integer): Double = {
    val df_tmp_data = source_user
      .select(source_user.col("id"))
      .where(source_user.col("id") === user_id)

    if (df_tmp_data.count() == 0)
      return -1

    val df_user_finishTime = source_order
      .join(df_tmp_data, df_tmp_data.col("id") === source_order.col("memberId"))
      .select(df_tmp_data.col("id"), source_order.col("finishTime"))
    df_user_finishTime.createOrReplaceTempView("df_user_finishTime")

    val max_finishTime = spark.sql(
      s"""
         |select max(finishTime)
         |from df_user_finishTime
      """.stripMargin).collect()(0).getString(0).toDouble

    val df_user_recentTime = spark.sql(
      s"""
         |select finishTime
         |from df_user_finishTime
         |where finishTime>($max_finishTime-15552000)
      """.stripMargin)
    df_user_recentTime.createOrReplaceTempView("df_user_recentTime")
    df_user_recentTime.show()

    val min_finishTime = spark.sql(
      s"""
         |select min(finishTime)
         |from df_user_recentTime
      """.stripMargin).collect()(0).getString(0).toDouble

    val count_finishTime = spark.sql(
      s"""
         |select count(finishTime)
         |from df_user_recentTime
      """.stripMargin).collect()(0).getLong(0)
    if (count_finishTime <= 1)
      return -2

    return (max_finishTime - min_finishTime) / count_finishTime

  }

  //未使用
  def ConsumptionCycleModel(): Integer = {
    return 0
    val df_tmp_data = source_user
      .join(source_order, source_user.col("id") === source_order.col("memberId"))
      .select(source_user.col("id") as "id", source_order.col("finishTime") as "finishTime")

    val df_tmp_maxTime = df_tmp_data
      .groupBy("id")
      .agg(max("finishTime") as "max_time")
      .select(col("id"), col("max_Time"))


    val df_tmp_recentTime = df_tmp_data
      .join(df_tmp_maxTime, df_tmp_data.col("id") === df_tmp_maxTime.col("id"))
      .select(df_tmp_data.col("id") as "id", count("finishTime") as "count", min("finishTime") as "min_time")
      .where(df_tmp_data.col("finishTime") > (df_tmp_maxTime.col("max_Time") - 15552000))

    val df_result = df_tmp_maxTime
      .join(df_tmp_recentTime, df_tmp_maxTime.col("id") === df_tmp_recentTime.col("id"))
      .select(df_tmp_maxTime.col("id"), (col("max_time") - col("min_time")) / col("count") as "avg_consumptionCycle")

    df_result.show(20, false)

    return 0
  }
}
