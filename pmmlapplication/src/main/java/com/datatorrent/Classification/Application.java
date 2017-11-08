/**
 * Put your copyright and license info here.
 */
package com.datatorrent.Classification;

import org.apache.apex.malhar.lib.fs.GenericFileOutputOperator;
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
    ClassificationOperator classificationOperator=dag.addOperator("classificationOperator",ClassificationOperator.class);
//    FileOutputOp fileOutPutOp=dag.addOperator("fileOutPutOp",FileOutputOp.class);
    GenericFileOutputOperator.StringFileOutputOperator fileOutPutOp=dag.addOperator("fileOutPutOp", GenericFileOutputOperator.StringFileOutputOperator.class);
//    AbstractFileOutputOperator abstractFileOutputOperator=dag.addOperator("a")

    dag.addStream("scoringdata", inputOp.output, classificationOperator.input).setLocality(Locality.CONTAINER_LOCAL);
    dag.addStream("printoutput",classificationOperator.output,fileOutPutOp.input);
  }
}
