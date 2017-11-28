/**
 * Put your copyright and license info here.
 */
package com.example.PythonTest;

import org.apache.hadoop.conf.Configuration;

import com.datatorrent.api.annotation.ApplicationAnnotation;
import com.datatorrent.api.StreamingApplication;
import com.datatorrent.api.DAG;
import dt.operator.scoring.TestOperator;

@ApplicationAnnotation(name="PythonApp")
public class Application implements StreamingApplication
{

  @Override
  public void populateDAG(DAG dag, Configuration conf)
  {
    Generator gen = dag.addOperator("Generator", Generator.class);
    TestOperator op = dag.addOperator("Python", TestOperator.class);
    dag.addStream("scoring-data", gen.output, op.inA);
  }
}
