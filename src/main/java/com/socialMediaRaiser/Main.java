package com.socialMediaRaiser;

import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;

import java.util.*;

public class Main {

    private static TwitterBotByInfluencers twitterBot = new TwitterBotByInfluencers();

    public static void main(String[] args) {

        FollowProperties.init();
        //  twitterBot.checkNotFollowBack(tweetName, true, true, yesterday());
        twitterBot.getPotentialFollowers(FollowProperties.getStringProperty(FollowProperties.TWEET_NAME), 20, true, true);
    }

    private static Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -2);
        return cal.getTime();
    }

}