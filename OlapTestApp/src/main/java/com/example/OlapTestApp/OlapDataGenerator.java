package com.example.OlapTestApp;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.commons.lang.math.NumberUtils;

import com.datatorrent.api.DefaultOutputPort;
import com.datatorrent.api.InputOperator;
import com.datatorrent.common.util.BaseOperator;

/**
 * Created by bhupesh on 20/7/17.
 */
public class OlapDataGenerator extends BaseOperator implements InputOperator
{
  public static final char base = 'A';
  public int numDimensions = 3;
  public int numMeasures = 2;
  public int[] cardinalities = new int[]{2,2,2};
  public double fanout = 1;
  public int maxTuplesPerWindow = 10;

  private int minCardinality;
  private int maxCardinality;
  private static Random r = new Random();
  private int numTuplesThisWindow = 0;


  public final transient DefaultOutputPort<Object> data = new DefaultOutputPort<>();

  public OlapDataGenerator()
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

  @Override
  public void beginWindow(long windowId)
  {
    numTuplesThisWindow = 0;
  }

  @Override
  public void emitTuples()
  {
    if (numTuplesThisWindow >= maxTuplesPerWindow) {
      return;
    }
    numTuplesThisWindow++;
    String generated = gen();
    String[] s = generated.trim().split(":");
    POJO pojo = new POJO();
    pojo.time = System.currentTimeMillis();
    pojo.a = s[0];
    pojo.b = s[1];
    pojo.c = s[2];
    pojo.x = Long.parseLong(s[3]);
    pojo.y = Long.parseLong(s[4]);
    data.emit(pojo);
  }

  public String gen()
  {
    int lastCardinality = maxCardinality;
    int lastVal = ThreadLocalRandom.current().nextInt(minCardinality, maxCardinality);
    StringBuffer record = new StringBuffer();
    for (int c = 0; c < numDimensions; c++) {
      record.append((char)(base + c));
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

}
