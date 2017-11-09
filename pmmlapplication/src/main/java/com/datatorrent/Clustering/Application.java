package com.datatorrent.Clustering;

import org.apache.hadoop.conf.Configuration;
import com.datatorrent.api.DAG;
import com.datatorrent.api.StreamingApplication;
import com.datatorrent.api.annotation.ApplicationAnnotation;
import com.datatorrent.pmml.operator.ClusteringScoringOperator;

@ApplicationAnnotation(name="PMML-Clustering-Scoring-App")
public class Application implements StreamingApplication
{
  @Override
  public void populateDAG(DAG dag, Configuration conf)
  {
    ClusteringInput inputOp = dag.addOperator("inputOp1", ClusteringInput.class);
    inputOp.setEmitBatchSize(1);
    ClusteringScoringOperator scoring = dag.addOperator("clusteringOperator", ClusteringScoringOperator.class);
    ScoringOutputOperator logger = dag.addOperator("Logger1", ScoringOutputOperator.class);

    dag.addStream("data to scoring", inputOp.scoringOut, scoring.input);
    dag.addStream("scoring to output", scoring.output, logger.input);
  }
}
