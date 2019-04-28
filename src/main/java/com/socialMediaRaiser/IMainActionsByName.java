package com.socialMediaRaiser;

import java.util.List;

public interface IMainActionsByName {

    boolean follow(String userName);
    boolean unfollow(String userName);
    Boolean areFriends(String userName1, String userName2);
  //  int getNbFollowersByName(String userName);
  //  int getNbFollowingsByName(String userName);
    List<String> getFollowerNames(String userName);
    List<String> getFollowingNames(String userName);
    List<Long> getFollowingIdsByName(String userName);

}
