package com.socialMediaRaiser;

import com.socialMediaRaiser.twitter.helpers.AbstractIOHelper;
import com.socialMediaRaiser.twitter.helpers.dto.getUser.AbstractUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class AbstractBot implements InfoGetter, ActionPerformer  {

    private AbstractIOHelper IOHelper;

    public abstract List<AbstractUser> getPotentialFollowers(String ownerId, int count, boolean follow, boolean saveResults);

    protected LinkedHashMap<String, Boolean> areFriends(String userId, List<String> otherIds, boolean unfollow, boolean writeOnSheet){
        LinkedHashMap<String, Boolean> result = new LinkedHashMap<>();
        int nbUnfollows = 0;
        int nbFollowingBack = 0;
        for(String otherId : otherIds){
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
                    if(nbUnfollows%5==0){
                        try {
                            System.out.println("Sleeping 30sec");
                            TimeUnit.SECONDS.sleep(30);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            if(writeOnSheet){
                if(userFollowsBack!=null){
                    this.getIOHelper().updateFollowBackInformation(otherId, userFollowsBack);
                } else{
                    System.err.println("null value for" + otherId);
                }
            }
            result.put(otherId, userFollowsBack);
            System.out.println();
        }
        System.out.println("Follow back : " + (nbFollowingBack*100)/otherIds.size() + "% (nb unfollows : " + nbUnfollows+ ")");
        return result;
    }
}