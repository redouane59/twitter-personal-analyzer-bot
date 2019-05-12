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

      //  savePotentialFollowersFromInflluencers( 200, true);
      checkNotFollowBack(false);

    }

    public static void savePotentialFollowers(int count, boolean follow) throws IOException {
        List<? extends AbstractUser> result = twitterBot.getPotentialFollowers(tweetName, count, follow);
        new IOHelper().write(result);
    }

    public static void savePotentialFollowersFromInflluencers(int count, boolean follow) throws IOException {
        List<? extends AbstractUser> result = new ArrayList<>();
        result = twitterBot.getPotentialFollowers(tweetName, count, follow);
        new IOHelper().write(result);
    }

    public static void checkNotFollowBack(boolean unfollow) throws IOException {
        List<Long> followedPreviously = helper.getPreviouslyFollowedIds(true, false);
        int listSize = followedPreviously.size();
        int nbNeededElements = 250;
        if(listSize>nbNeededElements){
            followedPreviously.subList(listSize-250,listSize);
        }

        User user = twitterBot.getUserFromUserName(tweetName);
        Map<Long, Boolean> result = twitterBot.areFriends(user.getId(), followedPreviously);

        helper.updateFollowBackInformation(result);
        if(unfollow) {
            int nbUnfollows = 0;
            for (Map.Entry<Long, Boolean> entry : result.entrySet()) {
                if(entry.getValue()==false){
                        System.out.print(entry.getKey() + " ");
                        boolean ur = twitterBot.unfollow(entry.getKey());
                        if(ur){
                            nbUnfollows++;
                        }
                }
            }
            System.out.println(nbUnfollows + " users unfollowed / " + followedPreviously.size());
        }
    }
}