package com.socialMediaRaiser.twitter;

import com.socialMediaRaiser.twitter.helpers.dto.getUser.TweetDTO;
import com.socialMediaRaiser.twitter.helpers.dto.getUser.UserDTO;

import java.util.Date;
import java.util.List;

public interface ITwitterBot {

    List<Long> getRetweetersId(Long tweetId);
    List<UserDTO> getFollowerUsers(String userName);
    List<UserDTO> getFollowerUsers(Long userId);
    List<UserDTO> getFollowingsUsers(String userName);
    List<UserDTO> getFollowingsUsers(Long userId);
    void likeTweet(Long tweetId);
    void retweetTweet(Long tweetId);
    List<TweetDTO> searchForTweets(String query, int count, Date fromDate, Date toDate);

}

