package com.github.redouane59.twitterbot.impl;

import com.github.redouane59.twitter.dto.user.User;
import com.github.redouane59.twitter.dto.user.UserV1;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InteractiveUser extends UserV1 {

  private UserInteraction userInteraction;

  public InteractiveUser(User u) {
    this.setId(u.getId());
    this.setName(u.getName());
    this.setDescription(u.getDescription());
    this.setFollowersCount(u.getFollowersCount());
    this.setFollowersCount(u.getFollowingCount());
    this.setTweetCount(u.getTweetCount());
    this.setLocation(u.getLocation());
    this.setFollowing(u.isFollowing());
  }

  public double getFollowersRatio() {
    return (double) this.getFollowersCount() / (double) this.getFollowingCount();
  }
}
