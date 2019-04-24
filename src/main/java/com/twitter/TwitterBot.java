package com.twitter;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Data
public class TwitterBot extends AbstractTwitterBot{

    public List<User> getPotentialFollowersFromFollowerFollowers(String ownerName, int count){
        List<User> potentialFollowers = new ArrayList<>();
        List<User> baseUsers = this.getFollowersUsers(ownerName);
        Collections.shuffle(baseUsers);
        int i=0;
        User user;
        while(i< baseUsers.size() && potentialFollowers.size()<count){
            user = baseUsers.get(i);
            System.out.println("----- Watching followers of " + user.getScreen_name() + " -----");
            List<User> userFollowers = this.getPotentialFollowersFromUserFollowers(ownerName, user.getScreen_name(), count);
            for(User userFollower : userFollowers){
                int indexOf = potentialFollowers.indexOf(userFollower);
                if(indexOf==-1){
                    potentialFollowers.add(userFollower);
                } else{
                    potentialFollowers.get(indexOf).incrementCommonFollowers();
                }
            }
            System.out.println("***********************************");
            System.out.println("current potential followers size : " + potentialFollowers.size());
            System.out.println("***********************************");
            i++;
        }

        return potentialFollowers;
    }

    private List<User> getPotentialFollowersFromUserFollowers(String ownerName, String userName, int count) {
        List<User> potentialFollowers = new ArrayList<>();
        List<User> baseUsers;
        if (this.getUrlHelper().canCallGetFollowers()) {
            baseUsers = this.getFollowersUsers(userName);
        } else {
            return new ArrayList<>();
        }

        int i = 0;
        while (i < baseUsers.size() && potentialFollowers.size() < count) {
            User potentialFollower = baseUsers.get(i);
            Boolean isUserFollowed = true;
            if (potentialFollower.shouldBeFollowed()) {
                isUserFollowed = this.userIsFollowed(ownerName, potentialFollower.getScreen_name());
                if(isUserFollowed==null){
                    try {
                        TimeUnit.MINUTES.sleep(2);
                        this.getUrlHelper().resetQuarterCounters();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (!isUserFollowed) { // add condition
                    System.out.println(potentialFollower.getScreen_name()
                            + " should be followed and is not yet followed -> potentialFollowers");
                    potentialFollowers.add(potentialFollower); // add follow here if condition
                } else{
                    System.out.println(potentialFollower.getScreen_name()
                            + " should be followed but is already followed");
                }
            } else{
                System.out.println(potentialFollower.getScreen_name() + " should not be followed");
            }
            i++;
        }
        System.out.println("*** "
                + potentialFollowers.size() + "/" + baseUsers.size()
                + " saved for followers of " + userName
                + "***");
        return potentialFollowers;
    }

    public List<String> getUsersNotFollowingBack(String userName) {
        List<String> notFollowingsBackUsers = new ArrayList<>();
        User user = this.getUserInfoFromUserName(userName);
        List<Long> followingsId = this.getFollowingsIds(user.getId());
        int i=0;

        while(i<followingsId.size()
                && this.getUrlHelper().canCallFriendship()
                && this.getUrlHelper().canCallGetUser()){
            Long followingId = followingsId.get(i);
            User following = this.getUserInfoFromUserId(followingId);
            if(following.shouldBeFollowed()){
                Boolean areFriend = this.areFriends(userName, following.getScreen_name());
                if(areFriend!=null && !areFriend) {
                    notFollowingsBackUsers.add(following.getScreen_name());
                }
            };
            i++;
        }

        return notFollowingsBackUsers;
    }

    public List<String> getPotentialFollowersFromRetweet(String userName, Long retweetId) {

        List<User> retweeters = this.getRetweetersUsers(retweetId);
        List<String> potentialFollowers = new ArrayList<>();

        int i=0;
        while(i<retweeters.size() && potentialFollowers.size()<this.getUrlHelper().FOLLOW_MAX_CALLS){
            User retweeter = retweeters.get(i);
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
