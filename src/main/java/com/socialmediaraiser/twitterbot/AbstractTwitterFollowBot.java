package com.socialmediaraiser.twitterbot;

import com.socialmediaraiser.RelationType;
import com.socialmediaraiser.twitter.TwitterClient;
import com.socialmediaraiser.twitter.dto.tweet.ITweet;
import com.socialmediaraiser.twitter.dto.user.IUser;
import com.socialmediaraiser.twitter.helpers.RequestHelper;
import com.socialmediaraiser.twitterbot.impl.User;
import com.socialmediaraiser.twitterbot.scoring.Criterion;
import io.vavr.control.Option;
import lombok.Data;
import lombok.CustomLog;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Data
@CustomLog
public abstract class AbstractTwitterFollowBot {

    private AbstractIOHelper ioHelper;

    public abstract List<IUser> getPotentialFollowers(String ownerId, int count);
    private String ownerName;
    private List<IUser> potentialFollowers = new ArrayList<>();
    List<String> followedRecently;
    List<String> ownerFollowingIds;
    private boolean follow; // if try will follow users
    private boolean saveResults;
    private RequestHelper requestHelper = new RequestHelper();
    private TwitterClient TwitterClient;
    private static final String IDS = "ids";
    private static final String USERS = "users";
    private static final String CURSOR = "cursor";
    private static final String NEXT = "next";
    private static final String RETWEET_COUNT = "retweet_count";
    private static final String RELATIONSHIP = "relationship";
    private static final String FOLLOWING = "following";
    private static final String FOLLOWED_BY = "followed_by";
    private static final String SOURCE = "source";
    //   private static final int MAX_GET_F_CALLS = 30;

    public AbstractTwitterFollowBot(String ownerName, boolean follow, boolean saveResults) {
        this.ioHelper = new GoogleSheetHelper(ownerName);
        this.ownerName = ownerName;
        this.TwitterClient = new TwitterClient();
        this.follow = follow;
        this.saveResults = saveResults;
        this.followedRecently = this.getIoHelper().getPreviouslyFollowedIds();
        this.ownerFollowingIds = getTwitterClient().getFollowingIds(getTwitterClient().getUserFromUserName(ownerName).getId());
    }

    public void checkNotFollowBack(String ownerName, Date date, boolean override) {
        List<String> followedPreviously = this.getIoHelper().getPreviouslyFollowedIds(override, override, date);
        if(followedPreviously!=null && !followedPreviously.isEmpty()){
            IUser user = getTwitterClient().getUserFromUserName(ownerName);
            this.areFriends(user.getId(), followedPreviously, this.isFollow(), this.isSaveResults());
        } else{
            LOGGER.severe(()->"no followers found at this date");
        }
    }

    public IUser followNewUser(IUser potentialFollower){
        User user = new User(potentialFollower);
        getTwitterClient().follow(user.getId());
        user.setDateOfFollowNow();
        if (this.saveResults) {
            this.getIoHelper().addNewFollowerLine(user);
        }
        return user;
    }

    private void logError(Exception e, String response){
        LOGGER.severe(() -> e.getMessage() + " response = " + response);
    }

    public void unfollowAllUsersFromCriterion(Criterion criterion, int value, boolean unfollow){
        int maxUnfollows = 400;
        int nbUnfollows = 0;
        for(String id : this.ownerFollowingIds){
            IUser user = getTwitterClient().getUserFromUserId(id);
            if(nbUnfollows>=maxUnfollows) break;
            if(unfollow && this.shouldBeUnfollowed((User)user, criterion, value)){
                getTwitterClient().unfollow(user.getId());
                nbUnfollows++;
                LOGGER.info(()-> user.getName() + " -> unfollowed");
            }
        }
        LOGGER.info(nbUnfollows + " users unfollowed");
    }

    public boolean shouldFollow(IUser user){
        return (ownerFollowingIds.indexOf(user.getId())==-1
                && followedRecently.indexOf(user.getId())==-1
                && potentialFollowers.indexOf(user)==-1
                && this.shouldBeFollowed(user, this.getOwnerName()));
    }

