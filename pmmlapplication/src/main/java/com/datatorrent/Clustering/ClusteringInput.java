package com.datatorrent.Clustering;

import java.util.Map;

import org.apache.apex.malhar.lib.fs.LineByLineFileInputOperator;

import com.google.common.collect.Maps;

import com.datatorrent.api.DefaultOutputPort;

public class ClusteringInput extends LineByLineFileInputOperator
{
  public int maxTuplesPerWindow = 100;
  public transient int tuplesThisWindow = 0;

  public transient final DefaultOutputPort<Map<String, Object>> scoringOut = new DefaultOutputPort<>();

  @Override
  public void beginWindow(long windowId)
  {
    tuplesThisWindow = 0;
  }

  @Override
  public void emitTuples()
  {
    if (++tuplesThisWindow <= maxTuplesPerWindow) {
      super.emitTuples();
    } else {
      try {
        Thread.sleep(10);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  protected void emit(String tuple)
  {
    String str1[] = tuple.split(",");
    Map<String, Object> data = Maps.newHashMap();
    data.put("Sepal.Length", Double.parseDouble(str1[0]));
    data.put("Sepal.Width", Double.parseDouble(str1[1]));
    data.put("Petal,Length", Double.parseDouble(str1[0]));
    data.put("Petal.Width", Double.parseDouble(str1[1]));
    scoringOut.emit(data);
  }

  public int getMaxTuplesPerWindow()
  {
    return maxTuplesPerWindow;
  }

  public void setMaxTuplesPerWindow(int maxTuplesPerWindow)
  {
    this.maxTuplesPerWindow = maxTuplesPerWindow;
  }
}
