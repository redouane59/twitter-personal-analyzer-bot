package com.socialMediaRaiser.twitter.scoring;

import lombok.Getter;

@Getter
public enum Criterion {

    NB_FOLLOWERS(10, true),
    NB_FOLLOWINGS(10, true),
    RATIO(10, true),
    LAST_UPDATE(10, true),
    DESCRIPTION(10, true),
    LOCATION(10, false),
    COMMON_FOLLOWERS(10, true);

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
