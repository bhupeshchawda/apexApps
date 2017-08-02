package com.example.OlapBenchmark;

import com.example.OlapBenchmark.datagen.BenchmarkDataGeneratorUtility;
import com.example.OlapBenchmark.datagen.POJOGenerationFromData;
import com.example.OlapBenchmark.pojo.POJO;

import com.datatorrent.api.Context;
import com.datatorrent.api.DefaultOutputPort;
import com.datatorrent.api.InputOperator;
import com.datatorrent.common.util.BaseOperator;

/**
 * Created by bhupesh on 1/8/17.
 */
public class BenchmarkDataInput extends BaseOperator implements InputOperator
{

  // config
  private int numDimensions;
  private String cardinalities;
  private int numTuplesPerWindow;
  private int numQueryTuplesPerWindow;

  // operator private
  private long countThisWindow = 0;
  private long totalCount = 0;
  private long tupleCountBeforeQuery;

  // transients
  private transient BenchmarkDataGeneratorUtility generator;

  public final transient DefaultOutputPort<POJO> dataOutput = new DefaultOutputPort<>();
  public final transient DefaultOutputPort<Boolean> queryTrigger = new DefaultOutputPort<>();

  public BenchmarkDataInput()
  {
  }

  @Override
  public void beginWindow(long windowId)
  {
    countThisWindow = 0;
  }

  @Override
  public void setup(Context.OperatorContext context)
  {
    if (numDimensions != 3 && numDimensions != 5 && numDimensions != 7) {
      throw new RuntimeException("Unsupported number of dimensions for benchmark " + numDimensions);
    }
    if (cardinalities == null || cardinalities.split(",").length != numDimensions) {
      throw new RuntimeException("Number of cardinalities don't match number of dimensions " + cardinalities);
    }
    generator = new BenchmarkDataGeneratorUtility();
    generator.numDimensions = this.numDimensions;

    generator.cardinalities = new int[numDimensions];
    String[] c = cardinalities.split(",");
    for (int i = 0; i < c.length; i++) {
      generator.cardinalities[i] = Integer.parseInt(c[i].trim());
    }
    generator.init();
  }

  @Override
  public void emitTuples()
  {
    if (totalCount > tupleCountBeforeQuery) {
      if (++countThisWindow <= numQueryTuplesPerWindow) {
        queryTrigger.emit(true);
      }
    } else {
      if (++countThisWindow <= numTuplesPerWindow) {
        dataOutput.emit(POJOGenerationFromData.getPOJO(generator.gen(), numDimensions));
        totalCount++;
      }
    }
  }

  public int getNumDimensions()
  {
    return numDimensions;
  }

  public void setNumDimensions(int numDimensions)
  {
    this.numDimensions = numDimensions;
  }

  public String getCardinalities()
  {
    return cardinalities;
  }

  public void setCardinalities(String cardinalities)
  {
    this.cardinalities = cardinalities;
  }

  public int getNumTuplesPerWindow()
  {
    return numTuplesPerWindow;
  }

  public void setNumTuplesPerWindow(int numTuplesPerWindow)
  {
    this.numTuplesPerWindow = numTuplesPerWindow;
  }

  public long getTupleCountBeforeQuery()
  {
    return tupleCountBeforeQuery;
  }

  public void setTupleCountBeforeQuery(long tupleCountBeforeQuery)
  {
    this.tupleCountBeforeQuery = tupleCountBeforeQuery;
  }

  public int getNumQueryTuplesPerWindow()
  {
    return numQueryTuplesPerWindow;
  }

  public void setNumQueryTuplesPerWindow(int numQueryTuplesPerWindow)
  {
    this.numQueryTuplesPerWindow = numQueryTuplesPerWindow;
  }
}
