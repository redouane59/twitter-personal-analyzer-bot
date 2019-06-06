package com.socialMediaRaiser;

import com.socialMediaRaiser.twitter.AbstractTwitterBot;
import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.ITwitterBot;
import com.socialMediaRaiser.twitter.impl.TwitterBotByActivity;
import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;

import java.io.IOException;
import java.util.*;

public class Main {

    private static AbstractTwitterBot twitterBot = new TwitterBotByInfluencers();

    public static void main(String[] args) throws IOException {
        FollowProperties.load();
      // twitterBot.checkNotFollowBack( true, true, yesterday());
       twitterBot.getPotentialFollowers(FollowProperties.getStringProperty(FollowProperties.TWEET_NAME), 50, true, true);
    }

    private static Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -2);
        return cal.getTime();
    }

}