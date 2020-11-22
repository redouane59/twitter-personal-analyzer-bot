package com.github.redouane59.twitterbot.impl;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.dto.tweet.TweetType;
import com.github.redouane59.twitter.dto.user.User;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import io.vavr.Tuple2;
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
      String authorId;
      switch (sentTweet.getTweetType()) {
        case QUOTED:
          authorId = twitterClient.getTweet(sentTweet.getInReplyToStatusId(TweetType.QUOTED)).getAuthorId();
          userInteractions =
              userInteractions.put(authorId,
                                   userInteractions.getOrElse(authorId, new UserInteraction()).addQuoted(conversationId));
          break;
        case RETWEETED:
          String retweetedTweetId = sentTweet.getInReplyToStatusId(TweetType.RETWEETED);
          authorId = twitterClient.getTweet(retweetedTweetId).getAuthorId();
          userInteractions =
              userInteractions.put(authorId,
                                   userInteractions.getOrElse(authorId, new UserInteraction())
                                                   .addRetweeted(conversationId));
          break;
        case REPLIED_TO:
          authorId = sentTweet.getInReplyToUserId();
          userInteractions =
              userInteractions.put(authorId,
                                   userInteractions.getOrElse(authorId, new UserInteraction()).addAnswered(conversationId));
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

  private void printResult(Map<String, UserInteraction> userInteractions) {
    System.out.println("UserName;Ansd;Qtd;Rtd;Ans;Qts;Rts");
    for (Tuple2<String, UserInteraction> tp : userInteractions) {
      User user = twitterClient.getUserFromUserId(tp._1());
      if (user == null || user.getName() == null) {
        System.out.println("error user " + tp._1() + " not found");
      } else {
        System.out.println(user.getName()
                           + ";" + tp._2().getAnsweredIds().size()
                           + ";" + tp._2().getQuotedIds().size()
                           + ";" + tp._2().getRetweetedIds().size()
                           + ";" + tp._2().getAnswersIds().size()
                           + ";" + tp._2().getQuotesIds().size()
                           + ";" + tp._2().getRetweetsIds().size()
        );
      }

    }
  }

}
