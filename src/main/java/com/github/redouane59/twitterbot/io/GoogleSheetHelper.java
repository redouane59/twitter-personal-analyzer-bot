package com.github.redouane59.twitterbot.io;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitterbot.impl.RankedUser;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Getter
@Slf4j
public class GoogleSheetHelper implements IOHelper {

  private Sheets sheetsService;
  private String sheetId;
  private String tabName;

  @SneakyThrows
  public GoogleSheetHelper() {
    URL googleCredentialsUrl = GoogleCredentials.class.getClassLoader().getResource("google-credentials.json");
    if (googleCredentialsUrl == null) {
      LOGGER.error("google-credentials.json file not found in src/main/resources");
      return;
    }
    GoogleCredentials googleCredentials = TwitterClient.OBJECT_MAPPER.readValue(googleCredentialsUrl, GoogleCredentials.class);

    this.sheetId = googleCredentials.getSheetId();
    this.tabName = googleCredentials.getTabName();

    try {
      this.sheetsService = SheetsServiceUtil.getSheetsService();
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
    }
  }

  public void addUserLine(List<RankedUser> users) {
    List<List<Object>> values = new ArrayList<>();
    for (RankedUser user : users) {
      values.add(Arrays.asList(String.valueOf(user.getId()),
                               user.getName(),
                               user.getFollowersCount(),
                               user.getFollowingCount(),
                               user.getFollowersRatio(),
                               user.getTweetCount(),
                               user.getUserInteraction().getNbRecentTweets(),
                               user.getUserInteraction().getMedianInteractionScore(), // @todo to change
                               user.getDescription().
                                   replace("\"", " ")
                                   .replace(";", " ")
                                   .replace("\n", " "),
                               Optional.ofNullable(user.getLocation()).orElse(""),
                               user.isFollowing(),
                               user.getUserInteraction().getAnswersIds().size(),
                               user.getUserInteraction().getRetweetsIds().size(),
                               user.getUserInteraction().getAnsweredIds().size(),
                               user.getUserInteraction().getRetweetedIds().size(),
                               user.getUserInteraction().getLikedIds().size(),
                               user.getFollowerRatioGrade(),
                               user.getNbTweetsGrade(),
                               user.getInteractionRatioGrade(),
                               user.getRepliesReceivedGrade(),
                               user.getRetweetsReceivedGrade(),
                               //user.getLikesReceivedGrade(),
                               user.getRepliesGivenGrade(),
                               user.getRetweetsGivenGrade(),
                               user.getLikesGivenGrade(),
                               user.getProfileGrade(),
                               user.getInteractionGrade(),
                               user.getGrade()

      ));
    }
    ValueRange body = new ValueRange()
        .setValues(values);
    try {
      Sheets.Spreadsheets.Values.Append request =
          sheetsService.spreadsheets().values().append(this.sheetId, this.tabName + "!A1", body);
      request.setValueInputOption("RAW");
      request.execute();
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
      try {
        TimeUnit.SECONDS.sleep(50);
        this.addUserLine(users);
      } catch (InterruptedException e2) {
        LOGGER.error(e2.getMessage(), e);
        Thread.currentThread().interrupt();
      }
    }
  }

  public void writeAllUsers(List<RankedUser> allUsers, int nbUsersToAddPerCall) {
    List<RankedUser> usersToWrite = new ArrayList<>();
    for (RankedUser ru : allUsers) {
      usersToWrite.add(ru);
      if (usersToWrite.size() == nbUsersToAddPerCall) {
        this.addUserLine(usersToWrite);
        usersToWrite = new ArrayList<>();
        LOGGER.info("adding " + nbUsersToAddPerCall + " users ...");
        try {
          TimeUnit.MILLISECONDS.sleep(600);
        } catch (InterruptedException e) {
          LOGGER.error(e.getMessage());
        }
      }
    }
  }

  
}
