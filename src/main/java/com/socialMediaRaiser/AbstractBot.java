package com.socialMediaRaiser;

import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.helpers.AbstractIOHelper;
import com.socialMediaRaiser.twitter.helpers.dto.getUser.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class AbstractBot implements InfoGetter, ActionPerformer  {

    private AbstractIOHelper IOHelper;

    public abstract List<? extends UserDTO> getPotentialFollowers(Long ownerId, int count, boolean follow, boolean saveResults);

    public List<? extends UserDTO> getPotentialFollowers(String ownerName, int count, boolean follow, boolean saveResults){
        UserDTO user = this.getUserFromUserName(ownerName);
        return this.getPotentialFollowers(user.getId(), count, follow, saveResults);
    }

    protected LinkedHashMap<Long, Boolean> areFriends(Long userId, List<Long> otherIds, boolean unfollow, boolean writeOnSheet){
        LinkedHashMap<Long, Boolean> result = new LinkedHashMap<>();
        int nbUnfollows = 0;
        int nbFollowingBack = 0;
        for(Long otherId : otherIds){
            Boolean userFollowsBack = null;
            RelationType relation = this.getRelationType(userId, otherId);
            if(relation == RelationType.FRIENDS || relation == RelationType.FOLLOWER){
                userFollowsBack = true;
                nbFollowingBack++;
            } else if (relation!=null){
                userFollowsBack = false;
            }
            if(unfollow && relation !=null && !userFollowsBack && relation == RelationType.FOLLOWING){
                boolean unfollowResult = this.unfollow(otherId);
                if(unfollowResult) {
                    nbUnfollows++;
                }
            }
            if(writeOnSheet){
                this.getIOHelper().updateFollowBackInformation(otherId, userFollowsBack);
            }
            result.put(otherId, userFollowsBack);
            System.out.println();
        }
        System.out.println("Follow back : " + (nbFollowingBack*100)/otherIds.size() + "% (nb unfollows : " + nbUnfollows+ ")");
        return result;
    }
}