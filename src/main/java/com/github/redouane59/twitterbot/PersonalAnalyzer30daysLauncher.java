package com.github.redouane59.twitterbot;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import com.github.redouane59.twitterbot.impl.CustomerUser;
import com.github.redouane59.twitterbot.impl.PersonalAnalyzerBot30days;
import com.github.redouane59.twitterbot.impl.RankedUser;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonalAnalyzer30daysLauncher {

  public static void main(String[] args) throws IOException {

    TwitterCredentials twitterCredentials = TwitterClient.OBJECT_MAPPER
        .readValue(new File("C:/Users/Perso/Documents/GitHub/twitter-credentials.json"), TwitterCredentials.class);

    String                    userName = "RedTheOne";
    PersonalAnalyzerBot30days bot      = new PersonalAnalyzerBot30days(userName, twitterCredentials);
    List<RankedUser>          result   = bot.launch();

    List<CustomerUser> usersToWrite = new ArrayList<>();
    int                nbUsersToAdd = 50;

    for (RankedUser ru : result) {
      usersToWrite.add(ru);
      if (usersToWrite.size() == nbUsersToAdd) {
        bot.getIoHelper().addUserLine(usersToWrite);
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

}
