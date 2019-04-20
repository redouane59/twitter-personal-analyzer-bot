package twitter;

import java.util.List;

public interface IApplication {
    boolean follow(String userName) throws IllegalAccessException;
    boolean unfollow(String userName) throws IllegalAccessException;
    int getNbFollowers(Long userId) throws IllegalAccessException;
    int getNbFollowings(Long userId) throws IllegalAccessException;
    int getNbFollowers(String userName) throws IllegalAccessException;
    int getNbFollowings(String userName) throws IllegalAccessException;
    List<Long> getRetweetersId(Long tweetId) throws IllegalAccessException;
    List<String> getRetweetersNames(Long tweetId) throws IllegalAccessException;
    List<Long> getFollowersList(Long userId) throws IllegalAccessException;
    List<String> getFollowersList(String userName) throws IllegalAccessException;
    List<User> getFollowersUserList(String userName) throws IllegalAccessException;
    List<Long> getFollowingsList(Long userId) throws IllegalAccessException;
    List<String> getFollowingsList(String userName) throws IllegalAccessException;
    List<User> getFollowingsUserList(String userName) throws IllegalAccessException;
    Boolean areFriends(Long userId1, Long userId2) throws IllegalAccessException;
    Boolean areFriends(String userName1, String userName2) throws IllegalAccessException;
}

