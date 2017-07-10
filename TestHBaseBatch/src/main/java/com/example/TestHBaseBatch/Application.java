/**
 * Put your copyright and license info here.
 */
package com.example.TestHBaseBatch;

import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;

import com.datatorrent.api.DAG;
import com.datatorrent.api.DAG.Locality;
import com.datatorrent.api.StreamingApplication;
import com.datatorrent.api.annotation.ApplicationAnnotation;
import com.datatorrent.contrib.hbase.HBaseFieldInfo;
import com.datatorrent.contrib.hbase.HBaseStore;
import com.datatorrent.lib.io.ConsoleOutputOperator;
import com.datatorrent.lib.util.FieldInfo;
import com.datatorrent.lib.util.TableInfo;
import com.datatorrent.moodi.lib.nosql.hbase.batch.HBasePOJOBatchInputOperator;

@ApplicationAnnotation(name="MyFirstApplication")
public class Application implements StreamingApplication
{

  @Override
  public void populateDAG(DAG dag, Configuration conf)
  {
    HBasePOJOBatchInputOperator hbaseInput = dag.addOperator("hbase", HBasePOJOBatchInputOperator.class);

    String tableName = conf.get("dt.operator.hbase.prop.tableName");
    int clientPort = conf.getInt("dt.operator.hbase.prop.clientPort", 2181);
    String zookeeperQuorum = conf.get("dt.operator.hbase.prop.zookeeperQuorum");
    HBaseStore store = new HBaseStore();
    store.setTableName(tableName);
    store.setZookeeperClientPort(clientPort);
    store.setZookeeperQuorum(zookeeperQuorum);
    hbaseInput.setStore(store);
    hbaseInput.setPojoTypeName(POJOClass.class.getName());

    TableInfo<HBaseFieldInfo> tableInfo = new TableInfo<>();
    tableInfo.setRowOrIdExpression("id");
    List<HBaseFieldInfo> fieldsInfo = new ArrayList<>();
    fieldsInfo.add( new HBaseFieldInfo( "name", "name", FieldInfo.SupportType.STRING, "cf") );
    tableInfo.setFieldsInfo(fieldsInfo);
    hbaseInput.setTableInfo(tableInfo);

    ConsoleOutputOperator console = dag.addOperator("console", ConsoleOutputOperator.class);

    dag.addStream("randomData", hbaseInput.outputPort, console.input).setLocality(Locality.CONTAINER_LOCAL);
  }
}
