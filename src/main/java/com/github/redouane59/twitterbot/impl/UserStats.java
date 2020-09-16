package com.github.redouane59.twitterbot.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.With;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserStats {

  @With
  private int nbRepliesReceived  = 0;
  @With
  private int nbRetweetsReceived = 0;
  @With
  private int nbLikesReceived    = 0; // doesn't exist in the current API
  @With
  private int nbRepliesGiven     = 0;
  @With
  private int nbRetweetsGiven    = 0;
  @With
  private int nbLikesGiven       = 0;
  @With
  private int nbRecentTweets       = 0;
  @With
  private int medianInteractionScore       = 0;

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
        .withNbRepliesReceived(this.getNbRepliesReceived() + other.getNbRepliesReceived())
        .withNbRetweetsReceived(this.getNbRetweetsReceived() + other.getNbRetweetsReceived())
        .withNbLikesReceived(this.getNbLikesReceived() + other.getNbLikesReceived())
        .withNbRepliesGiven(this.getNbRepliesGiven() + other.getNbRepliesGiven())
        .withNbRetweetsGiven(this.getNbRetweetsGiven() + other.getNbRepliesGiven())
        .withNbLikesGiven(this.getNbLikesGiven() + other.getNbLikesGiven())
        .withNbRecentTweets(this.getNbRecentTweets()+ other.getNbRecentTweets());
      //  .withMedianInteractionScore(this.getMedianInteractionScore()+other.getMedianInteractionScore());
  }

  public static UserStatsBuilder builder(UserStats origin) {
    return new UserStatsBuilder(origin);
  }

  public static UserStatsBuilder builder() {
    return new UserStatsBuilder();
  }

  /**
   * Update stat info from a TweetInteraction object
   *
   * @param userId the id of the user
   * @param tweetInteraction the related tweetInteraction
   * @return a new UserStats instance
   */
  public UserStats updateFromTweetInteraction(String userId, TweetInteraction tweetInteraction) {

    return UserStats.builder(this)
                    .nbRepliesReceived(tweetInteraction.getAnswererIds()
                                                       .find(userId::equals)
                                                       .map(user -> this.nbRepliesReceived + 1)
                                                       .getOrElse(this.nbRepliesReceived))
                    .nbRetweetsReceived(tweetInteraction.getRetweeterIds()
                                                        .find(userId::equals)
                                                        .map(user -> this.nbRetweetsReceived + 1)
                                                        .getOrElse(this.nbRetweetsReceived))
                    .nbLikesReceived(tweetInteraction.getLikersIds()
                                                     .find(userId::equals)
                                                     .map(user -> this.nbLikesReceived + 1)
                                                     .getOrElse(this.nbLikesReceived))
                    .build();

  }

  public static class UserStatsBuilder {

    private UserStatsBuilder() {
      super();
    }

    private UserStatsBuilder(UserStats origin) {
      new UserStatsBuilder().nbLikesReceived(origin.getNbLikesReceived())
                            .nbLikesGiven(origin.getNbLikesGiven())
                            .nbRetweetsGiven(origin.getNbRetweetsGiven())
                            .nbRetweetsReceived(origin.getNbRetweetsReceived())
                            .nbRepliesGiven(origin.getNbRepliesGiven())
                            .nbRepliesReceived(origin.getNbRepliesReceived());
    }

  }

}
