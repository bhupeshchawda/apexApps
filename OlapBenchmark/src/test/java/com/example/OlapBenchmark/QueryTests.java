package com.example.OlapBenchmark;

import org.codehaus.jettison.json.JSONException;
import org.junit.Test;

import com.example.OlapBenchmark.datagen.QueryGeneratorUtility;

/**
 * Created by bhupesh on 2/8/17.
 */
public class QueryTests
{
  @Test
  public void testLog()
  {
    System.out.println(log2(8));
    System.out.println(log2(1024));
    System.out.println(log2(1023));
    System.out.println(log2(9));
    System.out.println(log2(7));
  }

  private int log2(int n)
  {
    int log = (int)(Math.log(n) / Math.log(2));
    return Math.pow(2, log) <= n ? log + 1 : log;
  }

  @Test
  public void testQuery1() throws JSONException
  {
    String query = QueryGeneratorUtility.getGroupByQueryOnNDimensions(5, 2, 1);
    System.out.println(query);
  }
}
