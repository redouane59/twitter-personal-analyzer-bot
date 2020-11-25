package com.github.redouane59.twitterbot.impl;

import com.github.redouane59.twitter.dto.user.User;
import java.text.DecimalFormat;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RankedUser extends InteractiveUser implements Comparable<RankedUser> {

  private DecimalFormat df = new DecimalFormat("#0.0");

  public RankedUser(User user, UserInteraction userInteraction) {
    super(user);
    this.setUserInteraction(userInteraction);
  }

  public double getFollowerRatioGrade() {
    return 3.2 * Math.atan(Math.pow(this.getFollowersRatio(), 3) / 10);
  }

  public double getNbTweetsGrade() {
    if (this.getUserInteraction().getNbRecentTweets() >= RankingConfiguration.NB_RECENT_TWEETS_MIN
        || this.getUserInteraction().getNbRecentTweets() <= RankingConfiguration.NB_RECENT_TWEETS_MAX) {
      return 5;
    } else if (this.getUserInteraction().getNbRecentTweets() < RankingConfiguration.NB_RECENT_TWEETS_MIN) {
      return 5 * this.getUserInteraction().getNbRecentTweets() / (double) RankingConfiguration.NB_RECENT_TWEETS_MIN;
    } else {
      return 5 * RankingConfiguration.NB_RECENT_TWEETS_MAX / (double) this.getUserInteraction().getNbRecentTweets();
    }
  }

  public double getInteractionRatioGrade() {
    Double relativeValue = (double) this.getUserInteraction().getMedianInteractionScore() / (double) this.getFollowersCount();
    return Math.min(1000 * relativeValue, 5);
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
    return this.getProfileGrade() * RankingConfiguration.PROFILE_COEFF
           + this.getInteractionGrade() * RankingConfiguration.INTERACTION_COEFF;
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
  public int compareTo(final RankedUser o) {
    return Double.compare(o.getGrade(), this.getGrade());
  }
}
