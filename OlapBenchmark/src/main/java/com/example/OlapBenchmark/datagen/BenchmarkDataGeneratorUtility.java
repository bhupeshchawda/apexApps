package com.example.OlapBenchmark.datagen;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang.math.NumberUtils;

/**
 * Created by bhupesh on 20/7/17.
 */
public class BenchmarkDataGeneratorUtility
{
  // Config
  public double fanout = 0.2;
  public int numDimensions;
  public int[] cardinalities;

  public long numTuplesToGenerate = 1000000;

  public int numMeasures = 3; // constant for now
  public static final char base = 'a';
  private int minCardinality;
  private int maxCardinality;
  private static Random r = new Random();

  public BenchmarkDataGeneratorUtility()
  {
  }

  public void init()
  {
    if (cardinalities.length != numDimensions) {
      throw new IllegalArgumentException("Cardinalities not specified for all dimensions");
    }
    minCardinality = NumberUtils.min(cardinalities);
    maxCardinality = NumberUtils.max(cardinalities);
    if (minCardinality == maxCardinality) {
      maxCardinality++;
    }
  }

  public String gen()
  {
    int lastCardinality = maxCardinality;
    int lastVal = ThreadLocalRandom.current().nextInt(minCardinality, maxCardinality);
    StringBuffer record = new StringBuffer();
    for (int c = 0; c < numDimensions; c++) {
      record.append(repeat((char)(base + c), 10));
      lastVal = vary(project(lastVal, lastCardinality, cardinalities[c]), cardinalities[c]);
      record.append(lastVal);
      record.append(':');
      lastCardinality = cardinalities[c];
    }
    for (int m = 0; m < numMeasures; m++) {
      record.append(r.nextInt(Integer.MAX_VALUE));
      record.append(':');
    }
    return record.substring(0, record.length() - 1);
  }

  private String repeat(char c, int n)
  {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < n; i++) {
      builder.append(c);
    }
    return builder.toString();
  }
  private int vary(int n, int cardinality)
  {
    int variance = (int)(cardinality * fanout / 2);
    int lower = n - variance;
    int upper = n + variance;
    int random = r.nextInt((upper - lower) + 1) + lower;
    int retVal = mod(random, cardinality);
    return retVal;
  }

  private int mod(int a, int b)
  {
    int r = a % b;
    return r < 0 ? r + b : r;
  }

  private int project(int n, int fromCard, int toCard)
  {
    return n * toCard / fromCard;
  }

  public static void main(String[] args) throws IOException
  {
    BenchmarkDataGeneratorUtility generator = new BenchmarkDataGeneratorUtility();
    generator.init();
    BufferedWriter bw = new BufferedWriter(new FileWriter("/tmp/generatedData"));
    int count = 0;
    while (count++ < generator.numTuplesToGenerate) {
      String record = generator.gen();
      bw.write(record + "\n");
      bw.flush();
    }
    bw.close();
  }
}
