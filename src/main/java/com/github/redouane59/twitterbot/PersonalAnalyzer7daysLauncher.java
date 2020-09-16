package com.github.redouane59.twitterbot;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import com.github.redouane59.twitterbot.impl.RankedUser;
import com.github.redouane59.twitterbot.impl.SevenDaysPersonalAnalyzerBot;
import java.io.File;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonalAnalyzer7daysLauncher {

  public static void main(String[] args) throws IOException {

    TwitterCredentials twitterCredentials = TwitterClient.OBJECT_MAPPER
        .readValue(new File("C:/Users/Perso/Documents/GitHub/twitter-credentials.json"), TwitterCredentials.class);

    if (args.length < 1) {
      LOGGER.error("missing arguments");
    } else {
      String                       userName = args[0];
      SevenDaysPersonalAnalyzerBot bot      = new SevenDaysPersonalAnalyzerBot(userName, twitterCredentials);
      List<RankedUser>             result   = bot.launch();
    }
  }


}
