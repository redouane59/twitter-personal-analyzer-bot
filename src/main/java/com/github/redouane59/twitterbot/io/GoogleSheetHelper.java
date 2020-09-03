package com.github.redouane59.twitterbot.io;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitterbot.impl.CustomerUser;
import com.github.redouane59.twitterbot.properties.GoogleCredentials;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import lombok.Getter;

@Getter
@Slf4j
public class GoogleSheetHelper implements IOHelper{

  private Sheets               sheetsService;
  private String               sheetId;
  private String               tabName;

  public GoogleSheetHelper() throws IOException {
    URL googleCredentialsUrl = GoogleCredentials.class.getClassLoader().getResource("google-credentials.json");
    if(googleCredentialsUrl==null){
      LOGGER.error("google-credentials.json file not found in src/main/resources");
      return;
    }
    GoogleCredentials googleCredentials = TwitterClient.OBJECT_MAPPER.readValue(googleCredentialsUrl, GoogleCredentials.class);

    this.sheetId            = googleCredentials.getSheetId();
    this.tabName            = googleCredentials.getTabName();

    try {
      this.sheetsService = SheetsServiceUtil.getSheetsService();
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
    }
  }

  public void addUserLine(List<CustomerUser> users) {
    List<List<Object>> values = new ArrayList<>();
    for (CustomerUser user : users) {
      values.add(Arrays.asList(String.valueOf(user.getId()),
                               user.getName(),
                               user.getFollowersCount(),
                               user.getFollowingCount(),
                               user.getTweetCount(),
                               user.getNbTweetsWithin7Days(),
                               user.getMedianInteractionScore(), // @todo to change
                               user.getDescription().
                                   replace("\"", " ")
                                   .replace(";", " ")
                                   .replace("\n", " "),
                               Optional.ofNullable(user.getLocation()).orElse(""),
                               user.getNbRepliesReceived(),
                               user.getNbRetweetsReceived(),
                               user.getNbRepliesGiven(),
                               user.getNbRetweetsGiven(),
                               user.getNbLikesGiven(),
                               user.isFollowing()));
    }
    ValueRange body = new ValueRange()
        .setValues(values);
    try {
      Sheets.Spreadsheets.Values.Append request =
          sheetsService.spreadsheets().values().append(this.sheetId, this.tabName + "!A1", body);
      request.setValueInputOption("RAW");
      request.execute();
    } catch (Exception e) {
      LOGGER.error(e.getMessage());
      try {
        TimeUnit.SECONDS.sleep(10);
        this.addUserLine(users);
      } catch (InterruptedException e2) {
        LOGGER.error(e2.getMessage());
        Thread.currentThread().interrupt();
      }
    }

  }
}
