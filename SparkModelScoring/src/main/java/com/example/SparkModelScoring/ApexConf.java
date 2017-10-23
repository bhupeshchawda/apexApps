package com.example.SparkModelScoring;

import java.io.Serializable;

import org.apache.spark.SparkConf;

import com.esotericsoftware.kryo.DefaultSerializer;
import com.esotericsoftware.kryo.serializers.JavaSerializer;

/**
 * Created by bhupesh on 23/10/17.
 */
@DefaultSerializer(JavaSerializer.class)
public class ApexConf extends SparkConf implements Serializable
{
  public ApexConf()
  {
  }

  @Override
  public ApexConf setMaster(String master)
  {
    return (ApexConf) super.setMaster(master);
  }

  @Override
  public ApexConf setAppName(String name)
  {
    return (ApexConf) super.setAppName(name);
  }
}