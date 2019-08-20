package com.socialMediaRaiser.twitter.impl;

import com.socialMediaRaiser.twitter.AbstractTwitterBot;
import com.socialMediaRaiser.twitter.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// follows followers of randomly selected followers
@Getter
@Setter
public class BasicTwitterBotByRelations extends AbstractTwitterBot {

    private List<User> potentialFollowers = new ArrayList<>();

    public BasicTwitterBotByRelations(String ownerName) {
        super(ownerName);
    }

    @Override
    public List<User> getPotentialFollowers(String ownerId, int count, boolean follow, boolean saveResult){
        List<User> ownerFollowers = this.getFollowerUsers(ownerId); // criticity here (15/15min)
        List<String> ownerFollowingIds = this.getFollowingIds(ownerId);
        Collections.shuffle(ownerFollowers);

        int i=0;
        User user;
        while(i< ownerFollowers.size() && potentialFollowers.size()<count){
            user = ownerFollowers.get(i);
            if(user.shouldBeTakenForItsFollowers()){
                this.addPotentialFollowersFromUserFollowers(user.getUserName(), ownerFollowingIds, count, follow);
            }
            i++;
        }
        potentialFollowers.sort(Comparator.comparing(User::getCommonFollowers).reversed());
        return potentialFollowers;
    }

    private void addPotentialFollowersFromUserFollowers(String userName, List<String> ownerFollowingIds, int count, boolean follow) {
        long startWorkingTime = System.currentTimeMillis();
        List<User> followerFollowers = this.getFollowerUsers(userName); // criticity here (15/15min)
        System.out.println("----- Watching followers of " + userName + "(" + followerFollowers.size() + ") -----");
        int nbUsersAdded = 0;
        int i = 0;
        while (i < followerFollowers.size() && potentialFollowers.size()<count) {
            User potentialFollower = followerFollowers.get(i);
            if (potentialFollower.shouldBeFollowed(this.getOwnerName())
            && ownerFollowingIds.indexOf(potentialFollower.getId())==-1) { // user not already followed
                    int indexInPotentialFollowersList = potentialFollowers.indexOf(potentialFollower);
                    if(indexInPotentialFollowersList==-1){ // not already found
                        nbUsersAdded++;
                        if(follow){
                            potentialFollower.setDateOfFollowNow();
                            boolean result = this.follow(potentialFollower.getId());
                            if(result){
                                potentialFollowers.add(potentialFollower);
                            }
                        } else{
                            System.out.println("potentialFollowers.add : " + potentialFollower.getUserName());
                            potentialFollowers.add(potentialFollower);
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

}
