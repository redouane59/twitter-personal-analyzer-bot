package com.twitter;

import java.util.List;

public interface ITwitterBot {

    List<Long> getRetweetersId(Long tweetId);
    List<User> getRetweetersUsers(Long tweetId);
    List<User> getFollowersUsers(String userName);
    List<User> getFollowingsUserList(String userName);

}

