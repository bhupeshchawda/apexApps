package com.example.pmmlEvaluator;

import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.dmg.pmml_4_3.PMML;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * Created by bhupesh on 8/8/17.
 */
public class PMMLReader
{
  public static PMML getPMML(String pmmlModelPath) throws IOException, JAXBException
  {
    FileSystem fs = FileSystem.newInstance(new Configuration());
    FSDataInputStream in = fs.open(new Path(pmmlModelPath));
    JAXBContext jaxbContext = JAXBContext.newInstance(PMML.class);
    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    PMML pmml = (PMML) jaxbUnmarshaller.unmarshal(in);
    return pmml;
  }

}
