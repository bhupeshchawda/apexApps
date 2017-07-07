package com.example.twitter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

import com.datatorrent.metrics.api.aggregation.NumberAggregate;
import com.datatorrent.metrics.api.appmetrics.AppMetricProcessor;

/**
 * Created by bhupesh on 6/7/17.
 */
public class AppMetricAggregatorImpl implements AppMetricProcessor.AppMetricAggregator
{
  @Override
  public Map<String, Object> aggregateAppLevelMetrics(Map<String, Map<String, NumberAggregate>> aggregates)
  {
    Object incoming = aggregates.get("HashtagExtractor").get("numTags").getSum();
    Object english = aggregates.get("Filter").get("englishTags").getSum();

    Map<String, Object> output = Maps.newHashMap();
    if((long)incoming != 0){
      double percentFiltered = ((long)english * 100.0) / (long)incoming;
      output.put("PercentEnglish", percentFiltered);
    }
    return output;
  }

  @Override
  public Set<String> getRequiredOperators()
  {
    return new HashSet<>(Arrays.asList("HashtagExtractor", "Filter"));
  }
}
