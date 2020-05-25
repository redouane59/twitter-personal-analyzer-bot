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
    private final Date initRepliesToDate = ConverterHelper.dayBeforeNow(60); // @todo to improve
    private final Date initRetweetsDate = ConverterHelper.dayBeforeNow(180);
    private String userId;
    public PersonalAnalyzerBot(String userName){
        this.userName = userName;
        this.ioHelper = new GoogleSheetHelper(userName);
    }

    public void launch(boolean includeFollowers, boolean includeFollowings, boolean onyFollowBackFollowers, String ArchiveFileName) throws IOException, InterruptedException {
        userId = this.twitterClient.getUserFromUserName(userName).getId();
        UserInteractions interactions = this.getNbInterractions(ArchiveFileName);
        List<IUser> followings = this.twitterClient.getFollowingUsers(userId);
        List<IUser> followers = this.twitterClient.getFollowerUsers(userId);
        Set<IUser> allUsers = new HashSet<>() {
            {
                addAll(followings);
                addAll(followers);
            } };

        List<User> usersToWrite = new ArrayList<>();
        int nbUsersToAdd = 50;
        for(IUser iUser : allUsers){
            if(hasToAddUser(iUser, followings, followers, includeFollowings, includeFollowers, onyFollowBackFollowers)){
                User user = new User(iUser);
                user.setNbRepliesFrom(interactions.get(iUser.getId()).getNbRepliesFrom());
                user.setNbRepliesTo(interactions.get(iUser.getId()).getNbRepliesTo());
                user.setNbRetweets(interactions.get(iUser.getId()).getNbRetweets());
                user.setNbLikesTo(interactions.get(iUser.getId()).getNbLikesTo());
                usersToWrite.add(user);
                if(usersToWrite.size()==nbUsersToAdd){
                    this.ioHelper.addNewFollowerLineSimple(usersToWrite);
                    usersToWrite = new ArrayList<>();
                    LOGGER.info("adding " + nbUsersToAdd);
                    TimeUnit.MILLISECONDS.sleep(500);
                }
            }
        }
        this.ioHelper.addNewFollowerLineSimple(usersToWrite);
        LOGGER.info("finish with success");
    }

    private boolean hasToAddUser(IUser user, List<IUser> followings, List<IUser> followers,
                                 boolean showFollowings, boolean showFollowers, boolean onyFollowBackUsers){
        // case 0 : only follow back users
        if(onyFollowBackUsers && followings.contains(user) && !followers.contains(user)){
            return false;
        }

        // case 1 : show all the people i'm following and all the users following me
        if(!showFollowers && !showFollowings){
            return true;
        }
        // case 2 : show all the people I'm following who are following me back
        else if(showFollowers && showFollowings && onyFollowBackUsers){
            return (followings.contains(user) && followers.contains(user));
        }
        // case 3 : show all the people i'm following or all the people who are following me
        else{
            return ((followings.contains(user) && showFollowings) || followers.contains(user) && showFollowers);
        }
    }

    private UserInteractions getNbInterractions(String archiveFileName) throws IOException {
        File file = new File(getClass().getClassLoader().getResource(archiveFileName).getFile());
        List<TweetDTOv1> tweets = this.removeRTsFromTweetList(twitterClient.readTwitterDataFile(file));
        UserInteractions userInteractions = new UserInteractions();
        this.countRepliesFromUserFromUserRecentSearch(userInteractions, tweets.get(0).getCreatedAt());
        this.countRepliesFromUserFromUserDataArchive(userInteractions, tweets, initRepliesToDate);
        this.countRecentRepliesToUser(userInteractions, true); // D-7 -> D0
        this.countRecentRepliesToUser(userInteractions, false); // D-30 -> D-7
        this.countRetweets(userInteractions, tweets, initRetweetsDate);
        this.countGivenLikes(userInteractions);
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

    // @todo how to count each conversation only once ?
    public void countRecentRepliesToUser(UserInteractions userInteractions, boolean currentWeek) {
        LOGGER.info("counting replies from...");
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
            query = "(to:"+userName+" has:mentions)" + "OR (url:redtheone -is:retweet)";
            tweetWithReplies= this.twitterClient.searchForTweetsWithin7days(query, fromDate, toDate);
        } else{
            query = "to:"+userName+" has:mentions";
            tweetWithReplies= this.twitterClient.searchForTweetsWithin30days(query, fromDate, toDate);
        }

        Map<String, List<String>> statusesAndAnswers = new HashMap<>();
        int savedAnswers = 0;

        for(ITweet tweet : tweetWithReplies){
            System.out.print(".");
            String initialTweetId = this.twitterClient.getInitialTweetId(tweet);
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

    // @todo how to count each conversation only once ? from ???
    private void countRepliesFromUserFromUserDataArchive(UserInteractions userInteractions, List<TweetDTOv1> tweets, Date initDate){
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
                String initialTweetId = this.twitterClient.getInitialTweetId(tweet);
                System.out.print(".");
                if(!answeredByUserTweets.contains(initialTweetId)) {
                    UserInteractions.UserInteraction userInteraction = userInteractions.get(inReplyUserId);
                    userInteraction.incrementNbRepliesTo();
                    answeredByUserTweets.add(initialTweetId);
                }
            }
        }
        LOGGER.info(repliesGiven + " replies given found, " + answeredByUserTweets.size() + " replies given saved");
    }

    // to correct to search FROM instead of TO
    private void countRepliesFromUserFromUserRecentSearch(UserInteractions userInteractions, Date mostRecentArchiveTweetDate){
        LOGGER.info("counting replies from user (API)...");
        long delta = (System.currentTimeMillis() - mostRecentArchiveTweetDate.getTime())/(1000*60*60*24);
        if(delta<1) return;
        List<ITweet> tweetWithReplies;
        String query;
        Date toDate;
        Date fromDate;
        query = "(from:"+userName+" has:mentions)";
        if(delta>7){
            toDate = DateUtils.truncate(ConverterHelper.minutesBeforeNow(120), Calendar.HOUR);
            fromDate = DateUtils.ceiling(DateUtils.addDays(toDate, -7),Calendar.HOUR);
        } else{
            toDate = DateUtils.truncate(ConverterHelper.minutesBeforeNow(120), Calendar.HOUR);
            fromDate = mostRecentArchiveTweetDate;
        }
        tweetWithReplies= this.twitterClient.searchForTweetsWithin7days(query, fromDate, toDate);

        Set<String> answeredByUserTweets = new HashSet<>();

        for(ITweet tweet : tweetWithReplies){
            String initialTweetId = this.twitterClient.getInitialTweetId(tweet);
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

    private void countGivenLikes(UserInteractions userInteractions){
        LOGGER.info("counting given likes excluding answers...");
        List<ITweet> likedTweets = this.twitterClient.getFavorites(userId, 5000);
        for(ITweet tweet : likedTweets){
            if(tweet.getInReplyToStatusId()==null) {
                UserInteractions.UserInteraction userInteraction = userInteractions.get(tweet.getAuthorId());
                userInteraction.incrementNbLikesTo();
            }
        }
        LOGGER.info(likedTweets.size() + " given liked tweets found");
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
            unfollowName.replace(" ","");
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