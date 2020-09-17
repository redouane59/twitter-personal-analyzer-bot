package com.github.redouane59.twitterbot.impl;

import com.github.redouane59.twitter.helpers.ConverterHelper;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public class SevenDaysPersonalAnalyzerBot extends AbstractPersonalAnalyzerBot {

  private final LocalDateTime initDate = ConverterHelper.dayBeforeNow(6).truncatedTo(ChronoUnit.DAYS);

  public SevenDaysPersonalAnalyzerBot(final String userName, TwitterCredentials twitterCredentials) {
    super(userName, twitterCredentials);
  }

  @Override
  protected Map<String, UserInteraction> countLikesGiven() {
    // @todo impossible ot to have 7 days likes
    return this.getApiSearchHelper().countGivenLikesOnStatuses();
  }

  @Override
  protected Map<String, UserInteraction> countRepliesGiven() {
    return this.getApiSearchHelper().countRecentRepliesGiven(initDate);
  }

  @Override
  protected Map<String, UserInteraction> countQuotesGiven() {
    return this.getApiSearchHelper().countRecentQuotesGiven(initDate);
  }

  //@todo quotes not working ?
  @Override
  protected Map<String, UserInteraction> countRetweetsGiven() {
    return this.getApiSearchHelper().countRecentRetweetsGiven(initDate);
  }

  @Override
  protected Map<String, TweetInteraction> countLikesReceived() {
    LOGGER.error("not implemented");
    return HashMap.empty();
  }

  @Override
  protected Map<String, TweetInteraction> countRepliesReceived() {
    Map<String, TweetInteraction> result = getApiSearchHelper().countRepliesReceived(true);
    return result;
  }

  @Override
  protected Map<String, TweetInteraction> countQuotesReceived() {
    return this.getApiSearchHelper().countQuotesReceived(true);
  }

  @Override
  protected Map<String, TweetInteraction> countRetweetsReceived() {
    Map<String, TweetInteraction> result = this.getApiSearchHelper().countRecentRetweetsReceived(initDate);
    return result;
  }

}
