package com.example.OlapBenchmark.datagen;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang.math.NumberUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Created by bhupesh on 20/7/17.
 */
public class BenchmarkDataGeneratorUtility
{
  // Config
  public double fanout = 0.2;
  public int numDimensions;
  public int numMeasures;
  public int[] cardinalities;
  public boolean isRealData;
  public String[] dimensions;
  public String[] measures;
  public String baseDataPath;
  public Map<String, List<String>> masterData;

  public long numTuplesToGenerate = 1000000;

  public static final char base = 'a';
  private int minCardinality;
  private int maxCardinality;
  private static Random r = new Random();

  public BenchmarkDataGeneratorUtility() throws IOException
  {
    numDimensions = 5;
    numMeasures = 1;
    dimensions = new String[] {"email", "country", "cctype", "currency", "isfraud"}; // ccnumber, currency type etc..
    isRealData = true;
    cardinalities = new int[] {20000, 127, 16, 59, 2};
    measures = new String[] {"amount"};
    baseDataPath = "src/main/resources/";

    // upload masterdata
    masterData = Maps.newHashMap();
    for (int i = 0; i < dimensions.length; i++) {
      masterData.put(dimensions[i], readFile(dimensions[i]));
    }
  }

  public List<String> readFile(String path) throws IOException
  {
    List<String> retVal = Lists.newArrayList();
    BufferedReader br = new BufferedReader(new FileReader(baseDataPath + "/" + path));
    String s;
    while ((s = br.readLine()) != null) {
      retVal.add(s);
    }
    return retVal;
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
      if (isRealData) {
        lastVal = vary(project(lastVal, lastCardinality, cardinalities[c]), cardinalities[c]);
        record.append(masterData.get(dimensions[c]).get(lastVal));
        record.append(":");
        lastCardinality = cardinalities[c];
      } else {
        record.append(repeat((char)(base + c), 10));
        lastVal = vary(project(lastVal, lastCardinality, cardinalities[c]), cardinalities[c]);
        record.append(lastVal);
        record.append(':');
        lastCardinality = cardinalities[c];
      }
    }
    for (int m = 0; m < numMeasures; m++) {
      record.append(r.nextInt(Integer.MAX_VALUE));
      record.append('|');
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
