package com.socialMediaRaiser;

import com.socialMediaRaiser.twitter.AbstractTwitterBot;
import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.RandomForestAlgoritm;
import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;
import com.socialMediaRaiser.twitter.impl.TwitterBotByLiveKeyWords;

import java.io.IOException;
import java.util.*;

// @todo dev followback hour reception

public class Main {

    //private static AbstractTwitterBot twitterBot = new TwitterBotByInfluencers();
    private static AbstractTwitterBot twitterBot = new TwitterBotByLiveKeyWords();

    public static void main(String[] args) throws Exception {
        RandomForestAlgoritm.process();
        FollowProperties.load();
        System.out.println("start working for " + FollowProperties.USER_NAME);
     //   twitterBot.checkNotFollowBack(true, true, yesterday());
        twitterBot.getPotentialFollowers(10, true, true);
    }

    private static Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

}