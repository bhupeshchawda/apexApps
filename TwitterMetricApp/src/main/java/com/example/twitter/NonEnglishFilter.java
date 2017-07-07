package com.example.twitter;

import com.datatorrent.api.AutoMetric;
import com.datatorrent.api.DefaultInputPort;
import com.datatorrent.api.DefaultOutputPort;
import com.datatorrent.common.util.BaseOperator;

/**
 * Created by bhupesh on 3/7/17.
 */
public class NonEnglishFilter extends BaseOperator
{
  @AutoMetric
  public long englishTags = 0;
  @AutoMetric
  public long nonEnglishTags = 0;

  @Override
  public void beginWindow(long windowId)
  {
    englishTags = 0;
    nonEnglishTags = 0;
  }

  public final transient DefaultInputPort<String> input = new DefaultInputPort<String>()
  {
    @Override
    public void process(String hashtag)
    {
      String filteredHashtag = hashtag.replaceAll("[^A-Za-z0-9-_@_]", "");
      if (!filteredHashtag.trim().isEmpty()) {
        output.emit(filteredHashtag);
        englishTags++;
      } else {
        nonEnglishTags++;
      }
    }
  };

  public final transient DefaultOutputPort<String> output = new DefaultOutputPort<>();

  public long getEnglishTags()
  {
    return englishTags;
  }

  public void setEnglishTags(long englishTags)
  {
    this.englishTags = englishTags;
  }

  public long getNonEnglishTags()
  {
    return nonEnglishTags;
  }

  public void setNonEnglishTags(long nonEnglishTags)
  {
    this.nonEnglishTags = nonEnglishTags;
  }
}
