package com.example.twitter;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.datatorrent.api.AutoMetric;
import com.datatorrent.api.DefaultInputPort;
import com.datatorrent.common.util.BaseOperator;
import com.datatorrent.common.util.Pair;

/**
 * Created by bhupesh on 4/7/17.
 */
public class TopCounterOperator extends BaseOperator
{
  public static final String KEY_HASHTAG = "hashtag";
  public static final String KEY_COUNT = "count";
  private int numWindows = 60;
  private long currentWindowId;
  private int count = 0;

  @AutoMetric
  private Collection<Collection<Pair<String, Object>>> data = Lists.newArrayList();
  private Map<Long, String> rawHashTags = Maps.newLinkedHashMap();
  private String hashtagCsv;

  @Override
  public void beginWindow(long windowId)
  {
    this.currentWindowId = windowId;
    hashtagCsv = "";
  }

  public final transient DefaultInputPort<String> input = new DefaultInputPort<String>()
  {
    @Override
    public void process(String tuple)
    {
      hashtagCsv += tuple.trim() + ",";
    }
  };

  @Override
  public void endWindow()
  {
    if (rawHashTags.size() > numWindows) {
      Long key = rawHashTags.keySet().iterator().next();
      rawHashTags.remove(key);
    }
    rawHashTags.put(currentWindowId, hashtagCsv);

    Map<String, Long> counts = Maps.newHashMap();
    for (String hashCsv : rawHashTags.values()) {
      for (String ht : hashCsv.split(",")) {
        if (ht != null && !ht.isEmpty()) {
          if (counts.containsKey(ht)) {
            counts.put(ht, counts.get(ht) + 1);
          } else {
            counts.put(ht, 1L);
          }
        }
      }
    }
    sortByValue(counts);

    data.clear();
    Iterator<Map.Entry<String, Long>> itr = counts.entrySet().iterator();
    for (int i = 0; i < 10; i++) {
      if (itr.hasNext()) {
        Map.Entry<String,Long> ht = itr.next();
        Pair<String, Object> p1 = new Pair<>(KEY_HASHTAG, (Object)ht.getKey());
        Pair<String, Object> p2 = new Pair<>(KEY_COUNT, (Object)ht.getValue());
        Collection<Pair<String,Object>> row = Lists.newArrayList();
        row.add(p1);
        row.add(p2);
        data.add(row);
      }
    }

//    // Just for testing
//    topN.clear();
//    Collection<Pair<String,Object>> row = Lists.newArrayList();
//    row.add(new Pair<String, Object>(KEY_COUNT,++count));
//    topN.add(row);
  }

  private static Map<String, Long> sortByValue(Map<String, Long> unsortMap) {
    List<Map.Entry<String, Long>> list =
      new LinkedList<Map.Entry<String, Long>>(unsortMap.entrySet());

    Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {
      public int compare(Map.Entry<String, Long> o1,
        Map.Entry<String, Long> o2) {
        return (o1.getValue()).compareTo(o2.getValue());
      }
    });

    Map<String, Long> sortedMap = new LinkedHashMap<String, Long>();
    for (Map.Entry<String, Long> entry : list) {
      sortedMap.put(entry.getKey(), entry.getValue());
    }
    return sortedMap;
  }

  public int getNumWindows()
  {
    return numWindows;
  }

  public void setNumWindows(int numWindows)
  {
    this.numWindows = numWindows;
  }

  public long getCurrentWindowId()
  {
    return currentWindowId;
  }

  public void setCurrentWindowId(long currentWindowId)
  {
    this.currentWindowId = currentWindowId;
  }

  public Collection<Collection<Pair<String, Object>>> getData()
  {
    return data;
  }

  public void setData(Collection<Collection<Pair<String, Object>>> topN)
  {
    this.data = topN;
  }
}
