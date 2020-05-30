package com.socialmediaraiser.twitterbot.impl.personalAnalyzer;

import com.socialmediaraiser.twitter.TwitterClient;
import com.socialmediaraiser.twitter.dto.tweet.TweetDTOv1;
import com.socialmediaraiser.twitter.dto.user.IUser;
import com.socialmediaraiser.twitter.helpers.ConverterHelper;
import com.socialmediaraiser.twitterbot.AbstractIOHelper;
import com.socialmediaraiser.twitterbot.GoogleSheetHelper;
import com.socialmediaraiser.twitterbot.PersonalAnalyzerLauncher;
import com.socialmediaraiser.twitterbot.impl.User;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

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
    private final Date initRetweetsDate = ConverterHelper.dayBeforeNow(60);
    DataArchiveHelper dataArchiveHelper;
    ApiSearchHelper apiSearchHelper;

    public PersonalAnalyzerBot(String userName){
        this.userName = userName;
        this.ioHelper = new GoogleSheetHelper(userName);
        this.dataArchiveHelper = new DataArchiveHelper(userName);
        this.apiSearchHelper = new ApiSearchHelper(userName);
    }

    public void launch(boolean includeFollowers, boolean includeFollowings, boolean onyFollowBackFollowers, String ArchiveFileName) throws IOException, InterruptedException {
        String userId = this.twitterClient.getUserFromUserName(userName).getId();
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
                    LOGGER.info("adding " + nbUsersToAdd + " users ...");
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
        // counts all the unique replies given by the user to others
       dataArchiveHelper.countRepliesGiven(userInteractions, tweets, initRepliesToDate);
        // counts all the retweets of user tweets done by others
       dataArchiveHelper.countRetweesReceived(userInteractions, tweets, initRetweetsDate);
        // counts all replies given recently to others
       apiSearchHelper.countRecentRepliesGiven(userInteractions, tweets.get(0).getCreatedAt());
        // counts all the replies received by others
        apiSearchHelper.countRepliesReceived(userInteractions, true); // D-7 -> D0
        apiSearchHelper.countRepliesReceived(userInteractions, false); // D-30 -> D-7
        apiSearchHelper.countGivenLikes(userInteractions);
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