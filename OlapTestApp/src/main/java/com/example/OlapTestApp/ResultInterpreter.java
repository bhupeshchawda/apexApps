//package com.example.OlapTestApp;
//
//import org.codehaus.jettison.json.JSONException;
//import org.codehaus.jettison.json.JSONObject;
//
//import com.datatorrent.api.DefaultInputPort;
//import com.datatorrent.api.DefaultOutputPort;
//import com.datatorrent.common.util.BaseOperator;
//import com.example.attic.partition.OlapResultUnifier;
//
///**
// * Created by bhupesh on 27/7/17.
// */
//public class ResultInterpreter extends BaseOperator
//{
//  public final transient DefaultInputPort<OlapResultUnifier.QueryResult> input = new DefaultInputPort<OlapResultUnifier.QueryResult>()
//  {
//    @Override
//    public void process(OlapResultUnifier.QueryResult queryResult)
//    {
//      JSONObject jo = new JSONObject();
//      try {
//        jo.put("id", "1");
//        jo.put("rows", queryResult.resultRows);
//      } catch (JSONException e) {
//        throw new RuntimeException(e);
//      }
//
//      System.out.println("Emitting: " + jo.toString());
//      output.emit(jo.toString());
//    }
//  };
//
//  public final transient DefaultOutputPort<String> output = new DefaultOutputPort<>();
//
//
//}
