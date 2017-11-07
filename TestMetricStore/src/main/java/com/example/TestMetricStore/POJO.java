package com.example.TestMetricStore;

/**
 * Created by bhupesh on 6/11/17.
 */
public class POJO
{
  private long timestamp;
  private Double random;

  public POJO()
  {
  }

  public POJO(Double num)
  {
    this.timestamp = System.currentTimeMillis();
    this.random = num;
  }

  public long getTimestamp()
  {
    return timestamp;
  }

  public void setTimestamp(long timestamp)
  {
    this.timestamp = timestamp;
  }

  public Double getRandom()
  {
    return random;
  }

  public void setRandom(Double random)
  {
    this.random = random;
  }

  @Override
  public String toString()
  {
    return "POJO{" +
      "timestamp=" + timestamp +
      ", random=" + random +
      '}';
  }
}
