package com.socialMediaRaiser.twitter.impl;

import com.socialMediaRaiser.twitter.AbstractTwitterBot;

import com.socialMediaRaiser.twitter.helpers.dto.getUser.UserDTO;
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

    private List<UserDTO> potentialFollowers = new ArrayList<>();

    public BasicTwitterBotByRelations(String ownerName) {
        super(ownerName);
    }

    @Override
    public List<UserDTO> getPotentialFollowers(Long ownerId, int count, boolean follow, boolean saveResult){
        List<UserDTO> ownerFollowers = this.getFollowerUsers(ownerId); // criticity here (15/15min)
        List<Long> ownerFollowingIds = this.getFollowingIds(ownerId);
        Collections.shuffle(ownerFollowers);

        int i=0;
        UserDTO user;
        while(i< ownerFollowers.size() && potentialFollowers.size()<count){
            user = ownerFollowers.get(i);
            if(user.shouldBeTakenForItsFollowers()){
                this.addPotentialFollowersFromUserFollowers(user.getUsername(), ownerFollowingIds, count, follow);
            }
            i++;
        }
        potentialFollowers.sort(Comparator.comparing(UserDTO::getCommonFollowers).reversed());
        return potentialFollowers;
    }

    private void addPotentialFollowersFromUserFollowers(String userName, List<Long> ownerFollowingIds, int count, boolean follow) {
        long startWorkingTime = System.currentTimeMillis();
        List<UserDTO> followerFollowers = this.getFollowerUsers(userName); // criticity here (15/15min)
        System.out.println("----- Watching followers of " + userName + "(" + followerFollowers.size() + ") -----");
        int nbUsersAdded = 0;
        int i = 0;
        while (i < followerFollowers.size() && potentialFollowers.size()<count) {
            UserDTO potentialFollower = followerFollowers.get(i);
            if (potentialFollower.shouldBeFollowed(this.getOwnerName())
            && ownerFollowingIds.indexOf(potentialFollower.getId())==-1) { // user not already followed
                    int indexInPotentialFollowersList = potentialFollowers.indexOf(potentialFollower);
                    if(indexInPotentialFollowersList==-1){ // not already found
                        nbUsersAdded++;
                        if(follow){
                            potentialFollower.setDateOfFollowNow();
                            boolean result = this.follow(potentialFollower.getUsername());
                            if(result){
                                potentialFollowers.add(potentialFollower);
                            }
                        } else{
                            System.out.println("potentialFollowers.add : " + potentialFollower.getUsername());
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
