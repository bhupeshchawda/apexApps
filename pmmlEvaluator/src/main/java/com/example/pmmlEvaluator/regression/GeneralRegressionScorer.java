package com.example.pmmlEvaluator.regression;

import java.util.List;
import java.util.Map;

import org.dmg.pmml_4_3.DataField;
import org.dmg.pmml_4_3.GeneralRegressionModel;
import org.dmg.pmml_4_3.MiningField;
import org.dmg.pmml_4_3.PCell;
import org.dmg.pmml_4_3.PMML;
import org.dmg.pmml_4_3.PPCell;

import com.example.pmmlEvaluator.PMMLScorer;

import static com.example.pmmlEvaluator.PMMLExtractionUtils.getCovariateList;
import static com.example.pmmlEvaluator.PMMLExtractionUtils.getDataDictionary;
import static com.example.pmmlEvaluator.PMMLExtractionUtils.getFactorList;
import static com.example.pmmlEvaluator.PMMLExtractionUtils.getMiningSchema;
import static com.example.pmmlEvaluator.PMMLExtractionUtils.getPPMatrixMap;
import static com.example.pmmlEvaluator.PMMLExtractionUtils.getParamMatrixMap;
import static com.example.pmmlEvaluator.PMMLExtractionUtils.getParameterMap;

/**
 * Created by bhupesh on 8/8/17.
 */
public class GeneralRegressionScorer implements PMMLScorer
{
  protected Map<String, MiningField> miningSchema;
  protected Map<String, DataField> dataDictionary;
  protected Map<String, String> parameterMap;
  protected List<String> factorList;
  protected List<String> covariateList;
  protected Map<String, Map<String, List<PPCell>>> ppMatrixMap;
  protected Map<String, List<PCell>> paramMatrixMap;
  protected GeneralRegressionModel model;
  protected PMML pmml;

  public GeneralRegressionScorer(PMML pmml, GeneralRegressionModel model)
  {
    this.pmml = pmml;
    this.model = model;
    init();
  }

  public void init()
  {
    List<Object> objects = model.getContent();
    miningSchema = getMiningSchema(objects);
    factorList = getFactorList(objects);
    covariateList = getCovariateList(objects);
    parameterMap = getParameterMap(objects);
    ppMatrixMap = getPPMatrixMap(objects);
    paramMatrixMap = getParamMatrixMap(objects);
    dataDictionary = getDataDictionary(pmml);
  }
}
