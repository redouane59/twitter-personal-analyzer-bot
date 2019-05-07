package com.socialMediaRaiser;

import com.socialMediaRaiser.twitter.TwitterBot;
import com.socialMediaRaiser.twitter.User;
import com.socialMediaRaiser.twitter.helpers.IOHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    private static TwitterBot twitterBot = new TwitterBot();
    private static String tweetName = "RedTheOne";
    public static void main(String[] args) throws IOException {

        savePotentialFollowersFrominflluencers( 100, true);

  //    savePotentialFollowers(300,true);

  //      checkNotFollowBack();

  //      searchNoFollowBackUsersFromFile(true);

    }

    public static void savePotentialFollowers(int count, boolean follow) throws IOException {
        List<User> result = twitterBot.searchPotentialFollowersFromRandomFollowerFollowers(tweetName, count, follow);
        new IOHelper().write(result);
    }

    public static void savePotentialFollowersFrominflluencers(int count, boolean follow) throws IOException {
        List<User> result = twitterBot.searchPotentialFollowersFromTargetedFollowerFollowers(tweetName, count, follow);
        new IOHelper().write(result);
    }

    public static void searchNoFollowBackUsers(boolean unfollow) {
        List<String> result = twitterBot.getUsersNotFollowingBack(tweetName, unfollow);
        twitterBot.unfollow(result);
    }

    public static void checkNotFollowBack() throws IOException {
        List<String[]> file = new IOHelper().readData("C:\\Users\\Perso\\Documents\\followed201954237.csv");
        List<String> followed = new ArrayList<>();
        for(String[] s : file){
            followed.add(s[0]);
        }
        System.out.println(followed);
        Map<String, Boolean> result = twitterBot.areFriends(tweetName, followed);
        new IOHelper().writeFollowed(result);
    }

    public static void searchNoFollowBackUsersFromFile(boolean unfollow) throws IOException {
        List<String[]> file = new IOHelper().readData("C:\\Users\\Perso\\Documents\\to_unfollow2.csv");
        List<String> unfollowed = new ArrayList<>();
        for(String[] s : file){
            if(unfollow && !s[0].equals("")){
                boolean result = twitterBot.unfollow(s[0]);
                if(result){
                    unfollowed.add(s[0]);
                }
            }
        }
        System.out.println(unfollowed);
    }

}