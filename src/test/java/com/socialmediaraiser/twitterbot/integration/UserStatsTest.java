package com.socialmediaraiser.twitterbot.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.socialmediaraiser.twitterbot.impl.UserInteraction;
import com.socialmediaraiser.twitterbot.impl.UserStats;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UserStatsTest {


  @BeforeAll
  static void init() {
  }


  @Test
  public void testMerge(){
    UserStats userStats1 = UserStats.builder().nbRepliesReceived(5)
                                    .nbRetweetsReceived(4)
                                    .nbLikesReceived(1)
                                    .nbRepliesGiven(1)
                                    .nbRetweetsGiven(1)
                                    .nbLikesGiven(1)
                                    .build();
    UserStats userStats2 = UserStats.builder().nbRepliesReceived(3)
                                    .nbRetweetsReceived(5)
                                    .nbLikesReceived(6)
                                    .nbRepliesGiven(2)
                                    .nbRetweetsGiven(2)
                                    .nbLikesGiven(2)
                                    .build();
    UserStats mergedUserStats = userStats1.merge(userStats2);
    assertEquals(8, mergedUserStats.getNbRepliesReceived());
    assertEquals(9, mergedUserStats.getNbRetweetsReceived());
    assertEquals(7, mergedUserStats.getNbLikesReceived());
    assertEquals(3, mergedUserStats.getNbRepliesGiven());
    assertEquals(3, mergedUserStats.getNbRetweetsGiven());
    assertEquals(3, mergedUserStats.getNbLikesGiven());
  }

}
