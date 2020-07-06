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

        URL toUnfollowUrl = PersonalAnalyzerLauncher.class.getClassLoader().getResource("to-unfollow.json");
        if(toUnfollowUrl==null){
          LOGGER.severe("to-unfollow.json file not found in src/main/resources");
          return;
        }
        String[] toUnfollow = TwitterClient.OBJECT_MAPPER.readValue(toUnfollowUrl, new TypeReference<Map<String,String[]>>() {}).get("users");

        URL whiteListUrl = PersonalAnalyzerLauncher.class.getClassLoader().getResource("white-list.json");
        if(whiteListUrl==null){
          LOGGER.severe("white-list.json file not found in src/main/resources");
        }
        String[] whiteList = TwitterClient.OBJECT_MAPPER.readValue(whiteListUrl, new TypeReference<Map<String,String[]>>() {}).get("users");

        PersonalAnalyzerBot   bot       = new PersonalAnalyzerBot(userName);
        bot.unfollow(toUnfollow, whiteList);
      }
    }
  }


}
