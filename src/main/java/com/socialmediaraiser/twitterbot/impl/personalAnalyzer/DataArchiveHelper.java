package com.socialmediaraiser.twitterbot.impl.personalAnalyzer;

import com.socialmediaraiser.twitter.dto.tweet.ITweet;
import com.socialmediaraiser.twitter.dto.tweet.TweetDTOv1;
import com.socialmediaraiser.twitter.dto.tweet.TweetType;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.CustomLog;

@CustomLog
public class DataArchiveHelper extends AbstractSearchHelper {

  private List<ITweet> tweets = new ArrayList<>();

  public DataArchiveHelper(String userName, String archiveFileName, Date initDate) {
    super(userName);
    File             file      = new File(getClass().getClassLoader().getResource(archiveFileName).getFile());
    List<TweetDTOv1> allTweets = null;
    try {
      allTweets = this.getTwitterClient().readTwitterDataFile(file);
    } catch (IOException e) {
      LOGGER.severe(e.getMessage());
    }
    for (TweetDTOv1 tweet : allTweets) {
      Date tweetDate = tweet.getCreatedAt();
      if (tweetDate != null && tweetDate.compareTo(initDate) > 0) {
        this.tweets.add(tweet);
      }
    }
  }

  public Map<String, UserInteraction> countRepliesGiven() {
    LOGGER.info("\ncounting replies from user (archive)...");
    Map<String, UserInteraction> result = Stream.ofAll(tweets)
                                                .filter(tweet -> tweet.getInReplyToUserId()!=null)
                                                .filter(tweet -> this.isUserInList(tweet.getInReplyToUserId()))
                                                .filter(tweet -> !this.getUserId().equals(this.getTwitterClient().getInitialTweet(tweet, true).getAuthorId()))
                                                .peek(tweet -> LOGGER.info("analyzing reply : " + tweet.getText())) // @todo display userName?
                                                .map(tweet -> this.getTwitterClient().getInitialTweet(tweet, true))
                                                .filter(tweet -> tweet.getAuthorId()!=null) // @todo mentions without reply don't work (ex: 1261371673560973312)
                                                .groupBy(ITweet::getAuthorId)
                                                .map(this::getTurpleAnswer);
    return result;
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

  // @todo use Stream
  private TweetInteraction countRetweetsOfTweet(ITweet tweet) {
    TweetInteraction result       = new TweetInteraction();
    List<String>     retweeterIds = this.getTwitterClient().getRetweetersId(tweet.getId());
    LOGGER.info("counting " + retweeterIds.size() + " retweeters of tweet " + tweet.getId());
    for (String retweeterId : retweeterIds) {
      if (this.isUserInList(retweeterId)) {
        result.getRetweeterIds().add(retweeterId);
      }
    }
    return result;
  }

  // @todo use Stream
  public Map<String, UserInteraction> countRetweetsGiven() {
    LOGGER.info("\ncounting retweets given (archive)...");
    Stream<ITweet> givenRetweets = Stream.ofAll(this.filterTweetsByRetweet(true));
    Map<String, UserInteraction> result = givenRetweets
        .map(tweet -> this.getTwitterClient().getTweet(tweet.getId()))
        .filter(tweet -> tweet.getInReplyToStatusId(TweetType.RETWEETED)!=null)
        .peek(tweet -> LOGGER.info("analyzing RT : " + tweet.getText()))
        .map(tweet -> this.getTwitterClient().getTweet(tweet.getInReplyToStatusId(TweetType.RETWEETED))) // @todo ko
        .filter(tweet -> tweet.getId()!=null)
        .filter(tweet -> this.isUserInList(tweet.getAuthorId()))
        .groupBy(ITweet::getAuthorId)
        .map(this::getTurpleRetweet);

    return result;
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
