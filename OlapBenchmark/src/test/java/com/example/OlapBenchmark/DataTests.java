package com.example.OlapBenchmark;

import org.junit.Test;

import com.example.OlapBenchmark.datagen.BenchmarkDataGeneratorUtility;

/**
 * Created by bhupesh on 2/8/17.
 */
public class DataTests
{
  @Test
  public void testDataGen()
  {
    BenchmarkDataGeneratorUtility dataGeneratorUtility = new BenchmarkDataGeneratorUtility();
    dataGeneratorUtility.cardinalities = new int[]{10,100,1000,10,10};
    dataGeneratorUtility.numDimensions = 5;
    dataGeneratorUtility.numTuplesToGenerate = 10;
    dataGeneratorUtility.init();
    String s = dataGeneratorUtility.gen();
    System.out.println(s);
  }
}
