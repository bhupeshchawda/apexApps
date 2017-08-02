package com.example.OlapBenchmark;

import org.codehaus.jettison.json.JSONException;

import com.example.OlapBenchmark.datagen.QueryGeneratorUtility;

import com.datatorrent.api.Context;
import com.datatorrent.api.DefaultInputPort;
import com.datatorrent.api.DefaultOutputPort;
import com.datatorrent.common.util.BaseOperator;

/**
 * Created by bhupesh on 1/8/17.
 */
public class BenchmarkQueryInput extends BaseOperator
{
  // config
  private int numDimensions;
  private int queryDimensions;

  // operator private
  private long countThisWindow = 0;
  private int queryId = 0;

  public final transient DefaultInputPort<Boolean> trigger = new DefaultInputPort<Boolean>()
  {
    @Override
    public void process(Boolean aBoolean)
    {
      emitQuery();
    }
  };

  public final transient DefaultOutputPort<String> queryOutput = new DefaultOutputPort<>();

  public BenchmarkQueryInput()
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
  }

  public void emitQuery()
  {
    try {
      String query = QueryGeneratorUtility.getGroupByQueryOnNDimensions(numDimensions, queryDimensions, ++queryId);
      queryOutput.emit(query);
    } catch (JSONException e) {
      throw new RuntimeException("Exception in generating query ", e);
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

  public int getQueryDimensions()
  {
    return queryDimensions;
  }

  public void setQueryDimensions(int queryDimensions)
  {
    this.queryDimensions = queryDimensions;
  }
}
