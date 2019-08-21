package com.socialMediaRaiser.twitter;

import com.socialMediaRaiser.twitter.helpers.dto.IUser;

import java.util.Date;
import java.util.List;

public interface ITwitterBot {

    List<String> getRetweetersId(String tweetId);
    List<IUser> getFollowerUsers(String userId);
    List<IUser> getFollowingsUsers(String userId);
    void likeTweet(String tweetId);
    void retweetTweet(String tweetId);
    List<Tweet> searchForTweets(String query, int count, Date fromDate, Date toDate);

}

