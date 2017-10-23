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

    String path = "/home/bhupesh/DT/Mindstix/recommender_model";
    ApexContext sc  = new ApexContext(new ApexConf().setMaster("local").setAppName("Test"));
//    JavaSparkContext jsc = new JavaSparkContext(conf);
    MatrixFactorizationModel sameModel = MatrixFactorizationModel.load(sc, path);
    System.out.println(sameModel.productFeatures());


    // Train and load own model
//    Test test = new Test();
//    test.testModel();

//    TestParquet test = new TestParquet();
//    test.test();

  }


}