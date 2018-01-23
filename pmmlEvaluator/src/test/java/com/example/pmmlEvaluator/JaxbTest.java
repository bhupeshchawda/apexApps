package com.example.pmmlEvaluator;

import java.io.File;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.dmg.pmml_4_3.GeneralRegressionModel;
import org.dmg.pmml_4_3.PMML;
import org.dmg.pmml_4_3.PPCell;
import org.dmg.pmml_4_3.PPMatrix;
import org.junit.Test;

/**
 * Created by bhupesh on 8/8/17.
 */
public class JaxbTest
{
  @Test
  public void testJaxb() throws JAXBException
  {
    File file = new File("src/test/resources/test.xml");
    JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);

    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    Customer customer = (Customer) jaxbUnmarshaller.unmarshal(file);
    System.out.println(customer);
  }

  @Test
  public void testPmmlClasses() throws JAXBException
  {
    File file = new File("src/test/resources/logisticRegression.xml");
    JAXBContext jaxbContext = JAXBContext.newInstance(GeneralRegressionModel.class);

    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
    PMML pmml = (PMML) jaxbUnmarshaller.unmarshal(file);
    List<Object> models  = pmml.getAssociationModelOrBayesianNetworkModelOrBaselineModel();
    System.out.println(models.size());
    GeneralRegressionModel model = (GeneralRegressionModel)models.get(0);
    System.out.println(model.getModelType());
    List<Object> objects = model.getContent();
    for (Object object : objects) {
      if (object instanceof PPMatrix) {
        System.out.println("PPMatrix found");
        PPMatrix ppMatrix = (PPMatrix)object;
        List<PPCell> ppCells = ppMatrix.getPPCell();
        for (PPCell ppCell : ppCells) {
          System.out.println(ppCell.getParameterName());
          System.out.println(ppCell.getPredictorName());
          System.out.println(ppCell.getValue());
        }
      }
    }
  }
}
