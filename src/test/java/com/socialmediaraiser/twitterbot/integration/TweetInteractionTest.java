package com.socialmediaraiser.twitterbot.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.socialmediaraiser.twitterbot.impl.TweetInteraction;
import com.socialmediaraiser.twitterbot.impl.UserStats;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
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

  private TweetInteraction getTweetIntereaction(){
    Set<String>                  answerersIds     = HashSet.of("1","2","3");
    Set<String>                  retweetIds       = HashSet.of("1","3","4");
    Set<String>                  likersIds        = HashSet.of("1","5","6");
    return TweetInteraction.builder()
                           .answererIds(answerersIds)
                           .retweeterIds(retweetIds)
                           .likersIds(likersIds).build();
  }

  @Test
  public void testTweetInteractionToUserStats(){

    Map<String, UserStats> result = this.getTweetIntereaction().toUserStatsMap();

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

  @Test
  public void testMapOfTweetInteractionToMapOfUserStats(){
    Map<String, TweetInteraction> receivedInteractions = HashMap.of("Tweet1",this.getTweetIntereaction());
    receivedInteractions = receivedInteractions.put("Tweet2", this.getTweetIntereaction());
    Map<String, UserStats> usersStatsFromReceived = receivedInteractions.toStream()
                                                                        .map(Tuple2::_2)
                                                                        .map(tweetInteraction -> tweetInteraction.toUserStatsMap())
                                                                        .collect(HashMap::<String, UserStats>empty, HashMap::merge, (collector, statsPerUser) -> collector.merge(statsPerUser, UserStats::merge));
    assertTrue(usersStatsFromReceived.size()>0);
    assertEquals(2,usersStatsFromReceived.get("1").get().getNbRepliesReceived());
    assertEquals(2,usersStatsFromReceived.get("2").get().getNbRepliesReceived());
    assertEquals(2,usersStatsFromReceived.get("3").get().getNbRepliesReceived());
    assertEquals(2,usersStatsFromReceived.get("1").get().getNbRetweetsReceived());
    assertEquals(2,usersStatsFromReceived.get("3").get().getNbRetweetsReceived());
    assertEquals(2,usersStatsFromReceived.get("4").get().getNbRetweetsReceived());
    assertEquals(2,usersStatsFromReceived.get("1").get().getNbLikesReceived());
    assertEquals(2,usersStatsFromReceived.get("5").get().getNbLikesReceived());
    assertEquals(2,usersStatsFromReceived.get("6").get().getNbLikesReceived());
  }

}
