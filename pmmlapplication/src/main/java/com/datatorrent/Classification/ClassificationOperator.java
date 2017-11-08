package com.datatorrent.Classification;

import java.util.Map;

import org.dmg.pmml_4_3.PMML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import com.google.common.collect.Maps;

import com.datatorrent.api.AutoMetric;
import com.datatorrent.api.Context;
import com.datatorrent.api.DefaultInputPort;
import com.datatorrent.api.DefaultOutputPort;
import com.datatorrent.common.util.BaseOperator;
import com.datatorrent.pmml.algorithms.classification.SVMScorer;
import com.datatorrent.pmml.scorer.ClassificationScorer;
import com.datatorrent.pmml.scorer.PMMLScorerFactory;
import com.datatorrent.pmml.utils.PMMLUtils;

public class ClassificationOperator extends BaseOperator
{

  @AutoMetric
  private int fraud = 0;

  @AutoMetric
  private int nonfraud = 0;

  @AutoMetric
  private int fraudpermin = 0;

  private String pmmlPath = "";
  int j;
  int i;
  transient FileSystem fs;
  ClassificationScorer scorer;
  String label = "";
  private static final Logger LOG = LoggerFactory.getLogger(ClassificationOperator.class);
  public transient DefaultOutputPort<String> output = new DefaultOutputPort<>();

  public transient DefaultInputPort<String> input = new DefaultInputPort<String>()
  {
    @Override
    public void setup(Context.PortContext context)
    {
      try {
        i = 0;
        j = 0;
        Configuration configuration = new Configuration();
        fs = FileSystem.get(configuration);
        PMML pmml = PMMLUtils.getPMML(fs, pmmlPath);
//    PMML pmml = PMMLUtils.getPMML(FileSystem.newInstance(new Configuration()), "/home/devraj/fraudmodel.pmml");
        scorer = (ClassificationScorer)PMMLScorerFactory.getScorer(pmml, 0);
        fs.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
      super.setup(context);
    }

    @Override
    public void process(String s)
    {
      String str1[] = s.split(",");
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

      Map<String, Number> scores = scorer.score(data);

      for (Map.Entry<String, Number> entry : scores.entrySet()) {
        LOG.info(entry.getKey());

        if (entry.getKey().equalsIgnoreCase("nonfraud")) {
          label = entry.getKey();
          nonfraud++;
        } else if (entry.getKey().equalsIgnoreCase("fraud")) {
          label = entry.getKey();
          fraud++;
        }
      }
      output.emit(s + ":" + label);
    }
  };

  @Override
  public void endWindow()
  {
    i++;
    if (i % 20 == 0) {
      j++;
      fraudpermin = fraud / j;
    }
  }

  @Override
  public void beginWindow(long windowId)
  {
    super.beginWindow(windowId);
  }

  public String getPmmlPath()
  {
    return pmmlPath;
  }

  public void setPmmlPath(String pmmlPath)
  {
    this.pmmlPath = pmmlPath;
  }
}

