/**
 * Put your copyright and license info here.
 */
package com.example.OlapTestApp;

import org.apache.hadoop.conf.Configuration;

import com.datatorrent.api.Context;
import com.datatorrent.api.DAG;
import com.datatorrent.api.StreamingApplication;
import com.datatorrent.api.annotation.ApplicationAnnotation;
import com.datatorrent.lib.appdata.schemas.SchemaUtils;
import com.datatorrent.lib.io.ConsoleOutputOperator;
import com.datatorrent.olap.operator.OlapMerger;
import com.datatorrent.olap.operator.OlapWriter;
import com.datatorrent.olap.operator.helper.OlapStorageAgent;

@ApplicationAnnotation(name="OlapTestWriterApp")
public class Application implements StreamingApplication
{
  @Override
  public void populateDAG(DAG dag, Configuration conf)
  {
    RandomOlapDataGenerator dataGenerator = dag.addOperator("DataGen", RandomOlapDataGenerator.class);

    OlapWriter olapWriter = dag.addOperator("OlapWriter", OlapWriter.class);
    olapWriter.setSchemas(SchemaUtils.jarResourceFileToString("input.json"));
    olapWriter.setSchemaKey("dataSource");
    olapWriter.setHdfsLocation("/olap");
    olapWriter.setMaxRowsPerIndex(10);

    OlapMerger olapMerger = dag.addOperator("OlapMerger", OlapMerger.class);

    ConsoleOutputOperator console = dag.addOperator("Console", ConsoleOutputOperator.class);

    dag.addStream("dataToWriter", dataGenerator.data, olapWriter.input);
    dag.addStream("writerToMerger", olapWriter.output, olapMerger.input);
    dag.addStream("mergerToConsole", olapMerger.output, console.input);

    dag.setInputPortAttribute(olapWriter.input, Context.PortContext.TUPLE_CLASS, POJO.class);
    OlapStorageAgent storageAgent = new OlapStorageAgent(".", conf);
    dag.setAttribute(Context.OperatorContext.STORAGE_AGENT, storageAgent);
  }
}
