package com.socialMediaRaiser.twitter;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Data
public class TwitterBot extends AbstractTwitterBot{

    // @TODO find something to manage count correctly
    public List<User> searchPotentialFollowersFromFollowerFollowers(String ownerName, int count, boolean follow){
        List<User> potentialFollowers = new ArrayList<>();
        List<User> ownerFollowers = this.getFollowerUsers(ownerName);
        List<Long> ownerFollowingIds = this.getFollowingIdsByName(ownerName);
        Collections.shuffle(ownerFollowers);

        int i=0;
        User user;
        while(i< ownerFollowers.size() && potentialFollowers.size()<count){
            long startWorkingTime = System.currentTimeMillis();
            user = ownerFollowers.get(i);
            System.out.println("----- Watching followers of " + user.getScreen_name() + " -----");
            List<User> usersToFollow = this.searchPotentialFollowersFromUserFollowers(ownerName, ownerFollowingIds, user.getScreen_name(), count, follow);
            for(User followerFollower : usersToFollow){
                int indexOf = potentialFollowers.indexOf(followerFollower);
                if(indexOf==-1){
                    potentialFollowers.add(followerFollower);
                } else{
                    potentialFollowers.get(indexOf).incrementCommonFollowers();
                }
            }
            long stopWorkingTime = System.currentTimeMillis();

            System.out.println("***********************************");
            System.out.println(usersToFollow.size() + " users found from "
                    + user.getScreen_name() + " followers in " + (stopWorkingTime-startWorkingTime)/(float)1000 + " s");
            System.out.println("Total = " + potentialFollowers.size());
            System.out.println("***********************************");
            i++;
        }
        potentialFollowers.sort(Comparator.comparing(User::getCommonFollowers).reversed());
        return potentialFollowers;
    }

    private List<User> searchPotentialFollowersFromUserFollowers(String ownerName, List<Long> ownerFollowingIds, String userName, int count, boolean follow) {

        List<User> potentialFollowers = new ArrayList<>();
        List<User> followerFollowers = this.getFollowerUsers(userName); // each 15min

        int i = 0;
        while (i < followerFollowers.size()) {
            User potentialFollower = followerFollowers.get(i);
            if (potentialFollower.shouldBeFollowed()) {
                if (ownerFollowingIds.indexOf(potentialFollower.getId())==-1) {
                    potentialFollowers.add(potentialFollower);
                    if(follow){ // @TODO add condition not already followed
                        LocalDateTime now = LocalDateTime.now();
                        potentialFollower.setDateOfFollow(
                                now.getDayOfMonth()+"/"+now.getMonthValue()
                                +" "+now.getHour()+":"+now.getMinute());
                        this.follow(potentialFollower.getScreen_name());
                    }
                }
            }
            i++;
        }
        return potentialFollowers;
    }

    public List<String> getUsersNotFollowingBack(String userName, Boolean unfollow) {
        List<String> notFollowingsBackUsers = new ArrayList<>();
        User user = this.getUserInfoFromUserName(userName);
        List<Long> followingsId = this.getFollowingIds(user.getId());
        int i=0;

        while(i<followingsId.size()){
            Long followingId = followingsId.get(i);
            User following = this.getUserInfoFromUserId(followingId);
            if(following.shouldBeFollowed()){
                Boolean areFriend = this.areFriends(userName, following.getScreen_name());
                if(areFriend!=null && !areFriend) {
                    notFollowingsBackUsers.add(following.getScreen_name());
                    if(unfollow){
                        this.unfollow(following.getScreen_name());
                    }
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
            if(retweeter.shouldBeFollowed()
                    &&!this.userIsFollowed(userName, retweeter.getScreen_name())){
                potentialFollowers.add(retweeter.getScreen_name());
            }

            i++;
        }

        return potentialFollowers;
    }


}
