package com.example.pmmlEvaluator.naiveBayes;

import java.util.List;
import java.util.Map;

import org.dmg.pmml_4_3.BayesInput;
import org.dmg.pmml_4_3.BayesOutput;
import org.dmg.pmml_4_3.DataField;
import org.dmg.pmml_4_3.DerivedField;
import org.dmg.pmml_4_3.Discretize;
import org.dmg.pmml_4_3.DiscretizeBin;
import org.dmg.pmml_4_3.GaussianDistribution;
import org.dmg.pmml_4_3.Interval;
import org.dmg.pmml_4_3.MiningField;
import org.dmg.pmml_4_3.NaiveBayesModel;
import org.dmg.pmml_4_3.OPTYPE;
import org.dmg.pmml_4_3.PMML;
import org.dmg.pmml_4_3.PairCounts;
import org.dmg.pmml_4_3.PoissonDistribution;
import org.dmg.pmml_4_3.TargetValueCount;
import org.dmg.pmml_4_3.TargetValueCounts;
import org.dmg.pmml_4_3.TargetValueStat;
import org.dmg.pmml_4_3.TargetValueStats;

import com.example.pmmlEvaluator.PMMLScorer;
import com.google.common.collect.Maps;

import static com.example.pmmlEvaluator.PMMLExtractionUtils.getBayesInputs;
import static com.example.pmmlEvaluator.PMMLExtractionUtils.getBayesOutput;
import static com.example.pmmlEvaluator.PMMLExtractionUtils.getDataDictionary;
import static com.example.pmmlEvaluator.PMMLExtractionUtils.getMiningSchema;

/**
 * Created by bhupesh on 4/9/17.
 */
public class NaiveBayesScorer implements PMMLScorer
{
  protected PMML pmml;
  protected NaiveBayesModel model;

  protected Map<String, MiningField> miningSchema;
  protected Map<String, DataField> dataDictionary;
  protected Map<String, BayesInput> bayesInputs;
  protected BayesOutput bayesOutput;

  protected double [] L;

  public NaiveBayesScorer(PMML pmml, NaiveBayesModel model)
  {
    this.pmml = pmml;
    this.model = model;
    init();
  }

  public void init()
  {
    List<Object> objects = model.getContent();
    miningSchema = getMiningSchema(objects);
    dataDictionary = getDataDictionary(pmml);
    bayesInputs = getBayesInputs(objects);
    bayesOutput = getBayesOutput(objects);
    L = new double[bayesOutput.getTargetValueCounts().getTargetValueCount().size()];
  }

  public Map<String, Double> score(Map<String, Object> tuple)
  {
    Map<String, Double> retVal = Maps.newHashMap();

    double lSum = 0;
    for (int i = 0; i < L.length; i++) {
      L[i] = computeL(tuple, i);
      lSum += L[i];
    }

    for (int i = 0; i< bayesOutput.getTargetValueCounts().getTargetValueCount().size(); i++) {
      TargetValueCount targetValueCount = bayesOutput.getTargetValueCounts().getTargetValueCount().get(i);
      double score = L[i] / lSum;
      retVal.put(targetValueCount.getValue(), score);
    }
    return retVal;
  }

  public double computeL(Map<String, Object> tuple, int targetIndex)
  {
    String targetValue = bayesOutput.getTargetValueCounts().getTargetValueCount().get(targetIndex).getValue();
    double retVal = getTargetValueCount(bayesOutput.getTargetValueCounts(), targetValue);
    for (String fieldName : tuple.keySet()) {
      if (dataDictionary.get(fieldName).getOptype().equals(OPTYPE.CATEGORICAL) ||
        (dataDictionary.get(fieldName).getOptype().equals(OPTYPE.CONTINUOUS)
          && bayesInputs.get(fieldName).getDerivedField() != null
          && bayesInputs.get(fieldName).getDerivedField().getDiscretize() != null)) {
        double prob = computeProbabilityDiscrete(tuple, fieldName, targetValue);
        if (prob > 0) {
          retVal *= prob;
        }
      } else if (dataDictionary.get(fieldName).getOptype().equals(OPTYPE.CONTINUOUS)) {
        double prob = computeProbabilityContinuous(tuple, fieldName, targetValue);
        if (prob > 0) {
          retVal *= prob;
        }
      }
    }
    return retVal;
  }

