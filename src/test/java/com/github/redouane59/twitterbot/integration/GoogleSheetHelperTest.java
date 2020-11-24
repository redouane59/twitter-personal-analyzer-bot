package com.github.redouane59.twitterbot.integration;

import com.github.redouane59.twitterbot.io.GoogleSheetHelper;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;

public class GoogleSheetHelperTest {

  private GoogleSheetHelper googleSheetHelper = new GoogleSheetHelper();

  public GoogleSheetHelperTest() throws IOException {
  }

  @BeforeAll
  static void init() {
  }

  /*

  @Test
  public void testAddNewLine() {
    CustomerUser user1 = new CustomerUser();
    user1.setId("12345");
    user1.setName("Red1");
    user1.setDescription("First description");
    user1.setFollowersCount(1);
    user1.setFollowingCount(2);
    user1.setTweetCount(3);
    UserStats userStats = UserStats.builder()
                                   .nbRepliesReceived(4)
                                   .nbRetweetsReceived(5)
                                   .nbRepliesGiven(6)
                                   .nbRetweetsGiven(7)
                                   .nbRecentTweets(1)
                                   .nbLikesGiven(8).build();
    user1.setUserStats(userStats);
    CustomerUser user2 = new CustomerUser();
    user2.setId("00001");
    user2.setName("Red2");
    user2.setDescription("2nd description");
    user2.setUserStats(userStats);
    googleSheetHelper.addUserLine(List.of(user1, user2).asJava());
  }

   */

}
