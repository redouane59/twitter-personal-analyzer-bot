package com.github.redouane59.twitterbot.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.redouane59.twitter.helpers.ConverterHelper;
import com.github.redouane59.twitterbot.impl.ApiSearchHelper;
import com.github.redouane59.twitterbot.impl.DataArchiveHelper;
import com.github.redouane59.twitterbot.impl.TweetInteraction;
import com.github.redouane59.twitterbot.impl.UserInteraction;
import io.vavr.collection.Map;
import java.time.LocalDateTime;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
@Log
public class BotTest {

  private final LocalDateTime     iniDate  = ConverterHelper.dayBeforeNow(30);
  private final String            userName = "RedTheOne";
  private       DataArchiveHelper dataArchiveHelper;
  private       ApiSearchHelper   apiSearchHelper;

  @Test
  public void testCountRepliesGiven() {
    dataArchiveHelper = new DataArchiveHelper(userName, userName.toLowerCase() + "-tweet-history.json", iniDate);
    Map<String, UserInteraction> result = dataArchiveHelper.countRepliesGiven();
    assertTrue(result.get("830543389624061952").get().getAnswersIds().length() > 0);
  }

  @Test
  public void testQuotesGiven() {
    dataArchiveHelper = new DataArchiveHelper(userName, userName.toLowerCase() + "-tweet-history.json", iniDate);
    Map<String, UserInteraction> result = dataArchiveHelper.countQuotesGiven();
    assertTrue(result.length() > 0);
  }

  @Test
  public void testCountRecentRepliesGiven() {
    dataArchiveHelper = new DataArchiveHelper(userName, userName.toLowerCase() + "-tweet-history.json", iniDate);
    apiSearchHelper   = new ApiSearchHelper(userName);
    Map<String, UserInteraction>
        result =
        apiSearchHelper.countRecentRepliesGiven(dataArchiveHelper.filterTweetsByRetweet(false).get(0).getCreatedAt());
    assertTrue(result.get("830543389624061952").get().getAnswersIds().length() > 0);
  }

  @Test
  public void testCountRecentRetweetsGiven() {
    dataArchiveHelper = new DataArchiveHelper(userName, userName.toLowerCase() + "-tweet-history.json", iniDate);
    apiSearchHelper   = new ApiSearchHelper(userName);
    Map<String, UserInteraction> result = apiSearchHelper.countRecentRetweetsGiven(
        dataArchiveHelper.filterTweetsByRetweet(false).get(0).getCreatedAt());
    assertTrue(result.length() > 0);
  }

  @Test
  public void testCountRecentRetweetsReceived() {
    dataArchiveHelper = new DataArchiveHelper(userName, userName.toLowerCase() + "-tweet-history.json", iniDate);
    apiSearchHelper   = new ApiSearchHelper(userName);
    Map<String, TweetInteraction> result = apiSearchHelper.countRecentRetweetsReceived(
        dataArchiveHelper.filterTweetsByRetweet(false).get(0).getCreatedAt());
    assertTrue(result.length() > 0);
  }

  @Test
  public void testCountRetweets() {
    dataArchiveHelper = new DataArchiveHelper(userName, userName.toLowerCase() + "-tweet-history.json", iniDate);
    Map<String, TweetInteraction> result = dataArchiveHelper.countRetweetsReceived();
    assertTrue(result.length() > 0);
  }

  @Test
  public void testRecentQuotesGiven() {
    apiSearchHelper   = new ApiSearchHelper(userName);
    dataArchiveHelper = new DataArchiveHelper(userName, userName.toLowerCase() + "-tweet-history.json", iniDate);
    Map<String, UserInteraction>
        result =
        apiSearchHelper.countRecentQuotesGiven(dataArchiveHelper.filterTweetsByRetweet(false).get(0).getCreatedAt());
    assertTrue(result.size() > 0);
  }

  @Test
  public void testRecentRepliesReced() {
    apiSearchHelper   = new ApiSearchHelper(userName);
    dataArchiveHelper = new DataArchiveHelper(userName, userName.toLowerCase() + "-tweet-history.json", iniDate);
    Map<String, TweetInteraction> result = apiSearchHelper.countQuotesReceived(true);
    assertTrue(result.size() > 0);
  }

  @Test
  public void testInteractionRate() {
    apiSearchHelper = new ApiSearchHelper(userName);
    double result = apiSearchHelper.getMedianInteractionScore(apiSearchHelper.getTwitterClient().getUserFromUserName("Billydrakkk"));
    LOGGER.info("Billydrakkk" + " score = " + result);
    assertTrue(result > 0);
  }

  @Test
  public void testScoreMedian() {
    apiSearchHelper = new ApiSearchHelper(userName);
    double result = apiSearchHelper.getMedianInteractionScore(apiSearchHelper.getTwitterClient().getUserFromUserName(userName));
    LOGGER.info(userName + " median = " + result);
    assertTrue(result > 0);
  }

  @Test
  public void testGetNbTweetsWithin7Days() {
    apiSearchHelper = new ApiSearchHelper(userName);
    int result = apiSearchHelper.getNbTweetsWithin7Days(apiSearchHelper.getTwitterClient().getUserFromUserName(userName));
    LOGGER.info(userName + " nb tweets 7 days = " + result);
    assertTrue(result > 0);
  }
}
