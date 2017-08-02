package com.example.OlapBenchmark.datagen;

import com.example.OlapBenchmark.pojo.POJO;
import com.example.OlapBenchmark.pojo.POJO7;
import com.example.OlapBenchmark.pojo.POJO3;
import com.example.OlapBenchmark.pojo.POJO5;

/**
 * Created by bhupesh on 1/8/17.
 */
public class POJOGenerationFromData
{
  public static POJO getPOJO(String generatedCsv, int numDimensions)
  {
    String[] s = generatedCsv.trim().split(":");
    if (numDimensions == 3) {
      POJO3 pojo = new POJO3();
      pojo.time = System.currentTimeMillis();
      pojo.a = s[0];
      pojo.b = s[1];
      pojo.c = s[2];
      pojo.x = Long.parseLong(s[3]);
      pojo.y = Long.parseLong(s[4]);
      pojo.z = Long.parseLong(s[5]);
      return pojo;
    } else if (numDimensions == 5) {
      POJO5 pojo = new POJO5();
      pojo.time = System.currentTimeMillis();
      pojo.a = s[0];
      pojo.b = s[1];
      pojo.c = s[2];
      pojo.d = s[3];
      pojo.e = s[4];
      pojo.x = Long.parseLong(s[5]);
      pojo.y = Long.parseLong(s[6]);
      pojo.z = Long.parseLong(s[7]);
      return pojo;
    } else if (numDimensions == 7) {
      POJO7 pojo = new POJO7();
      pojo.time = System.currentTimeMillis();
      pojo.a = s[0];
      pojo.b = s[1];
      pojo.c = s[2];
      pojo.d = s[3];
      pojo.e = s[4];
      pojo.f = s[5];
      pojo.g = s[6];
      pojo.x = Long.parseLong(s[7]);
      pojo.y = Long.parseLong(s[8]);
      pojo.z = Long.parseLong(s[9]);
      return pojo;
    }
    return null;
  }

}
