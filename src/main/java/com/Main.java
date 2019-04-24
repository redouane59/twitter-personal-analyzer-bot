package com;

import com.twitter.TwitterBot;
import com.twitter.User;
import com.twitter.helpers.IOHelper;

import java.io.IOException;
import java.util.List;

public class Main {

    private static TwitterBot twitterBot = new TwitterBot();
    private static String tweetName = "RedTheOne";
    public static void main(String[] args) throws IllegalAccessException, InterruptedException, IOException {

        getPotentialFollowersAndFollowThem();
        //  List<String> notFollowingBackUsers = twitterBot.getUsersNotFollowingBack(tweetName);
        // List<String> result = twitterBot.unfollow(notFollowingBackUsers);
        //  List<String> result = twitterBot.follow(potentialFollowersFromFollowerFollowers);
        //    System.out.println(result.size() + " results | " + result);
    }

    public static void getPotentialFollowersAndFollowThem() throws IOException {
        List<User> potentialFollowersFromFollowerFollowers = twitterBot.getPotentialFollowersFromFollowerFollowers(tweetName, 300);
        new IOHelper().write(potentialFollowersFromFollowerFollowers);
        twitterBot.follow(potentialFollowersFromFollowerFollowers);
    }

    public static void getNoFollowBackUsersAndUnfollowThem() throws IllegalAccessException {
        List<String> result = twitterBot.getUsersNotFollowingBack(tweetName);
        twitterBot.unfollow(result);

    }

}