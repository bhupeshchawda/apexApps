/**
 * Put your copyright and license info here.
 */
package com.example.TestMetricStore;

import org.apache.hadoop.conf.Configuration;

import com.datatorrent.api.DAG;
import com.datatorrent.api.DAG.Locality;
import com.datatorrent.api.StreamingApplication;
import com.datatorrent.api.annotation.ApplicationAnnotation;
import com.datatorrent.lib.io.ConsoleOutputOperator;

@ApplicationAnnotation(name="Reader")
public class ReaderApp implements StreamingApplication
{

  @Override
  public void populateDAG(DAG dag, Configuration conf)
  {
    // Sample DAG with 2 operators
    // Replace this code with the DAG you want to build
    StoreReader reader = dag.addOperator("Reader", new StoreReader());
    ConsoleOutputOperator output = dag.addOperator("Console", new ConsoleOutputOperator());

    dag.addStream("data", reader.output, output.input);
  }
}
