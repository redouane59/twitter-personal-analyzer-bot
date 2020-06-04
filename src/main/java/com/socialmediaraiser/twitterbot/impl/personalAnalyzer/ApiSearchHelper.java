package com.socialmediaraiser.twitterbot.impl.personalAnalyzer;

import com.socialmediaraiser.twitter.dto.tweet.ITweet;
import com.socialmediaraiser.twitter.helpers.ConverterHelper;
import lombok.CustomLog;
import org.apache.commons.lang3.time.DateUtils;

import java.util.*;


@CustomLog
public class ApiSearchHelper extends AbstractSearchHelper {

    public ApiSearchHelper(String userName){
        super(userName);
    }

    // @todo mix adding query parameter
    // @todo to the same for given retweets
    // countRepliesFromUserFromUserRecentSearch
    public void countRecentRepliesGiven(UserInteractions userInteractions, Date mostRecentArchiveTweetDate){
        LOGGER.info("\nCounting replies from user (API)...");
        long delta = (System.currentTimeMillis() - mostRecentArchiveTweetDate.getTime())/(1000*60*60*24);
        if(delta<1) return;
        List<ITweet> tweetWithReplies;
        String query;
        Date toDate;
        Date fromDate;

        query = "(from:"+this.getUserName()+" has:mentions)";
        if(delta>7){
            toDate = DateUtils.truncate(ConverterHelper.minutesBeforeNow(120), Calendar.HOUR);
            fromDate = DateUtils.ceiling(DateUtils.addDays(toDate, -7),Calendar.HOUR);
        } else{
            toDate = DateUtils.truncate(ConverterHelper.minutesBeforeNow(120), Calendar.HOUR);
            fromDate = mostRecentArchiveTweetDate;
        }
        tweetWithReplies= this.getTwitterClient().searchForTweetsWithin7days(query, fromDate, toDate);

        Set<String> answeredByUserTweets = new HashSet<>();

        for(ITweet tweet : tweetWithReplies){
            if(this.isUserInList(tweet.getInReplyToUserId())){
                ITweet initialTweet = this.getTwitterClient().getInitialTweet(tweet, true);
                // we only count the answer to a tweet once
                if(!this.getUserId().equals(initialTweet.getAuthorId()) && !answeredByUserTweets.contains(initialTweet.getId())){
                    System.out.print(".");
                    UserInteractions.UserInteraction userInteraction = userInteractions.get(tweet.getAuthorId());
                    userInteraction.incrementNbRepliesGiven();
                    answeredByUserTweets.add(initialTweet.getId());
                }
            }
        }
        LOGGER.info("\n" + tweetWithReplies.size() + " replies given found, " + answeredByUserTweets.size() + " saved");
    }

    // @todo change it and mix with data
    public void countRepliesReceived(UserInteractions userInteractions, boolean currentWeek) {
        LOGGER.info("\nCounting replies to user...");
        Date toDate;
        Date fromDate;
        if(currentWeek){
            toDate = DateUtils.truncate(ConverterHelper.minutesBeforeNow(120), Calendar.HOUR);
            fromDate = DateUtils.ceiling(DateUtils.addDays(toDate, -7),Calendar.HOUR);
        } else{
            toDate = DateUtils.truncate(ConverterHelper.dayBeforeNow(7),Calendar.DAY_OF_MONTH);
            fromDate = DateUtils.ceiling(DateUtils.addDays(toDate, -23), Calendar.DAY_OF_MONTH);
        }

        List<ITweet> tweetWithReplies;
        String query;
        if(currentWeek){
            query = "(to:"+this.getUserName()+" has:mentions)" + "OR (url:redtheone -is:retweet)";
            tweetWithReplies= this.getTwitterClient().searchForTweetsWithin7days(query, fromDate, toDate);
        } else{
            query = "to:"+this.getUserName()+" has:mentions";
            tweetWithReplies= this.getTwitterClient().searchForTweetsWithin30days(query, fromDate, toDate);
        }

        Map<String, List<String>> statusesAndAnswers = new HashMap<>();
        int savedAnswers = 0;

        for(ITweet tweet : tweetWithReplies){
            if(this.isUserInList(tweet.getAuthorId())){
                ITweet initialTweet = this.getTwitterClient().getInitialTweet(tweet, true);
                if(this.getUserId().equals(initialTweet.getAuthorId())){
                    System.out.print(".");
                    // if the initial tweet id is not on the map, we add it
                    if(!statusesAndAnswers.containsKey(initialTweet.getId())){
                        statusesAndAnswers.put(initialTweet.getId(), new ArrayList<>());
                    }
                    if(!statusesAndAnswers.get(initialTweet.getId()).contains(tweet.getAuthorId())) {
                        UserInteractions.UserInteraction userInteraction = userInteractions.get(tweet.getAuthorId());
                        userInteraction.incrementNbRepliesReceived();
                        statusesAndAnswers.get(initialTweet.getId()).add(tweet.getAuthorId());
                        savedAnswers++;
                    }
                }
            }

        }
        LOGGER.info("\n" + tweetWithReplies.size() + " replies to user found, " + savedAnswers + " saved");
    }

