package com.example.twitter;

import java.util.Map;

import com.google.common.collect.Maps;

import com.datatorrent.metrics.api.appmetrics.DefaultAppMetricProcessor;

/**
 * Created by bhupesh on 3/7/17.
 */
public class AppMetricComputer extends DefaultAppMetricProcessor
{
  @Override
  public Map<String, Object> computeAppLevelMetrics(Map<String, Map<String, Object>> completedMetrics)
  {
    System.out.println("Completed: " + completedMetrics);
    Map<String, Object> output = Maps.newHashMap();
    if ( completedMetrics != null &&
      completedMetrics.containsKey("HashtagExtractor") &&
      completedMetrics.containsKey("Filter")) {
      if (completedMetrics.get("HashtagExtractor").get("numTags") == null ||
        completedMetrics.get("Filter").get("englishTags") == null) {
        return output;
      }
      Long incoming = (long) completedMetrics.get("HashtagExtractor").get("numTags");
      Long english = (long) completedMetrics.get("Filter").get("englishTags");

      if(incoming != null && english != null){
        if(incoming != 0){
          double percentFiltered = (english * 100.0) / incoming;
          output.put("PercentEnglish", percentFiltered);
        }
      }
    }
    return output;
  }
}
