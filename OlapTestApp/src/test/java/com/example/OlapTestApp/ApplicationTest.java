/**
 * Put your copyright and license info here.
 */
package com.example.OlapTestApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.validation.ConstraintViolationException;

import org.junit.Assert;
import org.junit.Test;

import org.apache.hadoop.conf.Configuration;

import com.datatorrent.api.LocalMode;
import com.datatorrent.stram.plan.logical.LogicalPlan;

/**
 * Test the DAG declaration in local mode.
 */
public class ApplicationTest {

  @Test
  public void testApplication() throws IOException, Exception {
    try {
      LocalMode lma = LocalMode.newInstance();
      Configuration conf = new Configuration(false);
      conf.addResource(this.getClass().getResourceAsStream("/META-INF/properties.xml"));
      lma.prepareDAG(new Application(), conf);
      LocalMode.Controller lc = lma.getController();
      lc.run(1000000); // runs for 10 seconds and quits
    } catch (ConstraintViolationException e) {
      Assert.fail("constraint violations: " + e.getConstraintViolations());
    }
  }

  @Test
  public void logicalPlanTest() throws IOException, ClassNotFoundException
  {
//    Kryo kryo = new Kryo();
//    InputStream in = new FileInputStream(new File("/tmp/dt-conf.ser"));
//    Input input = new Input(in);
//    Object obj = kryo.readClassAndObject(input);
//    System.out.println("Object read " + obj);

    LogicalPlan plan = new LogicalPlan();
//    Application app = new Application();
//    Configuration conf = new Configuration();
//    app.populateDAG(plan, conf);
//
//    System.out.println(plan);

    InputStream in = new FileInputStream(new File("/tmp/dt-conf.ser"));
    plan = LogicalPlan.read(in);

    System.out.println(plan);
  }

}
