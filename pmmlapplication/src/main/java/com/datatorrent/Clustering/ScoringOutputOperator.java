package com.datatorrent.Clustering;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datatorrent.api.AutoMetric;
import com.datatorrent.api.DefaultInputPort;
import com.datatorrent.common.util.BaseOperator;

public class ScoringOutputOperator extends BaseOperator
{
  public final static Logger logger = LoggerFactory.getLogger(com.datatorrent.Classification.ScoringOutputOperator.class);

  @AutoMetric
  private int cluster1 = 0;

  @AutoMetric
  private int cluster2 = 0;

  @AutoMetric
  private int cluster3 = 0;

  @Override
  public void beginWindow(long windowId)
  {
   cluster1=0;
    cluster2=0;
    cluster3=0;
  }

  public final transient DefaultInputPort<Object> input = new DefaultInputPort<Object>()
  {
    String label="";
    @Override
    public void process(Object tuple)
    {
      double minvalue = Double.MAX_VALUE;
      logger.info("{}", tuple);
      Map<String, Object> t = (Map<String, Object>)tuple;

      for (Map.Entry<String, Object> entry : t.entrySet()) {
        if (new Double(entry.getValue().toString()) < minvalue) {
          minvalue = new Double(entry.getValue().toString());
        }
      }

      for (Map.Entry<String, Object> entry : t.entrySet()) {
        if (new Double(entry.getValue().toString()) == minvalue) {
          System.out.println(entry.getKey());
          label = entry.getKey();
        }
      }

      if (label.equals(new String("Cluster 1"))) {
        cluster1++;
      } else if (label.equals(new String("Cluster 2"))) {
        cluster2++;
      } else if (label.equals(new String("Cluster 3"))) {
        cluster3++;
      }
    }
  };
}
