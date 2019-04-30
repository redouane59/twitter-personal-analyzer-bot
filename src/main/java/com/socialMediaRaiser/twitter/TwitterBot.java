package com.socialMediaRaiser.twitter;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Data
public class TwitterBot extends AbstractTwitterBot{

    private List<User> potentialFollowers = new ArrayList<>();

    public List<User> searchPotentialFollowersFromFollowerFollowers(String userName, int count, boolean follow){
        User user = this.getUserFromUserName(userName);
        return this.searchPotentialFollowersFromFollowerFollowers(user.getId(), count, follow);
    }

    public List<User> searchPotentialFollowersFromFollowerFollowers(Long ownerId, int count, boolean follow){
        List<User> ownerFollowers = this.getFollowerUsers(ownerId); // @Todo criticity here (15/15min)
        List<Long> ownerFollowingIds = this.getFollowingIds(ownerId);
        Collections.shuffle(ownerFollowers);

        int i=0;
        User user;
        while(i< ownerFollowers.size() && potentialFollowers.size()<count){
            user = ownerFollowers.get(i);
            if(user.shouldBeTakenForItsFollowers()){
                this.addPotentialFollowersFromUserFollowers(user.getScreen_name(), ownerFollowingIds, count, follow);
            }
            i++;
        }
        potentialFollowers.sort(Comparator.comparing(User::getCommonFollowers).reversed());
        return potentialFollowers;
    }

    private void addPotentialFollowersFromUserFollowers(String userName, List<Long> ownerFollowingIds, int count, boolean follow) {
        long startWorkingTime = System.currentTimeMillis();
        List<User> followerFollowers = this.getFollowerUsers(userName, potentialFollowers.size()); //@TODO criticity here (15/15min)
        System.out.println("----- Watching followers of " + userName + "(" + followerFollowers.size() + ") -----");
        int nbUsersAdded = 0;
        int i = 0;
        while (i < followerFollowers.size() && potentialFollowers.size()<count) {
            User potentialFollower = followerFollowers.get(i);
            if (potentialFollower.shouldBeFollowed()
            && ownerFollowingIds.indexOf(potentialFollower.getId())==-1) { // user not already followed
                    int indexInPotentialFollowersList = potentialFollowers.indexOf(potentialFollower);
                    if(indexInPotentialFollowersList==-1){ // not already found
                        potentialFollowers.add(potentialFollower);
                        nbUsersAdded++;
                        if(follow){
                            LocalDateTime now = LocalDateTime.now();
                            potentialFollower.setDateOfFollow(now.getDayOfMonth()+"/"+now.getMonthValue()
                                            +" "+now.getHour()+":"+now.getMinute());
                            this.follow(potentialFollower.getScreen_name());
                        } else{
                            System.out.println("potentialFollowers.add : " + potentialFollower.getScreen_name());
                        }
                    } else{
                        potentialFollowers.get(indexInPotentialFollowersList).incrementCommonFollowers();
                    }
            }
            i++;
        }

        long stopWorkingTime = System.currentTimeMillis();
        System.out.println("***********************************");
        System.out.println(nbUsersAdded + " users added from "
                + userName + " followers in " + (stopWorkingTime-startWorkingTime)/(float)1000 + " s");
        System.out.println("Total = " + potentialFollowers.size());
        System.out.println("***********************************");
    }

    public List<String> getUsersNotFollowingBack(String userName, Boolean unfollow) {
        List<String> notFollowingsBackUsers = new ArrayList<>();
        User user = this.getUserFromUserName(userName);
        List<Long> followingsId = this.getFollowingIds(user.getId());
        int i=0;

        while(i<followingsId.size()){
            Long followingId = followingsId.get(i);
            User following = this.getUserFromUserId(followingId);
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
