package com.socialmediaraiser.twitterbot;

import com.socialmediaraiser.twitterbot.impl.PersonalAnalyzerBot;
import java.io.IOException;
import lombok.CustomLog;

@CustomLog
public class PersonalAnalyzerLauncher {

  public static void main(String[] args){
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
        String[] toUnfollow = { };
        String[] whiteList = {"InesInesz", "caroletonneau", "KayiTurk_fr", "Asiorak", "Sarah_Bcf", "vivi83032363"
            , "Gabriel_Bleron", "NaelABC", "Ciudadana93", "Irfane_Clement", "OneblueTeam", "Luffy_Affranchi", "silversilvery",
                              "abdelhvmid", "souf_belkadi", "GiuseppeParavi1", "Riad7ben", "YSerena_", "Celia_Tvbti", "sombrenuance",
                              "salmaechouay", "VictorLefranc", "julien_Gres", "rk_Ngadi", "FrenchieAC", "OtmanBsh", "sof1aninho",
                              "SaraLeenHayder", "Mouradson", "OphelieEdb", "Naouffel"};
        PersonalAnalyzerBot bot              = new PersonalAnalyzerBot(userName);
        bot.unfollow(toUnfollow, whiteList);
      }
    }
  }


}
