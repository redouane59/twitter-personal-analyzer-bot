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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonalAnalyzerBot extends AbstractAnalyzerBot {

  public PersonalAnalyzerBot(String userName, TwitterCredentials twitterCredentials) {
    super(userName, twitterCredentials);
  }

  /**
   * @return a map of userId, UserInteraction
   */
  public Map<String, UserInteraction> getUsersInteractions() {
    Map<String, UserInteraction> userInteractions = HashMap.empty();
    // retrieve all the user sent tweets through API
    List<Tweet> sentTweets = this.getTwitterClient().searchForTweetsWithin7days("from:" + this.getUserName());
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
    Map<String, UserInteraction> newUserInteractions = HashMap.ofEntries(oldUserInteractions);
    String                       authorId;
    switch (sentTweet.getTweetType()) {
      case QUOTED:
        authorId = this.getTwitterClient().getTweet(sentTweet.getInReplyToStatusId(TweetType.QUOTED)).getAuthorId();
        newUserInteractions =
            newUserInteractions.put(authorId,
                                    newUserInteractions.getOrElse(authorId, new UserInteraction()).addQuoted(conversationId));
        break;
      case RETWEETED:
        String retweetedTweetId = sentTweet.getInReplyToStatusId(TweetType.RETWEETED);
        authorId = this.getTwitterClient().getTweet(retweetedTweetId).getAuthorId();
        newUserInteractions =
            newUserInteractions.put(authorId,
                                    newUserInteractions.getOrElse(authorId, new UserInteraction())
                                                       .addRetweeted(conversationId));
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
    List<Tweet>                  receivedTweets      = this.getTwitterClient().searchForTweetsWithin7days("to:" + this.getUserName());
    for (Tweet receivedTweet : receivedTweets) {
      newUserInteractions =
          newUserInteractions.put(receivedTweet.getAuthorId(),
                                  newUserInteractions.getOrElse(receivedTweet.getAuthorId(), new UserInteraction()).addAnswer(receivedTweet.getId()));
    }
    return newUserInteractions;
  }

  /**
   * Get a score based on the average interaction ratio on the last 7 days tweets (max: 100 tweets)
   *
   * @param user the user that should be analyzed
   * @return a score
   */
  public int getInteractionScore(User user) {
    String        query          = "from:" + user.getName() + " -is:reply -is:retweet";
    LocalDateTime fromDate       = ConverterHelper.dayBeforeNow(6).truncatedTo(ChronoUnit.DAYS);
    LocalDateTime toDate         = ConverterHelper.minutesBeforeNow(120).truncatedTo(ChronoUnit.DAYS);
    List<Tweet>   lastUserTweets = this.getTwitterClient().searchForTweetsWithin7days(query, fromDate, toDate);
    int           nbFollowers    = user.getFollowersCount();
    if (lastUserTweets.size() == 0 || nbFollowers == 0) {
      return 0;
    }
    // RT : 4 pts / Quote : 3 pts / Reply : 2 pts / like : 1 pt
    int points = lastUserTweets.stream()
                               .map(tweet -> 4 * tweet.getRetweetCount()
                                             + 3 * tweet.getQuoteCount()
                                             + 2 * tweet.getReplyCount()
                                             + tweet.getLikeCount())
                               .mapToInt(Integer::intValue)
                               .sum();

    return 10000 * points / lastUserTweets.size() / nbFollowers;
  }

  public int getNbTweetsWithin7Days(User user) {
    String        query    = "from:" + user.getName() + " -is:reply -is:retweet";
    LocalDateTime fromDate = ConverterHelper.dayBeforeNow(6).truncatedTo(ChronoUnit.DAYS); // @todo to improve
    LocalDateTime toDate   = ConverterHelper.minutesBeforeNow(120).truncatedTo(ChronoUnit.DAYS);
    return this.getTwitterClient().searchForTweetsWithin7days(query, fromDate, toDate).size();
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

  public boolean hasToAddUser(User user, List<User> followings, List<User> followers,
                              boolean showFollowings, boolean showFollowers, boolean onyFollowBackUsers) {
    // case 0 : only follow back users
    if (onyFollowBackUsers && followings.contains(user) && !followers.contains(user)) {
      return false;
    }
    // case 1 : show all the people i'm following and all the users following me
    if (!showFollowers && !showFollowings) {
      return true;
    }
    // case 2 : show all the people I'm following who are following me back
    else if (showFollowers && showFollowings && onyFollowBackUsers) {
      return (/*followings.contains(user) &&*/ followers.contains(user));
    }
    // case 3 : show all the people i'm following or all the people who are following me
    else {
      return ((followings.contains(user) && showFollowings) || followers.contains(user) && showFollowers);
    }
  }

  public List<RankedUser> getRankedUsers(final Map<String, UserInteraction> userInteractions) {
    List<RankedUser> result = new ArrayList<>();
    for (Tuple2<String, UserInteraction> tp : userInteractions) {
      if (tp._1() != null) {
        User user = this.getTwitterClient().getUserFromUserId(tp._1());
        if (user != null && ((UserV2) user).getData() != null) {
          UserInteraction userInteraction = tp._2()
                                              .withNbRecentTweets(this.getNbTweetsWithin7Days(user))
                                              .withMedianInteractionScore(this.getMedianInteractionScore(user));
          RankedUser rankedUser = new RankedUser(user, userInteraction);
          result.add(rankedUser);
        } else {
          LOGGER.error("User not found : id = " + tp._1());
        }
      } else {
        LOGGER.error("id is null");
      }
    }
    Collections.sort(result);
    return result;
  }
}
