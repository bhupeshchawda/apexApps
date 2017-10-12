package com.example.SparkModelScoring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.recommendation.MatrixFactorizationModel;

public class ScoringApp{

  public transient static Logger LOG = LoggerFactory.getLogger(ScoringApp.class);

  public static void main(String[] args)
  {
    // Load existing model

    String path = "/tmp/recommender_model/";
    SparkConf conf = new SparkConf().setAppName("Example").setMaster("local").set("spark.executor.memory","1g");
    JavaSparkContext jsc = new JavaSparkContext(conf);
    MatrixFactorizationModel sameModel = MatrixFactorizationModel.load(jsc.sc(), path);
    System.out.println(sameModel);


    // Train and load own model
//    Test test = new Test();
//    test.testModel();

//    TestParquet test = new TestParquet();
//    test.test();

  }


}