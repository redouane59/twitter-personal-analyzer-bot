package com.socialmediaraiser.twitterbot.impl;
import com.socialmediaraiser.twitter.TwitterClient;
import com.socialmediaraiser.twitter.dto.user.IUser;
import com.socialmediaraiser.twitter.helpers.ConverterHelper;
import com.socialmediaraiser.twitterbot.GoogleSheetHelper;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.CustomLog;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;


@Getter
@Setter
@CustomLog
public class PersonalAnalyzerBot {

  private       String            userName;
  private       GoogleSheetHelper  ioHelper;
  private       TwitterClient     twitterClient = new TwitterClient();
  private final Date              iniDate       = ConverterHelper.dayBeforeNow(30);
  private       DataArchiveHelper dataArchiveHelper;
  private       ApiSearchHelper   apiSearchHelper;

  public PersonalAnalyzerBot(String userName, String archiveFileName) {
    this.userName          = userName;
    this.ioHelper          = new GoogleSheetHelper();
    this.dataArchiveHelper = new DataArchiveHelper(userName, archiveFileName, iniDate);
    this.apiSearchHelper   = new ApiSearchHelper(userName);
  }

  public void launch(boolean includeFollowers, boolean includeFollowings, boolean onyFollowBackFollowers)
  throws InterruptedException {
    String      userId       = this.twitterClient.getUserFromUserName(userName).getId();
    Map<String, UserStats>  interactions = this.getUserStatsMap();
    List<IUser> followings   = this.twitterClient.getFollowingUsers(userId);
    List<IUser> followers = this.twitterClient.getFollowerUsers(userId);
    Set<IUser>  allUsers  = HashSet.ofAll(followings).addAll(followers); // @todo duplicate

    List<User> usersToWrite = new ArrayList<>();
    int        nbUsersToAdd = 50;
    for (IUser iUser : allUsers) {
      if (hasToAddUser(iUser, followings, followers, includeFollowings, includeFollowers, onyFollowBackFollowers)) {
        User user = new User(iUser);
        user.setNbRepliesReceived(interactions.get(iUser.getId()).get().getNbRepliesReceived());
        user.setNbRepliesGiven(interactions.get(iUser.getId()).get().getNbRepliesGiven());
        user.setNbRetweetsReceived(interactions.get(iUser.getId()).get().getNbRetweetsReceived());
        user.setNbLikesGiven(interactions.get(iUser.getId()).get().getNbLikesGiven());
        user.setNbRetweetsGiven(interactions.get(iUser.getId()).get().getNbRetweetsGiven());
        usersToWrite.add(user);
        if (usersToWrite.size() == nbUsersToAdd) {
          this.ioHelper.addNewFollowerLineSimple(usersToWrite);
          usersToWrite = new ArrayList<>();
          LOGGER.info("adding " + nbUsersToAdd + " users ...");
          TimeUnit.MILLISECONDS.sleep(500);
        }
      }
    }
    this.ioHelper.addNewFollowerLineSimple(usersToWrite);
    LOGGER.info("finish with success");
  }

  private boolean hasToAddUser(IUser user, List<IUser> followings, List<IUser> followers,
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
      return (followings.contains(user) && followers.contains(user));
    }
    // case 3 : show all the people i'm following or all the people who are following me
    else {
      return ((followings.contains(user) && showFollowings) || followers.contains(user) && showFollowers);
    }
  }

  private Map<String, UserStats> getUserStatsMap() {
    Map<String, UserInteraction> givenInteractions = this.getGivenInteractions();
    Map<String, TweetInteraction> receivedInteractions = this.getReceivedInteractions();
    return this.mapsToUserInteractions(givenInteractions,receivedInteractions);
  }

  private Map<String, UserStats> mapsToUserInteractions(Map<String, UserInteraction> givenInteractions, Map<String,
      TweetInteraction> receivedInteractions){
    LOGGER.info("mapsToUserIntereactions...");

    Map<String, UserStats> userStatsFromGiven =
        HashMap.ofEntries(givenInteractions.toStream()
                                           .groupBy(Tuple2::_1)
                                           .map(ui -> buildTurpleFromUserInteractions(ui._1(), ui._2())));

    Map<String, UserStats> usersStatsFromReceived = receivedInteractions.toStream()
                                                     .map(Tuple2::_2)
                                                     .map(tweetInteraction -> tweetInteraction.toUserStatsMap())
                                                     .collect(HashMap::<String, UserStats>empty, HashMap::merge, (collector, statsPerUser) -> collector.merge(statsPerUser, UserStats::merge));
    return userStatsFromGiven.merge(usersStatsFromReceived, UserStats::merge); // @todo KO
  }

  private Tuple2<String, UserStats> buildTurpleFromUserInteractions(String userId,
                                                                    Stream<Tuple2<String, UserInteraction>> userInteractions){
    return Tuple.of(userId,
                    userInteractions.foldLeft(new UserStats(),
                                              (userStats, userInteraction) ->
                                                  userStats.addRepliesGiven(userInteraction._2().getAnswersIds().size())
                                                           .addRetweetsGiven(userInteraction._2().getRetweetsIds().size())
                                                           .addLikesGiven(userInteraction._2().getLikesIds().size())));
  }


  private Map<String, TweetInteraction> getReceivedInteractions() {
    Map<String, TweetInteraction> map1 = dataArchiveHelper.countRetweetsReceived();
    Map<String, TweetInteraction> map2 = apiSearchHelper.countRepliesReceived(true);
    Map<String, TweetInteraction> map3 = apiSearchHelper.countRepliesReceived(false);
    return map1.merge(map2,TweetInteraction::merge).merge(map3,TweetInteraction::merge);
  }

  private Map<String, UserInteraction> getGivenInteractions(){
    //@todo  apiSearchHelper.countRecentRepliesGiven(userInteractions, dataArchiveHelper.filterTweetsByRetweet(false).get(0).getCreatedAt()); // @todo test 2nd arg

    return dataArchiveHelper.countRetweetsGiven()
                            .merge(dataArchiveHelper.countRepliesGiven(), UserInteraction::merge)
                            .merge(apiSearchHelper.countGivenLikesOnStatuses(),UserInteraction::merge);
  }

  @SneakyThrows
  public void unfollow(String[] toUnfollow, String[] whiteList) {

    int nbUnfollows = 0;
    for (String unfollowName : toUnfollow) {
      unfollowName.replace(" ", "");
      if (!Arrays.asList(whiteList).contains(unfollowName)) {
        this.getTwitterClient().unfollowByName(unfollowName);
        nbUnfollows++;
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.println(unfollowName + " unfollowed");
      }
    }
    LOGGER.info(nbUnfollows + " users unfollowed with success !");
  }
}
