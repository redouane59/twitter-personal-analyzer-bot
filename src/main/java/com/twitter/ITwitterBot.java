package com.twitter;

import java.util.List;

public interface ITwitterBot {

    List<Long> getRetweetersId(Long tweetId) throws IllegalAccessException;
    List<TwitterUser> getRetweetersUsers(Long tweetId) throws IllegalAccessException;
    List<TwitterUser> getFollowersUsers(String userName) throws IllegalAccessException;
    List<TwitterUser> getFollowingsUserList(String userName) throws IllegalAccessException;

}

