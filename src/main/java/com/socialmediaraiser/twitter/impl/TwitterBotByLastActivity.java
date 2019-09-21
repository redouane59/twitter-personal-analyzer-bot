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

    private int maxFriendship = 390;
    private static final Logger LOGGER = Logger.getLogger(TwitterBotByLastActivity.class.getName());
    public TwitterBotByLastActivity(String ownerName, boolean follow, boolean saveResults) {
        super(ownerName, follow, saveResults);
    }

    @Override
    public List<AbstractUser> getPotentialFollowers(String ownerId, int count){
        if(count>maxFriendship){
            count = maxFriendship;
        }

        List<Tweet> lastTweets = null;
        String startDate = "201907200000";
        String endDate = "201907221000";
        lastTweets = this.searchForTweets("@"+this.getOwnerName(), 100, startDate, endDate);
        int iteration=0;
        while(iteration<lastTweets.size() && this.getPotentialFollowers().size() < count){
            Tweet tweet = lastTweets.get(iteration);
            AbstractUser potentialFollower = tweet.getUser();
            // @todo how to not count commonFollowers in scoring ?
            if(this.shouldFollow(potentialFollower)) {
                if (this.isFollow()) {
                    AbstractUser user = this.followNewUser(potentialFollower);
                    if(user!=null) this.getPotentialFollowers().add(user);
                } else {
                    LOGGER.info(()->"potentialFollowers added : " + potentialFollower.getUsername());
                    this.getPotentialFollowers().add(potentialFollower);
                }
            }
            iteration++;
        }


        LOGGER.info(()->"********************************");
        LOGGER.info(this.getPotentialFollowers().size() + " followers followed / "
                + iteration + " users analyzed");
        LOGGER.info(()->"********************************");

        return this.getPotentialFollowers();
    }
}
