package com.socialmediaraiser.twitterbot.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.socialmediaraiser.twitterbot.impl.TweetInteraction;
import com.socialmediaraiser.twitterbot.impl.UserInteraction;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TweetInteractionTest {


  @BeforeAll
  static void init() {
  }


  @Test
  public void testAddAnswererId(){
    TweetInteraction tweetInteraction = new TweetInteraction();
    Set<String>      answerersIds      = HashSet.of("1","2","3");
    tweetInteraction = tweetInteraction.withAnswererIds(answerersIds);
    assertEquals(3, tweetInteraction.getAnswererIds().size());
    tweetInteraction = tweetInteraction.addAnswerer("4");
    assertEquals(4, tweetInteraction.getAnswererIds().size());
  }

}
