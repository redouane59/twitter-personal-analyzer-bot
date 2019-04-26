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


        savePotentialFollowers();
      /*  List<String[]> file = new IOHelper().readData("C:\\Users\\Perso\\Documents\\20194252234.csv");
        List<String> followed = new ArrayList<>();
        for(String[] s : file){
            followed.add(s[0]);
        }
        System.out.println(followed);
        Map<String, Boolean> result = twitterBot.areFriends(tweetName, followed);
        new IOHelper().writeFollowed(result); */

      //  getPotentialFollowersAndFollowThem();

        //getNoFollowBackUsersAndUnfollowThem();
        /*
        new IOHelper().writeFollowed(result); */
        //  List<String> notFollowingBackUsers = twitterBot.getUsersNotFollowingBack(tweetName);
        // List<String> result = twitterBot.unfollow(notFollowingBackUsers);
        //  List<String> result = twitterBot.follow(potentialFollowersFromFollowerFollowers);
        //    System.out.println(result.size() + " results | " + result);
    }

    public static void savePotentialFollowers() throws IOException {
        List<User> potentialFollowersFromFollowerFollowers = twitterBot.searchPotentialFollowersFromFollowerFollowers(tweetName, 1000, false);
        new IOHelper().write(potentialFollowersFromFollowerFollowers);
    }

    public static void getNoFollowBackUsersAndUnfollowThem() throws IllegalAccessException {
        List<String> result = twitterBot.getUsersNotFollowingBack(tweetName, false);
        twitterBot.unfollow(result);
    }

}