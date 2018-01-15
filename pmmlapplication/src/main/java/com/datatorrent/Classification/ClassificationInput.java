package com.datatorrent.Classification;

import java.util.Map;

import org.apache.apex.malhar.lib.fs.LineByLineFileInputOperator;

import com.google.common.collect.Maps;

import com.datatorrent.api.DefaultOutputPort;

/**
 * Created by bhupesh on 8/11/17.
 */
public class ClassificationInput extends LineByLineFileInputOperator
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

    data.put("Time", Double.parseDouble(str1[0]));
    data.put("V1", Double.parseDouble(str1[1]));
    data.put("V2", Double.parseDouble(str1[2]));
    data.put("V3", Double.parseDouble(str1[3]));
    data.put("V4", Double.parseDouble(str1[4]));
    data.put("V5", Double.parseDouble(str1[5]));
    data.put("V6", Double.parseDouble(str1[6]));
    data.put("V7", Double.parseDouble(str1[7]));
    data.put("V8", Double.parseDouble(str1[8]));
    data.put("V9", Double.parseDouble(str1[9]));
    data.put("V10", Double.parseDouble(str1[10]));
    data.put("V11", Double.parseDouble(str1[11]));
    data.put("V12", Double.parseDouble(str1[12]));
    data.put("V13", Double.parseDouble(str1[13]));
    data.put("V14", Double.parseDouble(str1[14]));
    data.put("V15", Double.parseDouble(str1[15]));
    data.put("V16", Double.parseDouble(str1[16]));
    data.put("V17", Double.parseDouble(str1[17]));
    data.put("V18", Double.parseDouble(str1[18]));
    data.put("V19", Double.parseDouble(str1[19]));
    data.put("V20", Double.parseDouble(str1[20]));
    data.put("V21", Double.parseDouble(str1[21]));
    data.put("V22", Double.parseDouble(str1[22]));
    data.put("V23", Double.parseDouble(str1[23]));
    data.put("V24", Double.parseDouble(str1[24]));
    data.put("V25", Double.parseDouble(str1[25]));
    data.put("V26", Double.parseDouble(str1[26]));
    data.put("V27", Double.parseDouble(str1[27]));
    data.put("V28", Double.parseDouble(str1[28]));
    data.put("Amount", Double.parseDouble(str1[29]));

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
