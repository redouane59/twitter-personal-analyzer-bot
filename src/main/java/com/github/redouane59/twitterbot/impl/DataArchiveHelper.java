package com.github.redouane59.twitterbot.impl;

import com.github.redouane59.twitter.dto.tweet.ITweet;
import com.github.redouane59.twitter.dto.tweet.TweetDTOv1;
import com.github.redouane59.twitter.dto.tweet.TweetType;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataArchiveHelper extends AbstractSearchHelper {

  private List<ITweet> tweets = new ArrayList<>();

  public DataArchiveHelper(String userName, String archiveFileName, LocalDateTime fromDate) {
    super(userName);
    File             file      = new File(Objects.requireNonNull(getClass().getClassLoader().getResource(archiveFileName)).getFile());
    List<TweetDTOv1> allTweets = new ArrayList<>();
    try {
      allTweets = this.getTwitterClient().readTwitterDataFile(file);
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
    }
    for (TweetDTOv1 tweet : allTweets) {
      LocalDateTime tweetDate = tweet.getCreatedAt();
      if (tweetDate != null && tweetDate.compareTo(fromDate) > 0) {
        this.tweets.add(tweet);
      }
    }
  }

  public Map<String, UserInteraction> countRepliesGiven() {
    LOGGER.info("\ncounting replies from user (archive)...");
    return Stream.ofAll(tweets)
                 .filter(tweet -> tweet.getInReplyToUserId()!=null)
                 .filter(tweet -> this.isUserInList(tweet.getInReplyToUserId()))
                 .filter(tweet -> !this.getUserId().equals(this.getTwitterClient().getTweet(tweet.getConversationId()).getAuthorId()))
                 .peek(tweet -> LOGGER.info("analyzing DATA reply : " + tweet.getText()))
                 .map(tweet -> this.getTwitterClient().getTweet(tweet.getConversationId()))
                 .filter(tweet -> tweet.getAuthorId()!=null)
                 .groupBy(ITweet::getAuthorId)
                 .map(this::getTupleAnswerGiven);
  }

  public Map<String, UserInteraction> countQuotesGiven() {
    LOGGER.info("\ncounting quotes from user (archive)...");
    return Stream.ofAll(tweets)
                 .map(tweet -> this.getTwitterClient().getTweet(tweet.getId()))
                 .filter(tweet -> tweet.getId()!=null)
                 .filter(tweet -> tweet.getTweetType() == TweetType.QUOTED)
                 .filter(tweet -> tweet.getInReplyToStatusId(TweetType.QUOTED)!=null)
                 .peek(tweet -> LOGGER.info("analyzing DATA quote given : " + tweet.getText()))
                 .filter(tweet -> this.isUserInList(this.getTwitterClient().getTweet(tweet.getInReplyToStatusId(TweetType.QUOTED)).getAuthorId()))
                 .groupBy(tweet -> this.getTwitterClient().getTweet(tweet.getInReplyToStatusId(TweetType.QUOTED)).getAuthorId())
                 .map(this::getTupleRetweetGiven);
  }

  /**
   * Count the users who retweeted user tweets
   * @return a map with tweet id as key and TweeteInteraction as value
   */
  public Map<String, TweetInteraction> countRetweetsReceived() {
    LOGGER.info("\ncounting retweets received (archive)...");
    return Stream.ofAll(filterTweetsByRetweet(false))
                 .filter(tweet -> tweet.getRetweetCount() > 0 && !tweet.getText().startsWith(("@")))
                 .toMap(ITweet::getId,
                        this::countRetweetsOfTweet);
  }

  private TweetInteraction countRetweetsOfTweet(ITweet tweet) {
    List<String>     retweeterIds = this.getTwitterClient().getRetweetersId(tweet.getId());
    LOGGER.info("counting " + retweeterIds.size() + " retweeters of tweet " + tweet.getId());
    return Stream.ofAll(retweeterIds)
                                              .filter(this::isUserInList)
                                              .foldLeft(new TweetInteraction(), TweetInteraction::addRetweeter);
  }

  public Map<String, UserInteraction> countRetweetsGiven() {
    LOGGER.info("\ncounting retweets given (archive)...");
    Stream<ITweet> givenRetweets = Stream.ofAll(this.filterTweetsByRetweet(true));
    return givenRetweets
        .map(tweet -> this.getTwitterClient().getTweet(tweet.getId()))
        .filter(tweet -> tweet.getInReplyToStatusId(TweetType.RETWEETED)!=null)
        .peek(tweet -> LOGGER.info("analyzing RT : " + tweet.getText()))
        .map(tweet -> this.getTwitterClient().getTweet(tweet.getInReplyToStatusId(TweetType.RETWEETED)))
        .filter(tweet -> tweet.getId()!=null)
        .filter(tweet -> this.isUserInList(tweet.getAuthorId()))
        .groupBy(ITweet::getAuthorId)
        .map(this::getTupleRetweetGiven);
  }

  // @todo add mentions argument
  public List<ITweet> filterTweetsByRetweet(boolean onlyRetweets) {
    List<ITweet> result = new ArrayList<>();
    for (ITweet tweet : this.tweets) {
      if (tweet.getText().startsWith("RT @") == onlyRetweets) {
        result.add(tweet);
      }
    }
    return result;
  }
}
