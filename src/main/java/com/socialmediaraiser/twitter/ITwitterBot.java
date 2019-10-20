package com.socialmediaraiser.twitter;

import com.socialmediaraiser.twitter.helpers.dto.getuser.AbstractUser;

import java.util.List;

public interface ITwitterBot {

    List<String> getRetweetersId(String tweetId);
    List<AbstractUser> getFollowerUsers(String userId);
    List<AbstractUser> getFollowingsUsers(String userId);
    void likeTweet(String tweetId);
    void retweetTweet(String tweetId);
    List<Tweet> searchForTweets(String query, int count, String fromDate, String toDate);

}
