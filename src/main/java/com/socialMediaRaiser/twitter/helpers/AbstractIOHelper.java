package com.socialMediaRaiser.twitter.helpers;

import com.socialMediaRaiser.AbstractUser;
import com.socialMediaRaiser.twitter.User;

import java.util.List;
import java.util.Map;

public abstract class AbstractIOHelper {

    public abstract List<Long> getPreviouslyFollowedIds(boolean showFalse, boolean showTrue);
    public abstract void updateFollowBackInformation(Map<Long, Boolean> result); // @todo implementation here ?
    public abstract void updateFollowBackInformation(Long userId, Boolean result);
    public abstract void addNewFollowerLine(AbstractUser user);


        public List<Long> getPreviouslyFollowedIds() {
        return this.getPreviouslyFollowedIds(true, true);
    }

}