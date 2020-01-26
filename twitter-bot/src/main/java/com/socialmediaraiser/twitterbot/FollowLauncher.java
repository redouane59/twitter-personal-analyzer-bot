package com.socialmediaraiser.twitterbot;

import com.socialmediaraiser.twitterbot.AbstractTwitterBot;
import com.socialmediaraiser.twitterbot.FollowProperties;
import com.socialmediaraiser.twitterbot.RandomForestAlgoritm;
import com.socialmediaraiser.twitterbot.impl.TwitterBotByInfluencers;

import java.util.logging.Logger;

// @todo dev followback hour reception

public class FollowLauncher {

    private static final Logger LOGGER = Logger.getLogger(FollowLauncher.class.getName());

    public static void main(String[] args) throws Exception {
        if(args.length<2){
            LOGGER.severe(()->"missing arguments, expecting 2 : ownerName[String], number of needed followers[int]. Args :");
            for(String arg : args){
                LOGGER.info(()->arg);
            }
        } else{
            String ownerName = args[0];
            int nbNeededFollowers = Integer.parseInt(args[1]);
                LOGGER.info(()->"Start working on @" + ownerName + " for "+nbNeededFollowers + " followers.");
            AbstractTwitterBot twitterBot = new TwitterBotByInfluencers(ownerName, true, true);
            //twitterBot = new TwitterBotByLiveKeyWords(ownerName); // @TODO in arg
            RandomForestAlgoritm.process();
            FollowProperties.load(ownerName);
            twitterBot.getPotentialFollowers(ownerName, nbNeededFollowers);
            LOGGER.info(()->"end program");
        }
    }
}
