package com.socialmediaraiser.core;

import com.socialmediaraiser.core.twitter.helpers.AbstractIOHelper;
import com.socialmediaraiser.core.twitter.helpers.dto.getuser.AbstractUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@AllArgsConstructor
@NoArgsConstructor
@Data
public abstract class AbstractBot implements InfoGetter, ActionPerformer {

    private AbstractIOHelper ioHelper;
    private static final Logger LOGGER = Logger.getLogger(AbstractBot.class.getName());

    public abstract List<AbstractUser> getPotentialFollowers(String ownerId, int count);

    protected LinkedHashMap<String, Boolean> areFriends(String userId, List<String> otherIds, boolean unfollow, boolean writeOnSheet){
        LinkedHashMap<String, Boolean> result = new LinkedHashMap<>();
        int nbUnfollows = 0;
        for(String otherId : otherIds){
            boolean userShouldBeUnfollowed = shouldBeUnfollowed(userId, otherId, writeOnSheet);
            if(unfollow && userShouldBeUnfollowed){
                boolean unfollowResult = this.unfollow(otherId);
                if(unfollowResult) {
                    nbUnfollows++;
                    this.temporiseUnfollows(nbUnfollows);
                }
            }
            result.put(otherId, !userShouldBeUnfollowed);
        }
        LOGGER.info("Follow back : " + ((otherIds.size()-nbUnfollows)*100)/otherIds.size() + "% (nb unfollows : " + nbUnfollows+ ")");
        return result;
    }

    private boolean shouldBeUnfollowed(String userId, String otherId, boolean writeOnSheet){
        RelationType relation = this.getRelationType(userId, otherId);
        if(relation==null) LOGGER.severe(() -> "null value for" + otherId);

        Boolean userFollowsBack = false;
        if(relation == RelationType.FRIENDS || relation == RelationType.FOLLOWER){
            userFollowsBack = true;
        }

        if(writeOnSheet){
            this.getIoHelper().updateFollowBackInformation(otherId, userFollowsBack);
        }

        return !userFollowsBack && relation == RelationType.FOLLOWING;
    }

    private void temporiseUnfollows(int nbUnfollows){
        if(nbUnfollows%5==0){
            try {
                LOGGER.info(()->"Sleeping 5sec");
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                LOGGER.severe(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

}