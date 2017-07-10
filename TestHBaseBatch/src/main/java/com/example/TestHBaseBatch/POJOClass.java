package com.example.TestHBaseBatch;

/**
 * Created by bhupesh on 10/7/17.
 */
public class POJOClass
{
  public String id;
  public String name;

  public String getId()
  {
    return id;
  }

  public void setId(String id)
  {
    this.id = id;
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  @Override
  public String toString()
  {
    return "POJOClass{" +
      "id='" + id + '\'' +
      ", name='" + name + '\'' +
      '}';
  }
}
