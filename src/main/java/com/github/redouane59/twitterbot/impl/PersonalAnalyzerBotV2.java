package com.github.redouane59.twitterbot.impl;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.dto.tweet.TweetType;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import java.util.List;

public class PersonalAnalyzerBotV2 {

  private String        userName;
  private TwitterClient twitterClient;

  public PersonalAnalyzerBotV2(String userName, TwitterCredentials twitterCredentials) {
    this.userName      = userName;
    this.twitterClient = new TwitterClient(twitterCredentials);

  }


  public Map<String, UserStats> getUserStatsMap() {
    Map<String, UserInteraction> userInteractions = HashMap.empty();

    List<Tweet> sentTweets = twitterClient.searchForTweetsWithin7days("from:" + this.userName);

    for (Tweet sentTweet : sentTweets) {
      String conversationId = sentTweet.getConversationId();
      switch (sentTweet.getTweetType()) {
        case QUOTED:
          String authorId = twitterClient.getTweet(sentTweet.getInReplyToStatusId(TweetType.QUOTED)).getAuthorId();
          userInteractions =
              userInteractions.put(authorId,
                                   userInteractions.getOrElse(authorId, new UserInteraction()).addQuoted(conversationId));
          break;
        case RETWEETED:
          userInteractions =
              userInteractions.put(sentTweet.getInReplyToStatusId(TweetType.RETWEETED),
                                   userInteractions.getOrElse(sentTweet.getInReplyToStatusId(TweetType.RETWEETED), new UserInteraction())
                                                   .addRetweeted(conversationId));
          break;
        case REPLIED_TO:
          userInteractions =
              userInteractions.put(sentTweet.getInReplyToUserId(),
                                   userInteractions.getOrElse(sentTweet.getInReplyToUserId(), new UserInteraction()).addAnswered(conversationId));
          break;
        case DEFAULT:
          // ...
          break;
      }

      if (sentTweet.getTweetType() != TweetType.RETWEETED) {
        // retweeters
        if (sentTweet.getRetweetCount() > 0) {
          List<String> retweeters = twitterClient.getRetweetersId(sentTweet.getId());
          for (String retweeterId : retweeters) {
            userInteractions =
                userInteractions.put(retweeterId, userInteractions.getOrElse(retweeterId, new UserInteraction()).addRetweet(conversationId));
          }
        }
        // quoters
        String      sentTweetUrl = "https://twitter.com/" + userName + "/status/" + sentTweet.getId();
        List<Tweet> quotedTweets = twitterClient.searchForTweetsWithin7days("url:\"" + sentTweetUrl + "\"");
        for (Tweet quotedTweet : quotedTweets) {
          userInteractions =
              userInteractions.put(quotedTweet.getAuthorId(),
                                   userInteractions.getOrElse(quotedTweet.getAuthorId(), new UserInteraction()).addQuote(conversationId));
        }

      }
    }
    // answerers
    List<Tweet> receivedTweets = twitterClient.searchForTweetsWithin7days("to:" + this.userName);
    for (Tweet receivedTweet : receivedTweets) {
      userInteractions =
          userInteractions.put(receivedTweet.getAuthorId(),
                               userInteractions.getOrElse(receivedTweet.getAuthorId(), new UserInteraction()).addAnswer(receivedTweet.getId()));
    }

    return null;
  }


}
