package com.example.SparkModelScoring

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}


/**
  * Created by bhupesh on 12/10/17.
  */
class TestParquet {

  def test(): Unit ={

    val conf = new SparkConf().setAppName("Simple Application").setMaster("local")
    val sc = new SparkContext(conf)

    val sqlContext = new SQLContext(sc)
    val data = sqlContext.read.parquet("/tmp/recommender_model/data/user/part-r-00003-43a03666-68bf-493b-9623-48685472d85a.gz.parquet")
    println(data)
  }
}
