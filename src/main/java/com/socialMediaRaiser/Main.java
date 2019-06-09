package com.socialMediaRaiser;

import com.socialMediaRaiser.twitter.AbstractTwitterBot;
import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;

import java.io.IOException;
import java.util.*;

public class Main {

    private static AbstractTwitterBot twitterBot = new TwitterBotByInfluencers();

    public static void main(String[] args) {
        FollowProperties.load();
      // twitterBot.checkNotFollowBack( true, true, yesterday());
       twitterBot.getPotentialFollowers(FollowProperties.USER_NAME, 350, true, true);
    }

    private static Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -2);
        return cal.getTime();
    }

}