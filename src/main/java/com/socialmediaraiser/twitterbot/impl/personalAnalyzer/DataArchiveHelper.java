package com.socialmediaraiser.twitterbot.impl.personalAnalyzer;

import com.socialmediaraiser.twitter.dto.tweet.ITweet;
import com.socialmediaraiser.twitter.dto.tweet.TweetDTOv1;
import com.socialmediaraiser.twitter.dto.tweet.TweetType;
import lombok.CustomLog;

import java.io.File;
import java.io.IOException;
import java.util.*;

@CustomLog
public class DataArchiveHelper extends AbstractSearchHelper {

    private List<TweetDTOv1> tweets = new ArrayList<>();

    public DataArchiveHelper(String userName){
        super(userName);
    }
    public DataArchiveHelper(String userName, String archiveFileName, Date initDate) {
        super(userName);
        File file = new File(getClass().getClassLoader().getResource(archiveFileName).getFile());
        List<TweetDTOv1> allTweets = null;
        try {
            allTweets = this.getTwitterClient().readTwitterDataFile(file);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
        for(TweetDTOv1 tweet : allTweets){
            Date tweetDate = tweet.getCreatedAt();
            if(tweetDate!=null && tweetDate.compareTo(initDate)>0){
                this.tweets.add(tweet);
            }
        }
    }

    public void countRepliesGiven(UserInteractions userInteractions){
        LOGGER.info("\ncounting replies from user (archive)...");
        Set<String> answeredByUserTweets = new HashSet<>();
        int repliesGiven = 0;
        for(TweetDTOv1 tweet : tweets){ // @todo check for retweets + exclude own answers
            // checking the reply I gave to other users
            String inReplyUserId = tweet.getInReplyToUserId();
            if(inReplyUserId!=null && this.isUserInList(tweet.getInReplyToUserId())) {
                repliesGiven++;
                ITweet initialTweet = this.getTwitterClient().getInitialTweet(tweet, true);
                if(!this.getUserId().equals(initialTweet.getAuthorId()) && !answeredByUserTweets.contains(initialTweet.getId())) {
                    System.out.print(".");
                    UserInteractions.UserInteraction userInteraction = userInteractions.get(inReplyUserId);
                    userInteraction.incrementNbRepliesGiven();
                    answeredByUserTweets.add(initialTweet.getId());
                }
            }
        }
        LOGGER.info(repliesGiven + " replies given found, " + answeredByUserTweets.size() + " replies given saved");
    }

    public void countRetweesReceived(UserInteractions userInteractions){
        LOGGER.info("\ncounting retweets received (archive)...");
        int rtCount = 0;
        for(TweetDTOv1 tweet : this.filterTweetsByRetweet(false)){
            if(tweet.getRetweetCount()>0 && !tweet.getText().startsWith(("@"))){
                this.countRetweetsOfTweet(tweet, userInteractions);
                rtCount++;
            }
        }
        LOGGER.info(rtCount + " retweets received found");
    }

    private void countRetweetsOfTweet(TweetDTOv1 tweet, UserInteractions userInteractions){
        List<String> retweeterIds = this.getTwitterClient().getRetweetersId(tweet.getId());
        LOGGER.info("counting " + retweeterIds.size() + " retweeters of tweet " + tweet.getId());
        for(String retweeterId : retweeterIds){
            if(this.isUserInList(retweeterId)){
                UserInteractions.UserInteraction userInteraction = userInteractions.get(retweeterId);
                userInteraction.incrementNbRetweetsReceived();
            }
        }
    }

    public void countGivenRetweets(UserInteractions userInteractions){
        LOGGER.info("\ncounting retweets given (archive)...");
        List<TweetDTOv1> retweets = this.filterTweetsByRetweet(true);
        int rtCount = 0;
        for(TweetDTOv1 tweet : retweets){
            ITweet fullTweet = this.getTwitterClient().getTweet(tweet.getId());
            String retweetedTweetId = fullTweet.getInReplyToStatusId(TweetType.RETWEETED);
            if(retweetedTweetId!=null){
                ITweet retweetedTweet = this.getTwitterClient().getTweet(retweetedTweetId); // @todo check null
                String userId = retweetedTweet.getAuthorId(); // because info missing in data archive
                if(this.isUserInList(userId)){
                    UserInteractions.UserInteraction userInteraction = userInteractions.get(userId);
                    userInteraction.incrementNbRetweetsGiven();
                    rtCount++;
                }
            }
        }
        LOGGER.info(rtCount + " retweets given found");

    }

    public List<TweetDTOv1> filterTweetsByRetweet(boolean onlyRetweets){
        List<TweetDTOv1> result = new ArrayList<>();
        for(TweetDTOv1 tweet : this.tweets){
            if(tweet.getText().startsWith("RT @") == onlyRetweets){
                result.add(tweet);
            }
        }
        return result;
    }
}
