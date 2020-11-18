package com.github.redouane59.twitterbot.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import com.github.redouane59.twitterbot.impl.PersonalAnalyzerBot30days;
import com.github.redouane59.twitterbot.impl.TweetInteraction;
import com.github.redouane59.twitterbot.impl.UserInteraction;
import io.vavr.collection.Map;
import java.io.File;
import java.io.IOException;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
@Log
public class Bot30daysTest {

  private final String                    userName = "RedTheOne";
  private       PersonalAnalyzerBot30days bot;

  {
    try {
      bot = new PersonalAnalyzerBot30days(userName, TwitterClient.OBJECT_MAPPER
          .readValue(new File("../twitter-credentials.json"), TwitterCredentials.class));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testCountRetweetsGivenMonth() {
    Map<String, UserInteraction> monthlyTweets = bot.getApiSearchHelper().countRecentRetweetsGiven(false);
    assertTrue(monthlyTweets.size() > 0);
  }

  @Test
  public void testCountRetweetsReceivedMonth() {
    Map<String, TweetInteraction> monthlyTweets = bot.getApiSearchHelper().countRecentRetweetsReceived(false);
    assertTrue(monthlyTweets.size() > 0);
  }

  @Test
  public void testCountRepliesGivenMonth() {
    Map<String, UserInteraction> monthlyTweets = bot.getApiSearchHelper().countRepliesGiven(false);
    assertTrue(monthlyTweets.size() > 0);
  }

  @Test
  public void testCountRepliesReceivedMonth() {
    Map<String, TweetInteraction> monthlyTweets = bot.getApiSearchHelper().countRepliesReceived(false);
    assertTrue(monthlyTweets.size() > 0);
  }

  @Test
  public void testCountQuotesGivenMonth() {
    Map<String, UserInteraction> monthlyTweets = bot.getApiSearchHelper().countQuotesGiven(false);
    assertTrue(monthlyTweets.size() > 0);
  }

  @Test
  public void testCountQuotesReceivedMonth() {
    Map<String, TweetInteraction> monthlyTweets = bot.getApiSearchHelper().countQuotesReceived(false);
    assertTrue(monthlyTweets.size() > 0);
  }

}
