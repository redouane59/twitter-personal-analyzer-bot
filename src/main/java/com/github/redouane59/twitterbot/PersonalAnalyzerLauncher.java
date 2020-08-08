package com.github.redouane59.twitterbot;

import com.github.redouane59.twitterbot.impl.PersonalAnalyzerBot;
import java.io.IOException;
import java.net.URL;
import lombok.CustomLog;

@CustomLog
public class PersonalAnalyzerLauncher {

  public static void main(String[] args) throws IOException {
    if (args.length < 2) {
      LOGGER.severe(() -> "missing arguments");
    } else {
      String              userName         = args[0];
      boolean             unfollowMode     = Boolean.parseBoolean(args[1]);
      if (!unfollowMode) {
        if (args.length < 5) LOGGER.severe(() -> "missing arguments");
        boolean includeFollowers        = Boolean.parseBoolean(args[2]);
        boolean includeFollowings       = Boolean.parseBoolean(args[3]);
        boolean onlyFollowBackFollowers = Boolean.parseBoolean(args[4]);
        String tweetArchivePath         = args[5];
        boolean useGoogleSheets         = Boolean.parseBoolean(args[6]);
        PersonalAnalyzerBot bot         = new PersonalAnalyzerBot(userName, tweetArchivePath, useGoogleSheets);
        bot.launch(includeFollowers, includeFollowings, onlyFollowBackFollowers);
      } else {
        if (args.length < 3) LOGGER.severe(() -> "missing arguments");
        PersonalAnalyzerBot   bot       = new PersonalAnalyzerBot(userName);
        URL toUnfollowUrl = PersonalAnalyzerLauncher.class.getClassLoader().getResource(args[2]);
        URL whiteListUrl = PersonalAnalyzerLauncher.class.getClassLoader().getResource(args[3]);
        bot.unfollow(bot.getUsersFromJson(toUnfollowUrl), bot.getUsersFromJson(whiteListUrl));
      }
    }
  }


}
