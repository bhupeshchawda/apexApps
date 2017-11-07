package com.example.TestMetricStore;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;

import com.datatorrent.api.Context;
import com.datatorrent.api.DefaultOutputPort;
import com.datatorrent.api.InputOperator;
import com.datatorrent.common.util.BaseOperator;
import com.datatorrent.store.api.Record;
import com.datatorrent.store.reader.impl.HDFSStoreReader;

/**
 * Created by bhupesh on 6/11/17.
 */
public class StoreReader extends BaseOperator implements InputOperator
{

  private transient HDFSStoreReader reader;
  private transient Configuration conf;
  long count = 0;

  public transient final DefaultOutputPort<POJO> output = new DefaultOutputPort<>();

  @Override
  public void setup(Context.OperatorContext context)
  {
    conf = new Configuration();
    try {
      reader = new HDFSStoreReader("/tmp", conf);
      reader.init("TEST", conf);
    } catch (IOException e) {
      throw new RuntimeException("Exception in setting up reader", e);
    }
  }

  @Override
  public void emitTuples()
  {
    Record<POJO> pojo = null;
    try {
      pojo = reader.getNextFullRecord();
    } catch (IOException e) {
      throw new RuntimeException("Exception in reading record ", e);
    }
    if (pojo != null) {
      output.emit(pojo.getRecord());
      count++;
    } else {
      System.out.println("Done Emitting");
    }
  }

  @Override
  public void endWindow()
  {
    System.out.println(count);
  }
}
