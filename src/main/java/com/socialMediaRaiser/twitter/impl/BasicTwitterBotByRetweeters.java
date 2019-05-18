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
    private Long retweetId = 12345L;
    private boolean saveResults = true; // @todo in arguments ?


    // @todo KO
    public List<User> getPotentialFollowers(Long userId, int count, boolean follow) {
        List<User> potentialFollowers = new ArrayList<>();
        List<Long> followedRecently = this.getIOHelper().getPreviouslyFollowedIds();
        List<Long> ownerFollowingIds = this.getFollowingIds(userId);
        List<Long> retweetersIds = this.getRetweetersId(retweetId);

        int i=0;
        while(i<retweetersIds.size() && potentialFollowers.size()<this.getUrlHelper().FOLLOW_MAX_CALLS){
            Long retweeterId = retweetersIds.get(i);
            if(ownerFollowingIds.indexOf(retweeterId)==-1
                    && followedRecently.indexOf(retweeterId)==-1){
                User retweeter = this.getUserFromUserId(retweeterId);
                if(retweeter.shouldBeFollowed()){
                    potentialFollowers.add(retweeter);
                    if(follow){
                        this.follow(retweeterId);
                    }
                    if(saveResults){
                        this.getIOHelper().addNewFollowerLine(retweeter);
                    }
                }
            }
            i++;
        }
        return potentialFollowers;
    }


    @Override
    protected List<Long> getFollowedRecently() {
        return null;
    }
}