    // new
    public Map<String, TweetInteraction> countRepliesReceived(boolean currentWeek) {
        LOGGER.info("\nCounting replies to user...");
        Map<String, TweetInteraction> result = new HashMap<>();
        Date toDate;
        Date fromDate;
        if(currentWeek){
            toDate = DateUtils.truncate(ConverterHelper.minutesBeforeNow(120), Calendar.HOUR);
            fromDate = DateUtils.ceiling(DateUtils.addDays(toDate, -7),Calendar.HOUR);
        } else{
            toDate = DateUtils.truncate(ConverterHelper.dayBeforeNow(7),Calendar.DAY_OF_MONTH);
            fromDate = DateUtils.ceiling(DateUtils.addDays(toDate, -23), Calendar.DAY_OF_MONTH);
        }

        List<ITweet> tweetWithReplies;
        String query;
        if(currentWeek){
            query = "(to:"+this.getUserName()+" has:mentions)" + "OR (url:redtheone -is:retweet)";
            tweetWithReplies= this.getTwitterClient().searchForTweetsWithin7days(query, fromDate, toDate);
        } else{
            query = "to:"+this.getUserName()+" has:mentions";
            tweetWithReplies= this.getTwitterClient().searchForTweetsWithin30days(query, fromDate, toDate);
        }

        int savedAnswers = 0;

        for(ITweet tweet : tweetWithReplies){
            if(this.isUserInList(tweet.getAuthorId())){
                ITweet initialTweet = this.getTwitterClient().getInitialTweet(tweet, true);
                System.out.print(".");
                if(!result.containsKey(initialTweet.getId())){
                    result.put(initialTweet.getId(), new TweetInteraction());
                }
                result.get(initialTweet.getId()).getAnswererIds().add(tweet.getAuthorId());
                savedAnswers++;
            }
        }
        LOGGER.info("\n" + tweetWithReplies.size() + " replies to user found, " + savedAnswers + " saved");
        return result;
    }
    // end new

    // excluding answers
    public void countGivenLikesOnStatuses(UserInteractions userInteractions){
        LOGGER.info("\nCounting given likes excluding answers...");
        String userId = this.getTwitterClient().getUserFromUserName(this.getUserName()).getId();
        List<ITweet> likedTweets = this.getTwitterClient().getFavorites(userId, 5000);
        for(ITweet tweet : likedTweets){
            if(tweet.getInReplyToStatusId()==null && this.isUserInList(tweet.getAuthorId())) {
                System.out.print(".");
                UserInteractions.UserInteraction userInteraction = userInteractions.get(tweet.getAuthorId());
                userInteraction.incrementNbLikesGiven();
            }
        }
        LOGGER.info("\n" + likedTweets.size() + " given liked tweets found");
    }
}
