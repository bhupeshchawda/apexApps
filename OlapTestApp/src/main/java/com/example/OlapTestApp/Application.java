/**
 * Put your copyright and license info here.
 */
package com.example.OlapTestApp;

import java.util.Arrays;

import org.apache.hadoop.conf.Configuration;

import com.datatorrent.api.Context;
import com.datatorrent.api.DAG;
import com.datatorrent.api.StreamingApplication;
import com.datatorrent.api.annotation.ApplicationAnnotation;
import com.datatorrent.common.partitioner.StatelessPartitioner;
import com.datatorrent.lib.appdata.schemas.SchemaUtils;
import com.datatorrent.lib.io.ConsoleOutputOperator;
import com.datatorrent.olap.EmbeddedDruidOLAPOperator;
import com.datatorrent.olap.partition.DimensionBasedStreamCodec;

@ApplicationAnnotation(name="MyFirstApplication")
public class Application implements StreamingApplication
{
  @Override
  public void populateDAG(DAG dag, Configuration conf)
  {
    OlapDataGenerator dataGenerator = dag.addOperator("dataGen", OlapDataGenerator.class);
    OlapQueryGenerator queryGenerator = dag.addOperator("queryGen", OlapQueryGenerator.class);
    EmbeddedDruidOLAPOperator olapOperator = dag.addOperator("olap", EmbeddedDruidOLAPOperator.class);
    olapOperator.setOlapSchema(SchemaUtils.jarResourceFileToString("input.json"));
//    PubSubWebSocketAppDataResult result = dag.addOperator("result", new PubSubWebSocketAppDataResult());
//    result.setTopic("test");
    ConsoleOutputOperator console = dag.addOperator("console", ConsoleOutputOperator.class);

    dag.addStream("datastream", dataGenerator.data, olapOperator.in);
    dag.addStream("querystream", queryGenerator.output, olapOperator.query);
    dag.addStream("queryresult", olapOperator.queryResult, console.input);

    dag.setInputPortAttribute(olapOperator.in, Context.PortContext.TUPLE_CLASS, POJO.class);

    DimensionBasedStreamCodec partitioningCodec = new DimensionBasedStreamCodec();
    partitioningCodec.setDimensions(Arrays.asList("a", "b"));
    partitioningCodec.setNumDimensionsToPartitionOn(2);
    dag.setInputPortAttribute(olapOperator.in, Context.PortContext.STREAM_CODEC, partitioningCodec);
    dag.setOperatorAttribute(olapOperator, Context.OperatorContext.PARTITIONER, new StatelessPartitioner<>(2));
  }
}
