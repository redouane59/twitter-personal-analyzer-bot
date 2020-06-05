package com.socialmediaraiser.twitterbot.impl.personalAnalyzer;

import com.socialmediaraiser.twitter.dto.tweet.ITweet;
import com.socialmediaraiser.twitter.helpers.ConverterHelper;
import com.socialmediaraiser.twitterbot.impl.personalAnalyzer.UserInteractions.UserInteractionX;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Map;
import io.vavr.collection.Stream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.CustomLog;
import org.apache.commons.lang3.time.DateUtils;

@CustomLog
public class ApiSearchHelper extends AbstractSearchHelper {

  public ApiSearchHelper(String userName) {
    super(userName);
  }

  // @todo mix adding query parameter
  // @todo to the same for given retweets
  // countRepliesFromUserFromUserRecentSearch
  public void countRecentRepliesGiven(UserInteractions userInteractions, Date mostRecentArchiveTweetDate) {
    LOGGER.info("\nCounting replies from user (API)...");
    long delta = (System.currentTimeMillis() - mostRecentArchiveTweetDate.getTime()) / (1000 * 60 * 60 * 24);
    if (delta < 1) {
      return;
    }
    List<ITweet> tweetWithReplies;
    String       query;
    Date         toDate;
    Date         fromDate;

    query = "(from:" + this.getUserName() + " has:mentions)";
    if (delta > 7) {
      toDate   = DateUtils.truncate(ConverterHelper.minutesBeforeNow(120), Calendar.HOUR);
      fromDate = DateUtils.ceiling(DateUtils.addDays(toDate, -7), Calendar.HOUR);
    } else {
      toDate   = DateUtils.truncate(ConverterHelper.minutesBeforeNow(120), Calendar.HOUR);
      fromDate = mostRecentArchiveTweetDate;
    }
    tweetWithReplies = this.getTwitterClient().searchForTweetsWithin7days(query, fromDate, toDate);

    Set<String> answeredByUserTweets = new HashSet<>();

    for (ITweet tweet : tweetWithReplies) {
      if (this.isUserInList(tweet.getInReplyToUserId())) {
        ITweet initialTweet = this.getTwitterClient().getInitialTweet(tweet, true);
        // we only count the answer to a tweet once
        if (!this.getUserId().equals(initialTweet.getAuthorId()) && !answeredByUserTweets.contains(initialTweet.getId())) {
          System.out.print(".");
          UserInteractionX userInteraction = userInteractions.get(tweet.getAuthorId());
          userInteraction.incrementNbRepliesGiven();
          answeredByUserTweets.add(initialTweet.getId());
        }
      }
    }
    LOGGER.info("\n" + tweetWithReplies.size() + " replies given found, " + answeredByUserTweets.size() + " saved");
  }

  /**
   * Count the replies received by the user from others
   * @param currentWeek if true, from D-7 until D, otherwise from D-30 until D-7
   * @return a map with tweet id as key and TweetInteraction as value
   */
  public Map<String, TweetInteraction> countRepliesReceived(boolean currentWeek) {
    LOGGER.info("Counting replies received - currentWeek = " + currentWeek + " ...");
    Stream<ITweet> tweetWithReplies = getReceivedReplies(currentWeek);
    return tweetWithReplies.filter(tweet -> this.isUserInList(tweet.getAuthorId()))
                           .filter(tweet -> this.getUserId().equals(
                               this.getTwitterClient().getInitialTweet(tweet, true).getAuthorId()))
                           .peek(initialTweet -> LOGGER.info("."))
                           .groupBy(tweet ->  this.getTwitterClient().getInitialTweet(tweet, true).getId())
                           .map(this::foldTweets);
  }

  private Tuple2<String, TweetInteraction> foldTweets(String tweetId, Stream<ITweet> tweets) {
    return Tuple.of(tweetId,
                    tweets.foldLeft(new TweetInteraction(),
                                    (interaction, tweet) -> interaction.addAnswerer(tweet.getAuthorId())));
  }

  /**
   * Get the replies tweets
   * @param currentWeek if true, use the labs2 endpoint to only count the last 7 days, otherwise classical search within 30 days until D-7
   * @return a stream if replies tweets
   */
  private Stream<ITweet> getReceivedReplies(boolean currentWeek) {
    if (currentWeek) {
      Date toDate = DateUtils.truncate(ConverterHelper.minutesBeforeNow(120), Calendar.HOUR);
      return Stream.ofAll(this.getTwitterClient()
                              .searchForTweetsWithin7days("(to:" + this.getUserName() + " has:mentions)" + "OR (url:redtheone -is:retweet)",
                                                          DateUtils.ceiling(DateUtils.addDays(toDate, -7), Calendar.HOUR),
                                                          toDate));
    }

    Date toDate = DateUtils.truncate(ConverterHelper.dayBeforeNow(7), Calendar.DAY_OF_MONTH);
    return Stream.ofAll(this.getTwitterClient()
                            .searchForTweetsWithin30days("to:" + this.getUserName() + " has:mentions",
                                                         DateUtils.ceiling(DateUtils.addDays(toDate, -23), Calendar.DAY_OF_MONTH),
                                                         toDate));
  }

  /**
   * Count the number of likes given to initial status (excluding answers)
   * @return
   */
  public Map<String, UserInteraction> countGivenLikesOnStatuses() {
    LOGGER.info("\nCounting given likes excluding answers...");

    Stream<ITweet> likedTweets = Stream.ofAll(this.getTwitterClient().getFavorites(this.getUserId(), 5000));
    return likedTweets
        .filter(tweet -> tweet.getInReplyToStatusId() == null)
        .filter(tweet -> this.isUserInList(tweet.getAuthorId()))
        .peek(tweet -> LOGGER.info("analyzing tweet : " + tweet.getText())) // @todo display userName?
        .groupBy(ITweet::getAuthorId)
        .map(this::getTurpleLike);
  }


}
