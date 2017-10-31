/**
 * Put your copyright and license info here.
 */
package com.datatorrent.Classification;

import org.apache.hadoop.conf.Configuration;

import com.datatorrent.api.annotation.ApplicationAnnotation;
import com.datatorrent.api.StreamingApplication;
import com.datatorrent.api.DAG;
import com.datatorrent.api.DAG.Locality;
import com.datatorrent.lib.io.fs.AbstractFileOutputOperator;

@ApplicationAnnotation(name="PMMLApplicationClassification")
public class Application implements StreamingApplication
{

  @Override
  public void populateDAG(DAG dag, Configuration conf)
  {

    FileInputOp inputOp=dag.addOperator("inputOp",FileInputOp.class);
    ScoringOperator scoringOperator=dag.addOperator("scoringOperator",ScoringOperator.class);
    FileOutputOp fileOutPutOp=dag.addOperator("fileOutPutOp",FileOutputOp.class);
//    AbstractFileOutputOperator abstractFileOutputOperator=dag.addOperator("a")

    dag.addStream("scoringdata", inputOp.output, scoringOperator.input).setLocality(Locality.CONTAINER_LOCAL);
    dag.addStream("printoutput",scoringOperator.output,fileOutPutOp.input);
  }
}
