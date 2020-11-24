package com.github.redouane59.twitterbot;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import com.github.redouane59.twitterbot.impl.PersonalAnalyzerBot;
import com.github.redouane59.twitterbot.impl.RankedUser;
import com.github.redouane59.twitterbot.impl.UserInteraction;
import com.github.redouane59.twitterbot.io.GoogleSheetHelper;
import io.vavr.collection.Map;
import java.io.File;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PersonalBotLauncher {

  public static void main(String[] args) throws IOException {

    TwitterCredentials twitterCredentials = TwitterClient.OBJECT_MAPPER
        .readValue(new File("C:/Users/Perso/Documents/GitHub/twitter-credentials.json"), TwitterCredentials.class);

    String                       userName         = "RedTheOne";
    PersonalAnalyzerBot          bot              = new PersonalAnalyzerBot(userName, twitterCredentials);
    Map<String, UserInteraction> userInteractions = bot.getUsersInteractions();
    List<RankedUser>             rankedUsers      = bot.getRankedUsers(userInteractions);
    GoogleSheetHelper            ioHelper         = new GoogleSheetHelper();
    ioHelper.writeAllUsers(rankedUsers, 50);

  }


}
