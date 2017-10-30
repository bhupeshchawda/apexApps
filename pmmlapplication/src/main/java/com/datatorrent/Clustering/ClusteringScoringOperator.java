package com.datatorrent.Clustering;

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
import com.datatorrent.pmml.scorer.ClusteringScorer;
import com.datatorrent.pmml.scorer.PMMLScorerFactory;
import com.datatorrent.pmml.utils.PMMLUtils;

public class  ClusteringScoringOperator extends BaseOperator
{

  double minvalue;

  @AutoMetric
  private int cluster1=0;

  @AutoMetric
  private int cluster2=0;

  @AutoMetric
  private int cluster3=0;

  int j;
  int i;
  transient FileSystem fs;
  ClusteringScorer scorer;
  String label="";
  private static final Logger LOG = LoggerFactory.getLogger(ClusteringScoringOperator.class);
  public transient DefaultOutputPort<String> output = new DefaultOutputPort<>();

  public transient DefaultInputPort<String> input = new DefaultInputPort<String>() {
    @Override
    public void setup(Context.PortContext context)
    {

      try {
        i=0;
        j=0;
        Configuration configuration=new Configuration();
        fs=FileSystem.get(configuration);
        PMML pmml = PMMLUtils.getPMML(fs, "/user/devraj/irismodel.pmml");
//    PMML pmml = PMMLUtils.getPMML(FileSystem.newInstance(new Configuration()), "/home/devraj/irismodel.pmml");
        scorer = (ClusteringScorer)PMMLScorerFactory.getScorer(pmml, 1);
        fs.close();
      }catch (Exception e){
        e.printStackTrace();
      }
      super.setup(context);
    }

    @Override
    public void process(String s) {
      String str1[] = s.split(",");
      Map<String, Number> data = Maps.newHashMap();

      data.put("Sepal.Length",Double.parseDouble(str1[0]));
      data.put("Sepal.Width",Double.parseDouble(str1[1]));
      data.put("Petal,Length",Double.parseDouble(str1[0]));
      data.put("Petal.Width",Double.parseDouble(str1[1]));


      minvalue=Double.MAX_VALUE;
      Map<String, Double> scores = scorer.score(data);

      for (Map.Entry<String, Double> entry : scores.entrySet()) {
        if (entry.getValue() < minvalue) {
          minvalue = entry.getValue();
        }
      }

      for (Map.Entry<String, Double> entry : scores.entrySet()){
        if(entry.getValue()==minvalue)
        {
          System.out.println(entry.getKey());
          label=entry.getKey();
        }
      }

      if(label.equals(new String("Cluster 1"))){
        cluster1++;
      }
      else if(label.equals(new String("Cluster 2"))){
        cluster2++;
      }
      else if(label.equals(new String("Cluster 3"))){
        cluster3++;
      }
     output.emit(s+":"+label);
    }
  };

  @Override
  public void endWindow()
  {

  }

  @Override
  public void beginWindow(long windowId)
  {
    super.beginWindow(windowId);
  }
}
