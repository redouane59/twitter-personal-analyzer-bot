package com.socialMediaRaiser.twitter.impl;

import com.socialMediaRaiser.twitter.AbstractTwitterBot;
import com.socialMediaRaiser.twitter.User;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// follows user who have RT a given tweet
@Data
public class BasicTwitterBotByRetweeters extends AbstractTwitterBot {

    private List<User> potentialFollowers = new ArrayList<>();

    // @todo KO
    public List<User> getPotentialFollowers(Long ownerId, int count, boolean follow) {
        return null;
    }

    public List<String> getPotentialFollowers(String userName, Long retweetId) {

        List<User> retweeters = this.getRetweetersUsers(retweetId);
        List<String> potentialFollowers = new ArrayList<>();

        int i=0;
        while(i<retweeters.size() && potentialFollowers.size()<this.getUrlHelper().FOLLOW_MAX_CALLS){
            User retweeter = retweeters.get(i);
            if(retweeter.shouldBeFollowed()
                    &&!this.isUserFollowed(userName, retweeter.getUserName())){
                potentialFollowers.add(retweeter.getUserName());
            }

            i++;
        }

        return potentialFollowers;
    }

}
