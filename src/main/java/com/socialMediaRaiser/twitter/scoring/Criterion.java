package com.socialMediaRaiser.twitter.scoring;

import com.socialMediaRaiser.twitter.FollowProperties;
import lombok.Getter;

@Getter
public enum Criterion {

    NB_FOLLOWERS(FollowProperties.getIntProperty(FollowProperties.SCORING_MAX_POINTS_NB_FOLLOWERS), FollowProperties.getBooleanProperty(FollowProperties.SCORING_IS_ACTIVE_NB_FOLLOWERS)),
    NB_FOLLOWINGS(FollowProperties.getIntProperty(FollowProperties.SCORING_MAX_POINTS_NB_FOLLOWINGS), FollowProperties.getBooleanProperty(FollowProperties.SCORING_IS_ACTIVE_NB_FOLLOWINGS)),
    RATIO(FollowProperties.getIntProperty(FollowProperties.SCORING_MAX_POINTS_RATIO), FollowProperties.getBooleanProperty(FollowProperties.SCORING_IS_ACTIVE_RATIO)),
    LAST_UPDATE(FollowProperties.getIntProperty(FollowProperties.SCORING_MAX_POINTS_LAST_UPDATE), FollowProperties.getBooleanProperty(FollowProperties.SCORING_IS_ACTIVE_LAST_UPDATE)),
    DESCRIPTION(FollowProperties.getIntProperty(FollowProperties.SCORING_MAX_POINTS_DESCRIPTION), FollowProperties.getBooleanProperty(FollowProperties.SCORING_IS_ACTIVE_DESCRIPTION)),
    LOCATION(FollowProperties.getIntProperty(FollowProperties.SCORING_MAX_POINTS_LOCATION), FollowProperties.getBooleanProperty(FollowProperties.SCORING_IS_ACTIVE_LOCATION)),
    COMMON_FOLLOWERS(FollowProperties.getIntProperty(FollowProperties.SCORING_MAX_POINTS_COMMON_FOLLOWERS), FollowProperties.getBooleanProperty(FollowProperties.SCORING_IS_ACTIVE_COMMON_FOLLOWERS));

    private int maxPoints;
    private boolean active;

    Criterion(int maxPoints, boolean active){
        this.maxPoints = maxPoints;
        this.active = active;
    }

    public static int getTotalMaxPoints(){
        int sum = 0;
        for (Criterion p : Criterion.values()) {
            if(p.isActive()){
                sum += p.getMaxPoints();
            }
        }
        return sum;
    }
}
