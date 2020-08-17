package com.github.redouane59.twitterbot.impl;

import com.github.redouane59.twitter.dto.user.IUser;
import com.github.redouane59.twitter.dto.user.UserDTOv1;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class User extends UserDTOv1 {

  private int  nbRetweetsReceived;
  private int  nbRepliesGiven;
  private int  nbRepliesReceived;
  private int  nbLikesGiven;
  private int  nbRetweetsGiven;
  private LocalDateTime dateOfFollow;
  private LocalDateTime dateOfFollowBack;
  private int  commonFollowers;
  private int nbTweetsWithin7Days;
  private int medianInteractionScore;

  public User(IUser u) {
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
