package com.socialMediaRaiser.twitter.config;

import com.socialMediaRaiser.twitter.User;
import lombok.Data;

import java.util.Date;

@Data
public class UserAnalyser {

    // @todo How to get the maximum amount of points given by all the scoring functions ?
    // @todo How to define common rules for these function (ex: result between 0 & 10) ?
    private static int limit = 7; // @todo this value should be dynamic

    public static boolean shouldBeFollowed(User user){
        int score = 0;
        score+= getNbFollowersScore(user.getFollowersCount());
        score+= getNbFollowingsScore(user.getFollowingCount());
        score+= getRatioScore(user.getFollowersRatio());
        score+= getLastUpdateScore(user.getLastUpdate());
        score+= getLangScore(user.getLang());
        score+= getDescriptionScore(user.getDescription());
        score+= getLocationScore(user.getLocation());
        if(score>=limit){
            return true;
        }
        return false;
    }

    private static int getNbFollowersScore(int nbFollowers){
        if(nbFollowers>FollowParameters.MIN_NB_FOLLOWERS
                && nbFollowers<FollowParameters.MAX_NB_FOLLOWERS){
            return 1;
        }
        return 0;
    }

    private static int getNbFollowingsScore(int nbFollowings){
        if(nbFollowings>FollowParameters.MIN_NB_FOLLOWINGS
                && nbFollowings<FollowParameters.MAX_NB_FOLLOWINGS){
            return 1;
        }
        return 0;
    }

    private static int getRatioScore(double ratio){
        if(ratio>FollowParameters.MIN_RATIO
                && ratio<FollowParameters.MAX_RATIO){
            return 1;
        }
        return 0;
    }

    private static int getLastUpdateScore(Date lastUpdate){
        Date now = new Date();
        if(lastUpdate!=null){
            long daysSinceLastUpdate = (now.getTime()-lastUpdate.getTime()) / (24 * 60 * 60 * 1000);
            if(daysSinceLastUpdate < FollowParameters.MAX_DAYS_SINCE_LAST_TWEET) {
                return 1;
            }
        }
        return 0;
    }

    private static int getLangScore(String lang){
        if(lang!=null && lang.equals(FollowParameters.LANGUAGE)){
            return 1;
        }
        return 0;
    }

    private static int getDescriptionScore(String description){
        return 1;
    }

    private static int getLocationScore(String location){
        return 1;
    }
}
