package com.socialmediaraiser.twitterbot.integration;

import com.socialmediaraiser.twitterbot.GoogleSheetHelper;
import com.socialmediaraiser.twitterbot.impl.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class GoogleSheetHelperTest {

  private static String            ownerName         = "RedTheOne";
  private        GoogleSheetHelper googleSheetHelper = new GoogleSheetHelper();

  @BeforeAll
  static void init() {
  }


  @Test
  public void testAddNewLine(){
    User user = new User();
    user.setId("12345");
    user.setName("Red");
    user.setDescription("Des cript ion");
    googleSheetHelper.addNewFollowerLineSimple(user);
  }

}
