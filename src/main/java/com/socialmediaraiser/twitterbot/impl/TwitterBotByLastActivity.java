package com.socialmediaraiser.twitterbot.impl;

import com.socialmediaraiser.twitter.IUser;
import com.socialmediaraiser.twitter.dto.tweet.ITweet;
import com.socialmediaraiser.twitterbot.AbstractTwitterFollowBot;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.logging.Logger;

@Getter
@Setter
public class TwitterBotByLastActivity extends AbstractTwitterFollowBot {

    private int maxFriendship = 390;
    private static final Logger LOGGER = Logger.getLogger(TwitterBotByLastActivity.class.getName());
    public TwitterBotByLastActivity(String ownerName, boolean follow, boolean saveResults) {
        super(ownerName, follow, saveResults);
    }

    @Override
    public List<IUser> getPotentialFollowers(String ownerId, int count){
        if(count>maxFriendship){
            count = maxFriendship;
        }

        List<ITweet> lastTweets = null;
        String startDate = "201907200000";
        String endDate = "201907221000";
        lastTweets = this.searchForTweets("@"+this.getOwnerName(), 100, startDate, endDate);
        int iteration=0;
        while(iteration<lastTweets.size() && this.getPotentialFollowers().size() < count){
            ITweet tweet = lastTweets.get(iteration);
            IUser potentialFollower = tweet.getUser();
            // @todo how to not count commonFollowers in scoring ?
            if(this.shouldFollow(potentialFollower)) {
                if (this.isFollow()) {
                    IUser user = this.followNewUser(potentialFollower);
                    if(user!=null) this.getPotentialFollowers().add(user);
                } else {
                    LOGGER.info(()->"potentialFollowers added : " + potentialFollower.getName());
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
