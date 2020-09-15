package com.github.redouane59.twitterbot.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.user.User;
import com.github.redouane59.twitter.helpers.ConverterHelper;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class SevenDaysPersonalAnalyzerBot extends AbstractPersonalAnalyzerBot {

  private final LocalDateTime     iniDate       = ConverterHelper.dayBeforeNow(7);


  public SevenDaysPersonalAnalyzerBot(final String userName, TwitterCredentials twitterCredentials) {
    super(userName, twitterCredentials);
  }

  public void launch(boolean includeFollowers, boolean includeFollowings, boolean onyFollowBackFollowers){
    Map<String, UserStats>  userStats = this.getUserStatsMap();
  }

  @Override
  protected Map<String, UserInteraction> countGivenLikes() {
    return null;
  }

  @Override
  protected Map<String, UserInteraction> countRepliesGiven() {
    return null;
  }

  @Override
  protected Map<String, UserInteraction> countQuotesGiven() {
    return null;
  }

  @Override
  protected Map<String, UserInteraction> countRetweetsGiven() {
    return null;
  }

  @Override
  protected Map<String, TweetInteraction> countLikesReceived() {
    return null;
  }

  @Override
  protected Map<String, TweetInteraction> countRepliesReceived() {
    return null;
  }

  @Override
  protected Map<String, TweetInteraction> countQuotesReceived() {
    return null;
  }

  @Override
  protected Map<String, TweetInteraction> countRetweetsReceived() {
    return null;
  }

}
