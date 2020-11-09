package com.github.redouane59.twitterbot.integration;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.helpers.ConverterHelper;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import com.github.redouane59.twitterbot.impl.PersonalAnalyzerBot30days;
import com.github.redouane59.twitterbot.impl.UserInteraction;
import io.vavr.collection.Map;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
@Log
public class Bot30daysTest {

  private final LocalDateTime             iniDate  = ConverterHelper.dayBeforeNow(30);
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
  public void test1() {
    Map<String, UserInteraction> result = bot.countRepliesGiven();
    assertTrue(result.size() > 0);
  }
}
