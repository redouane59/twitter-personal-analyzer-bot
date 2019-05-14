package com.socialMediaRaiser;

import com.socialMediaRaiser.twitter.User;
import com.socialMediaRaiser.twitter.helpers.GoogleSheetHelper;
import com.socialMediaRaiser.twitter.helpers.IOHelper;
import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static TwitterBotByInfluencers twitterBot = new TwitterBotByInfluencers();
    private static String tweetName = "RedTheOne";
    private static GoogleSheetHelper helper = new GoogleSheetHelper();

    public static void main(String[] args) throws IOException {

      //  twitterBot.checkNotFollowBack(tweetName, 350, true);
        twitterBot.getPotentialFollowers(tweetName, 300, true);
    }

}