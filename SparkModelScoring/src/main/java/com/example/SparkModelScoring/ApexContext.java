package com.example.SparkModelScoring;

import java.io.Serializable;

import org.apache.spark.SparkContext;

/**
 * Created by bhupesh on 23/10/17.
 */
public class ApexContext extends SparkContext implements Serializable
{
  public ApexContext()
  {
    super(new ApexConf());
  }

  public ApexContext(ApexConf config)
  {
    super(config);
  }
}
