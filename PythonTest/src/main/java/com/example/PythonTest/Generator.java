package com.example.PythonTest;

import java.util.Random;

import com.datatorrent.api.DefaultOutputPort;
import com.datatorrent.api.InputOperator;
import com.datatorrent.common.util.BaseOperator;

/**
 * Created by bhupesh on 16/11/17.
 */
public class Generator extends BaseOperator implements InputOperator
{

  public final transient DefaultOutputPort<POJO> output = new DefaultOutputPort<>();
  public Random r = new Random();

  @Override
  public void emitTuples()
  {
    output.emit(new POJO(r.nextDouble(), r.nextDouble(), r.nextDouble(), r.nextDouble()));
    try {
      Thread.sleep(10);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
