package com.socialMediaRaiser;

import com.socialMediaRaiser.twitter.helpers.dto.getUser.UserDTO;

import java.util.List;

public interface InfoGetter {

  RelationType getRelationType(Long userId1, Long userId2);

  List<Long> getFollowerIds(String userName);
  List<Long> getFollowerIds(Long userId);

  List<Long> getFollowingIds(String userName);
  List<Long> getFollowingIds(Long userId);

  UserDTO getUserFromUserName(String userName);
  UserDTO getUserFromUserId(Long userId);

  //  int getNbFollowers(String userName);
  //  int getNbFollowings(String userName);
  //   int getNbFollowers(Long userId);
  //   int getNbFollowings(Long userId);
}
