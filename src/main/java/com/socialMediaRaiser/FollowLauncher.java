package com.socialMediaRaiser;

import com.socialMediaRaiser.twitter.AbstractTwitterBot;
import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.RandomForestAlgoritm;
import com.socialMediaRaiser.twitter.helpers.dto.getUser.AbstractUser;
import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;
import com.socialMediaRaiser.twitter.impl.TwitterBotByLastActivity;
import com.socialMediaRaiser.twitter.impl.TwitterBotByLiveKeyWords;

//import javax.servlet.ServletContextEvent;
//import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

// @todo dev followback hour reception

public class FollowLauncher {

    private static final Logger LOGGER = Logger.getLogger(FollowLauncher.class.getName());
    private static AbstractTwitterBot twitterBot;

    public static void main(String[] args) throws Exception {
        if(args.length<2){
            LOGGER.severe(()->"missing arguments, expecting 2 : ownerName[String], number of needed followers[int]. Args :");
            for(String arg : args){
                LOGGER.info(()->arg);
            }
        } else{
            String ownerName = args[0];
            int nbNeededFollowers = Integer.valueOf(args[1]);
                LOGGER.info(()->"Start working on @" + ownerName + " for "+nbNeededFollowers + " followers.");
            twitterBot = new TwitterBotByInfluencers(ownerName);
            //twitterBot = new TwitterBotByLiveKeyWords(ownerName); // @TODO in arg
            RandomForestAlgoritm.process();
            FollowProperties.load(ownerName);
            twitterBot.getPotentialFollowers(ownerName, nbNeededFollowers, true, true);
            LOGGER.info(()->"end program");
        }
    }
}
