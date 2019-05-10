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

public class Main {

    private static TwitterBotByInfluencers twitterBot = new TwitterBotByInfluencers();
    private static String tweetName = "RedTheOne";
    public static void main(String[] args) throws IOException {

     //   savePotentialFollowersFromInflluencers( 300, true);
     // checkNotFollowBack(true);

    }

    public static void savePotentialFollowers(int count, boolean follow) throws IOException {
        List<? extends AbstractUser> result = twitterBot.getPotentialFollowers(tweetName, count, follow);
        new IOHelper().write(result);
    }

    public static void savePotentialFollowersFromInflluencers(int count, boolean follow) throws IOException {
        List<? extends AbstractUser> result = new ArrayList<>();
        try{
            result = twitterBot.getPotentialFollowers(tweetName, count, follow);
        } catch(Exception e){
            System.out.println(e);
        }
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

        List<AbstractUser> followed = new ArrayList<>();
        for(String[] s : file){
            Long id = Long.valueOf(s[0]);
            String userName = s[1];
            User user = new User(); // @TODO user builder
            user.setId(id);
            user.setUserName(userName);
            followed.add(user);
        }

        User user = twitterBot.getUserFromUserName(tweetName);
        Map<AbstractUser, Boolean> result = twitterBot.areFriends(user, followed);
        new IOHelper().writeFollowedWithUser(result);

        if(unfollow) {
            for (Map.Entry<AbstractUser, Boolean> entry : result.entrySet()) {
                if(entry.getValue()==false){
                        System.out.print(entry.getKey().getUserName() + " ");
                        twitterBot.unfollow(entry.getKey().getId());
                }
            }
        }
        // @todo log final
    }
}