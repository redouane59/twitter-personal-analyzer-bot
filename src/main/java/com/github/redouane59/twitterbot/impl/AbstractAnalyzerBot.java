package com.github.redouane59.twitterbot.impl;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.dto.user.User;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;

@Getter
public class AbstractAnalyzerBot {

  private String        userName;
  private String        userId;
  private TwitterClient twitterClient;
  private List<User>    followings;
  private List<User>    followers;
  private Set<User>     allUsers;

  public AbstractAnalyzerBot(String userName, TwitterCredentials twitterCredentials) {
    this.userName      = userName;
    this.twitterClient = new TwitterClient(twitterCredentials);
    this.userId        = this.twitterClient.getUserFromUserName(userName).getId();
    this.followings    = this.twitterClient.getFollowingUsers(userId);
    this.followers     = this.twitterClient.getFollowerUsers(userId);
    this.allUsers      = new HashSet<>() {
      {
        addAll(followings);
        addAll(followers);
      }
    };
  }

  private void printCSVResult(Map<String, UserInteraction> userInteractions) {
    System.out.println("UserName;Ansd;Qtd;Rtd;Ans;Qts;Rts");
    for (Tuple2<String, UserInteraction> tp : userInteractions) {
      User user = this.getTwitterClient().getUserFromUserId(tp._1());
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

  public Tuple2<String, UserInteraction> getTupleLikeGiven(String userId, Stream<Tweet> tweets) {
    return Tuple.of(userId,
                    tweets.foldLeft(new UserInteraction(),
                                    (interaction, tweet) -> interaction.addLike(tweet.getId())));
  }

}
