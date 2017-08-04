package com.example.OlapBenchmark;

import org.apache.hadoop.conf.Configuration;

import com.datatorrent.api.DAG;
import com.datatorrent.api.StreamingApplication;
import com.datatorrent.api.annotation.ApplicationAnnotation;
import com.datatorrent.olap.DruidOLAPIngestOperator;

/**
 * Created by bhupesh on 2/8/17.
 */
@ApplicationAnnotation(name="OlapIngestionBenchmarkApp")
public class OlapIngestionBenchmarkApp implements StreamingApplication
{
  @Override
  public void populateDAG(DAG dag, Configuration configuration)
  {
    BenchmarkDataInput input = dag.addOperator("input", BenchmarkDataInput.class);
    DruidOLAPIngestOperator olap = dag.addOperator("olap", DruidOLAPIngestOperator.class);

    dag.addStream("data", input.dataOutput, olap.in).setLocality(DAG.Locality.THREAD_LOCAL);

//    DimensionBasedStreamCodec partitioningCodec = new DimensionBasedStreamCodec();
//    partitioningCodec.setDimensions(Arrays.asList("a", "b", "c"));
//    partitioningCodec.setNumDimensionsToPartitionOn(1);
//    dag.setInputPortAttribute(olap.in, Context.PortContext.STREAM_CODEC, partitioningCodec);
//    dag.setOperatorAttribute(olap, Context.OperatorContext.PARTITIONER, new StatelessPartitioner<>(2));

  }
}