    protected LinkedHashMap<String, Boolean> areFriends(String userId, List<String> otherIds, boolean unfollow, boolean writeOnSheet){
        LinkedHashMap<String, Boolean> result = new LinkedHashMap<>();
        int nbUnfollows = 0;
        for(String otherId : otherIds){
            boolean userShouldBeUnfollowed = shouldBeUnfollowed(userId, otherId, writeOnSheet);
            if(unfollow && userShouldBeUnfollowed){
                this.getTwitterClient().unfollow(otherId);
                nbUnfollows++;
                this.temporiseUnfollows(nbUnfollows);
            }
            result.put(otherId, !userShouldBeUnfollowed);
        }
        LOGGER.info("Follow back : " + ((otherIds.size()-nbUnfollows)*100)/otherIds.size() + "% (nb unfollows : " + nbUnfollows+ ")");
        return result;
    }

    private boolean shouldBeUnfollowed(String userId, String otherId, boolean writeOnSheet){
        RelationType relation = this.getTwitterClient().getRelationType(userId, otherId);
        if(relation==null) LOGGER.severe(() -> "null value for" + otherId);

        Boolean userFollowsBack = false;
        if(relation == RelationType.FRIENDS || relation == RelationType.FOLLOWER){
            userFollowsBack = true;
        }

        if(writeOnSheet){
            this.getIoHelper().updateFollowBackInformation(otherId, userFollowsBack);
        }

        return !userFollowsBack && relation == RelationType.FOLLOWING;
    }

    private void temporiseUnfollows(int nbUnfollows){
        if(nbUnfollows%5==0){
            try {
                LOGGER.info(()->"Sleeping 5sec");
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                LOGGER.severe(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    public boolean shouldBeFollowed(IUser user, String ownerName){
        if(user.getName()!=null && user.getName().equals(ownerName)){
            return false;
        }
        return false; // @todo to fix
        //return this.scoringEngine.shouldBeFollowed(user);
    }

    public boolean shouldBeUnfollowed(User user, Criterion criterion, int value){
        switch (criterion){
            case LAST_UPDATE:
                if(user.getLastUpdate()!=null) {
                    long nbDaysSinceLastUpdate = (new Date().getTime() - user.getLastUpdate().getTime()) / (24 * 60 * 60 * 1000);
                    if(nbDaysSinceLastUpdate>value){
                        return true;
                    }
                }
                return false;
            case RATIO:
                if(user.getFollowersRatio()<value){
                    return true;
                }
                return false;
            case NB_FOLLOWERS:
                if(user.getFollowersCount()<value){
                    return true;
                }
                return false;
            default:
                LOGGER.severe("Criterion " + criterion.name() + " not found");
                return false;
        }
    }

    public boolean isLanguageOK(IUser user){
        return Option.of(user.getLang())
                .map(b -> user.getLang().equals(FollowProperties.getTargetProperties().getLanguage())).getOrElse(false);
    }

    public boolean getRandomForestPrediction(IUser user){
        User customUser = new User(user);
        customUser.setDateOfFollowNow();
        return RandomForestAlgoritm.getPrediction(customUser);
    }

    public boolean isInfluencer(User user){
        String descriptionWords = FollowProperties.getTargetProperties().getDescription();
        String[] descriptionWordsSplitted = descriptionWords.split(FollowProperties.getArraySeparator());
        String[] userDescriptionSplitted = user.getDescription().split(" ");

        String locationWords = FollowProperties.getTargetProperties().getLocation();
        String[] locationWordsSplitted = Option.of(locationWords.split(FollowProperties.getArraySeparator())).getOrElse(new String[0]);

        String[] userLocationSplitted = user.getLocation().split(" ");

        boolean matchDescription = false;
        boolean matchLocation = false;

        for(String s :userDescriptionSplitted){
            if(Arrays.stream(descriptionWordsSplitted).anyMatch(s::contains)){
                matchDescription = true;
            }
        }
        for(String s :userLocationSplitted){
            if(Arrays.stream(locationWordsSplitted).anyMatch(s::contains)){
                matchLocation = true;
            }
        }
        return (matchDescription
                && matchLocation
                && user.getFollowersRatio() > FollowProperties.getInfluencerProperties().getMinRatio()
                && user.getFollowersCount()> FollowProperties.getInfluencerProperties().getMinNbFollowers());
    }

    public boolean matchWords(ITweet tweet, List<String> words){
        for(String word : words){
            if(tweet.getText().contains(word)){
                return true;
            }
        }
        return false;
    }

}
