package com.socialMediaRaiser;

import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;
import com.socialMediaRaiser.twitter.scoring.FollowConfiguration;

import java.util.*;

public class Main {

    private static TwitterBotByInfluencers twitterBot = new TwitterBotByInfluencers();

    public static void main(String[] args) {

        FollowConfiguration followConfiguration = new FollowConfiguration();
      //  twitterBot.checkNotFollowBack(tweetName, true, true, yesterday());
        twitterBot.getPotentialFollowers(followConfiguration.getTweetName(), 50, true, true);
    }

    private static Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -2);
        return cal.getTime();
    }

}