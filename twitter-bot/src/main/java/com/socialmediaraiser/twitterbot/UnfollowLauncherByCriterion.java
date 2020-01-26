package com.socialmediaraiser.twitterbot;

import com.socialmediaraiser.twitterbot.impl.TwitterBotByInfluencers;
import com.socialmediaraiser.twitterbot.scoring.Criterion;
import io.vavr.control.Option;

import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

public class UnfollowLauncherByCriterion {

    private static final Logger LOGGER = Logger.getLogger(UnfollowLauncherByCriterion.class.getName());

    public static void main(String[] args) {
        if(args.length<1){
            LOGGER.severe(()->"missing arguments, expecting 1 : ownerName[String]");
        } else{
            String ownerName = args[0];
            int nbDays = Integer.parseInt(Option.of(args[1]).getOrElse("30"));
            LOGGER.info(()->"start working for " + ownerName + " for unfollows. ");
            AbstractTwitterBot twitterBot = new TwitterBotByInfluencers(ownerName, true, true);
            twitterBot.unfollowAllUsersFromCriterion(Criterion.LAST_UPDATE, nbDays, true);
            LOGGER.info(()->"end program");
        }
    }

    public static Date yesterday(int nbDays) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -nbDays);
        return cal.getTime();
    }

}
