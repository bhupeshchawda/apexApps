package com.example.pmmlEvaluator;

import java.io.IOException;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.dmg.pmml_4_3.PMML;
import org.junit.Test;

import com.example.pmmlEvaluator.naiveBayes.NaiveBayesScorer;
import com.example.pmmlEvaluator.jpmml.JpmmlGenericScorer;
import com.google.common.collect.Maps;

/**
 * Created by bhupesh on 4/9/17.
 */
public class NaiveBayesTest
{
  @Test
  public void testSaarangCode() throws IOException, JAXBException
  {
    Map<String, Object> data = Maps.newHashMap();
    data.put("gre", 890.0);
    data.put("gpa", 3.9);
    data.put("rank", 1.0);

    PMML pmml = PMMLReader.getPMML("src/test/resources/naiveBayesAdmit.xml");
    NaiveBayesScorer scorer = (NaiveBayesScorer)PMMLScorerFactory.getScorer(pmml, 0);
    System.out.println(scorer.score(data));
  }

  @Test
  public void testJPMML()
  {
    Map<String, Object> data = Maps.newHashMap();
    data.put("gre", 890);
    data.put("gpa", 3.9);
    data.put("rank", 1);

    JpmmlGenericScorer scorer = new JpmmlGenericScorer("src/test/resources/naiveBayesAdmit.xml");
    System.out.println(scorer.evaluate(data));
  }
}
