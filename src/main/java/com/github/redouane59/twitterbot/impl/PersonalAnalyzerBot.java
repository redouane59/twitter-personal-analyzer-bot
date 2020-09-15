package com.github.redouane59.twitterbot.impl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.user.User;
import com.github.redouane59.twitter.helpers.ConverterHelper;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

@Getter
@Setter
@Slf4j
public class PersonalAnalyzerBot extends AbstractPersonalAnalyzerBot {

  private final LocalDateTime     iniDate       = ConverterHelper.dayBeforeNow(30);
  private       DataArchiveHelper dataArchiveHelper;
  LocalDateTime mostRecentTweetDate = dataArchiveHelper.filterTweetsByRetweet(false).get(0).getCreatedAt();

  public PersonalAnalyzerBot(final String userName, TwitterCredentials twitterCredentials) {
    super(userName, twitterCredentials);
  }

  public void launch(boolean includeFollowers, boolean includeFollowings, boolean onyFollowBackFollowers){
    Map<String, UserStats>  userStats = this.getUserStatsMap();

    List<CustomerUser> usersToWrite = new ArrayList<>();
    int                nbUsersToAdd = 50;
    for (User User : this.getAllUsers()) {
      if (this.hasToAddUser(User, this.getFollowings(), this.getFollowers(), includeFollowings, includeFollowers, onyFollowBackFollowers)) {
        CustomerUser user = new CustomerUser(User);
        if(userStats.get(User.getId()).isDefined()) {
          user.setNbRepliesReceived(userStats.get(User.getId()).get().getNbRepliesReceived());
          user.setNbRepliesGiven(userStats.get(User.getId()).get().getNbRepliesGiven());
          user.setNbRetweetsReceived(userStats.get(User.getId()).get().getNbRetweetsReceived());
          user.setNbLikesGiven(userStats.get(User.getId()).get().getNbLikesGiven());
          user.setNbRetweetsGiven(userStats.get(User.getId()).get().getNbRetweetsGiven());
          user.setNbTweetsWithin7Days(this.getApiSearchHelper().getNbTweetsWithin7Days(user));
          user.setMedianInteractionScore(this.getApiSearchHelper().getMedianInteractionScore(user));
        }
        usersToWrite.add(user);
        if (usersToWrite.size() == nbUsersToAdd) {
          this.getIoHelper().addUserLine(usersToWrite);
          usersToWrite = new ArrayList<>();
          LOGGER.info("adding " + nbUsersToAdd + " users ...");
          try {
            TimeUnit.MILLISECONDS.sleep(600);
          } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
          }
        }
      }
    }
    this.getIoHelper().addUserLine(usersToWrite);
    LOGGER.info("finish with success : " + this.getAllUsers().length() + " users added");
  }

  @Override
  protected Map<String, UserInteraction> countGivenLikes() {
    return this.getApiSearchHelper().countGivenLikesOnStatuses();
  }

  @Override
  protected Map<String, UserInteraction> countRepliesGiven() {
    return this.getDataArchiveHelper().countRepliesGiven()
               .merge(this.getApiSearchHelper().countRecentRepliesGiven(mostRecentTweetDate), UserInteraction::merge);
  }

  @Override
  protected Map<String, UserInteraction> countQuotesGiven() {
    return this.getDataArchiveHelper().countQuotesGiven()
        .merge(this.getApiSearchHelper().countRecentQuotesGiven(mostRecentTweetDate), UserInteraction::merge);
  }

  @Override
  protected Map<String, UserInteraction> countRetweetsGiven() {
    return this.getDataArchiveHelper().countRetweetsGiven()
               .merge(this.getApiSearchHelper().countRecentRetweetsGiven(mostRecentTweetDate),UserInteraction::merge);
  }

  @Override
  protected Map<String, TweetInteraction> countLikesReceived() {
    return HashMap.empty();
  }

  @Override
  protected Map<String, TweetInteraction> countRepliesReceived() {
    return getApiSearchHelper().countRepliesReceived(true)
                          .merge(getApiSearchHelper().countRepliesReceived(false),TweetInteraction::merge);
  }

  @Override
  protected Map<String, TweetInteraction> countQuotesReceived() {
    return this.getApiSearchHelper().countQuotesReceived(true)
                          .merge(this.getApiSearchHelper().countQuotesReceived(false), TweetInteraction::merge);
  }

  @Override
  protected Map<String, TweetInteraction> countRetweetsReceived() {
    return this.getDataArchiveHelper().countRetweetsReceived()
                            .merge(this.getApiSearchHelper().countRecentRetweetsReceived(mostRecentTweetDate), TweetInteraction::merge);
  }


  @SneakyThrows
  public void unfollow(String[] toUnfollow, String[] whiteList) {
    int nbUnfollows = 0;
    for (String unfollowName : toUnfollow) {
      unfollowName.replace(" ", "");
      if (!Arrays.asList(whiteList).contains(unfollowName)) {
        this.getTwitterClient().unfollowByName(unfollowName);
        nbUnfollows++;
        TimeUnit.MILLISECONDS.sleep(600);
        System.out.println(unfollowName + " unfollowed");
      }
    }
    LOGGER.info(nbUnfollows + " users unfollowed with success !");
  }

  public String[] getUsersFromJson(URL fileUrl){
    if(fileUrl==null){
      LOGGER.error("file not found in src/main/resources");
      return new String[]{};
    }
    try {
      return TwitterClient.OBJECT_MAPPER.readValue(fileUrl, new TypeReference<java.util.Map<String,String[]>>() {}).get("users");
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
      return new String[]{};
    }
  }
}
