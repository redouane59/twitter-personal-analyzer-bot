package com.socialMediaRaiser.twitter.impl;

import com.socialMediaRaiser.AbstractBot;
import com.socialMediaRaiser.twitter.AbstractTwitterBot;
import com.socialMediaRaiser.twitter.Tweet;
import com.socialMediaRaiser.twitter.User;
import com.socialMediaRaiser.twitter.helpers.dto.getUser.AbstractUser;
import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Getter
@Setter
public class TwitterBotByLastActivity extends AbstractTwitterBot {

    private List<AbstractUser> potentialFollowers = new ArrayList<>();
    private int maxFriendship = 390;
    private static final Logger LOGGER = Logger.getLogger(TwitterBotByLastActivity.class.getName());

    public TwitterBotByLastActivity(String ownerName) {
        super(ownerName);
    }

    @Override
    public List<AbstractUser> getPotentialFollowers(String ownerId, int count, boolean follow, boolean saveResults){
        if(count>maxFriendship){
            count = maxFriendship;
        }
        List<String> ownerFollowingIds = this.getFollowingIds(ownerId);
        List<String> followedRecently = this.getIOHelper().getPreviouslyFollowedIds();

        List<Tweet> lastTweets = null;
        String startDate = "201907200000";
        String endDate = "201907221000";
        lastTweets = this.searchForTweets("@"+this.getOwnerName(), 100, startDate, endDate);
        int iteration=0;
        while(iteration<lastTweets.size() && potentialFollowers.size() < count){
            Tweet tweet = lastTweets.get(iteration);
            AbstractUser potentialFollower = tweet.getUser();
            // @todo how to not count commonFollowers in scoring ?
            if(ownerFollowingIds.indexOf(potentialFollower.getId())==-1
                    && followedRecently.indexOf(potentialFollower.getId())==-1
                    && potentialFollowers.indexOf(potentialFollower.getId())==-1) {

                if (potentialFollower.shouldBeFollowed(this.getOwnerName())) {
                    if (follow) {
                        boolean result = this.follow(potentialFollower.getId());
                        if (result) {
                            potentialFollower.setDateOfFollowNow();
                            potentialFollowers.add(potentialFollower);
                            if (saveResults) {
                                this.getIOHelper().addNewFollowerLine(potentialFollower);
                            }
                        }
                    } else {
                        LOGGER.info(()->"potentialFollowers added : " + potentialFollower.getUsername());
                        potentialFollowers.add(potentialFollower);
                    }
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

    private Date getDiffDay(int minus) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, minus);
        return cal.getTime();
    }
}
