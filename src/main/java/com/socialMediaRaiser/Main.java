package com.socialMediaRaiser;

import com.socialMediaRaiser.twitter.User;
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
    public static void main(String[] args) throws IOException {

        savePotentialFollowersFromInflluencers( 50, false);
     // checkNotFollowBack(true);

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
        LocalDateTime yesterday = LocalDateTime.now().minus(1, ChronoUnit.DAYS);
        String yesterdayPath = System.getProperty("user.home") + File.separatorChar
                + "Documents" + File.separatorChar
                + "followed"
                + yesterday.getYear()+yesterday.getMonthValue()+yesterday.getDayOfMonth()
                +".csv";
        List<String[]> file = new IOHelper().readData(yesterdayPath);

        List<AbstractUser> followedPreviously = new ArrayList<>();
        for(String[] s : file){
            Long id = Long.valueOf(s[0]);
            String userName = s[1];
            User user = User.builder().id(id).userName(userName).build();
            followedPreviously.add(user);
        }

        User user = twitterBot.getUserFromUserName(tweetName);
        Map<AbstractUser, Boolean> result = twitterBot.areFriends(user, followedPreviously);
        new IOHelper().writeFollowedWithUser(result);
        int nbUnfollows = 0;
        if(unfollow) {
            for (Map.Entry<AbstractUser, Boolean> entry : result.entrySet()) {
                if(entry.getValue()==false){
                        System.out.print(entry.getKey().getUserName() + " ");
                        boolean ur = twitterBot.unfollow(entry.getKey().getId());
                        if(ur){
                            nbUnfollows++;
                        }
                }
            }
        }
        System.out.println(nbUnfollows + " users unfollowed / " + followedPreviously.size());
    }
}