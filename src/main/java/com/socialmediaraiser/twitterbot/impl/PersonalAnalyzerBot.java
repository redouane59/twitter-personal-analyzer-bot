package com.socialmediaraiser.twitterbot.impl;

import com.socialmediaraiser.twitter.TwitterClient;
import com.socialmediaraiser.twitter.dto.tweet.ITweet;
import com.socialmediaraiser.twitter.dto.tweet.TweetDTOv1;
import com.socialmediaraiser.twitter.dto.user.IUser;
import com.socialmediaraiser.twitter.helpers.ConverterHelper;
import com.socialmediaraiser.twitterbot.AbstractIOHelper;
import com.socialmediaraiser.twitterbot.GoogleSheetHelper;
import com.socialmediaraiser.twitterbot.PersonalAnalyzerLauncher;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Getter
@Setter
public class PersonalAnalyzerBot {

    private String userName;
    private static final Logger LOGGER = Logger.getLogger(PersonalAnalyzerLauncher.class.getName());
    private AbstractIOHelper ioHelper;
    private TwitterClient twitterClient = new TwitterClient();
    private final Date initRepliesToDate = ConverterHelper.dayBeforeNow(60);
    private final Date initRetweetsDate = ConverterHelper.dayBeforeNow(180);

    public PersonalAnalyzerBot(String userName){
        this.userName = userName;
        this.ioHelper = new GoogleSheetHelper(userName);
    }

    public void launch(boolean showFollowers, boolean showFollowings) throws IOException, InterruptedException {
        UserInteractions interactions = this.getNbInterractions();
        String userId = this.twitterClient.getUserFromUserName(userName).getId();
        List<IUser> followings = this.twitterClient.getFollowingUsers(userId);
        List<IUser> followers = this.twitterClient.getFollowerUsers(userId);
        Set<IUser> allUsers = new HashSet<>() {
            {
                addAll(followings);
                addAll(followers);
            } };

        List<User> userToWrite = new ArrayList<>();
        int nbUsersToAdd = 10;
        for(IUser iUser : allUsers){
            if(hasToAddUser(iUser, followings, followers, showFollowings, showFollowers)){
                User user = new User(iUser);
                user.setNbRepliesFrom(interactions.get(iUser.getId()).getNbRepliesFrom());
                user.setNbRepliesTo(interactions.get(iUser.getId()).getNbRepliesTo());
                user.setNbRetweets(interactions.get(iUser.getId()).getNbRetweets());
                userToWrite.add(user);
                if(userToWrite.size()==nbUsersToAdd){
                    this.ioHelper.addNewFollowerLineSimple(userToWrite);
                    userToWrite = new ArrayList<>();
                    LOGGER.info("adding 10");
                }
            }
        }
        this.ioHelper.addNewFollowerLineSimple(userToWrite);
        LOGGER.info("finish with success");
    }

    private boolean hasToAddUser(IUser user, List<IUser> followings, List<IUser> followers, boolean showFollowings, boolean showFollowers){
        // case 1 : show all the people i'm following and all the users following me
        if(!showFollowers && !showFollowings){
            return true;
        }
        // case 2 : show all the people I'm following who are following me back
        else if(showFollowers && showFollowings){
            return (followings.contains(user) && followers.contains(user));
        }
        // case 3 : show all the people i'm following or all the people who are following me
        else{
            return ((followings.contains(user) && showFollowings) || followers.contains(user) && showFollowers);
        }
    }

    private UserInteractions getNbInterractions() throws IOException {
        File file = new File(getClass().getClassLoader().getResource("tweet-history.json").getFile());
        List<TweetDTOv1> tweets = this.removeRTsFromTweetList(twitterClient.readTwitterDataFile(file));
        UserInteractions userInteractions = new UserInteractions();
        this.countRetweets(userInteractions, tweets, initRetweetsDate);
        this.countRepliesTo(userInteractions, tweets, initRepliesToDate);
        this.countRecentRepliesFrom(userInteractions, true); // D-7 -> D0
        this.countRecentRepliesFrom(userInteractions, false); // D-30 -> D-7
        return userInteractions;
    }

    private List<TweetDTOv1> removeRTsFromTweetList(List<TweetDTOv1> tweetList){
        List<TweetDTOv1> result = new ArrayList<>();
        for(TweetDTOv1 tweet : tweetList){
            if(!tweet.getText().startsWith("RT @")){
                result.add(tweet);
            }
        }
        return result;
    }

    public void countRecentRepliesFrom(UserInteractions userInteractions, boolean currentWeek) {
        LOGGER.info("counting replies from...");
        Date toDate;
        Date fromDate;
        if(currentWeek){
            toDate = DateUtils.truncate(ConverterHelper.minutesBeforeNow(60), Calendar.HOUR);
            fromDate = DateUtils.ceiling(DateUtils.addDays(toDate, -7),Calendar.HOUR);
        } else{
            toDate = DateUtils.truncate(ConverterHelper.dayBeforeNow(7),Calendar.DAY_OF_MONTH);
            fromDate = DateUtils.ceiling(DateUtils.addDays(toDate, -23), Calendar.DAY_OF_MONTH);
        }

        List<ITweet> tweetWithReplies;
        String query;
        if(currentWeek){
            query = "(to:"+userName+" has:mentions)" + "OR (url:redtheone -is:retweet)";
            tweetWithReplies= this.twitterClient.searchForTweetsWithin7days(query, fromDate, toDate);
        } else{
            query = "to:"+userName+" has:mentions";
            tweetWithReplies= this.twitterClient.searchForTweetsWithin30days(query, fromDate, toDate);
        }
        for(ITweet tweet : tweetWithReplies){
            UserInteractions.UserInteraction userInteraction = userInteractions.get(tweet.getAuthorId());
            userInteraction.incrementNbRepliesFrom();
        }
    }

    private void countRepliesTo(UserInteractions userInteractions, List<TweetDTOv1> tweets, Date initDate){
        LOGGER.info("counting replies to...");
        Date tweetDate;
        for(TweetDTOv1 tweet : tweets){
            // checking the reply I gave to other users
            String inReplyUserId = tweet.getInReplyToUserId();
            tweetDate = tweet.getCreatedAt();
            if(inReplyUserId!=null && tweetDate!=null && tweetDate.compareTo(initDate)>0) {
                    UserInteractions.UserInteraction userInteraction = userInteractions.get(inReplyUserId);
                    userInteraction.incrementNbRepliesTo();
            }
        }
    }

    private void countRetweets(UserInteractions userInteractions, List<TweetDTOv1> tweets, Date initDate){
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
        List<String> retweeterIds = this.twitterClient.getRetweetersId(tweet.getId());
        LOGGER.info("counting " + retweeterIds.size() + " retweeters of tweet " + tweet.getId());
        for(String retweeterId : retweeterIds){
            UserInteractions.UserInteraction userInteraction = userInteractions.get(retweeterId);
            userInteraction.incrementNbRetweets();
        }
    }

    @SneakyThrows
    public void unfollow(String[] toUnfollow, String[] whiteList){

        int nbUnfollows = 0;
        for(String unfollowName : toUnfollow){
            if(!Arrays.asList(whiteList).contains(unfollowName)){
                this.getTwitterClient().unfollowByName(unfollowName);
                nbUnfollows++;
                TimeUnit.MILLISECONDS.sleep(500);
                System.out.println(unfollowName + " unfollowed");
            }
        }
        LOGGER.info(nbUnfollows + " users unfollowed with success !");
    }
}