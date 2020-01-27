package com.socialmediaraiser.twitterbot.impl;

import com.socialmediaraiser.core.twitter.helpers.dto.ConverterHelper;
import com.socialmediaraiser.core.twitter.helpers.dto.getuser.AbstractUser;
import com.socialmediaraiser.twitterbot.PersonalAnalyzerLauncher;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PersonalAnalyzerBot {

    private static final Logger LOGGER = Logger.getLogger(PersonalAnalyzerLauncher.class.getName());

    public void launch(String userName) throws IOException, ParseException, InterruptedException {
        TwitterBotByInfluencers bot = new TwitterBotByInfluencers(userName, false, true);
        Map<String, Integer> interractions = bot.getNbInterractions(ConverterHelper.getDateFromString("20200101"), userName);
        List<AbstractUser> followers = bot.getTwitterClient().getFollowingsUsers(bot.getTwitterClient().getUserFromUserName(userName).getId());
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
