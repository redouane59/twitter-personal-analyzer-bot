package com.github.redouane59.twitterbot.impl;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.user.User;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import com.github.redouane59.twitterbot.io.CsvHelper;
import com.github.redouane59.twitterbot.io.GoogleSheetHelper;
import com.github.redouane59.twitterbot.io.IOHelper;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Slf4j
public abstract class AbstractPersonalAnalyzerBot {

  private String          userName;
  private String          userId;
  private List<User>      followings;
  private List<User>      followers;
  private Set<User>       allUsers;
  private IOHelper        ioHelper;
  private TwitterClient   twitterClient;
  private ApiSearchHelper apiSearchHelper;
  private boolean         includeFollowers       = true;
  private boolean         includeFollowings      = true;
  private boolean         onyFollowBackFollowers = true;


  public AbstractPersonalAnalyzerBot(String userName, TwitterCredentials twitterCredentials) {
    this.twitterClient   = new TwitterClient(twitterCredentials);
    this.userName        = userName;
    this.userId          = this.twitterClient.getUserFromUserName(userName).getId();
    this.followings      = this.twitterClient.getFollowingUsers(userId);
    this.followers       = this.twitterClient.getFollowerUsers(userId);
    this.allUsers        = HashSet.ofAll(followings).addAll(followers);
    this.apiSearchHelper = new ApiSearchHelper(userName);
  }

  public AbstractPersonalAnalyzerBot(String userName, TwitterCredentials twitterCredentials, String archiveFileName, boolean useGoogleSheet)
  throws IOException {
    this(userName, twitterCredentials);
    if (useGoogleSheet) {
      this.ioHelper = new GoogleSheetHelper();
    } else {
      this.ioHelper = new CsvHelper();
    }
    this.apiSearchHelper = new ApiSearchHelper(userName);
  }

  /**
   * Run the main method in order to get the final user ranking
   */
  public List<RankedUser> launch(boolean includeFollowers, boolean includeFollowings, boolean onyFollowBackFollowers) {
    this.includeFollowings      = includeFollowers;
    this.includeFollowings      = includeFollowings;
    this.onyFollowBackFollowers = onyFollowBackFollowers;
    Map<String, UserStats> userStats = this.getUserStatsMap();
    if (includeFollowers) {
      for (User follower : followers) {
        if (!userStats.containsKey(follower.getId())) {
          userStats = userStats.put(follower.getId(), new UserStats());
        }
      }
    }
    if (includeFollowings) {
      for (User following : followings) {
        if (!userStats.containsKey(following.getId())) {
          userStats = userStats.put(following.getId(), new UserStats());
        }
      }
    }

    return this.mapUserStatsToUserRanking(userStats);
  }

  /**
   * Run the main method in order to get the final user ranking
   */
  public List<RankedUser> launch() {
    return this.launch(true, true, true);
  }

  /*
   * Transform a map of String,UserStats to a List of RankedUser
   */
  public List<RankedUser> mapUserStatsToUserRanking(Map<String, UserStats> userStats) {
    List<RankedUser> rankedUsers = new ArrayList<>();
    for (User rawUser : this.getAllUsers()) {
      if (this.hasToAddUser(rawUser, this.getFollowings(), this.getFollowers(), includeFollowings, includeFollowers, onyFollowBackFollowers)) {
        if (userStats.get(rawUser.getId()).isDefined()) {
          UserStats  userStat = userStats.get(rawUser.getId()).get();
          RankedUser user     = new RankedUser(rawUser, userStat);
          user.getUserStats().setNbRecentTweets(this.getApiSearchHelper().getNbTweetsWithin7Days(user));
          if (user.getFollowersCount() > 0) {
            user.getUserStats()
                .setMedianInteractionScore(this.getApiSearchHelper().getMedianInteractionScore(user) /*/ user.getFollowersCount()*/);
          }
          if (this.followings.contains(rawUser)) {
            user.setFollowing(true);
          }
          user.setUserStats(userStat);
          rankedUsers.add(user);
        }
      }
    }
    Collections.sort(rankedUsers);

    return rankedUsers;
  }

  /**
   * Generate a map of String (userId) and UserStats watching given and received interactions
   */
  public Map<String, UserStats> getUserStatsMap() {
    Map<String, UserInteraction>  givenInteractions    = this.getGivenInteractions();
    Map<String, TweetInteraction> receivedInteractions = this.getReceivedInteractions();
    return this.mapsToUserInteractions(givenInteractions, receivedInteractions);
  }

  /**
   * Generate a map of String (userId) and UserInteraction watching replies, quotes, retweets and likes given
   */
  public Map<String, UserInteraction> getGivenInteractions() {
    return this.countRetweetsGiven()
               .merge(this.countQuotesGiven(), UserInteraction::merge)
               .merge(this.countRepliesGiven(), UserInteraction::merge)
               .merge(this.countLikesGiven(), UserInteraction::merge);
  }

  public abstract Map<String, UserInteraction> countLikesGiven();

  public abstract Map<String, UserInteraction> countRepliesGiven();

  public abstract Map<String, UserInteraction> countQuotesGiven();

  public abstract Map<String, UserInteraction> countRetweetsGiven();

  /**
   * Generate a map of String (tweetId) and TweetInteraction watching replies, quotes, retweets and likes received
   */
  public Map<String, TweetInteraction> getReceivedInteractions() {
    return this.countRetweetsReceived()
               .merge(this.countQuotesReceived(), TweetInteraction::merge)
               .merge(this.countRepliesReceived(), TweetInteraction::merge)
               .merge(this.countLikesReceived(), TweetInteraction::merge);
  }

  public abstract Map<String, TweetInteraction> countLikesReceived();

  public abstract Map<String, TweetInteraction> countRepliesReceived();

  public abstract Map<String, TweetInteraction> countQuotesReceived();

  public abstract Map<String, TweetInteraction> countRetweetsReceived();

  /**
   * Generate a Map of String (userId) and Userstats merging given and receive Interactions maps
   */
  public Map<String, UserStats> mapsToUserInteractions(Map<String, UserInteraction> givenInteractions, Map<String,
      TweetInteraction> receivedInteractions) {
    LOGGER.info("merging given and received interactions...");
    Map<String, UserStats> userStatsFromGiven = HashMap.ofEntries(givenInteractions.toStream()
                                                                                   .groupBy(Tuple2::_1)
                                                                                   .map(ui -> buildTupleFromUserInteractions(ui._1(), ui._2())));

    Map<String, UserStats> usersStatsFromReceived = receivedInteractions.map(Tuple2::_2)
                                                                        .map(TweetInteraction::toUserStatsMap)
                                                                        .foldLeft(HashMap.<String, UserStats>empty(),
                                                                                  (firstMap, secondMap) -> firstMap.merge(secondMap,
                                                                                                                          UserStats::merge));
    return userStatsFromGiven.merge(usersStatsFromReceived, UserStats::merge);
  }


  private Tuple2<String, UserStats> buildTupleFromUserInteractions(String userId,
                                                                   Stream<Tuple2<String, UserInteraction>> userInteractions) {
    return Tuple.of(userId,
                    userInteractions.foldLeft(new UserStats(),
                                              (userStats, userInteraction) ->
                                                  userStats.addRepliesGiven(userInteraction._2().getAnswersIds().size())
                                                           .addRetweetsGiven(userInteraction._2().getRetweetsIds().size())
                                                           .addLikesGiven(userInteraction._2().getLikesIds().size())));
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


}
