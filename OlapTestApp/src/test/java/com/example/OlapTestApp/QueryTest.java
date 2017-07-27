package com.example.OlapTestApp;

import org.junit.Test;

/**
 * Created by bhupesh on 20/7/17.
 */
public class QueryTest
{
  @Test
  public void testBits()
  {
    int n = 64;
    int count = (int)(Math.log(n) / Math.log(2));
    System.out.println(count);
    System.out.println(n >> count);
    System.out.println(n >> count - 1);
  }
}
