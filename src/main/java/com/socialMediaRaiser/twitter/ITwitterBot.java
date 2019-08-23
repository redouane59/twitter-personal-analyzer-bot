package com.socialMediaRaiser.twitter;

import com.socialMediaRaiser.twitter.helpers.dto.getUser.AbstractUser;

import java.util.Date;
import java.util.List;

public interface ITwitterBot {

    List<String> getRetweetersId(String tweetId);
    List<AbstractUser> getFollowerUsers(String userId);
    List<AbstractUser> getFollowingsUsers(String userId);
    void likeTweet(String tweetId);
    void retweetTweet(String tweetId);
    List<Tweet> searchForTweets(String query, int count, Date fromDate, Date toDate);

}

