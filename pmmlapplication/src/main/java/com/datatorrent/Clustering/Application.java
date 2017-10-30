package com.datatorrent.Clustering;

import org.apache.hadoop.conf.Configuration;
import com.datatorrent.api.DAG;
import com.datatorrent.api.StreamingApplication;
import com.datatorrent.api.annotation.ApplicationAnnotation;
import com.datatorrent.lib.io.fs.AbstractFileOutputOperator;

@ApplicationAnnotation(name="PMMLApplicationClustering")
public class Application implements StreamingApplication
{

  @Override
  public void populateDAG(DAG dag, Configuration conf)
  {

    FileInputOp inputOp=dag.addOperator("inputOp",FileInputOp.class);
    ClusteringScoringOperator scoringOperator=dag.addOperator("scoringOperator",ClusteringScoringOperator.class);
//    AbstractFileOutputOperator abstractFileOutputOperator=dag.addOperator("abstractFileOutputOperator",AbstractFileOutputOperator.class);

    FileOutputOp fileOutPutOp=dag.addOperator("fileOutPutOp",FileOutputOp.class);

    dag.addStream("scoringdata", inputOp.output, scoringOperator.input).setLocality(DAG.Locality.CONTAINER_LOCAL);
    dag.addStream("printoutput",scoringOperator.output,fileOutPutOp.input);
  }
}
