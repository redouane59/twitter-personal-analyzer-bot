package com.socialMediaRaiser.twitter;

import java.util.List;

public interface ITwitterBot {

    List<Long> getRetweetersId(Long tweetId);
    List<User> getRetweetersUsers(Long tweetId);

    List<User> getFollowerUsers(String userName);
    List<User> getFollowerUsers(Long userId);
    List<User> getFollowingsUserList(String userName);

}

