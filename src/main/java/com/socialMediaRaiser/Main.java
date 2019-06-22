package com.socialMediaRaiser;

import com.socialMediaRaiser.twitter.AbstractTwitterBot;
import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.RandomForestAlgoritm;
import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;

import java.io.IOException;
import java.util.*;

public class Main {

    private static AbstractTwitterBot twitterBot = new TwitterBotByInfluencers();

    public static void main(String[] args) throws Exception {
        RandomForestAlgoritm.process();
        FollowProperties.load();
       //twitterBot.checkNotFollowBack( true, true, yesterday());
       twitterBot.getPotentialFollowers(FollowProperties.USER_NAME, 100, true, true);
    }

    private static Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

}