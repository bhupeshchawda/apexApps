package com.datatorrent.Clustering;

import org.apache.apex.malhar.lib.fs.GenericFileOutputOperator;
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
    ClusteringOperator scoringOperator=dag.addOperator("scoringOperator",ClusteringOperator.class);
    GenericFileOutputOperator.StringFileOutputOperator fileOutPutOp=dag.addOperator("fileOutPutOp",GenericFileOutputOperator.StringFileOutputOperator.class);

    dag.addStream("scoringdata", inputOp.output, scoringOperator.input).setLocality(DAG.Locality.CONTAINER_LOCAL);
    dag.addStream("printoutput",scoringOperator.output,fileOutPutOp.input);
  }
}
