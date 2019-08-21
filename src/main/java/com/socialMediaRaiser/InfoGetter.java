package com.socialMediaRaiser;

import com.socialMediaRaiser.twitter.helpers.dto.IUser;

import java.util.List;

public interface InfoGetter {

  RelationType getRelationType(String userId1, String userId2);

  List<String> getFollowerIds(String userId);

  List<String> getFollowingIds(String userId);

  IUser getUserFromUserName(String userName);
  IUser getUserFromUserId(String userId);

}
