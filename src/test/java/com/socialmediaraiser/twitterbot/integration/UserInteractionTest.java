package com.socialmediaraiser.twitterbot.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.socialmediaraiser.twitterbot.impl.UserInteraction;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UserInteractionTest {


  @BeforeAll
  static void init() {
  }


  @Test
  public void testAddAnswerId(){
    UserInteraction userInteraction = new UserInteraction();
    Set<String>     answersIds = HashSet.of("1","2","3");
    userInteraction = userInteraction.withAnswersIds(answersIds);
    assertEquals(3, userInteraction.getAnswersIds().size());
    userInteraction = userInteraction.addAnswer("4");
    assertEquals(4, userInteraction.getAnswersIds().size());
  }

}
