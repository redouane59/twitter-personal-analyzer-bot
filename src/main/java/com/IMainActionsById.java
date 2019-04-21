package com;

import java.util.List;

public interface IMainActionsById {

    boolean follow(Long userId) throws IllegalAccessException;
    boolean unfollow(Long userId) throws IllegalAccessException;
    Boolean areFriends(Long userId1, Long userId2) throws IllegalAccessException;
    int getNbFollowersById(Long userId) throws IllegalAccessException;
    int getNbFollowingsById(Long userId) throws IllegalAccessException;
    List<Long> getFollowersIds(Long userId) throws IllegalAccessException;
    List<Long> getFollowingsIds(Long userId) throws IllegalAccessException;


}
