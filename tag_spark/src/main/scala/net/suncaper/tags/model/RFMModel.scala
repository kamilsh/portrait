package net.suncaper.tags.model

import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.sql.{Column, DataFrame, SparkSession, functions}
import org.apache.spark.sql.execution.datasources.hbase.HBaseTableCatalog
import org.apache.spark.sql.functions._

object RFMModel {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder()
      .appName("shc test")
      .master("local")
      .getOrCreate()

    import spark.implicits._


    //    1、读取数据
    def catalog =
      s"""{
         |  "table":{"namespace":"default", "name":"tbl_orders"},
         |  "rowkey":"id",
         |  "columns":{
         |    "id":{"cf":"rowkey", "col":"id", "type":"Long"},
         |    "memberId":{"cf":"cf", "col":"memberId", "type":"string"},
         |    "orderSn":{"cf":"cf", "col":"orderSn", "type":"string"},
         |    "orderAmount":{"cf":"cf", "col":"orderAmount", "type":"string"},
         |    "finishTime":{"cf":"cf", "col":"finishTime", "type":"string"}
         |  }
         |}""".stripMargin

    val source: DataFrame = spark.read
      .option(HBaseTableCatalog.tableCatalog, catalog)
      .format("org.apache.spark.sql.execution.datasources.hbase")
      .load()

    //    2、数据归一化处理
    val colRencency = "rencency"
    val colFrequency = "frequency"
    val colMoneyTotal = "moneyTotal"
    val colFeature = "feature"
    val colPredict = "predict"
    val days_range = 662

    // 统计距离最近一次消费的时间
    val recencyCol = datediff(date_sub(current_timestamp(), days_range), from_unixtime(max('finishTime))) as colRencency
    // 统计订单总数
    val frequencyCol = count('orderSn) as colFrequency
    // 统计订单总金额
    val moneyTotalCol = sum('orderAmount) as colMoneyTotal

    val RFMResult = source.groupBy('memberId)
      .agg(recencyCol, frequencyCol, moneyTotalCol)

    //2.为RFM打分
    //R: 1-3天=5分，4-6天=4分，7-9天=3分，10-15天=2分，大于16天=1分
    //F: ≥200=5分，150-199=4分，100-149=3分，50-99=2分，1-49=1分
    //M: ≥20w=5分，10-19w=4分，5-9w=3分，1-4w=2分，<1w=1分
    val recencyScore: Column = when((col(colRencency) >= 1) && (col(colRencency) <= 3), 5)
      .when((col(colRencency) >= 4) && (col(colRencency) <= 6), 4)
      .when((col(colRencency) >= 7) && (col(colRencency) <= 9), 3)
      .when((col(colRencency) >= 10) && (col(colRencency) <= 15), 2)
      .when(col(colRencency) >= 16, 1)
      .as(colRencency)

    val frequencyScore: Column = when(col(colFrequency) >= 200, 5)
      .when((col(colFrequency) >= 150) && (col(colFrequency) <= 199), 4)
      .when((col(colFrequency) >= 100) && (col(colFrequency) <= 149), 3)
      .when((col(colFrequency) >= 50) && (col(colFrequency) <= 99), 2)
      .when((col(colFrequency) >= 1) && (col(colFrequency) <= 49), 1)
      .as(colFrequency)

    val moneyTotalScore: Column = when(col(colMoneyTotal) >= 200000, 5)
      .when(col(colMoneyTotal).between(100000, 199999), 4)
      .when(col(colMoneyTotal).between(50000, 99999), 3)
      .when(col(colMoneyTotal).between(10000, 49999), 2)
      .when(col(colMoneyTotal) <= 9999, 1)
      .as(colMoneyTotal)

    val RFMScoreResult = RFMResult.select('memberId, recencyScore, frequencyScore, moneyTotalScore)

    //    RFMScoreResult.show(50,false)


    //    3、训练K均值算法模型
    val vectorDF = new VectorAssembler()
      .setInputCols(Array(colRencency, colFrequency, colMoneyTotal))
      .setOutputCol(colFeature)
      .transform(RFMScoreResult)
    vectorDF.show(50, false)

    val kmeans = new KMeans()
      .setK(7)
      .setSeed(1000)
      .setMaxIter(2) //迭代次数
      .setFeaturesCol(colFeature)
      .setPredictionCol(colPredict)

    // train model
    val model = kmeans.fit(vectorDF)

    //    //    4、预测分类
    //    val predicted = model.transform(vectorDF)
    //    predicted.show(50, false)
    //TODO: 将结果写入HBASE

    //4、保存模型到HDFS
    model.save("model/user_value/kmeans")

    spark.stop()

  }
}
