package com.socialmediaraiser.twitterbot;

import com.socialmediaraiser.twitterbot.impl.followingBot.TwitterBotByInfluencers;
import lombok.CustomLog;

// @todo dev followback hour reception

@CustomLog
public class FollowLauncher {

  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      LOGGER.severe(() -> "missing arguments, expecting 2 : ownerName[String], number of needed followers[int]. Args :");
      for (String arg : args) {
        LOGGER.info(() -> arg);
      }
    } else {
      String ownerName         = args[0];
      int    nbNeededFollowers = Integer.parseInt(args[1]);
      LOGGER.info(() -> "Start working on @" + ownerName + " for " + nbNeededFollowers + " followers.");
      AbstractTwitterFollowBot twitterBot = new TwitterBotByInfluencers(ownerName, true, true);
      //twitterBot = new TwitterBotByLiveKeyWords(ownerName); // @TODO in arg
      RandomForestAlgoritm.process();
      FollowProperties.load(ownerName);
      twitterBot.getPotentialFollowers(ownerName, nbNeededFollowers);
      LOGGER.info(() -> "end program");
    }
  }
}
