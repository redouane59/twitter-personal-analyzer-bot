package com.socialmediaraiser.twitterbot.impl.personalAnalyzer;

import com.socialmediaraiser.twitter.dto.tweet.ITweet;
import com.socialmediaraiser.twitter.helpers.ConverterHelper;
import org.apache.commons.lang3.time.DateUtils;

import java.util.*;
import java.util.logging.Logger;


public class ApiSearchHelper extends AbstractSearchHelper {

    // @todo use lombok logger
    private static final Logger LOGGER = Logger.getLogger(ApiSearchHelper.class.getName());

    public ApiSearchHelper(String userName){
        super(userName);
    }

    // @todo mix adding query parameter
    // countRepliesFromUserFromUserRecentSearch
    public void countRepliesFromUser(UserInteractions userInteractions, Date mostRecentArchiveTweetDate){
        LOGGER.info("counting replies from user (API)...");
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
            String initialTweetId = this.getTwitterClient().getInitialTweetId(tweet);
            System.out.print(".");
            // we only count the answer to a tweet once
            if(!answeredByUserTweets.contains(initialTweetId)){
                UserInteractions.UserInteraction userInteraction = userInteractions.get(tweet.getAuthorId());
                userInteraction.incrementNbRepliesTo();
                answeredByUserTweets.add(initialTweetId);
            }
        }
        LOGGER.info(tweetWithReplies.size() + " replies given found, " + answeredByUserTweets.size() + " saved");
    }

    // @todo how to count each conversation only once ?
    public void countRepliesToUser(UserInteractions userInteractions, boolean currentWeek) {
        LOGGER.info("counting replies to user...");
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
            System.out.print(".");
            String initialTweetId = this.getTwitterClient().getInitialTweetId(tweet);
            // if the initial tweet id is not on the map, we add it
            if(!statusesAndAnswers.containsKey(initialTweetId)){
                statusesAndAnswers.put(initialTweetId, new ArrayList<>());
            }
            if(!statusesAndAnswers.get(initialTweetId).contains(tweet.getAuthorId())) {
                UserInteractions.UserInteraction userInteraction = userInteractions.get(tweet.getAuthorId());
                userInteraction.incrementNbRepliesFrom();
                statusesAndAnswers.get(initialTweetId).add(tweet.getAuthorId());
                savedAnswers++;
            }
        }
        LOGGER.info(tweetWithReplies.size() + " replies to user found, " + savedAnswers + " saved");
    }

    public void countGivenLikes(UserInteractions userInteractions){
        LOGGER.info("counting given likes excluding answers...");
        String userId = this.getTwitterClient().getUserFromUserName(this.getUserName()).getId();
        List<ITweet> likedTweets = this.getTwitterClient().getFavorites(userId, 5000);
        for(ITweet tweet : likedTweets){
            if(tweet.getInReplyToStatusId()==null) {
                UserInteractions.UserInteraction userInteraction = userInteractions.get(tweet.getAuthorId());
                userInteraction.incrementNbLikesTo();
            }
        }
        LOGGER.info(likedTweets.size() + " given liked tweets found");
    }
}
