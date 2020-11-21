package com.github.redouane59.twitterbot;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import com.github.redouane59.twitterbot.impl.PersonalAnalyzerBotV2;
import com.github.redouane59.twitterbot.impl.UserStats;
import io.vavr.collection.Map;
import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonalAnalyzerBotV2Launcher {

  public static void main(String[] args) throws IOException {

    TwitterCredentials twitterCredentials = TwitterClient.OBJECT_MAPPER
        .readValue(new File("C:/Users/Perso/Documents/GitHub/twitter-credentials.json"), TwitterCredentials.class);

    String                 userName = "RedTheOne";
    PersonalAnalyzerBotV2  bot      = new PersonalAnalyzerBotV2(userName, twitterCredentials);
    Map<String, UserStats> result   = bot.getUserStatsMap();

    LOGGER.info("fin");

  }


}
