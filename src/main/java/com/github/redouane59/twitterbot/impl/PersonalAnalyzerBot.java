package com.github.redouane59.twitterbot.impl;

import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.dto.tweet.TweetType;
import com.github.redouane59.twitter.dto.user.User;
import com.github.redouane59.twitter.dto.user.UserV2;
import com.github.redouane59.twitter.helpers.ConverterHelper;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonalAnalyzerBot extends AbstractAnalyzerBot {

  private final LocalDateTime fromDate = ConverterHelper.dayBeforeNow(30).truncatedTo(ChronoUnit.DAYS).plusHours(1);
  private final LocalDateTime toDate   = ConverterHelper.dayBeforeNow(7).truncatedTo(ChronoUnit.DAYS);
  public final  String        ENV_NAME = "30days";

  public PersonalAnalyzerBot(String userName, TwitterCredentials twitterCredentials) {
    super(userName, twitterCredentials);
  }

  /**
   * @return a map of userId, UserInteraction
   */
  public Map<String, UserInteraction> getUsersInteractions() {
    LOGGER.debug("--- getUsersInteractions ---");
    Map<String, UserInteraction> userInteractions = HashMap.empty();
    // counting likes given
    userInteractions = this.countLiked(userInteractions);
    // retrieve all the user sent tweets through API
    String      query      = "from:" + this.getUserName();
    List<Tweet> sentTweets = this.getTwitterClient().searchForTweetsWithin7days(query);
    sentTweets.addAll(this.getTwitterClient().searchForTweetsWithin30days(query, fromDate, toDate, ENV_NAME));
    LOGGER.debug("\nAnalyzing " + sentTweets.size() + " sent tweets...");
    for (Tweet sentTweet : sentTweets) {
      String conversationId = sentTweet.getConversationId();
      // counting quotes, retweets, and answers given
      userInteractions = countSentInteractions(sentTweet, conversationId, userInteractions);
      // counting retweets and quotes received
      if (sentTweet.getTweetType() != TweetType.RETWEETED) {
        userInteractions = countRetweeters(sentTweet, conversationId, userInteractions);
        userInteractions = countQuoters(sentTweet, conversationId, userInteractions);
      }
    }
    // counting answers received
    userInteractions = this.countAnswerers(userInteractions);
    return userInteractions;
  }


  /**
   * Counts a quotes, a retweets or answer given from a sent tweet
   */
  private Map<String, UserInteraction> countSentInteractions(Tweet sentTweet,
                                                             String conversationId,
                                                             final Map<String, UserInteraction> oldUserInteractions) {
    LOGGER.debug("analyzing tweet : " + sentTweet.getText().replace("\n", ""));
    Map<String, UserInteraction> newUserInteractions = HashMap.ofEntries(oldUserInteractions);
    String                       authorId;
    switch (sentTweet.getTweetType()) {
      case QUOTED:
        String tweetId = sentTweet.getInReplyToStatusId(TweetType.QUOTED);
        if (tweetId != null) {
          authorId            = this.getTwitterClient().getTweet(tweetId).getAuthorId();
          newUserInteractions =
              newUserInteractions.put(authorId,
                                      newUserInteractions.getOrElse(authorId, new UserInteraction()).addQuoted(conversationId));
        }
        break;
      case RETWEETED:
        String retweetedTweetId = sentTweet.getInReplyToStatusId(TweetType.RETWEETED);
        if (retweetedTweetId != null) {
          authorId            = this.getTwitterClient().getTweet(retweetedTweetId).getAuthorId();
          newUserInteractions =
              newUserInteractions.put(authorId,
                                      newUserInteractions.getOrElse(authorId, new UserInteraction())
                                                         .addRetweeted(conversationId));
        }
        break;
      case REPLIED_TO:
        authorId = sentTweet.getInReplyToUserId();
        newUserInteractions =
            newUserInteractions.put(authorId,
                                    newUserInteractions.getOrElse(authorId, new UserInteraction()).addAnswered(conversationId));
        break;
      case DEFAULT:
        // ...
        break;
    }

    return newUserInteractions;
  }

  /*
   * Counts retweets received on a tweet
   */
  private Map<String, UserInteraction> countRetweeters(Tweet sentTweet,
                                                       String conversationId,
                                                       final Map<String, UserInteraction> oldUserInteractions) {
    LOGGER.debug("analyzing retweets of : " + sentTweet.getText().replace("\n", ""));
    Map<String, UserInteraction> newUserInteractions = HashMap.ofEntries(oldUserInteractions);
    if (sentTweet.getRetweetCount() > 0) {
      List<String> retweeters = this.getTwitterClient().getRetweetersId(sentTweet.getId());
      for (String retweeterId : retweeters) {
        newUserInteractions =
            newUserInteractions.put(retweeterId, newUserInteractions.getOrElse(retweeterId, new UserInteraction()).addRetweet(conversationId));
      }
    }
    return newUserInteractions;
  }

  /**
   * Counts quotes received on a tweet
   */
  private Map<String, UserInteraction> countQuoters(Tweet sentTweet,
                                                    String conversationId,
                                                    final Map<String, UserInteraction> oldUserInteractions) {
    LOGGER.debug("analyzing quotes of : " + sentTweet.getText().replace("\n", ""));
    Map<String, UserInteraction> newUserInteractions = HashMap.ofEntries(oldUserInteractions);
    String                       sentTweetUrl        = "https://twitter.com/" + this.getUserName() + "/status/" + sentTweet.getId();
    List<Tweet>                  quotedTweets        = this.getTwitterClient().searchForTweetsWithin7days("url:\"" + sentTweetUrl + "\"");
    for (Tweet quotedTweet : quotedTweets) {
      newUserInteractions =
          newUserInteractions.put(quotedTweet.getAuthorId(),
                                  newUserInteractions.getOrElse(quotedTweet.getAuthorId(), new UserInteraction()).addQuote(conversationId));
    }
    return newUserInteractions;
  }

  /**
   * Counts all answers received within 7 days
   */
  private Map<String, UserInteraction> countAnswerers(final Map<String, UserInteraction> oldUserInteractions) {
    Map<String, UserInteraction> newUserInteractions = HashMap.ofEntries(oldUserInteractions);
    String                       query               = "to:" + this.getUserName();
    List<Tweet>                  receivedTweets      = this.getTwitterClient().searchForTweetsWithin7days(query);
    receivedTweets.addAll(this.getTwitterClient().searchForTweetsWithin30days(query, fromDate, toDate, ENV_NAME));
    LOGGER.debug("\nAnalyzing " + receivedTweets.size() + " received tweets...");
    for (Tweet receivedTweet : receivedTweets) {
      LOGGER.debug("analyzing answer : " + receivedTweet.getText().replace("\n", ""));
      newUserInteractions =
          newUserInteractions.put(receivedTweet.getAuthorId(),
                                  newUserInteractions.getOrElse(receivedTweet.getAuthorId(), new UserInteraction()).addAnswer(receivedTweet.getId()));
    }
    return newUserInteractions;
  }

  private Map<String, UserInteraction> countLiked(final Map<String, UserInteraction> oldUserInteractions) {
    Map<String, UserInteraction> newUserInteractions = HashMap.ofEntries(oldUserInteractions);
    Stream<Tweet>                likedTweets         = Stream.ofAll(this.getTwitterClient().getFavorites(this.getUserId(), 5000));
    return newUserInteractions.merge(likedTweets
                                         .filter(tweet -> tweet.getInReplyToStatusId() == null)
                                         // .filter(tweet -> this.isUserInList(tweet.getAuthorId()))
                                         .peek(tweet -> LOGGER.info("analyzing tweet : " + tweet.getText().replace("\n", "")))
                                         .groupBy(Tweet::getAuthorId)
                                         .map(this::getTupleLikeGiven));
  }

  public int getNbTweetsWithin7Days(User user) {
    String query = "from:" + user.getName() + " -is:reply -is:retweet";
    return this.getTwitterClient().searchForTweetsWithin7days(query).size();
  }

  public int getMedianInteractionScore(User user) {
    String        query          = "from:" + user.getName() + " -is:reply -is:retweet";
    LocalDateTime fromDate       = ConverterHelper.dayBeforeNow(6).truncatedTo(ChronoUnit.DAYS);
    LocalDateTime toDate         = ConverterHelper.minutesBeforeNow(120).truncatedTo(ChronoUnit.DAYS);
    List<Tweet>   lastUserTweets = this.getTwitterClient().searchForTweetsWithin7days(query, fromDate, toDate);
    List<Integer> scores;
    if (lastUserTweets.size() == 0) {
      return 0;
    }
    // RT : 4 pts / Quote : 3 pts / Reply : 2 pts / like : 1 pt
    scores = lastUserTweets.stream()
                           .map(tweet -> RankingConfiguration.INTERACTION_SCORE_RT_COEFF * tweet.getRetweetCount()
                                         + RankingConfiguration.INTERACTION_SCORE_QUOTE_COEFF * tweet.getQuoteCount()
                                         + RankingConfiguration.INTERACTION_SCORE_REPLY_COEFF * tweet.getReplyCount()
                                         + RankingConfiguration.INTERACTION_SCORE_LIKE_COEFF * tweet.getLikeCount())
                           .collect(Collectors.toList());
    int size = scores.size();
    double result = scores.stream().sorted()
                          .skip((size - 1) / 2).limit(2 - size % 2).mapToInt(x -> x).average().orElse(0.0);
    return (int) result;
  }

  public boolean hasToAddUser(User user, boolean showFollowings, boolean showFollowers, boolean onyFollowBackUsers) {
    // case 0 : only follow back users
    if (onyFollowBackUsers && this.getFollowings().contains(user) && !this.getFollowers().contains(user)) {
      return false;
    }
    // case 1 : show all the people i'm following and all the users following me
    if (!showFollowers && !showFollowings) {
      return true;
    }
    // case 2 : show all the people I'm following who are following me back
    else if (showFollowers && showFollowings && onyFollowBackUsers) {
      return (/*followings.contains(user) &&*/ this.getFollowers().contains(user));
    }
    // case 3 : show all the people i'm following or all the people who are following me
    else {
      return ((this.getFollowings().contains(user) && showFollowings) || this.getFollowers().contains(user) && showFollowers);
    }
  }

  public List<RankedUser> getRankedUsers(final Map<String, UserInteraction> userInteractions) {
    LOGGER.debug("\n--- getRankedUsers ---");
    Map<String, UserInteraction> newUserInteractions = HashMap.ofEntries(userInteractions);
    List<RankedUser>             result              = new ArrayList<>();
    // @Todo generate missing rankedUsers
    // adding users without interactions
    for (User u : this.getAllUsers()) {
      if (!newUserInteractions.containsKey(u.getId())) {
        newUserInteractions = newUserInteractions.put(u.getId(), new UserInteraction());
      }
    }
    for (Tuple2<String, UserInteraction> tp : newUserInteractions) {
      if (tp._1() != null) {
        try {
          User user = this.getTwitterClient().getUserFromUserId(tp._1());
          if (user != null && ((UserV2) user).getData() != null) {
            if (this.hasToAddUser(user, true, true, true)) {
              LOGGER.debug("adding " + user.getName());
              UserInteraction userInteraction = tp._2()
                                                  .withNbRecentTweets(this.getNbTweetsWithin7Days(user))
                                                  .withMedianInteractionScore(this.getMedianInteractionScore(user));
              RankedUser rankedUser = new RankedUser(user, userInteraction);
              result.add(rankedUser);
            }
          } else {
            LOGGER.error("User not found : id = " + tp._1());
          }
        } catch (Exception e) {
          LOGGER.error(e.getMessage());
        }
      } else {
        LOGGER.error("id is null");
      }
    }
    Collections.sort(result);
    return result;
  }
}
