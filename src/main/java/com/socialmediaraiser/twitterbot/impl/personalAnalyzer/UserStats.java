package com.socialmediaraiser.twitterbot.impl.personalAnalyzer;

import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.val;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStats {

  @With
  private int    nbRepliesReceived  = 0;
  @With
  private int    nbRetweetsReceived = 0;
  @With
  private int    nbRepliesGiven     = 0;
  @With
  private int    nbRetweetsGiven    = 0;
  @With
  private int    nbLikesGiven       = 0;

  public UserStats addRepliesReceived(int newReplies) {
    return this.withNbRepliesGiven(this.nbRepliesReceived + newReplies);
  }

  public UserStats addRetweetsReceived(int newRetweets) {
    return this.withNbRepliesGiven(this.nbRetweetsReceived + newRetweets);
  }


  public UserStats addRepliesGiven(int newReplies) {
    return this.withNbRepliesGiven(this.nbRepliesGiven + newReplies);
  }

  public UserStats addRetweetsGiven(int newRetweets) {
    return this.withNbRepliesGiven(this.nbRetweetsGiven + newRetweets);
  }

  public UserStats addLikesGiven(int newLikes) {
    return this.withNbRepliesGiven(this.nbLikesGiven + newLikes);
  }




}
