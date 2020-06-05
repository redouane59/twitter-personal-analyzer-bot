package com.socialmediaraiser.twitterbot.impl.personalAnalyzer;

import com.socialmediaraiser.twitter.dto.tweet.ITweet;
import com.socialmediaraiser.twitter.dto.tweet.TweetDTOv1;
import com.socialmediaraiser.twitter.dto.tweet.TweetType;
import com.socialmediaraiser.twitterbot.impl.personalAnalyzer.UserInteractions.UserInteractionX;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.CustomLog;

@CustomLog
public class DataArchiveHelper extends AbstractSearchHelper {

  private List<TweetDTOv1> tweets = new ArrayList<>();

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
    Map<String, UserInteraction> result = HashMap.empty();
    int         repliesGiven         = 0;
    for (TweetDTOv1 tweet : tweets) { // @todo check for retweets + exclude own answers
      // checking the reply I gave to other users
      String inReplyUserId = tweet.getInReplyToUserId();
      if (inReplyUserId != null && this.isUserInList(tweet.getInReplyToUserId())) {
        repliesGiven++;
        ITweet initialTweet = this.getTwitterClient().getInitialTweet(tweet, true);
        if (!this.getUserId().equals(initialTweet.getAuthorId())) {
          System.out.print(".");
          if(!result.containsKey(inReplyUserId)){
            result = result.put(inReplyUserId, new UserInteraction());
          }
          result.get(inReplyUserId).get().addRetweet(initialTweet.getId());
        }
      }
    }
    LOGGER.info(repliesGiven + " replies given found, " + result.size() + " replies given saved");
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
                 .toMap(TweetDTOv1::getId,
                        this::countRetweetsOfTweet);
  }

  private TweetInteraction countRetweetsOfTweet(TweetDTOv1 tweet) {
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

  public Map<String, UserInteraction> countRetweetsGiven() {
    LOGGER.info("\ncounting retweets given (archive)...");

    Map<String, UserInteraction> result = HashMap.empty();
    List<TweetDTOv1> retweets = this.filterTweetsByRetweet(true);
    int              rtCount  = 0;
    for (TweetDTOv1 tweet : retweets) {
      ITweet fullTweet        = this.getTwitterClient().getTweet(tweet.getId());
      String retweetedTweetId = fullTweet.getInReplyToStatusId(TweetType.RETWEETED);
      if (retweetedTweetId != null) {
        ITweet retweetedTweet = this.getTwitterClient().getTweet(retweetedTweetId); // @todo check null
        String userId         = retweetedTweet.getAuthorId(); // because info missing in data archive
        if (this.isUserInList(userId)) {
          if(!result.containsKey(userId) || result.get(userId).isEmpty()){
            result = result.put(userId, new UserInteraction());
          }
          result.get(userId).get().addRetweet(retweetedTweetId);
          System.out.println(".");
          rtCount++;
        }
      }
    }
    LOGGER.info(rtCount + " retweets given found");
    return result;
  }

  // @todo add mentions argument
  public List<TweetDTOv1> filterTweetsByRetweet(boolean onlyRetweets) {
    List<TweetDTOv1> result = new ArrayList<>();
    for (TweetDTOv1 tweet : this.tweets) {
      if (tweet.getText().startsWith("RT @") == onlyRetweets) {
        result.add(tweet);
      }
    }
    return result;
  }
}
