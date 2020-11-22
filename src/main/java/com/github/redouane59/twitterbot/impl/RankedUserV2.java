package com.github.redouane59.twitterbot.impl;

import com.github.redouane59.twitter.dto.user.User;
import java.text.DecimalFormat;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RankedUserV2 extends CustomerUserV2 implements Comparable<RankedUserV2> {

  private DecimalFormat df = new DecimalFormat("#0.0");

  public RankedUserV2(User user, UserInteraction userInteraction) {
    super(user);
    this.setUserInteraction(userInteraction);
  }

  public double getFollowerRatioGrade() {
    return this.getFollowersRatio() > 5 ? 5 : this.getFollowersRatio();
  }

  public double getNbTweetsGrade() {
    if (this.getUserInteraction().getNbRecentTweets() >= 21 || this.getUserInteraction().getNbRecentTweets() <= 70) {
      return 5;
    } else if (this.getUserInteraction().getNbRecentTweets() < 21) {
      return 5 * this.getUserInteraction().getNbRecentTweets() / (double) 21;
    } else {
      return 5 * 70 / (double) this.getUserInteraction().getNbRecentTweets();
    }
  }

  public double getInteractionRatioGrade() {
    return Math.min(5 * this.getUserInteraction().getMedianInteractionScore() / (double) 20, 5);
  }

  public double getRepliesReceivedGrade() {
    return Math.min(this.getUserInteraction().getAnswersIds().size(), 5);
  }

  public double getRetweetsReceivedGrade() {
    return Math.min(this.getUserInteraction().getRetweetsIds().size(), 5);
  }

  public double getLikesReceivedGrade() {
    return Math.min(this.getUserInteraction().getLikesIds().size() / (double) 2, 5);
  }

  public double getRepliesGivenGrade() {
    return Math.min(this.getUserInteraction().getAnsweredIds().size(), 5);
  }

  public double getRetweetsGivenGrade() {
    return Math.min(this.getUserInteraction().getRetweetedIds().size(), 5);
  }

  public double getLikesGivenGrade() {
    return Math.min(this.getUserInteraction().getLikedIds().size() / (double) 2, 5);
  }

  // 1/3 for profile and 2/3 for interactions
  public double getGrade() {
    return this.getProfileGrade() + 2 * this.getInteractionGrade();
  }

  public double getInteractionGrade() {
    return (this.getGivenInteractionsGrade() + this.getReceivedInteractionsGrade()) / (double) 2;
  }

  public double getGivenInteractionsGrade() {
    return (this.getRepliesGivenGrade() + this.getLikesGivenGrade() + getRetweetsGivenGrade()) / (double) 3;
  }

  // should be divided by 3 once the likes received can be counted
  public double getReceivedInteractionsGrade() {
    return (this.getRepliesReceivedGrade() + this.getRetweetsReceivedGrade() + this.getLikesReceivedGrade()) / (double) 2;
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
           + df.format(1.35 * this.getGrade())
           + "/20\n"
           + "- Profile grade : "
           + df.format(this.getProfileGrade())
           + "\n"
           + "     follower ratio: "
           + df.format(this.getFollowersRatio())
           + " ("
           + df.format(this.getFollowerRatioGrade())
           + "/5)\n"
           + "     recent tweets : "
           + this.getUserInteraction().getNbRecentTweets()
           + " ("
           + df.format(this.getNbTweetsGrade())
           + "/5)\n"
           + "     interactions median : "
           + this.getUserInteraction().getMedianInteractionScore()
           + " ("
           + df.format(this.getInteractionRatioGrade())
           + "/5)\n"
           + "- Interactions grade : "
           + df.format(this.getInteractionGrade())
           + "\n"
           + "     Replies received : "
           + this.getUserInteraction().getAnswersIds().size()
           + " ("
           + df.format(this.getRepliesReceivedGrade())
           + "/5)\n"
           + "     Retweets received : "
           + this.getUserInteraction().getRetweetsIds().size()
           + " ("
           + df.format(this.getRetweetsReceivedGrade())
           + "/5)\n"
           + "     Likes received (KO) : "
           + this.getUserInteraction().getLikesIds().size()
           + " ("
           + df.format(this.getLikesReceivedGrade())
           + "/5)\n"
           + "     Replies given : "
           + this.getUserInteraction().getAnsweredIds().size()
           + " ("
           + df.format(this.getRepliesGivenGrade())
           + "/5)\n"
           + "     Retweets given : "
           + this.getUserInteraction().getRetweetedIds().size()
           + " ("
           + df.format(this.getRetweetsGivenGrade())
           + "/5)\n"
           + "     Likes given : "
           + this.getUserInteraction().getLikedIds().size()
           + " ("
           + df.format(this.getLikesGivenGrade())
           + "/5)\n"
        ;
  }

  @Override
  public int compareTo(final RankedUserV2 o) {
    return Double.compare(o.getGrade(), this.getGrade());
  }
}
