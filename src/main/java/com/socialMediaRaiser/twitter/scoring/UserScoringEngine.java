package com.socialMediaRaiser.twitter.scoring;

import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.User;
import com.socialMediaRaiser.twitter.properties.ScoringProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Getter
public class UserScoringEngine {

    private int limit;

    public UserScoringEngine(int minimumPercentMatch){
        if(minimumPercentMatch<=100 && minimumPercentMatch>=0){
            this.limit = FollowProperties.scoringProperties.getTotalMaxPoints()*minimumPercentMatch/100;
        } else{
            System.err.println("argument should be between 0 & 100");
            this.limit = 100;
        }
    }

    public boolean shouldBeFollowed(User user){
        return getUserScore(user) >= limit;
    }

    public int getUserScore(User user){
        System.out.print("User="+user.getUserName());
        FollowProperties.scoringProperties.getProperty(Criterion.NB_FOLLOWERS).setValue(user.getFollowersCount());
        FollowProperties.scoringProperties.getProperty(Criterion.NB_FOLLOWINGS).setValue(user.getFollowingsCount());
        FollowProperties.scoringProperties.getProperty(Criterion.RATIO).setValue(user.getFollowersRatio());
        FollowProperties.scoringProperties.getProperty(Criterion.DESCRIPTION).setValue(user.getDescription());
        FollowProperties.scoringProperties.getProperty(Criterion.LOCATION).setValue(user.getLocation());
        FollowProperties.scoringProperties.getProperty(Criterion.COMMON_FOLLOWERS).setValue(user.getCommonFollowers());
        FollowProperties.scoringProperties.getProperty(Criterion.NB_FAVS).setValue(user.getFavouritesCount());
        FollowProperties.scoringProperties.getProperty(Criterion.NB_TWEETS).setValue(user.getStatusesCount());
        return this.computeScore();
    }

    private int computeScore(){
        int score = 0;
        for(ScoringProperty prop : FollowProperties.scoringProperties.getProperties()){
            if(prop.isActive() && prop.getValue()!=null) {
                // @todo argument casts dirty
                switch (prop.getCriterion()) {
                    case NB_FOLLOWERS:
                        score += getNbFollowersScore((int) prop.getValue());
                        break;
                    case NB_FOLLOWINGS:
                        score += getNbFollowingsScore((int) prop.getValue());
                        break;
                    case RATIO:
                        score += getRatioScore((double) prop.getValue());
                        break;
                    case LAST_UPDATE:
                        score += getLastUpdateScore((Date) prop.getValue());
                        break;
                    case DESCRIPTION:
                        score += getDescriptionScore(prop.getValue().toString());
                        break;
                    case LOCATION:
                        score += getLocationScore(prop.getValue().toString());
                        break;
                    case COMMON_FOLLOWERS:
                        score += getCommonFollowersScore((int) prop.getValue());
                        break;
                }
            }
        }
        System.out.println(" : " + score +"/"+limit);
        return score;
    }

    private int getCommonFollowersScore(int value) {
        int maxPoints = FollowProperties.scoringProperties.getProperty(Criterion.COMMON_FOLLOWERS).getMaxPoints();
        int maxFollow = FollowProperties.targetProperties.getNbBaseFollowers();
        return maxPoints*value/maxFollow;
    }

    private int getNbFollowersScore(int nbFollowers){
        int maxPoints = FollowProperties.scoringProperties.getProperty(Criterion.NB_FOLLOWERS).getMaxPoints();
        if(nbFollowers> FollowProperties.targetProperties.getMinNbFollowers()
                && nbFollowers< FollowProperties.targetProperties.getMaxNbFollowers()){
            return maxPoints;
        }
        return 0;
    }

    private int getNbFollowingsScore(int nbFollowings){
        int maxPoints = FollowProperties.scoringProperties.getProperty(Criterion.NB_FOLLOWINGS).getMaxPoints();
        if(nbFollowings> FollowProperties.targetProperties.getMinNbFollowings()
                && nbFollowings< FollowProperties.targetProperties.getMaxNbFollowings()){
            return maxPoints;
        }
        return 0;
    }

    private int getRatioScore(double ratio){
        int maxPoints = FollowProperties.scoringProperties.getProperty(Criterion.RATIO).getMaxPoints();
        if(ratio> FollowProperties.targetProperties.getMinRatio()
                && ratio< FollowProperties.targetProperties.getMaxRatio()){
            return maxPoints;
        }
        return 0;
    }

    private int getLastUpdateScore(Date lastUpdate){
        int maxPoints = FollowProperties.scoringProperties.getProperty(Criterion.LAST_UPDATE).getMaxPoints();
        Date now = new Date();
        if(lastUpdate!=null){
            long daysSinceLastUpdate = (now.getTime()-lastUpdate.getTime()) / (24 * 60 * 60 * 1000);
            if(daysSinceLastUpdate < FollowProperties.targetProperties.getMaxDaysSinceLastTweet()) {
                return maxPoints;
            }
        }
        return 0;
    }

    private int getDescriptionScore(String description){
        int maxPoints = FollowProperties.scoringProperties.getProperty(Criterion.DESCRIPTION).getMaxPoints();
        String[] words = FollowProperties.targetProperties.getDescription().split(",");
        String[] descriptionSplitted = description.split(" ");
        for(String s :descriptionSplitted){
            if(Arrays.stream(words).anyMatch(s.toLowerCase()::contains)){
                return maxPoints;
            }
        }
        return 0;
    }

    // @todo
    private int getLocationScore(String location){
        int maxPoints = FollowProperties.scoringProperties.getProperty(Criterion.LOCATION).getMaxPoints();
        return maxPoints;
    }
}
