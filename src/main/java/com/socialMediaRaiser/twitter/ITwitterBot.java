package com.socialMediaRaiser.twitter;

import java.util.Date;
import java.util.List;

public interface ITwitterBot {

    List<Long> getRetweetersId(Long tweetId);
    List<User> getFollowerUsers(String userName);
    List<User> getFollowerUsers(Long userId);
    List<User> getFollowingsUsers(String userName);
    List<User> getFollowingsUsers(Long userId);
    void likeTweet(Long tweetId);
    void retweetTweet(Long tweetId);
    List<Tweet> searchForTweets(String query, int count, Date fromDate, Date toDate);

}

