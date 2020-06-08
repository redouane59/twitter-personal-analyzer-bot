package com.socialmediaraiser.twitterbot.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

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
  private int    nbLikesReceived = 0; // doesn't exist in the current API
  @With
  private int    nbRepliesGiven     = 0;
  @With
  private int    nbRetweetsGiven    = 0;
  @With
  private int    nbLikesGiven       = 0;

  public UserStats addRepliesReceived(int newReplies) {
    return this.withNbRepliesReceived(this.nbRepliesReceived + newReplies);
  }

  public UserStats addRetweetsReceived(int newRetweets) {
    return this.withNbRetweetsReceived(this.nbRetweetsReceived + newRetweets);
  }

  public UserStats addLikesReceived(int newLikes) {
    return this.withNbLikesReceived(this.nbLikesReceived + newLikes);
  }

  public UserStats addRepliesGiven(int newReplies) {
    return this.withNbRepliesGiven(this.nbRepliesGiven + newReplies);
  }

  public UserStats addRetweetsGiven(int newRetweets) {
    return this.withNbRetweetsGiven(this.nbRetweetsGiven + newRetweets);
  }

  public UserStats addLikesGiven(int newLikes) {
    return this.withNbLikesGiven(this.nbLikesGiven + newLikes);
  }

  public UserStats merge(UserStats other) {
    return this
        .withNbRepliesReceived(this.getNbRepliesReceived()+other.getNbRepliesReceived())
        .withNbRetweetsReceived(this.getNbRetweetsReceived()+other.getNbRetweetsReceived())
        .withNbLikesReceived(this.getNbLikesReceived()+other.getNbLikesReceived())
        .withNbRepliesGiven(this.getNbRepliesGiven()+other.getNbRepliesGiven())
        .withNbRetweetsGiven(this.getNbRetweetsGiven()+other.getNbRepliesGiven())
        .withNbLikesGiven(this.getNbLikesGiven()+other.getNbLikesGiven());
  }

  /**
   * Update stat info from a TweetInteraction object
   * @param userId the id of the user
   * @param tweetInteraction the related tweetInteraction
   * @return a new UserStats instance
   */
  public UserStats updateFromTweetInteraction(String userId, TweetInteraction tweetInteraction){
    UserStats result = this; // @todo is it durty or not ? As I currently only manage integers...
    if(tweetInteraction.getAnswererIds().contains(userId)){
      result = result.addRepliesReceived(1);
    }
    if(tweetInteraction.getRetweeterIds().contains(userId)){
      result = result.addRetweetsReceived(1);
    }
    if(tweetInteraction.getLikersIds().contains(userId)){
      result = result.addLikesReceived(1);
    }
    return result;
  }

}
