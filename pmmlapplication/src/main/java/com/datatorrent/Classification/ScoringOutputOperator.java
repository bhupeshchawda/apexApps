package com.datatorrent.Classification;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datatorrent.api.AutoMetric;
import com.datatorrent.api.DefaultInputPort;
import com.datatorrent.common.util.BaseOperator;

/**
 * Created by bhupesh on 8/11/17.
 */
public class ScoringOutputOperator extends BaseOperator
{
  public final static Logger logger = LoggerFactory.getLogger(ScoringOutputOperator.class);

  @AutoMetric
  private int fraud = 0;

  @AutoMetric
  private int nonfraud = 0;

  @AutoMetric
  private int fraudpermin = 0;

  @Override
  public void beginWindow(long windowId)
  {
    fraud = 0;
    nonfraud = 0;
  }

  public final transient DefaultInputPort<Object> input = new DefaultInputPort<Object>()
  {
    @Override
    public void process(Object tuple)
    {
      logger.info("{}", tuple);
      Map<String, Object> t = (Map<String, Object>)tuple;

      if (t.get("Fraud") != null) {
        fraud++;
      } else if (t.get("Nonfraud") != null) {
        nonfraud++;
      } else {
        logger.info("ERROR");
      }
    }
  };
}
