package com.datatorrent.Classification;

import java.util.HashMap;
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
import com.datatorrent.pmml.scorer.ClassificationScorer;
import com.datatorrent.pmml.scorer.PMMLScorerFactory;
import com.datatorrent.pmml.utils.PMMLUtils;

public class  ScoringOperator extends BaseOperator
{
  @AutoMetric
  private int no=0;

  @AutoMetric
  private int yes=0;

  transient FileSystem fs;
  ClassificationScorer scorer;
  private static final Logger LOG = LoggerFactory.getLogger(ScoringOperator.class);

 public transient DefaultOutputPort<String> output = new DefaultOutputPort<>();
  public transient DefaultInputPort<String> input = new DefaultInputPort<String>() {
    @Override
    public void setup(Context.PortContext context)
    {

  try {
    Configuration configuration=new Configuration();
    fs=FileSystem.get(configuration);
        PMML pmml = PMMLUtils.getPMML(fs, "/user/devraj/naivebayesmodel.pmml");
//    PMML pmml = PMMLUtils.getPMML(FileSystem.newInstance(new Configuration()), "/home/devraj/svmiris.xml");
        scorer = (ClassificationScorer)PMMLScorerFactory.getScorer(pmml, 0);
        fs.close();
      }catch (Exception e){
        e.printStackTrace();
      }
      super.setup(context);
    }

    @Override
    public void process(String s) {
      String str1[] = s.split(",");
      String label="";
      Map<String, Number> data = Maps.newHashMap();

      data.put("gre",Double.parseDouble(str1[0]));
      data.put("gpa",Double.parseDouble(str1[1]));
      data.put("rank",Double.parseDouble(str1[2]));
      Map<String, Double> scores = scorer.score(data);

      Number maxValue = Integer.MIN_VALUE;
      for (Number value : scores.values()) {
        if (value.doubleValue() > maxValue.doubleValue()) {
          maxValue = value;
        }
      }

      for (Map.Entry<String, Double> entry : scores.entrySet()) {
          if(entry.getValue()==maxValue)
          {

            LOG.info(entry.getKey());
            if(entry.getKey().equalsIgnoreCase("0"))
            {
              label="no";
              no++;
            }
            else if(entry.getKey().equalsIgnoreCase("1")){
              label="yes";
              yes++;
            }

          }
      }
      output.emit(s+":"+label);
    }

  };

}
