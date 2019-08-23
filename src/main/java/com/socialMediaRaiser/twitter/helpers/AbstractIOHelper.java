package com.socialMediaRaiser.twitter.helpers;

import com.socialMediaRaiser.twitter.helpers.dto.getUser.AbstractUser;

import java.util.Date;
import java.util.List;

public abstract class AbstractIOHelper {

    public static final String DATE_FORMAT = "yyyy/MM/dd HH:mm";

    public List<String> getPreviouslyFollowedIds(boolean showFalse, boolean showTrue){
        return this.getPreviouslyFollowedIds(showFalse, showTrue, null);
    }

    public List<String> getPreviouslyFollowedIds() {
        return this.getPreviouslyFollowedIds(true, true);
    }

    public abstract List<String> getPreviouslyFollowedIds(boolean showFalse, boolean showTrue, Date date);
    public abstract void updateFollowBackInformation(String userId, Boolean result);
    public abstract void addNewFollowerLine(AbstractUser user);

}