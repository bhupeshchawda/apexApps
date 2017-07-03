package com.example.twitter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import com.datatorrent.api.AutoMetric;
import com.datatorrent.common.util.Pair;

/**
 * Created by bhupesh on 3/7/17.
 */
public class CCPAggregator implements AutoMetric.Aggregator, Serializable
{
  private Map<String, Object> result = Maps.newHashMap();

  @Override
  public Map<String, Object> aggregate(long windowId, Collection<AutoMetric.PhysicalMetricsContext> physicalMetrics)
  {
    Collection<Collection<Pair<String, Object>>> ret = new ArrayList<>();

    for (AutoMetric.PhysicalMetricsContext pmc : physicalMetrics) {
      for (Map.Entry<String, Object> metrics : pmc.getMetrics().entrySet()) {
        String key = metrics.getKey();
        Object value = metrics.getValue();
        switch (key) {
          case "data":
            Collection<Collection<Pair<String, Object>>> temp  = (Collection<Collection<Pair<String, Object>>>)value;
            ret.addAll(temp);
            System.out.println("Data is " + ret);
            break;
        }
      }
    }

    if (ret.size() > 0) {
      result.put("data", ret);
      System.out.println("Returning Data " + ret);
    }
    return result;

  }
}
