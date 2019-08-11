package com.socialMediaRaiser.twitter.impl;

import com.socialMediaRaiser.twitter.AbstractTwitterBot;
import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.Tweet;
import com.socialMediaRaiser.twitter.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Getter
@Setter
public class TwitterBotByLastActivity extends AbstractTwitterBot {

    private List<User> potentialFollowers = new ArrayList<>();
    private int maxFriendship = 390;

    public TwitterBotByLastActivity(String ownerName) {
        super(ownerName);
    }

    @Override
    public List<User> getPotentialFollowers(Long ownerId, int count, boolean follow, boolean saveResults){
        if(count>maxFriendship){
            count = maxFriendship;
        }
        List<Long> ownerFollowingIds = this.getFollowingIds(ownerId);
        List<Long> followedRecently = this.getIOHelper().getPreviouslyFollowedIds();

        List<Tweet> lastTweets = null;
        String startDate = "201907200000";
        String endDate = "201907221000";
        try {
            lastTweets = this.searchForTweets("@"+this.getOwnerName(), 100, new SimpleDateFormat("yyyyMMddHHmm").parse(startDate), new SimpleDateFormat("yyyyMMddHHmm").parse(endDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int iteration=0;
        while(iteration<lastTweets.size() && potentialFollowers.size() < count){
            Tweet tweet = lastTweets.get(iteration);
            User potentialFollower = tweet.getUser();
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
                        System.out.println("potentialFollowers added : " + potentialFollower.getUserName());
                        potentialFollowers.add(potentialFollower);
                    }
                }
            }
            iteration++;
        }


        System.out.println("********************************");
        System.out.println(potentialFollowers.size() + " followers followed / "
                + iteration + " users analyzed");
        System.out.println("********************************");

        return potentialFollowers;
    }

    private Date getDiffDay(int minus) {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, minus);
        return cal.getTime();
    }
}
