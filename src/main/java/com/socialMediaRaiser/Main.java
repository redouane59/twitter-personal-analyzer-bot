package com.socialMediaRaiser;

import com.socialMediaRaiser.twitter.helpers.IOHelper;
import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    private static TwitterBotByInfluencers twitterBot = new TwitterBotByInfluencers();
    private static String tweetName = "RedTheOne";
    public static void main(String[] args) throws IOException {

        savePotentialFollowersFromInflluencers( 380, true);


      //  checkNotFollowBack(true);

    }

    public static void savePotentialFollowers(int count, boolean follow) throws IOException {
        List<? extends AbstractUser> result = twitterBot.getPotentialFollowers(tweetName, count, follow);
        new IOHelper().write(result);
    }

    public static void savePotentialFollowersFromInflluencers(int count, boolean follow) throws IOException {
        List<? extends AbstractUser> result = twitterBot.getPotentialFollowers(tweetName, count, follow);
        new IOHelper().write(result);
    }

    public static void checkNotFollowBack(boolean unfollow) throws IOException {
        LocalDateTime yesterday = LocalDateTime.now().minus(1, ChronoUnit.DAYS);
        String yesterdayPath = System.getProperty("user.home") + File.separatorChar
                + "Documents" + File.separatorChar
                + "followed"
                + yesterday.getYear()+yesterday.getMonthValue()+yesterday.getDayOfMonth()
                +".csv";
        List<String[]> file = new IOHelper().readData(yesterdayPath);

        List<String> followed = new ArrayList<>();
        for(String[] s : file){
            followed.add(s[0]);
        }
        Map<String, Boolean> result = twitterBot.areFriends(tweetName, followed);
        if(unfollow) {
            for (Map.Entry<String, Boolean> entry : result.entrySet()) {
                if(entry.getValue()==false){
                    twitterBot.unfollow(entry.getKey());
                }
            }
        }
        new IOHelper().writeFollowed(result);
    }

}