package com.socialmediaraiser.twitterbot;

import com.socialmediaraiser.core.twitter.AbstractTwitterBot;
import com.socialmediaraiser.core.twitter.FollowProperties;
import com.socialmediaraiser.core.twitter.helpers.GoogleSheetHelper;
import com.socialmediaraiser.core.twitter.helpers.dto.getuser.AbstractUser;
import com.socialmediaraiser.core.twitter.scoring.Criterion;
import com.socialmediaraiser.twitterbot.impl.TwitterBotByInfluencers;
import io.vavr.control.Option;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PersonalAnalyzerLauncher {

    private static final Logger LOGGER = Logger.getLogger(PersonalAnalyzerLauncher.class.getName());

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        if(args.length<1){
            LOGGER.severe(()->"missing arguments, expecting 1 : ownerName[String]");
        } else{
            String userName = args[0];
            TwitterBotByInfluencers bot = new TwitterBotByInfluencers(userName, false, true);
            Map<String, Integer> interractions = bot.getNbInterractions("2019-09-24", userName);
            List<AbstractUser> followers = bot.getTwitterHelper().getFollowingsUsers(bot.getTwitterHelper().getUserFromUserName(userName).getId());
            for(AbstractUser user : followers){
                user.setNbInteractions(interractions.getOrDefault(user.getId(),0));
                // add RT and/or likes
                bot.getIoHelper().addNewFollowerLineSimple(user);
                TimeUnit.MILLISECONDS.sleep(700);
                LOGGER.info("adding " + user.getUsername() + "...");
            }
            LOGGER.info("finish with success");
        }
    }


}
