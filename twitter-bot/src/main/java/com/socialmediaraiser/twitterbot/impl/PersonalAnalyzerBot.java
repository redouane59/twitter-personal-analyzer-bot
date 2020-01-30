package com.socialmediaraiser.twitterbot.impl;

import com.socialmediaraiser.core.twitter.TwitterClient;
import com.socialmediaraiser.core.twitter.helpers.JsonHelper;
import com.socialmediaraiser.core.twitter.helpers.dto.ConverterHelper;
import com.socialmediaraiser.core.twitter.helpers.dto.tweet.Tweet;
import com.socialmediaraiser.core.twitter.helpers.dto.tweet.TweetDataDTO;
import com.socialmediaraiser.core.twitter.helpers.dto.user.AbstractUser;
import com.socialmediaraiser.twitterbot.AbstractIOHelper;
import com.socialmediaraiser.twitterbot.GoogleSheetHelper;
import com.socialmediaraiser.twitterbot.PersonalAnalyzerLauncher;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Data
public class PersonalAnalyzerBot {

    private String userName;
    private static final Logger LOGGER = Logger.getLogger(PersonalAnalyzerLauncher.class.getName());
    private AbstractIOHelper ioHelper;
    private TwitterClient twitterClient = new TwitterClient();

    public PersonalAnalyzerBot(String userName){
        this.userName = userName;
        this.ioHelper = new GoogleSheetHelper(userName);
    }
    public void launch() throws IOException, ParseException, InterruptedException {
        Map<String, Integer> interractions = this.getNbInterractions(ConverterHelper.getDateFromString("20200101"), this.userName);
        List<AbstractUser> followingsUsers = this.twitterClient.getFollowingsUsers(this.twitterClient.getUserFromUserName(userName).getId());
        for(AbstractUser user : followingsUsers){
            user.setNbInteractions(interractions.getOrDefault(user.getId(),0));
            // add RT and/or likes
            this.ioHelper.addNewFollowerLineSimple(user); // @todo how to manage better the limit ? Less calls to implement
            TimeUnit.MILLISECONDS.sleep(600);
            LOGGER.info("adding " + user.getUsername() + "...");
        }
        LOGGER.info("finish with success");
    }

    public Map<String, Integer> getNbInterractions(Date fromDate, String userName) throws IOException, ParseException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("tweet.json").getFile());
        List<TweetDataDTO> tweets = twitterClient.readTwitterDataFile(file);
        // @todo add condition on followers
        Map<String, Integer> result = new HashMap<>();
        Date tweetDate;
        int repliesGivenCount = 0;
        int repliesReceivedCount = 0;
        int retweetCount = 0;
        for(TweetDataDTO tweetDataDTO : tweets){
            // checking the reply I gave to other users
            String inReplyUserId = tweetDataDTO.getTweet().getInReplyToUserId();
            tweetDate = JsonHelper.getDateFromTwitterString(tweetDataDTO.getTweet().getCreatedAt());
            if(inReplyUserId!=null){
                if(tweetDate!=null && tweetDate.compareTo(fromDate)>0) {
                    result.put(inReplyUserId, 1+result.getOrDefault(inReplyUserId, 0));
                    repliesGivenCount++;
                }
            }
            if(tweetDate!=null && tweetDate.compareTo(fromDate)>0){
                // checking the user who retweeted me
                if(tweetDataDTO.getTweet().getRetweetCount()>0){
                    List<String> retweeterIds = this.twitterClient.getRetweetersId(tweetDataDTO.getTweet().getId());
                    for(String retweeterId : retweeterIds){
                        result.put(retweeterId, 1+result.getOrDefault(retweeterId, 0));
                        retweetCount++;
                    }
                }
            }
        }

        System.out.println(result.size() + " users found : " + repliesGivenCount + " given replies & " + retweetCount + " retweets");

        Date currentDate = ConverterHelper.minutesBefore(120);
        // 10 requests for 10 weeks
        for(int i=1; i<=10;i++){
            // checking the reply other gave me (40 days)
            List<Tweet> tweetWithReplies = this.twitterClient.searchForLast100Tweets30days("@"+userName,
                    ConverterHelper.getStringFromDate(currentDate)
            );
            for(Tweet tweet : tweetWithReplies){
                if(!tweet.getText().contains("RT")){
                    result.put(tweet.getUser().getId(), 1+result.getOrDefault(tweet.getUser().getId(), 0));
                    repliesReceivedCount++;
                }
                currentDate = tweet.getCreatedAt();
            }
        }
        System.out.println(result.size() + " users found : "
                + repliesGivenCount + " given replies & "
                + retweetCount + " retweets & "
                + repliesReceivedCount + " received replies");
        return result;
    }
}
