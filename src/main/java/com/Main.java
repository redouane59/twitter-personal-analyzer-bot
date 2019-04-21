package com;

import com.twitter.TwitterBot;

import java.util.List;

public class Main {

    public static void main(String[] args) throws IllegalAccessException, InterruptedException {

        TwitterBot twitterBot = new TwitterBot();
        String tweetName = "RedTheOne";
      //  List<String> potentialFollowersFromFollowerFollowers = twitterBot.getPotentialFollowersFromRelatives(tweetName, RelationType.FOLLOWER, RelationType.FOLLOWER);
        List<String> potentialFollowers = twitterBot.getPotentialFollowersFromRetweet(tweetName, 1115859975669260288L);
    //    List<String> result = twitterBot.follow(potentialFollowersFromFollowerFollowers);
      //  List<String> result = twitterBot.getUsersNotFollowingBack(tweetName);
    //    System.out.println(result.size() + " results | " + result);

    }

}