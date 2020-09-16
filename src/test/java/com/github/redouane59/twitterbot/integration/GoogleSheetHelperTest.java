package com.github.redouane59.twitterbot.integration;

import com.github.redouane59.twitterbot.impl.UserStats;
import com.github.redouane59.twitterbot.io.GoogleSheetHelper;
import com.github.redouane59.twitterbot.impl.CustomerUser;
import io.vavr.collection.List;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class GoogleSheetHelperTest {

  private GoogleSheetHelper googleSheetHelper = new GoogleSheetHelper();

  public GoogleSheetHelperTest() throws IOException {
  }

  @BeforeAll
  static void init() {
  }


  @Test
  public void testAddNewLine(){
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
    .nbLikesGiven(8).build();
    CustomerUser user2 = new CustomerUser();
    user2.setId("00001");
    user2.setName("Red2");
    user2.setDescription("2nd description");
    googleSheetHelper.addUserLine(List.of(user1, user2).asJava());
  }

}
