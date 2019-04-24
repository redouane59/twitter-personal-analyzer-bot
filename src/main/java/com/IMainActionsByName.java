package com;

import com.twitter.User;

import java.util.List;

public interface IMainActionsByName {

    boolean follow(String userName) throws IllegalAccessException;
    boolean unfollow(String userName) throws IllegalAccessException;
    Boolean areFriends(String userName1, String userName2) throws IllegalAccessException;
    int getNbFollowers(String userName) throws IllegalAccessException;
    int getNbFollowings(String userName) throws IllegalAccessException;
    List<String> getFollowerNames(String userName) throws IllegalAccessException;
    List<String> getFollowingNames(String userName) throws IllegalAccessException;

}
