package com.example.pmmlEvaluator;

import java.io.IOException;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.dmg.pmml_4_3.PMML;
import org.junit.Test;

import com.example.pmmlEvaluator.jpmml.JpmmlGenericScorer;
import com.example.pmmlEvaluator.regression.MultinomialLogisticRegressionScorer;
import com.google.common.collect.Maps;

/**
 * Created by bhupesh on 9/8/17.
 */
public class GeneralRegressionScorerTest
{
  @Test
  public void testGeneralRegressionDT() throws IOException, JAXBException
  {
    PMML pmml = PMMLReader.getPMML("src/test/resources/testPmml.xml");
    MultinomialLogisticRegressionScorer scorer = (MultinomialLogisticRegressionScorer)PMMLScorerFactory.getScorer(pmml, 0);
    Map<String, Object> recordToScore = Maps.newHashMap();
    recordToScore.put("gre", 800);
    recordToScore.put("gpa", 3.9);
    recordToScore.put("rank", 1);
    System.out.println(scorer.score(recordToScore));
  }

  @Test
  public void testGeneralRegressionJPMML()
  {
    JpmmlGenericScorer scorer = new JpmmlGenericScorer("src/test/resources/testPmml.xml");
    Map<String, Object> recordToScore = Maps.newHashMap();
    recordToScore.put("gre", 800);
    recordToScore.put("gpa", 3.9);
    recordToScore.put("rank", 1);
    System.out.println(scorer.evaluate(recordToScore));
  }
}
