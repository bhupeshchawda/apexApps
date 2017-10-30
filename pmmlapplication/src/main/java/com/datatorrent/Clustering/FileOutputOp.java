package com.datatorrent.Clustering;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.datatorrent.api.Context;
import com.datatorrent.api.DefaultInputPort;
import com.datatorrent.common.util.BaseOperator;
import com.datatorrent.lib.io.fs.AbstractFileOutputOperator;

public class FileOutputOp extends BaseOperator
{

  transient FileSystem fs;
  transient FSDataOutputStream fin;
  public transient DefaultInputPort<String> input = new DefaultInputPort<String>()
  {
    @Override
    public void setup(Context.PortContext context)
    {
      try {
        Configuration configuration = new Configuration();
        fs = FileSystem.get(configuration);
        Path filenamePath = new Path("/user/devraj/clusteringoutput.txt");

        if (fs.exists(filenamePath))
        {
          //remove the file
          fs.delete(filenamePath, true);
        }

        //FSOutputStream to write the inputmsg into the HDFS file
        fin = fs.create(filenamePath);

      } catch (Exception e) {
        e.printStackTrace();
      }
      super.setup(context);
    }

    @Override
    public void teardown()
    {
      try{
        fin.close();
        fs.close();
      }catch (IOException e){
        e.printStackTrace();
      }
      super.teardown();
    }

    @Override
    public void process(String s)
    {
      try {
        fin.writeUTF(s);
      }catch (IOException e){
        e.printStackTrace();
      }
    }
  };
}


