package com.socialMediaRaiser;

import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.helpers.AbstractIOHelper;
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

    public abstract List<? extends AbstractUser> getPotentialFollowers(Long ownerId, int count, boolean follow, boolean saveResults);

    public List<? extends AbstractUser> getPotentialFollowers(int count, boolean follow, boolean saveResults){
        AbstractUser user = this.getUserFromUserName(FollowProperties.USER_NAME);
        return this.getPotentialFollowers(user.getId(), count, follow, saveResults);
    }

    protected LinkedHashMap<Long, Boolean> areFriends(Long userId, List<Long> otherIds, boolean unfollow, boolean writeOnSheet){
        LinkedHashMap<Long, Boolean> result = new LinkedHashMap<>();
        int nbUnfollows = 0;
        for(Long otherId : otherIds){
            Boolean userFollowsBack = null;
            RelationType relation = this.getRelationType(userId, otherId);
            if(relation == RelationType.FRIENDS || relation == RelationType.FOLLOWER){
                userFollowsBack = true;
            } else if (relation!=null){
                userFollowsBack = false;
            }
            if(unfollow && !userFollowsBack && relation == RelationType.FOLLOWING){
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
        System.out.println("Followback : " + ((otherIds.size()-nbUnfollows)*100)/otherIds.size() + "%");
        return result;
    }
}