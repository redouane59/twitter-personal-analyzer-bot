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

        checkNotFollowBack();

      //  savePotentialFollowers(false);

        //getNoFollowBackUsersAndUnfollowThem();
        /*
        new IOHelper().writeFollowed(result); */
        //  List<String> notFollowingBackUsers = twitterBot.getUsersNotFollowingBack(tweetName);
        // List<String> result = twitterBot.unfollow(notFollowingBackUsers);
        //  List<String> result = twitterBot.follow(potentialFollowersFromFollowerFollowers);
        //    System.out.println(result.size() + " results | " + result);
    }

    public static void savePotentialFollowers(boolean follow) throws IOException {
        List<User> potentialFollowersFromFollowerFollowers = twitterBot.searchPotentialFollowersFromFollowerFollowers(tweetName, 1000, follow);
        new IOHelper().write(potentialFollowersFromFollowerFollowers);
    }

    public static void getNoFollowBackUsersAndUnfollowThem() {
        List<String> result = twitterBot.getUsersNotFollowingBack(tweetName, false);
        twitterBot.unfollow(result);
    }

    public static void checkNotFollowBack() throws IOException {
        List<String[]> file = new IOHelper().readData("C:\\Users\\Perso\\Documents\\followed20194271118.csv");
        List<String> followed = new ArrayList<>();
        for(String[] s : file){
            followed.add(s[0]);
        }
        System.out.println(followed);
        Map<String, Boolean> result = twitterBot.areFriends(tweetName, followed);
        new IOHelper().writeFollowed(result);
    }

}