package com.example.pmmlEvaluator.jpmml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.dmg.pmml.FieldName;
import org.dmg.pmml.PMML;
import org.jpmml.evaluator.FieldValue;
import org.jpmml.evaluator.InputField;
import org.jpmml.evaluator.ModelEvaluator;
import org.jpmml.evaluator.ModelEvaluatorFactory;
import org.jpmml.model.PMMLUtil;
import org.xml.sax.SAXException;

/**
 * Created by bhupesh on 4/9/17.
 */
public class JpmmlGenericScorer
{
  PMML pmml;
  ModelEvaluator<?> modelEvaluator;

  public JpmmlGenericScorer(String pmmlFilePath)
  {
    try(InputStream is = new FileInputStream(new File(pmmlFilePath))){
      pmml = PMMLUtil.unmarshal(is);
    } catch (IOException | SAXException | JAXBException e) {
      e.printStackTrace();
    }

    if (pmml != null) {
      ModelEvaluatorFactory factory = ModelEvaluatorFactory.newInstance();
      modelEvaluator = factory.newModelEvaluator(pmml);
    }
  }

  public Map<FieldName, ?> evaluate(Map<String, Object> tuple)
  {
    Map<FieldName, FieldValue> arguments = preprocess(tuple);
    System.out.println(arguments);
    Map<FieldName, ?> results = modelEvaluator.evaluate(arguments);
    return results;
  }

  public Map<FieldName, FieldValue> preprocess(Map<String, Object> tuple)
  {
    Map<FieldName, FieldValue> arguments = new LinkedHashMap<>();

    List<InputField> inputFields = modelEvaluator.getActiveFields();
    for(InputField inputField : inputFields){
      FieldName inputFieldName = inputField.getName();

      Object rawValue = tuple.get(inputFieldName.getValue());

      FieldValue inputFieldValue = inputField.prepare(rawValue);

      arguments.put(inputFieldName, inputFieldValue);
    }
    return arguments;
  }

}
