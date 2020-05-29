package com.socialmediaraiser.twitterbot.impl.personalAnalyzer;

import com.socialmediaraiser.twitter.dto.tweet.TweetDTOv1;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class DataArchiveHelper extends AbstractSearchHelper {

    // @todo use lombok logger
    private static final Logger LOGGER = Logger.getLogger(DataArchiveHelper.class.getName());


    public DataArchiveHelper(String userName){
        super(userName);
    }

    public void countRepliesFromUser(UserInteractions userInteractions, List<TweetDTOv1> tweets, Date initDate){
        LOGGER.info("counting replies from user (archive)...");
        Date tweetDate;
        Set<String> answeredByUserTweets = new HashSet<>();
        int repliesGiven = 0;
        for(TweetDTOv1 tweet : tweets){
            // checking the reply I gave to other users
            String inReplyUserId = tweet.getInReplyToUserId();
            tweetDate = tweet.getCreatedAt();
            if(inReplyUserId!=null && tweetDate!=null && tweetDate.compareTo(initDate)>0) {
                repliesGiven++;
                String initialTweetId = this.getTwitterClient().getInitialTweetId(tweet);
                if(!answeredByUserTweets.contains(initialTweetId)) {
                    System.out.print(".");
                    UserInteractions.UserInteraction userInteraction = userInteractions.get(inReplyUserId);
                    userInteraction.incrementNbRepliesTo();
                    answeredByUserTweets.add(initialTweetId);
                }
            }
        }
        LOGGER.info(repliesGiven + " replies given found, " + answeredByUserTweets.size() + " replies given saved");
    }

    public void countRetweets(UserInteractions userInteractions, List<TweetDTOv1> tweets, Date initDate){
        Date tweetDate;
        for(TweetDTOv1 tweet : tweets){
            tweetDate = tweet.getCreatedAt();
            if(tweetDate!=null && tweetDate.compareTo(initDate)>0){
                if(tweet.getRetweetCount()>0 && !tweet.getText().startsWith(("@"))){
                    this.countRetweetsOfTweet(tweet, userInteractions);
                }
            }
        }
    }

    private void countRetweetsOfTweet(TweetDTOv1 tweet, UserInteractions userInteractions){
        List<String> retweeterIds = this.getTwitterClient().getRetweetersId(tweet.getId());
        LOGGER.info("counting " + retweeterIds.size() + " retweeters of tweet " + tweet.getId());
        for(String retweeterId : retweeterIds){
            UserInteractions.UserInteraction userInteraction = userInteractions.get(retweeterId);
            userInteraction.incrementNbRetweets();
        }
    }
}
