/**
 * Put your copyright and license info here.
 */
package com.datatorrent.Classification;

import org.apache.apex.malhar.lib.fs.GenericFileOutputOperator;
import org.apache.hadoop.conf.Configuration;

import com.datatorrent.api.annotation.ApplicationAnnotation;
import com.datatorrent.api.StreamingApplication;
import com.datatorrent.api.DAG;
import com.datatorrent.api.DAG.Locality;
import com.datatorrent.lib.io.ConsoleOutputOperator;
import com.datatorrent.lib.io.fs.AbstractFileOutputOperator;
import com.datatorrent.pmml.operator.ClassificationScoringOperator;
import com.datatorrent.pmml.scorer.ClassificationScorer;

@ApplicationAnnotation(name = "PMML-Classification-Scoring-App")
public class Application implements StreamingApplication
{

  @Override
  public void populateDAG(DAG dag, Configuration conf)
  {

    ClassificationInput inputOp = dag.addOperator("inputOp", ClassificationInput.class);
    inputOp.setEmitBatchSize(1);
    ClassificationScoringOperator scoring = dag.addOperator("classificationOperator", ClassificationScoringOperator.class);
    ScoringOutputOperator logger = dag.addOperator("Logger", ScoringOutputOperator.class);

    dag.addStream("data to scoring", inputOp.scoringOut, scoring.input);
    dag.addStream("scoring to output", scoring.output, logger.input);
  }
}
