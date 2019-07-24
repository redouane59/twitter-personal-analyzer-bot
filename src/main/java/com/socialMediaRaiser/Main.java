package com.socialMediaRaiser;

import com.socialMediaRaiser.twitter.AbstractTwitterBot;
import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.RandomForestAlgoritm;
import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;
import com.socialMediaRaiser.twitter.impl.TwitterBotByLastActivity;
import com.socialMediaRaiser.twitter.impl.TwitterBotByLiveKeyWords;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.*;

// @todo dev followback hour reception

public class Main implements ServletContextListener {

   // private static AbstractTwitterBot twitterBot = new TwitterBotByInfluencers();
    private static AbstractTwitterBot twitterBot = new TwitterBotByLastActivity();

    public static void main(String[] args) throws Exception {
        RandomForestAlgoritm.process();
        FollowProperties.load();
        System.out.println("start working for " + FollowProperties.USER_NAME);
        twitterBot.checkNotFollowBack(true, true, yesterday(), false);
        //twitterBot.getPotentialFollowers(100, true, true);
    }

    private static Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("Context destroyed code here");
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent)
    {
        System.out.println("\nSMR - start program\n");
        try {
            RandomForestAlgoritm.process();
            FollowProperties.load();
            System.out.println("SMR - start working for " + FollowProperties.USER_NAME);
            AbstractTwitterBot twitterBot = new TwitterBotByLiveKeyWords();
            twitterBot.getPotentialFollowers(400, true, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("SMR - end program");
    }
}
