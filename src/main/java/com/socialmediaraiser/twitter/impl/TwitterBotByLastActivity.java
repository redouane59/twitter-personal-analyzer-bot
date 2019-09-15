package com.socialmediaraiser.twitter.impl;

import com.socialmediaraiser.twitter.AbstractTwitterBot;
import com.socialmediaraiser.twitter.Tweet;
import com.socialmediaraiser.twitter.helpers.dto.getuser.AbstractUser;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Getter
@Setter
public class TwitterBotByLastActivity extends AbstractTwitterBot {

    private List<AbstractUser> potentialFollowers = new ArrayList<>();
    private int maxFriendship = 390;
    private static final Logger LOGGER = Logger.getLogger(TwitterBotByLastActivity.class.getName());
    List<String> ownerFollowingIds;
    List<String> followedRecently;
    public TwitterBotByLastActivity(String ownerName) {
        super(ownerName);
    }

    @Override
    public List<AbstractUser> getPotentialFollowers(String ownerId, int count, boolean follow, boolean saveResults){
        if(count>maxFriendship){
            count = maxFriendship;
        }
        ownerFollowingIds = this.getFollowingIds(ownerId);
        followedRecently = this.getIoHelper().getPreviouslyFollowedIds();

        List<Tweet> lastTweets = null;
        String startDate = "201907200000";
        String endDate = "201907221000";
        lastTweets = this.searchForTweets("@"+this.getOwnerName(), 100, startDate, endDate);
        int iteration=0;
        while(iteration<lastTweets.size() && potentialFollowers.size() < count){
            Tweet tweet = lastTweets.get(iteration);
            AbstractUser potentialFollower = tweet.getUser();
            // @todo how to not count commonFollowers in scoring ?
            if(this.shouldFollow(potentialFollower)) {
                if (follow) {
                    AbstractUser user = this.followNewUser(potentialFollower, saveResults);
                    if(user!=null) potentialFollowers.add(user);
                } else {
                    LOGGER.info(()->"potentialFollowers added : " + potentialFollower.getUsername());
                    potentialFollowers.add(potentialFollower);
                }
            }
            iteration++;
        }


        LOGGER.info(()->"********************************");
        LOGGER.info(potentialFollowers.size() + " followers followed / "
                + iteration + " users analyzed");
        LOGGER.info(()->"********************************");

        return potentialFollowers;
    }

    // @todo duplicate
    public boolean shouldFollow(AbstractUser user){
        return (ownerFollowingIds.indexOf(user.getId())==-1
                && followedRecently.indexOf(user.getId())==-1
                && potentialFollowers.indexOf(user)==-1
                && user.shouldBeFollowed(this.getOwnerName()));
    }
}
