package com.github.redouane59.twitterbot.impl;

import com.github.redouane59.twitter.dto.user.User;
import com.github.redouane59.twitter.dto.user.UserV1;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerUser extends UserV1 {

  private UserStats     userStats;
  private LocalDateTime dateOfFollow;
  private LocalDateTime dateOfFollowBack;
  private int           commonFollowers;

  public CustomerUser(User u) {
    super(u.getId(), u.getName(), null, u.getDescription(),
          u.isProtectedAccount(), u.getFollowersCount(), u.getFollowingCount(),
          u.getLang(), u.getTweetCount(), null, null, u.getLocation(), u.isFollowing());
  }

  public void setDateOfFollowNow() {
    this.dateOfFollow = LocalDateTime.now();
  }

  public double getFollowersRatio() {
    return (double) this.getFollowersCount() / (double) this.getFollowingCount();
  }
}
