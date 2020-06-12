package com.socialmediaraiser.twitterbot.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import com.socialmediaraiser.twitter.helpers.ConverterHelper;
import com.socialmediaraiser.twitterbot.impl.ApiSearchHelper;
import com.socialmediaraiser.twitterbot.impl.DataArchiveHelper;
import com.socialmediaraiser.twitterbot.impl.TweetInteraction;
import com.socialmediaraiser.twitterbot.impl.UserInteraction;
import io.vavr.collection.Map;
import java.util.Date;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class BotTest {

  private Date   iniDate = ConverterHelper.dayBeforeNow(30);
  private String userName = "RedTheOne";
  private DataArchiveHelper dataArchiveHelper;
  private ApiSearchHelper   apiSearchHelper;

  @BeforeAll
  static void init() {
  }

  @Test
  public void testCountRepliesGiven(){
    dataArchiveHelper = new DataArchiveHelper(userName, userName.toLowerCase() + "-tweet-history.json", iniDate);
    Map<String, UserInteraction> result = dataArchiveHelper.countRepliesGiven();
    assertTrue(result.get("830543389624061952").get().getAnswersIds().length()>0);
  }

  @Test
  public void testCountRecentRepliesGiven(){
    dataArchiveHelper = new DataArchiveHelper(userName, userName.toLowerCase() + "-tweet-history.json", iniDate);
    apiSearchHelper = new ApiSearchHelper(userName);
    Map<String, UserInteraction> result = apiSearchHelper.countRecentRepliesGiven(dataArchiveHelper.filterTweetsByRetweet(false).get(0).getCreatedAt());
    assertTrue(result.get("830543389624061952").get().getAnswersIds().length()>0);
  }

  @Test
  public void testCountRecentRetweetsGiven(){
    dataArchiveHelper = new DataArchiveHelper(userName, userName.toLowerCase() + "-tweet-history.json", iniDate);
    apiSearchHelper = new ApiSearchHelper(userName);
    Map<String, UserInteraction> result = apiSearchHelper.countRecentRetweetsGiven(
        dataArchiveHelper.filterTweetsByRetweet(false).get(0).getCreatedAt());
    assertTrue(result.length()>0);
  }

  @Test
  public void testCountRecentRetweetsReceived(){
    dataArchiveHelper = new DataArchiveHelper(userName, userName.toLowerCase() + "-tweet-history.json", iniDate);
    apiSearchHelper = new ApiSearchHelper(userName);
    Map<String, TweetInteraction> result = apiSearchHelper.countRecentRetweetsReceived(
        dataArchiveHelper.filterTweetsByRetweet(false).get(0).getCreatedAt());
    assertTrue(result.length()>0);
  }

  @Test
  public void testCountRetweets(){
    dataArchiveHelper = new DataArchiveHelper(userName, userName.toLowerCase() + "-tweet-history.json", iniDate);
    Map<String, TweetInteraction> result = dataArchiveHelper.countRetweetsReceived();
    assertTrue(result.length()>0);
  }

}
