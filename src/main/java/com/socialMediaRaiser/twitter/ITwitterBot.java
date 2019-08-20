package com.socialMediaRaiser.twitter;

import java.util.Date;
import java.util.List;

public interface ITwitterBot {

    List<String> getRetweetersId(String tweetId);
    List<User> getFollowerUsers(String userId);
    List<User> getFollowingsUsers(String userId);
    void likeTweet(String tweetId);
    void retweetTweet(String tweetId);
    List<Tweet> searchForTweets(String query, int count, Date fromDate, Date toDate);

}

