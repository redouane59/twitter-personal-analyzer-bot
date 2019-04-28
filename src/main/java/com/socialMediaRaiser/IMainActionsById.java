package com.socialMediaRaiser;

import java.util.List;

public interface IMainActionsById {

    boolean follow(Long userId);
    boolean unfollow(Long userId);
    Boolean areFriends(Long userId1, Long userId2);
 //   int getNbFollowersById(Long userId);
 //   int getNbFollowingsById(Long userId);
    List<Long> getFollowerIds(Long userId);
    List<Long> getFollowingIds(Long userId);

}
