package com.twitter;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Data
public class TwitterBot extends AbstractTwitterBot{

    public List<User> searchPotentialFollowersFromFollowerFollowers(String ownerName, int count, boolean follow){
        List<User> potentialFollowers = new ArrayList<>();
        List<User> baseUsers = this.getFollowersUsers(ownerName);
        Collections.shuffle(baseUsers);
        int i=0;
        User user;
        while(i< baseUsers.size() && potentialFollowers.size()<count){
            user = baseUsers.get(i);
            System.out.println("----- Watching followers of " + user.getScreen_name() + " -----");
            List<User> userFollowers = this.searchPotentialFollowersFromUserFollowers(ownerName, user.getScreen_name(), count, follow);
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
        potentialFollowers.sort(Comparator.comparing(User::getCommonFollowers).reversed());
        return potentialFollowers;
    }

    private List<User> searchPotentialFollowersFromUserFollowers(String ownerName, String userName, int count, boolean follow) {
        List<User> potentialFollowers = new ArrayList<>();
        List<User> baseUsers = this.getFollowersUsers(userName);

        int i = 0;
        while (i < baseUsers.size() && potentialFollowers.size() < count) {
            User potentialFollower = baseUsers.get(i);
            if (potentialFollower.shouldBeFollowed()) {
                Boolean isUserFollowed = this.userIsFollowed(ownerName, potentialFollower.getScreen_name());
                if (!isUserFollowed) {
                    System.out.println(potentialFollower.getScreen_name()
                            + " should be followed and is not yet followed -> potentialFollowers");
                    potentialFollowers.add(potentialFollower); // add follow here if condition
                    if(follow){
                        LocalDateTime now = LocalDateTime.now();
                        potentialFollower.setHourOfFollow(
                                now.getDayOfMonth()+"/"+now.getMonthValue()
                                +" "+now.getHour()+":"+now.getMinute());
                        this.follow(potentialFollower.getScreen_name());
                    }
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

    public List<String> getUsersNotFollowingBack(String userName, Boolean unfollow) {
        List<String> notFollowingsBackUsers = new ArrayList<>();
        User user = this.getUserInfoFromUserName(userName);
        List<Long> followingsId = this.getFollowingsIds(user.getId());
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
