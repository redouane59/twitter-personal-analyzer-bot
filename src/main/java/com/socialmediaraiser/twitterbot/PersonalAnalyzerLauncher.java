package com.socialmediaraiser.twitterbot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.socialmediaraiser.twitter.TwitterClient;
import com.socialmediaraiser.twitterbot.impl.PersonalAnalyzerBot;
import com.socialmediaraiser.twitterbot.properties.GoogleCredentials;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
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
        PersonalAnalyzerBot bot         = new PersonalAnalyzerBot(userName, tweetArchivePath);
        bot.launch(includeFollowers, includeFollowings, onlyFollowBackFollowers);
      } else {
        if (args.length < 3) LOGGER.severe(() -> "missing arguments");
        String toUnfollowPath         = args[2];
        String whiteListPath          = args[3];
        PersonalAnalyzerBot   bot       = new PersonalAnalyzerBot(userName);
        URL toUnfollowUrl = PersonalAnalyzerLauncher.class.getClassLoader().getResource(toUnfollowPath);
        URL whiteListUrl = PersonalAnalyzerLauncher.class.getClassLoader().getResource(whiteListPath);
        bot.unfollow(bot.getUsersFromJson(toUnfollowUrl), bot.getUsersFromJson(whiteListUrl));
      }
    }
  }


}
