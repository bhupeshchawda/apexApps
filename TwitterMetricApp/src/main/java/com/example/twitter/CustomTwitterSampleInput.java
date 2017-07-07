package com.example.twitter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import com.datatorrent.contrib.twitter.TwitterSampleInput;

import twitter4j.HashtagEntity;
import twitter4j.Status;
import twitter4j.URLEntity;

/**
 * Created by chinmay on 7/7/17.
 */
public class CustomTwitterSampleInput extends TwitterSampleInput
{
  private static final Logger LOG = LoggerFactory.getLogger(CustomTwitterSampleInput.class);
  private long cacheSize = 1000;
  private boolean enableCache = true;

  private transient List<Status> cachedStatuses = Lists.newArrayList();
  private transient boolean tsActivate = true;
  private transient boolean emitFromCache = true;
  @Override
  public void beginWindow(long windowId)
  {
    super.beginWindow(windowId);
    emitFromCache = true;
  }

  @Override
  public void onStatus(Status status)
  {
    if (!tsActivate) {
      return;
    }
    super.onStatus(status);
    if (enableCache) {
      cachedStatuses.add(status);
      if (cachedStatuses.size() >= cacheSize) {
        // This will close the twitterstream.
        deactivate();
        LOG.info("TSStream deactived. Further tweets will be from cached stream");
        tsActivate = false;
      }
    }
  }

  @Override
  public void emitTuples()
  {
    if (tsActivate) {
      super.emitTuples();
    } else {
      if (!emitFromCache) {
        return;
      }
      for (Status s : pickNRandom((int)((cacheSize / 2)))) {
        if (status.isConnected()) {
          status.emit(s);
        }

        if (text.isConnected()) {
          text.emit(s.getText());
        }

        if (url.isConnected()) {
          URLEntity[] entities = s.getURLEntities();
          if (entities != null) {
            for (URLEntity ue : entities) {
              url.emit((ue.getExpandedURL() == null ? ue.getURL() : ue.getExpandedURL()).toString());
            }
          }
        }

        if (hashtag.isConnected()) {
          HashtagEntity[] hashtagEntities = s.getHashtagEntities();
          if (hashtagEntities != null) {
            for (HashtagEntity he : hashtagEntities) {
              hashtag.emit(he.getText());
            }
          }
        }
      }
      emitFromCache = false;
    }
  }

  public List<Status> pickNRandom(int n) {
    List<Status> copy = new LinkedList<Status>(cachedStatuses);
    Collections.shuffle(copy);
    return copy.subList(0, n);
  }

  public void setCacheSize(long cacheSize)
  {
    this.cacheSize = cacheSize;
  }

  public long getCacheSize()
  {
    return cacheSize;
  }

  public boolean isEnableCache()
  {
    return enableCache;
  }

  public void setEnableCache(boolean enableCache)
  {
    this.enableCache = enableCache;
  }
}
