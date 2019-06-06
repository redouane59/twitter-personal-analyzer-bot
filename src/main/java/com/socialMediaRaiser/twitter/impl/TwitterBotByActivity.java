package com.socialMediaRaiser.twitter.impl;

import com.socialMediaRaiser.twitter.AbstractTwitterBot;
import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.Tweet;
import com.socialMediaRaiser.twitter.User;
import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Data
public class TwitterBotByActivity extends AbstractTwitterBot {

    private List<User> potentialFollowers = new ArrayList<>();
    private int maxFriendship = 390;

    @Override
    public List<User> getPotentialFollowers(Long ownerId, int count, boolean follow, boolean saveResults){
        if(count>maxFriendship){
            count = maxFriendship;
        }
        List<Long> ownerFollowingIds = this.getFollowingIds(ownerId);
        List<Long> followedRecently = this.getIOHelper().getPreviouslyFollowedIds();

        List<Tweet> lastTweets = null; // @todo param
        try {
            lastTweets = this.searchForTweets("@RedTheOne", 200, getDiffDay(-1), new SimpleDateFormat("yyyyMMddHHmm").parse("201906021900"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int iteration=0;
        while(iteration<lastTweets.size() && potentialFollowers.size() < count){
            Tweet tweet = lastTweets.get(iteration);
            User potentialFollower = tweet.getUser();
            // @todo how to not count commonFollowers in scoring ?
            // @todo add indexof potentialFollower to avoid double
            if(ownerFollowingIds.indexOf(potentialFollower.getId())==-1 && followedRecently.indexOf(potentialFollower.getId())==-1) {

                if (potentialFollower.shouldBeFollowed()) {
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
