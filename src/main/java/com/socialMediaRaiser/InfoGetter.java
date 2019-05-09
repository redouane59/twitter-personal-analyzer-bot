package com.socialMediaRaiser;

import java.util.List;

public interface InfoGetter {

  Boolean areFriends(String userName1, String userName2);
  Boolean areFriends(Long userId1, Long userId2);

  List<String> getFollowerNames(String userName);
  List<String> getFollowerNames(Long userId);
  List<Long> getFollowerIds(String userName);
  List<Long> getFollowerIds(Long userId);

  List<String> getFollowingNames(String userName);
  List<String> getFollowingNames(Long userId);
  List<Long> getFollowingIds(String userName);
  List<Long> getFollowingIds(Long userId);

  AbstractUser getUserFromUserName(String userName);
  AbstractUser getUserFromUserId(Long userId);

  //  int getNbFollowers(String userName);
  //  int getNbFollowings(String userName);
  //   int getNbFollowers(Long userId);
  //   int getNbFollowings(Long userId);
}
