package com.socialmediaraiser.twitterbot.integration;

    import static org.junit.jupiter.api.Assertions.assertTrue;

    import com.socialmediaraiser.twitter.helpers.ConverterHelper;
    import com.socialmediaraiser.twitterbot.GoogleSheetHelper;
    import com.socialmediaraiser.twitterbot.impl.DataArchiveHelper;
    import com.socialmediaraiser.twitterbot.impl.User;
    import com.socialmediaraiser.twitterbot.impl.UserInteraction;
    import io.vavr.collection.Map;
    import java.util.Date;
    import org.junit.jupiter.api.BeforeAll;
    import org.junit.jupiter.api.Test;

public class BotTest {

  private Date   iniDate;
  private String userName;
  private DataArchiveHelper dataArchiveHelper;

  @BeforeAll
  static void init() {
  }

  @Test
  public void testCountRepliesGiven(){
    userName = "RedTheOne";
    iniDate = ConverterHelper.dayBeforeNow(30);
    dataArchiveHelper = new DataArchiveHelper(userName, userName.toLowerCase() + "-tweet-history.json", iniDate);
    Map<String, UserInteraction> result = dataArchiveHelper.countRepliesGiven();
    assertTrue(result.get("830543389624061952").get().getAnswersIds().length()>0);
  }

}
