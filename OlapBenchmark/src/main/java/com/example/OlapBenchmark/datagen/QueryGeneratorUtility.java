package com.example.OlapBenchmark.datagen;

import java.text.SimpleDateFormat;
import java.util.BitSet;
import java.util.Date;
import java.util.Random;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import static com.example.OlapBenchmark.datagen.BenchmarkDataGeneratorUtility.base;

/**
 * Created by bhupesh on 2/8/17.
 */
public class QueryGeneratorUtility
{

  public static Random random = new Random();

  public static String getGroupByQueryOnNDimensions(int totalDimensions, int numDimensions, int queryId) throws JSONException
  {
    BitSet bitSet = new BitSet(totalDimensions);
    for (int i = 0; i < numDimensions; i++) {
      int bit = random.nextInt(totalDimensions);
      if (!bitSet.get(bit)) {
        bitSet.set(bit);
      } else {
        i--;
      }
    }
    return getRandomGroupByQuery(bitSetToInt(bitSet), queryId);
  }

  private static int bitSetToInt(BitSet bitSet)
  {
    int bitInteger = 0;
    for(int i = 0 ; i < 32; i++)
      if(bitSet.get(i))
        bitInteger |= (1 << i);
    return bitInteger;
  }

  private static int log2(int n)
  {
    int log = (int)(Math.log(n) / Math.log(2));
    return Math.pow(2, log) <= n ? log + 1 : log;
  }

  /**
   * Based on number of set bits in the given number - n
   */
  public static String getRandomGroupByQuery(int n, int queryId) throws JSONException
  {
    JSONObject json = new JSONObject();
    json.put("context", new JSONObject().put("queryId", ++queryId+""));
    json.put("queryType","groupBy");
    json.put("dataSource","");
    json.put("granularity","MINUTE");
    json.put("aggregations", new JSONArray()
      .put(new JSONObject().put("type","longSum").put("name","sum_agg_x").put("fieldName","sum_agg_x"))
      .put(new JSONObject().put("type","longSum").put("name","sum_agg_y").put("fieldName","sum_agg_y")));
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    json.put("intervals",sdf.format(0) + "/" + sdf.format(new Date()));

    JSONArray dimensions = new JSONArray();
    int count = log2(n);
    for (int j = 0; j < count; j++) {
      if ((n >> j  & 0x1) == 1) {
        dimensions.put(String.valueOf((char)(base + j)));
      }
    }
    json.put("dimensions", dimensions);
    return json.toString();
  }

}
