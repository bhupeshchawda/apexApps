package com.example.OlapBenchmark;

import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;

import com.datatorrent.api.Context;
import com.datatorrent.api.DAG;
import com.datatorrent.api.StreamingApplication;
import com.datatorrent.api.annotation.ApplicationAnnotation;
import com.datatorrent.common.partitioner.StatelessPartitioner;
import com.datatorrent.olap.EmbeddedDruidOLAPOperator;
import com.datatorrent.olap.partition.DimensionBasedStreamCodec;

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
    EmbeddedDruidOLAPOperator olap = dag.addOperator("olap", EmbeddedDruidOLAPOperator.class);

    dag.addStream("data", input.dataOutput, olap.in);

    DimensionBasedStreamCodec partitioningCodec = new DimensionBasedStreamCodec();
    partitioningCodec.setDimensions(Arrays.asList("a", "b", "c"));
    partitioningCodec.setNumDimensionsToPartitionOn(1);
    dag.setInputPortAttribute(olap.in, Context.PortContext.STREAM_CODEC, partitioningCodec);
    dag.setOperatorAttribute(olap, Context.OperatorContext.PARTITIONER, new StatelessPartitioner<>(2));

  }
}
