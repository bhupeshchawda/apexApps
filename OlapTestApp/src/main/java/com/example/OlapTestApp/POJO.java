package com.example.OlapTestApp;

/**
 * Created by bhupesh on 20/7/17.
 */
public class POJO
{
  public String dataSource;
  public long time;
  public String a;
  public String b;
  public String c;
  public long x;
  public long y;

  public POJO()
  {
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

  public String getDataSource()
  {
    return dataSource;
  }

  public void setDataSource(String dataSource)
  {
    this.dataSource = dataSource;
  }

  @Override
  public String toString()
  {
    return "POJO{" +
      "time=" + time +
      ", a='" + a + '\'' +
      ", b='" + b + '\'' +
      ", c='" + c + '\'' +
      ", x=" + x +
      ", y=" + y +
      '}';
  }
}
