package com.github.redouane59.twitterbot.integration;

import com.github.redouane59.twitterbot.io.CsvHelper;
import com.github.redouane59.twitterbot.impl.User;
import io.vavr.collection.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CsvHelperTest {

  private CsvHelper csvHelper = new CsvHelper();

  @BeforeAll
  static void init() {
  }


  @Test
  public void testAddNewLine(){
    User       user1  = new User();
    user1.setId("12345");
    user1.setName("Red1");
    user1.setDescription("First description");
    user1.setFollowersCount(1);
    user1.setFollowingCount(2);
    user1.setTweetCount(3);
    user1.setNbRepliesReceived(4);
    user1.setNbRetweetsReceived(5);
    user1.setNbRepliesGiven(6);
    user1.setNbRetweetsGiven(7);
    user1.setNbLikesGiven(8);
    User       user2  = new User();
    user2.setId("00001");
    user2.setName("Red2");
    user2.setDescription("2nd description");
    csvHelper.addUserLine(List.of(user1, user2).asJava());
  }

}
