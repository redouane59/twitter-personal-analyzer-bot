package com.github.redouane59.twitterbot.impl;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.dto.tweet.TweetType;
import com.github.redouane59.twitter.dto.user.User;
import com.github.redouane59.twitter.dto.user.UserV1;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Stream;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;

@Getter
public abstract class AbstractSearchHelper {

  private String        userName;
  private String        userId;
  private TwitterClient twitterClient;

  {
    try {
      twitterClient = new TwitterClient(TwitterClient.OBJECT_MAPPER
                                            .readValue(new File("C:/Users/Perso/Documents/GitHub/twitter-credentials.json"),
                                                       TwitterCredentials.class));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private List<User> followings;
  private List<User> followers;
  private Set<User>  allUsers;

  public AbstractSearchHelper(String userName) {
    this.userName   = userName;
    this.userId     = this.twitterClient.getUserFromUserName(userName).getId();
    this.followings = this.twitterClient.getFollowingUsers(userId);
    this.followers  = this.twitterClient.getFollowerUsers(userId);
    this.allUsers   = new HashSet<>() {
      {
        addAll(followings);
        addAll(followers);
      }
    };
  }

  public boolean isUserInList(String userId) {
    if (userId == null) {
      return false;
    }
    UserV1 retweeter = UserV1.builder().id(userId).build();
    return (this.getAllUsers().contains(retweeter));
  }

  public Tuple2<String, UserInteraction> getTupleLikeGiven(String userId, Stream<Tweet> tweets) {
    return Tuple.of(userId,
                    tweets.foldLeft(new UserInteraction(),
                                    (interaction, tweet) -> interaction.addLike(tweet.getId())));
  }

  public Tuple2<String, UserInteraction> getTupleAnswerGiven(String userId, Stream<Tweet> tweets) {
    return Tuple.of(userId,
                    tweets.foldLeft(new UserInteraction(),
                                    (interaction, tweet) -> interaction.addAnswer(tweet.getId())));
  }

  public Tuple2<String, UserInteraction> getTupleRetweetGiven(String userId, Stream<Tweet> tweets) {
    return Tuple.of(userId,
                    tweets.foldLeft(new UserInteraction(),
                                    (interaction, tweet) -> interaction.addRetweet(tweet.getId())));
  }

  public Tuple2<String, TweetInteraction> getTupleRetweetReceived(String userId, Stream<Tweet> tweets) {
    return Tuple.of(userId,
                    tweets.foldLeft(new TweetInteraction(),
                                    (interaction, tweet) -> interaction.addRetweeter(tweet.getAuthorId())));
  }


  public Tuple2<String, TweetInteraction> getTurpleAnswerReceived(String tweetId, Stream<Tweet> tweets) {
    return Tuple.of(tweetId,
                    tweets.foldLeft(new TweetInteraction(),
                                    (interaction, tweet) -> interaction.addAnswerer(tweet.getAuthorId())));
  }

  // @todo KO if quote inside the thread
  public Tweet getInitialTweet(Tweet tweet) {
    if (tweet.getConversationId() != null) {
      return this.getTwitterClient().getTweet(tweet.getConversationId());
    }
    Tweet currentTweet = tweet;
    while (currentTweet.getInReplyToStatusId() != null && currentTweet.getTweetType() != TweetType.QUOTED) {
      currentTweet = this.getTwitterClient().getTweet(currentTweet.getInReplyToStatusId());
    }
    return currentTweet;
  }

}
