package com.socialmediaraiser.twitterbot.impl;

import com.socialmediaraiser.twitter.TwitterClient;
import com.socialmediaraiser.twitter.dto.tweet.ITweet;
import com.socialmediaraiser.twitter.dto.user.IUser;
import com.socialmediaraiser.twitter.dto.user.UserDTOv1;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Stream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;

@Getter
public abstract class AbstractSearchHelper {

  private String        userName;
  private String        userId;
  private TwitterClient twitterClient = new TwitterClient();
  private List<IUser>   followings;
  private List<IUser>   followers;
  private Set<IUser>    allUsers;

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
    UserDTOv1 retweeter = UserDTOv1.builder().id(userId).build();
    return (this.getAllUsers().contains(retweeter));
  }

  public Tuple2<String, UserInteraction> getTupleLikeGiven(String userId, Stream<ITweet> tweets) {
    return Tuple.of(userId,
                    tweets.foldLeft(new UserInteraction(),
                                    (interaction, tweet) -> interaction.addLike(tweet.getId())));
  }

  public Tuple2<String, UserInteraction> getTupleAnswerGiven(String userId, Stream<ITweet> tweets) {
    return Tuple.of(userId,
                    tweets.foldLeft(new UserInteraction(),
                                    (interaction, tweet) -> interaction.addAnswer(tweet.getId())));
  }

  public Tuple2<String, UserInteraction> getTupleRetweetGiven(String userId, Stream<ITweet> tweets) {
    return Tuple.of(userId,
                    tweets.foldLeft(new UserInteraction(),
                                    (interaction, tweet) -> interaction.addRetweet(tweet.getId())));
  }

  public Tuple2<String, TweetInteraction> getTupleRetweetReceived(String userId, Stream<ITweet> tweets) {
    return Tuple.of(userId,
                    tweets.foldLeft(new TweetInteraction(),
                                    (interaction, tweet) -> interaction.addRetweeted(tweet.getId())));
  }
}
