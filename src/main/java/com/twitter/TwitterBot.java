package com.twitter;

import com.RelationType;
import com.twitter.helpers.URLHelper;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
public class TwitterBot extends AbstractTwitterBot{



    public List<String> getPotentialFollowersFromRelatives(String tweetName, RelationType relationLevel1, RelationType relationLevel2){
        List<String> potentialFollowers = new ArrayList<>();
        try {
            List<TwitterUser> baseTwitterUsers = new ArrayList<>();
            if(relationLevel1 == RelationType.FOLLOWER){
                baseTwitterUsers = this.getFollowersUsers(tweetName);
            } else if (relationLevel1 == RelationType.FOLLOWING){
                baseTwitterUsers = this.getFollowingsUserList(tweetName);
            }
            Collections.shuffle(baseTwitterUsers);
            potentialFollowers = this.getPotentialFollowersFromUserList(baseTwitterUsers, tweetName, relationLevel2);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return potentialFollowers;
    }

    private List<String> getPotentialFollowersFromUserList(List<TwitterUser> twitterUsers, String userName, RelationType relationType) throws IllegalAccessException{
        List<String> potentialFollowers = new ArrayList<>();
        int i=0;
        TwitterUser twitterUser;
        while(i< twitterUsers.size() && potentialFollowers.size()<URLHelper.FOLLOW_MAX_CALLS){
            twitterUser = twitterUsers.get(i);
            potentialFollowers.addAll(this.getPotentialFollowersFromUser(twitterUser.getScreen_name(), userName, relationType));
            i++;
        }
        return potentialFollowers;
    }

    private List<String> getPotentialFollowersFromUser(String otherUserName, String userName, RelationType relationType) throws IllegalAccessException {
        List<String> potentialFollowers = new ArrayList<>();
        List<TwitterUser> baseTwitterUsers = new ArrayList<>();
        if(relationType == RelationType.FOLLOWER){
            if(this.getUrlHelper().canCallGetFollowers()){
                baseTwitterUsers = this.getFollowersUsers(otherUserName);
            } else{
                return new ArrayList<>();
            }
        } else if(relationType == RelationType.FOLLOWING){
            if(this.getUrlHelper().canCallGetFollowings()){
                baseTwitterUsers = this.getFollowingsUserList(otherUserName);
            } else{
                return new ArrayList<>();
            }
        }

        int i = 0;
        while(i< baseTwitterUsers.size()){
            TwitterUser potentialFollower = baseTwitterUsers.get(i);
            if(this.getUrlHelper().canCallFriendship()){
                if(potentialFollower.shouldBeFollowed()
                        &&!this.userIsFollowed(userName, potentialFollower.getScreen_name())) {
                    potentialFollowers.add(potentialFollower.getScreen_name());
                }
            } else{
                try {
                    TimeUnit.MINUTES.sleep(15);
                    this.getUrlHelper().resetQuarterCounters();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            i++;
        }

        return potentialFollowers;
    }

    public List<String> getUsersNotFollowingBack(String userName) throws IllegalAccessException {
        List<String> notFollowingsBackUsers = new ArrayList<>();
        List<TwitterUser> followings = this.getFollowingsUserList(userName);
        for(TwitterUser following : followings){
            if(this.getUrlHelper().canCallFriendship() && !this.areFriends(userName, following.getScreen_name()) && following.shouldBeFollowed()){
                notFollowingsBackUsers.add(following.getScreen_name());
            };
        }
        return notFollowingsBackUsers;
    }



    public List<String> getPotentialFollowersFromRetweet(String userName, Long retweetId) throws IllegalAccessException {

        List<TwitterUser> retweeters = this.getRetweetersUsers(retweetId);
        List<String> potentialFollowers = new ArrayList<>();

        int i=0;
        while(i<retweeters.size() && potentialFollowers.size()<this.getUrlHelper().FOLLOW_MAX_CALLS){
            TwitterUser retweeter = retweeters.get(i);
            if(this.getUrlHelper().canCallFriendship()){
                if(retweeter.shouldBeFollowed()
                        &&!this.userIsFollowed(userName, retweeter.getScreen_name())){
                    potentialFollowers.add(retweeter.getScreen_name());
                }
            } else{
                try {
                    TimeUnit.MINUTES.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.getUrlHelper().resetQuarterCounters();
            }
            i++;
        }


        return potentialFollowers;
    }

}
