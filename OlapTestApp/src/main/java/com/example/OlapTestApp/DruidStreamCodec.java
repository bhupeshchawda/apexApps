package com.example.OlapTestApp;

import java.util.BitSet;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import com.datatorrent.lib.codec.KryoSerializableStreamCodec;
import com.datatorrent.lib.util.PojoUtils;

/**
 * Created by bhupesh on 21/7/17.
 */
public class DruidStreamCodec extends KryoSerializableStreamCodec<Object>
{
  private List<String> dimensions;
  private int numDimensionsToPartitionOn;
  private transient Map<String, PojoUtils.Getter<Object, String>> getters;

  @Override
  public int getPartition(Object o)
  {
    if (getters == null || getters.isEmpty()) {
      createGetters(o.getClass());
    }
    BitSet bitSet = new BitSet(numDimensionsToPartitionOn);
    for (int i = 0; i < numDimensionsToPartitionOn; i++) {
      String dimensionValue = getters.get(dimensions.get(i)).get(o);
      if (dimensionValue.hashCode() % 2 == 1) {
        bitSet.set(i);
      }
    }
    return convert(bitSet);
  }

  private void createGetters(Class<?> clazz)
  {
    getters = Maps.newHashMap();
    for (String dimension: dimensions) {
      PojoUtils.Getter getter = PojoUtils.createGetter(clazz, dimension, String.class);
      getters.put(dimension, getter);
    }
  }

  public int convert(BitSet bits) {
    int value = 0;
    for (int i = 0; i < bits.length(); i++) {
      value += bits.get(i) ? (1 << i) : 0;
    }
    return value;
  }

  public List<String> getDimensions()
  {
    return dimensions;
  }

  public void setDimensions(List<String> dimensions)
  {
    this.dimensions = dimensions;
  }

  public int getNumDimensionsToPartitionOn()
  {
    return numDimensionsToPartitionOn;
  }

  public void setNumDimensionsToPartitionOn(int numDimensionsToPartitionOn)
  {
    this.numDimensionsToPartitionOn = numDimensionsToPartitionOn;
  }
}


























