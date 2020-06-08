package com.socialmediaraiser.twitterbot.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.socialmediaraiser.twitterbot.impl.TweetInteraction;
import com.socialmediaraiser.twitterbot.impl.UserStats;
import io.vavr.collection.HashSet;
import io.vavr.collection.Map;
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

  @Test
  public void testTweetInteractionToUserStats(){
    Set<String>                  answerersIds     = HashSet.of("1","2","3");
    Set<String>                  retweetIds       = HashSet.of("1","3","4");
    Set<String>                  likersIds        = HashSet.of("1","5","6");
    TweetInteraction             tweetInteraction = TweetInteraction.builder()
                                                                    .answererIds(answerersIds)
                                                                    .retweeterIds(retweetIds)
                                                                    .likersIds(likersIds).build();
    Map<String, UserStats> result = tweetInteraction.toUserStatsMap();

    assertEquals(1, result.get("1").get().getNbRepliesReceived());
    assertEquals(1, result.get("1").get().getNbRetweetsReceived());
    assertEquals(1, result.get("1").get().getNbLikesReceived());
    assertEquals(1, result.get("2").get().getNbRepliesReceived());
    assertEquals(0, result.get("2").get().getNbRetweetsReceived());
    assertEquals(0, result.get("2").get().getNbLikesReceived());
    assertEquals(1, result.get("3").get().getNbRepliesReceived());
    assertEquals(1, result.get("3").get().getNbRetweetsReceived());
    assertEquals(0, result.get("3").get().getNbLikesReceived());
    assertEquals(0, result.get("4").get().getNbRepliesReceived());
    assertEquals(1, result.get("4").get().getNbRetweetsReceived());
    assertEquals(0, result.get("4").get().getNbLikesReceived());
    assertEquals(0, result.get("5").get().getNbRepliesReceived());
    assertEquals(0, result.get("5").get().getNbRetweetsReceived());
    assertEquals(1, result.get("5").get().getNbLikesReceived());
    assertEquals(0, result.get("6").get().getNbRepliesReceived());
    assertEquals(0, result.get("6").get().getNbRetweetsReceived());
    assertEquals(1, result.get("6").get().getNbLikesReceived());
  }

}
