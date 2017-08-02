package com.example.OlapBenchmark.pojo;

/**
 * Created by bhupesh on 20/7/17.
 */
public class POJO3 extends POJO
{
  public long time;
  public String a;
  public String b;
  public String c;
  public long x;
  public long y;
  public long z;

  public POJO3()
  {
  }

  @Override
  public String toString()
  {
    return "POJO3{" +
      "time=" + time +
      ", a='" + a + '\'' +
      ", b='" + b + '\'' +
      ", c='" + c + '\'' +
      ", x=" + x +
      ", y=" + y +
      ", z=" + z +
      '}';
  }

  public long getTime()
  {
    return time;
  }

  public void setTime(long time)
  {
    this.time = time;
  }

  public String getA()
  {
    return a;
  }

  public void setA(String a)
  {
    this.a = a;
  }

  public String getB()
  {
    return b;
  }

  public void setB(String b)
  {
    this.b = b;
  }

  public String getC()
  {
    return c;
  }

  public void setC(String c)
  {
    this.c = c;
  }

  public long getX()
  {
    return x;
  }

  public void setX(long x)
  {
    this.x = x;
  }

  public long getY()
  {
    return y;
  }

  public void setY(long y)
  {
    this.y = y;
  }

  public long getZ()
  {
    return z;
  }

  public void setZ(long z)
  {
    this.z = z;
  }
}
