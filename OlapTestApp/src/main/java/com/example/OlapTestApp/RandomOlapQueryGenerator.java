package com.example.OlapTestApp;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.datatorrent.api.DefaultOutputPort;
import com.datatorrent.api.InputOperator;
import com.datatorrent.common.util.BaseOperator;

/**
 * Created by bhupesh on 20/7/17.
 */
public class RandomOlapQueryGenerator extends BaseOperator implements InputOperator
{
  private final static char base = 'a';

  // properties
  private int maxTuplesPerWindow = 1;
  private int numDimensions = 3;

  // internal
  private transient int numTuplesThisWindow = 0;
  private transient int maxCombinations = 0;
  private int queryId = 0;

  public final transient DefaultOutputPort<String> output = new DefaultOutputPort<>();

  @Override
  public void beginWindow(long windowId)
  {
    numTuplesThisWindow = 0;
  }

  @Override
  public void emitTuples()
  {
    if (numTuplesThisWindow >= maxTuplesPerWindow) {
      return;
    }
    numTuplesThisWindow++;
    try {
      if (--maxCombinations < 0) {
        maxCombinations = (int)Math.pow(2, numDimensions) - 1;
      }
      output.emit(getGroupByQuery(maxCombinations));
    } catch (JSONException e) {
      throw new RuntimeException(e);
    }
  }

  private String getGroupByQuery(int n) throws JSONException
  {
    JSONObject json = new JSONObject();
    json.put("context", new JSONObject().put("queryId", ++queryId+""));
    json.put("queryType","groupBy");
    json.put("dataSource","");
    json.put("granularity","MINUTE");
    json.put("aggregations", new JSONArray()
      .put(new JSONObject().put("type","longSum").put("name","sum_agg_x").put("fieldName","sum_agg_x"))
      .put(new JSONObject().put("type","longSum").put("name","sum_agg_y").put("fieldName","sum_agg_y"))
      .put(new JSONObject().put("type","longSum").put("name","count").put("fieldName","count")));
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    json.put("intervals",sdf.format(0) + "/" + sdf.format(new Date()));

    JSONArray dimensions = new JSONArray();
    int count = (int)Math.pow(2, numDimensions);
    for (int j = 0; j < count; j++) {
      if ((n >> j  & 0x1) == 1) {
        dimensions.put(String.valueOf((char)(base + j)));
      }
    }
    json.put("dimensions", dimensions);
    return json.toString();

//    Query q;
//    ObjectMapper mapper = new DefaultObjectMapper();
//    try {
//      q = mapper.readValue(json.toString(), Query.class);
//    } catch (IOException e) {
//      throw new RuntimeException(e);
//    }
//    return q;
  }
}
