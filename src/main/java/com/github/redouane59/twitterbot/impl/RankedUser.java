package com.github.redouane59.twitterbot.impl;

import com.github.redouane59.twitter.dto.user.User;
import java.text.DecimalFormat;
import lombok.Value;

@Value
public class RankedUser extends CustomerUser implements Comparable<RankedUser> {

  DecimalFormat df = new DecimalFormat("#0.0");

  public RankedUser(User u, UserStats userStats) {
    super(u);
    this.setUserStats(userStats);
  }

  public RankedUser(UserStats userStats) {
    this.setUserStats(userStats);
  }

  public double getFollowerRatioGrade() {
    return this.getFollowersRatio() > 5 ? 5 : this.getFollowersRatio();
  }

  public double getNbTweetsGrade() {
    if (this.getUserStats().getNbRecentTweets() >= 21 || this.getUserStats().getNbRecentTweets() <= 70) {
      return 5;
    } else if (this.getUserStats().getNbRecentTweets() < 21) {
      return 5 * this.getUserStats().getNbRecentTweets() / (double) 21;
    } else {
      return 5 * 70 / (double) this.getUserStats().getNbRecentTweets();
    }
  }

  public double getInteractionRatioGrade() {
    return Math.min(5 * this.getUserStats().getMedianInteractionScore() / (double) 20, 5);
  }

  public double getRepliesReceivedGrade() {
    return Math.min(this.getUserStats().getNbRepliesReceived(), 5);
  }

  public double getRetweetsReceivedGrade() {
    return Math.min(this.getUserStats().getNbRetweetsReceived(), 5);
  }

  public double getRepliesGivenGrade() {
    return Math.min(this.getUserStats().getNbRepliesGiven(), 5);
  }

  public double getLikesGivenGrade() {
    return Math.min(this.getUserStats().getNbLikesGiven() / (double) 2, 5);
  }

  public double getRetweetsGivenGrade() {
    return Math.min(this.getUserStats().getNbRetweetsGiven(), 5);
  }

  public double getGrade() {
    return this.getProfileGrade() + 2 * this.getInteractionGrade();
  }

  public double getInteractionGrade() {
    return (this.getGivenInteractionsGrade() + this.getReceivedInteractionsGrade()) / (double) 2;
  }

  public double getGivenInteractionsGrade() {
    return (this.getRepliesGivenGrade() + this.getLikesGivenGrade() + getRetweetsGivenGrade()) / (double) 3;
  }

  public double getReceivedInteractionsGrade() {
    return (this.getRepliesReceivedGrade() + this.getRetweetsReceivedGrade()) / (double) 2;
  }

  public double getProfileGrade() {
    return (this.getFollowerRatioGrade() + this.getNbTweetsGrade() + this.getInteractionRatioGrade()) / (double) 3;
  }

  @Override
  public String toString() {
    return "***********************\n"
           + "@"
           + this.getName()
           + " : "
           + df.format(this.getGrade())
           + "/10\n"
           + "- Profile grade : "
           + df.format(this.getProfileGrade())
           + "\n"
           + "     follower ratio: "
           + df.format(this.getFollowersRatio())
           + " ("
           + df.format(this.getFollowerRatioGrade())
           + "/5)\n"
           + "     recent tweets : "
           + this.getUserStats().getNbRecentTweets()
           + " ("
           + df.format(this.getNbTweetsGrade())
           + "/5)\n"
           + "     interactions median : "
           + this.getUserStats().getMedianInteractionScore()
           + " ("
           + df.format(this.getInteractionRatioGrade())
           + "/5)\n"
           + "- Interactions grade : "
           + df.format(this.getInteractionGrade())
           + "\n"
           + "     Replies received : "
           + this.getUserStats().getNbRepliesReceived()
           + " ("
           + df.format(this.getRepliesReceivedGrade())
           + "/5)\n"
           + "     Retweets received : "
           + this.getUserStats().getNbRetweetsReceived()
           + " ("
           + df.format(this.getRetweetsReceivedGrade())
           + "/5)\n"
           + "     Likes received : "
           + this.getUserStats().getNbLikesReceived()
           + " (0/5)\n"
           + "     Replies given : "
           + this.getUserStats().getNbRepliesGiven()
           + " ("
           + df.format(this.getRepliesGivenGrade())
           + "/5)\n"
           + "     Retweets given : "
           + this.getUserStats().getNbRetweetsGiven()
           + " ("
           + df.format(this.getRetweetsGivenGrade())
           + "/5)\n"
           + "     Likes given : "
           + this.getUserStats().getNbLikesGiven()
           + " ("
           + df.format(this.getLikesGivenGrade())
           + "/5)\n"
        ;
  }

  @Override
  public int compareTo(final RankedUser o) {
    return Double.compare(o.getGrade(), this.getGrade());
  }
}
