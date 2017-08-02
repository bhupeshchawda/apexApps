package com.example.OlapBenchmark;

import org.apache.hadoop.conf.Configuration;

import com.datatorrent.api.DAG;
import com.datatorrent.api.StreamingApplication;
import com.datatorrent.api.annotation.ApplicationAnnotation;
import com.datatorrent.lib.io.ConsoleOutputOperator;
import com.datatorrent.olap.EmbeddedDruidOLAPOperator;

/**
 * Created by bhupesh on 2/8/17.
 */
@ApplicationAnnotation(name="OlapIngestionAndQueryBenchmarkApp")
public class OlapIngestionAndQueryBenchmarkApp implements StreamingApplication
{
  @Override
  public void populateDAG(DAG dag, Configuration configuration)
  {
    BenchmarkDataInput input = dag.addOperator("input", BenchmarkDataInput.class);
    EmbeddedDruidOLAPOperator olap = dag.addOperator("olap", EmbeddedDruidOLAPOperator.class);
    BenchmarkQueryInput query = dag.addOperator("query", BenchmarkQueryInput.class);
    ConsoleOutputOperator console = dag.addOperator("console", ConsoleOutputOperator.class);

    dag.addStream("data", input.dataOutput, olap.in);
    dag.addStream("trigger", input.queryTrigger, query.trigger);
    dag.addStream("query", query.queryOutput, olap.query);
    dag.addStream("console", olap.queryResult, console.input);
  }
}
