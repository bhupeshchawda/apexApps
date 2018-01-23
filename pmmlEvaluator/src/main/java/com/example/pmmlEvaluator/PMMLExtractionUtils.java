package com.example.pmmlEvaluator;

import java.util.List;
import java.util.Map;

import org.dmg.pmml_4_3.BayesInput;
import org.dmg.pmml_4_3.BayesInputs;
import org.dmg.pmml_4_3.BayesOutput;
import org.dmg.pmml_4_3.CovariateList;
import org.dmg.pmml_4_3.DataDictionary;
import org.dmg.pmml_4_3.DataField;
import org.dmg.pmml_4_3.FactorList;
import org.dmg.pmml_4_3.MiningField;
import org.dmg.pmml_4_3.MiningSchema;
import org.dmg.pmml_4_3.PCell;
import org.dmg.pmml_4_3.PMML;
import org.dmg.pmml_4_3.PPCell;
import org.dmg.pmml_4_3.PPMatrix;
import org.dmg.pmml_4_3.ParamMatrix;
import org.dmg.pmml_4_3.Parameter;
import org.dmg.pmml_4_3.ParameterList;
import org.dmg.pmml_4_3.Predictor;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Created by bhupesh on 9/8/17.
 */
public class PMMLExtractionUtils
{
  public static List<String> getFactorList(List<Object> objects)
  {
    List factorList = Lists.newArrayList();
    for (Object object : objects) {
      if (object instanceof FactorList) {
        FactorList factors = (FactorList)object;
        for (Predictor predictor : factors.getPredictor()) {
          factorList.add(predictor.getName());
        }
      }
    }
    return factorList;
  }

  public static List<String> getCovariateList(List<Object> objects)
  {
    List covariateList = Lists.newArrayList();
    for (Object object : objects) {
      if (object instanceof CovariateList) {
        CovariateList covariates = (CovariateList)object;
        for (Predictor predictor : covariates.getPredictor()) {
          covariateList.add(predictor.getName());
        }
      }
    }
    return covariateList;
  }

  public static Map<String, String> getParameterMap(List<Object> objects)
  {
    Map<String, String> parameterMap = Maps.newLinkedHashMap();
    for (Object object : objects) {
      if (object instanceof ParameterList) {
        parameterMap = Maps.newLinkedHashMap();
        ParameterList parameterList = (ParameterList)object;
        for (Parameter parameter : parameterList.getParameter()) {
          parameterMap.put(parameter.getName(), parameter.getLabel());
        }
      }
    }
    return parameterMap;
  }

  /**
   * Map: Target category -> List<PPCell>
   */
  public static Map<String, Map<String, List<PPCell>>> getPPMatrixMap(List<Object> objects)
  {
    Map<String, Map<String, List<PPCell>>> ppMatrixMap = Maps.newLinkedHashMap();
    for (Object object : objects) {
      if (object instanceof PPMatrix) { // PPMatrix
        PPMatrix ppMatrix = (PPMatrix)object;
        List<PPCell> ppCells = ppMatrix.getPPCell();
        for (PPCell ppCell : ppCells) {
          String parameterName = ppCell.getParameterName();
          String targetCategory = ppCell.getTargetCategory();
          Map<String, List<PPCell>> predictorValuePairs = ppMatrixMap.get(targetCategory);
          if (predictorValuePairs == null) {
            predictorValuePairs = Maps.newHashMap();
            ppMatrixMap.put(targetCategory, predictorValuePairs);
          }
          List<PPCell> currentCells = predictorValuePairs.get(parameterName);
          if (currentCells == null) {
            currentCells = Lists.newArrayList();
            predictorValuePairs.put(parameterName, currentCells);
          }
          currentCells.add(ppCell);
        }
      }
    }
    return ppMatrixMap;
  }

  /**
   * Map: Target Category -> Parameter Name -> List<PCell>
   */
  public static Map<String, List<PCell>> getParamMatrixMap(List<Object> objects)
  {
    Map<String, List<PCell>> paramMatrixMap = Maps.newLinkedHashMap();
    for (Object object : objects) {
      if (object instanceof ParamMatrix) { // ParamMatrix
        ParamMatrix paramMatrix = (ParamMatrix)object;
        List<PCell> pCells = paramMatrix.getPCell();
        for (PCell pCell : pCells) {
          String targetCategory = pCell.getTargetCategory();
          List<PCell> params = paramMatrixMap.get(targetCategory);
          if (params == null) {
            params = Lists.newArrayList();
            paramMatrixMap.put(targetCategory, params);
          }
          params.add(pCell);
        }
      }
    }
    return paramMatrixMap;
  }

  public static Map<String, MiningField> getMiningSchema(List<Object> objects)
  {
    Map<String, MiningField> retVal = Maps.newLinkedHashMap();
    for (Object object : objects) {
      if (object instanceof MiningSchema) {
        MiningSchema miningSchema = (MiningSchema)object;
        List<MiningField> miningFields = miningSchema.getMiningField();
        for (MiningField miningField : miningFields) {
          retVal.put(miningField.getName(), miningField);
        }
      }
    }
    return retVal;
  }

  public static Map<String, DataField> getDataDictionary(PMML pmml)
  {
    Map<String, DataField> retVal = Maps.newLinkedHashMap();
    DataDictionary dataDictionary = pmml.getDataDictionary();
    for (DataField d : dataDictionary.getDataField()) {
      retVal.put(d.getName(), d);
    }
    return retVal;
  }

  /**
   * Naive Bayes
   */

  public static Map<String, BayesInput> getBayesInputs(List<Object> objects)
  {
    Map<String, BayesInput> retVal = Maps.newHashMap();
    for (Object object : objects) {
      if (object instanceof BayesInputs) {
        BayesInputs bayesInputs = (BayesInputs)object;
        for (BayesInput bayesInput : bayesInputs.getBayesInput()) {
          retVal.put(bayesInput.getFieldName(), bayesInput);
        }
      }
    }
    return retVal;
  }

  public static BayesOutput getBayesOutput(List<Object> objects)
  {
    Map<String, BayesInput> retVal = Maps.newHashMap();
    for (Object object : objects) {
      if (object instanceof BayesOutput) {
        return (BayesOutput)object;
      }
    }
    return null;
  }

}
