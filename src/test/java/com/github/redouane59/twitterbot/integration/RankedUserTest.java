package com.github.redouane59.twitterbot.integration;

import org.junit.jupiter.api.BeforeAll;

public class RankedUserTest {

  private double errorMargin = 0.1;

  @BeforeAll
  static void init() {
  }
  /*

  private RankedUser getUser1() {
    UserStats userStats = UserStats.builder().nbRepliesReceived(1)
                                   .nbRetweetsReceived(0)
                                   .nbLikesReceived(0)
                                   .nbRepliesGiven(7)
                                   .nbRetweetsGiven(4)
                                   .nbLikesGiven(7)
                                   .nbRecentTweets(61)
                                   .medianInteractionScore(6)
                                   .build();

    RankedUser rankedUser = new RankedUser(userStats);
    rankedUser.setFollowersCount(907);
    rankedUser.setFollowingCount(201);
    return rankedUser;
  }

  @Test
  public void testFollowerRatioGrade() {
    RankedUser rankedUser = this.getUser1();
    assertTrue(Math.abs(rankedUser.getFollowerRatioGrade() - 4.5) < errorMargin);
  }

  @Test
  public void testNbTweetsGrade() {
    RankedUser rankedUser = this.getUser1();
    assertTrue(Math.abs(rankedUser.getNbTweetsGrade() - 5) < errorMargin);
  }

  @Test
  public void testRepliesReceivedGrade() {
    RankedUser rankedUser = this.getUser1();
    assertTrue(Math.abs(rankedUser.getRepliesReceivedGrade() - 1) < errorMargin);
  }

  @Test
  public void testRetweetsReceivedGrade() {
    RankedUser rankedUser = this.getUser1();
    assertTrue(Math.abs(rankedUser.getRetweetsReceivedGrade() - 0) < errorMargin);
  }

  @Test
  public void testRepliesGivenGrade() {
    RankedUser rankedUser = this.getUser1();
    assertTrue(Math.abs(rankedUser.getRepliesGivenGrade() - 5) < errorMargin);
  }

  @Test
  public void testLikesGivenGrade() {
    RankedUser rankedUser = this.getUser1();
    assertTrue(Math.abs(rankedUser.getLikesGivenGrade() - 3.5) < errorMargin);
  }

  @Test
  public void testRtGivenGrade() {
    RankedUser rankedUser = this.getUser1();
    assertTrue(Math.abs(rankedUser.getRetweetsGivenGrade() - 4) < errorMargin);
  }

  @Test
  public void testReceivedInteractionsGrade() {
    RankedUser rankedUser = this.getUser1();
    assertTrue(Math.abs(rankedUser.getReceivedInteractionsGrade() - 0.5) < errorMargin);
  }

  @Test
  public void testGivenInteractionsGrade() {
    RankedUser rankedUser = this.getUser1();
    assertTrue(Math.abs(rankedUser.getGivenInteractionsGrade() - 4.1) < errorMargin);
  }

  @Test
  public void testProfileGrade() {
    RankedUser rankedUser = this.getUser1();
    assertTrue(Math.abs(rankedUser.getProfileGrade() - 3.7) < errorMargin);
  }

  @Test
  public void testInteractionGrade() {
    RankedUser rankedUser = this.getUser1();
    assertTrue(Math.abs(rankedUser.getInteractionGrade() - 2.375) < errorMargin);
  }

  @Test
  public void testGrade() {
    RankedUser rankedUser = this.getUser1();
    assertTrue(Math.abs(rankedUser.getGrade() - 6.1) < errorMargin);
  }


   */

}
