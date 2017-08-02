package com.example.OlapBenchmark.schema;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.druid.data.input.impl.DimensionSchema;
import io.druid.granularity.QueryGranularity;
import io.druid.query.aggregation.AggregatorFactory;

/**
 * Created by chinmay on 19/7/17.
 */
public class OLAPSchema
{
  private String timeColumn;
  private List<DimensionSchema> dimensions;

  @JsonDeserialize(using = GranularitySerde.class)
  private QueryGranularity granularity;

  private List<AggregatorFactory> measures;

  public String getTimeColumn()
  {
    return timeColumn;
  }

  public void setTimeColumn(String timeColumn)
  {
    this.timeColumn = timeColumn;
  }

  public List<DimensionSchema> getDimensions()
  {
    return dimensions;
  }

  public void setDimensions(List<DimensionSchema> dimensions)
  {
    this.dimensions = dimensions;
  }

  public QueryGranularity getGranularity()
  {
    return granularity;
  }

  public void setGranularity(QueryGranularity granularity)
  {
    this.granularity = granularity;
  }

  public List<AggregatorFactory> getMeasures()
  {
    return measures;
  }

  public void setMeasures(List<AggregatorFactory> measures)
  {
    this.measures = measures;
  }

  public static class GranularitySerde extends JsonDeserializer<QueryGranularity>
  {
    @Override
    public QueryGranularity deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException
    {
      String s = jsonParser.readValueAs(String.class);
      return QueryGranularity.fromString(s);
    }
  }
}
