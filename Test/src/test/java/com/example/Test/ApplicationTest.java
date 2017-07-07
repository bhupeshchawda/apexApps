/**
 * Put your copyright and license info here.
 */
package com.example.Test;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Date;

import org.junit.Test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.datatorrent.metrics.api.objects.records.MetricsRecord;

/**
 * Test the DAG declaration in local mode.
 */
public class ApplicationTest {

  @Test
  public void testReadRecords() throws IOException, Exception {
    FileSystem fs = FileSystem.newInstance(new Configuration());
    DataInputStream in = fs.open(new Path("src/test/resources/metric.1499235719673"));

    while (true) {
      MetricsRecord record = MetricsRecord.getMetricsRecordInstance(in);
      record.getRecordData().readFields(in);
      MetricsRecord.readEORData(in);
      long ts = record.getRecordMeta().getTimestamp();
      System.out.println( new Date(ts) + " " + record.getRecordData());
    }
  }

}
