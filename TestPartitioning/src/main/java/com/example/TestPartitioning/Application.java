/**
 * Put your copyright and license info here.
 */
package com.example.TestPartitioning;

import org.apache.hadoop.conf.Configuration;

import com.datatorrent.api.Context;
import com.datatorrent.api.DAG;
import com.datatorrent.api.Operator;
import com.datatorrent.api.StreamingApplication;
import com.datatorrent.api.annotation.ApplicationAnnotation;
import com.datatorrent.common.partitioner.StatelessPartitioner;

@ApplicationAnnotation(name="MyFirstApplication")
public class Application implements StreamingApplication
{

  @Override
  public void populateDAG(DAG dag, Configuration conf)
  {
    RandomNumberGenerator randomGenerator = dag.addOperator("randomGenerator", RandomNumberGenerator.class);
    IngestOperator ingest = dag.addOperator("ingest", IngestOperator.class);
    MergerOperator merger = dag.addOperator("merger", MergerOperator.class);
    QueryOperator query = dag.addOperator("query", QueryOperator.class);

    RandomNumberGenerator restQuery = dag.addOperator("restQuery", RandomNumberGenerator.class);
    RESTResponseOperator restResponse = dag.addOperator("restResponse", RESTResponseOperator.class);

    dag.addStream("input", randomGenerator.out, ingest.input);
    dag.addStream("IngestToMerger", ingest.output1, merger.input).setLocality(DAG.Locality.CONTAINER_LOCAL);
    dag.addStream("IngestToQuery", ingest.output2, query.input1);
    dag.addStream("MergerToQuery", merger.output, query.input2);
    dag.addStream("RESTQueryToQuery", restQuery.out, query.input3);
    dag.addStream("QueryToRESTResponse", query.output, restResponse.input1);
    dag.addStream("RESTQueryToRESTResponse", restQuery.out2, restResponse.input2);

    dag.setOperatorAttribute(ingest, Context.OperatorContext.PARTITIONER, new StatelessPartitioner<Operator>(3));
    dag.setInputPortAttribute(merger.input, Context.PortContext.PARTITION_PARALLEL, true);
    dag.setOperatorAttribute(query, Context.OperatorContext.PARTITIONER, new StatelessPartitioner<Operator>(2));
  }
}
