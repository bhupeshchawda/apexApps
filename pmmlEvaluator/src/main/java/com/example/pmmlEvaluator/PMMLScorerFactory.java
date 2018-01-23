package com.example.pmmlEvaluator;

import java.util.List;

import org.dmg.pmml_4_3.GeneralRegressionModel;
import org.dmg.pmml_4_3.NaiveBayesModel;
import org.dmg.pmml_4_3.PMML;

import com.example.pmmlEvaluator.naiveBayes.NaiveBayesScorer;
import com.example.pmmlEvaluator.regression.MultinomialLogisticRegressionScorer;

/**
 * Created by bhupesh on 8/8/17.
 */
public class PMMLScorerFactory
{
  /**
   * Use pmml.getAssociationModelOrBaselineModelOrClusteringModel() to identify the model to be used for scoring.
   */
  public static PMMLScorer getScorer(PMML pmml, int n)
  {
    List<Object> models = pmml.getAssociationModelOrBayesianNetworkModelOrBaselineModel();
    Object model = models.get(n);

    if (model instanceof GeneralRegressionModel) {
      GeneralRegressionModel generalRegressionModel = (GeneralRegressionModel)model;
      String modelType = generalRegressionModel.getModelType();
      switch (modelType) {
        case "multinomialLogistic":
        case "generalizedLinear":
          return new MultinomialLogisticRegressionScorer(pmml, generalRegressionModel);
        default:
          throw new IllegalArgumentException("Model type not supported yet " + modelType);
      }
    } else if (model instanceof NaiveBayesModel) {
      return new NaiveBayesScorer(pmml, (NaiveBayesModel)model);
    }
    else {
      throw new IllegalArgumentException("Model " + model.getClass() + " not supported yet");
    }

  }
}
