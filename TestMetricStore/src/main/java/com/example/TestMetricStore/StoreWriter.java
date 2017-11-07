package com.example.TestMetricStore;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;

import com.datatorrent.api.Context;
import com.datatorrent.api.DefaultInputPort;
import com.datatorrent.common.util.BaseOperator;
import com.datatorrent.store.api.Record;
import com.datatorrent.store.writer.impl.HDFSStoreWriter;

/**
 * Created by bhupesh on 6/11/17.
 */
public class StoreWriter extends BaseOperator
{

  private transient HDFSStoreWriter writer;
  private transient Configuration conf;

  @Override
  public void setup(Context.OperatorContext context)
  {
    conf = new Configuration();
    try {
      writer = new HDFSStoreWriter("/tmp", conf, true);
      writer.init("TEST", conf);
    } catch (IOException e) {
      throw new RuntimeException("Exception in creating store", e);
    }
  }

  public transient final DefaultInputPort<Double> input = new DefaultInputPort<Double>()
  {
    @Override
    public void process(Double tuple)
    {
      try {
        writer.writeRecord(new Record(new POJO(tuple)));
      } catch (IOException e) {
        throw new RuntimeException("Exception in writing record " + tuple, e);
      }
    }
  };

  @Override
  public void teardown()
  {
    try {
      writer.close();
    } catch (IOException e) {
      throw new RuntimeException("Exception in closing writer", e);
    }
  }
}