  public double computeProbabilityContinuous(Map<String, Object> tuple, String fieldName, String targetValue)
  {
    double retVal = 0;
    Object attribute = tuple.get(fieldName);
    BayesInput bayesInput = bayesInputs.get(fieldName);
    TargetValueStats targetValueStats = bayesInput.getTargetValueStats();
    for (TargetValueStat targetValueStat : targetValueStats.getTargetValueStat()) {
      if (targetValueStat.getValue().equals(targetValue)) {
        List<Object> distribution = targetValueStat.getContent();
        for (Object object : distribution) {
          if (object instanceof GaussianDistribution) {
            GaussianDistribution gauss = (GaussianDistribution)object;
            switch (dataDictionary.get(fieldName).getDataType()) {
              case DOUBLE:
              case INTEGER:
                retVal = ((double)attribute - gauss.getMean());
                retVal *= retVal;
                retVal /= (2 * gauss.getVariance());
                retVal = Math.exp(retVal * -1);
                retVal /= Math.sqrt(2 * 3.142 * gauss.getVariance());
                return retVal;
            }
          } else if (object instanceof PoissonDistribution) {
            // TODO
          }
        }
      }
    }
    return retVal;
  }

  public double computeProbabilityDiscrete(Map<String, Object> tuple, String fieldName, String targetValue)
  {
    double retVal = 0;
    BayesInput bayesInput = bayesInputs.get(fieldName);
    List<PairCounts> pairCounts = bayesInput.getPairCounts();
    DerivedField derivedField = bayesInput.getDerivedField();
    String first = null;
    if (derivedField != null) {
      Discretize discretize = derivedField.getDiscretize();
      if (discretize.getDiscretizeBin() != null && !discretize.getDiscretizeBin().isEmpty()) {
        for (DiscretizeBin discretizeBin : discretize.getDiscretizeBin()) {
          Interval interval = discretizeBin.getInterval();
          if (interval != null) {
            switch (dataDictionary.get(fieldName).getDataType()) {
              case DOUBLE:
              case INTEGER:
                double value = (double)tuple.get(fieldName);
                switch (interval.getClosure()) {
                  case "closedOpen":
                    if (value >= interval.getLeftMargin() && value < interval.getRightMargin()) {
                      first = discretizeBin.getBinValue();
                    } else {
                      continue;
                    }
                    break;
                  case "closedClosed":
                    if (value >= interval.getLeftMargin() && value <= interval.getRightMargin()) {
                      first = discretizeBin.getBinValue();
                    } else {
                      continue;
                    }
                    break;
                  case "openClosed":
                    if (value > interval.getLeftMargin() && value <= interval.getRightMargin()) {
                      first = discretizeBin.getBinValue();
                    } else {
                      continue;
                    }
                    break;
                  case "openOpen":
                    if (value > interval.getLeftMargin() && value < interval.getRightMargin()) {
                      first = discretizeBin.getBinValue();
                    } else {
                      continue;
                    }
                    break;
                  default:
                    throw new RuntimeException("Unexpected interval closure");
                }
              default:
                throw new RuntimeException("Unhandled type " + dataDictionary.get(fieldName).getDataType());
            }
          }
        }
      }
    } else {
      first = tuple.get(fieldName) + "";
    }

    // handle pair counts

    for (PairCounts pairCount : pairCounts) {
      if (pairCount.getValue().equals(first)) {
        double targetCountForAttribute = getTargetValueCount(pairCount.getTargetValueCounts(), targetValue);
        double targetValueCount = getTargetValueCount(bayesOutput.getTargetValueCounts(), targetValue);

        retVal = targetCountForAttribute / targetValueCount;
        break;
      }
    }
    return retVal;
  }

  public double getTargetValueCount(TargetValueCounts targetValueCounts, String targetValue)
  {
    for (TargetValueCount targetValueCount : targetValueCounts.getTargetValueCount()) {
      if (targetValueCount.getValue().equals(targetValue)) {
        return targetValueCount.getCount();
      }
    }
    return 0;
  }
}
