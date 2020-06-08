package com.socialmediaraiser.twitterbot.impl;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// @todo to remove
public class UsersStats {

  List<UserStatsDetail> values = new ArrayList<>();

  public UserStatsDetail get(String userId) {
    for (UserStatsDetail userInteraction : values) {
      if (userInteraction.getUserId().equals(userId)) {
        return userInteraction;
      }
    }
    UserStatsDetail userInteraction = new UserStatsDetail(userId);
    this.values.add(userInteraction);
    return userInteraction;
  }

  @Getter
  @Setter
  public static class UserStatsDetail {

    private String userId;
    private int    nbRepliesGiven     = 0;
    private int    nbRepliesReceived  = 0;
    private int    nbRetweetsReceived = 0;
    private int    nbRetweetsGiven    = 0;
    private int    nbLikesGiven       = 0;

    public UserStatsDetail(String userId) {
      this.userId = userId;
    }

    public void incrementNbRepliesGiven() {
      nbRepliesGiven++;
    }

    public void incrementNbRepliesReceived() {
      nbRepliesReceived++;
    }

    public void incrementNbLikesGiven() {
      nbLikesGiven++;
    }

    public void incrementNbRetweetsReceived() {
      nbRetweetsReceived++;
    }

    public void incrementNbRetweetsGiven() {
      nbRetweetsGiven++;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
      UserStatsDetail other = (UserStatsDetail) o;
      return other.getUserId().equals(this.userId);
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 17 * hash + (this.userId != null ? this.userId.hashCode() : 0);
      return hash;
    }
  }


}
