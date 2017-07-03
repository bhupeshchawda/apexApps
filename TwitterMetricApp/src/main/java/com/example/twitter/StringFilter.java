package com.example.twitter;

import com.datatorrent.api.DefaultInputPort;
import com.datatorrent.api.DefaultOutputPort;
import com.datatorrent.common.util.BaseOperator;

/**
 * Created by bhupesh on 3/7/17.
 */
public class StringFilter extends BaseOperator
{
  public final transient DefaultInputPort<String> input = new DefaultInputPort<String>()
  {
    @Override
    public void process(String tuple)
    {
      if (!tuple.trim().isEmpty()) {
        output.emit(tuple);
      }
    }
  };
  public final transient DefaultOutputPort<String> output = new DefaultOutputPort<>();
}
