package com.socialMediaRaiser;

import com.socialMediaRaiser.twitter.AbstractTwitterBot;
import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.RandomForestAlgoritm;
import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;
import io.vavr.control.Option;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

public class UnfollowLauncher {

    private static AbstractTwitterBot twitterBot;
    private static final Logger LOGGER = Logger.getLogger(UnfollowLauncher.class.getName());

    public static void main(String[] args) {
        if(args.length<1){
            LOGGER.severe(()->"missing arguments, expecting 1 : ownerName[String]");
        } else{
            String ownerName = args[0];
            int nbDays = Integer.valueOf(Option.of(args[1]).getOrElse("1"));
            LOGGER.info(()->"start working for " + ownerName + " for unfollows. ");
            twitterBot = new TwitterBotByInfluencers(ownerName);
            twitterBot.checkNotFollowBack(ownerName,true, true, yesterday(nbDays), false);
            LOGGER.info(()->"end program");
        }
    }

    public static Date yesterday(int nbDays) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -nbDays);
        return cal.getTime();
    }

}
