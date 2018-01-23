package com.example.pmmlEvaluator.regression;

import java.util.List;
import java.util.Map;

import org.dmg.pmml_4_3.DataField;
import org.dmg.pmml_4_3.FIELDUSAGETYPE;
import org.dmg.pmml_4_3.GeneralRegressionModel;
import org.dmg.pmml_4_3.MiningField;
import org.dmg.pmml_4_3.PCell;
import org.dmg.pmml_4_3.PMML;
import org.dmg.pmml_4_3.PPCell;
import org.dmg.pmml_4_3.Value;

import com.example.pmmlEvaluator.PMMLExtractionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Created by bhupesh on 8/8/17.
 */
public class MultinomialLogisticRegressionScorer extends GeneralRegressionScorer
{
  public MultinomialLogisticRegressionScorer(PMML pmml, GeneralRegressionModel model)
  {
    super(pmml, model);
  }

  /**
   * Map: Target Category -> Probability or Score
   */
  public Map<String, Double> score(Map<String, Object> tuple)
  {
    Map<String, Double> scores = Maps.newHashMap();

    // Identify target categories
    List<Value> targetCategories = Lists.newArrayList();
    Map<String, DataField> dictionary = PMMLExtractionUtils.getDataDictionary(pmml);
    for (Map.Entry<String, MiningField> entry : miningSchema.entrySet()) {
      if (entry.getValue().getUsageType().value().equals(FIELDUSAGETYPE.PREDICTED.value())) {
        targetCategories.addAll(dictionary.get(entry.getKey()).getValue());
      }
    }

    for (int i = 0; i < targetCategories.size(); i++) {
      String targetCategory = targetCategories.get(i).getValue();

      Map<String, List<PPCell>> parameterPredictorCells = ppMatrixMap.get(targetCategory);
      if (parameterPredictorCells == null) {
        parameterPredictorCells = ppMatrixMap.get(null);
      }

      List<PCell> parameterCells = paramMatrixMap.get(targetCategory);
      if (parameterCells == null) {
        parameterCells = paramMatrixMap.get(null);
      }
      double score = getScore(tuple, parameterCells, parameterPredictorCells);
      scores.put(targetCategory, score);
    }

//    Map<String, List<PPCell>> parameterPredictorCells = ppMatrixMap.get(null);
//    List<PCell> parameterCells = paramMatrixMap.get(null);
//    double score = getScore(tuple, parameterCells, parameterPredictorCells);
//    scores.put("Final", score);

    return scores;
  }

  public double getScore(Map<String, Object> tuple,
      List<PCell> parameterCells, Map<String, List<PPCell>> parameterPredictorCells)
  {
    double retVal = 0;
    for (PCell pCell : parameterCells) {
      List<PPCell> ppCells = parameterPredictorCells.get(pCell.getParameterName());
      double value = 0;
      if (ppCells != null) {
        value = combine(tuple, ppCells) * pCell.getBeta();
      } else {
        value = pCell.getBeta();
      }
      retVal += value;
    }
    return retVal;
  }

  public double combine(Map<String, Object> tuple, List<PPCell> ppCells)
  {
    double retVal = 1;
    if (ppCells == null) {
      return 1;
    }
    for (PPCell ppCell : ppCells) {
      if (factorList.contains(ppCell.getPredictorName())) {
        if (!tuple.get(ppCell.getPredictorName()).toString().equals(ppCell.getValue())) {
          return 0;
        }
      } else if (covariateList.contains(ppCell.getPredictorName())) {
        retVal *= Math.pow(Double.parseDouble(tuple.get(ppCell.getPredictorName()).toString()),
          Double.parseDouble(ppCell.getValue()));
      }
    }
    return retVal;
  }
}
