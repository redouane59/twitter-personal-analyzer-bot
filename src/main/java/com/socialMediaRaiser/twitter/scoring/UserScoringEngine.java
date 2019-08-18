package com.socialMediaRaiser.twitter.scoring;

import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.User;
import com.socialMediaRaiser.twitter.properties.ScoringProperty;
import lombok.Getter;

import java.util.*;

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
        int score = getUserScore(user);
        System.out.println("score of " + score + "/"+limit+ " for " + user.getUserName());
        return score >= limit;
    }

    public int getUserScore(User user){
        FollowProperties.scoringProperties.getProperty(Criterion.NB_FOLLOWERS).setValue(user.getFollowersCount());
        FollowProperties.scoringProperties.getProperty(Criterion.NB_FOLLOWINGS).setValue(user.getFollowingsCount());
        FollowProperties.scoringProperties.getProperty(Criterion.RATIO).setValue(user.getFollowersRatio());
        String description = user.getDescription();
        // @odo only if public account
        if(user.getMostRecentTweet()!=null
                && user.getMostRecentTweet().size()>0
                && !user.isProtectedAccount()){ // adding the last tweet to description
            description.concat(user.getMostRecentTweet().get(0).getText());
        }
        FollowProperties.scoringProperties.getProperty(Criterion.DESCRIPTION).setValue(description);
        FollowProperties.scoringProperties.getProperty(Criterion.LOCATION).setValue(user.getLocation());
        FollowProperties.scoringProperties.getProperty(Criterion.COMMON_FOLLOWERS).setValue(user.getCommonFollowers());
        FollowProperties.scoringProperties.getProperty(Criterion.NB_FAVS).setValue(user.getFavouritesCount());
        FollowProperties.scoringProperties.getProperty(Criterion.NB_TWEETS).setValue(user.getStatusesCount());
        FollowProperties.scoringProperties.getProperty(Criterion.LAST_UPDATE).setValue(user.getLastUpdate());
        return this.computeScore();
    }

    private int computeScore(){
        int score = 0;
        for(ScoringProperty prop : FollowProperties.scoringProperties.getProperties()){
            if(prop.isActive() && prop.getValue()!=null) {
                int modifValue = 0;
                switch (prop.getCriterion()) {
                    case NB_FOLLOWERS:
                        modifValue = getNbFollowersScore((int) prop.getValue());
                        break;
                    case NB_FOLLOWINGS:
                        modifValue = getNbFollowingsScore((int) prop.getValue());
                        break;
                    case RATIO:
                        modifValue = getRatioScore((double) prop.getValue());
                        break;
                    case LAST_UPDATE:
                        modifValue = getLastUpdateScore((Date) prop.getValue());
                        break;
                    case DESCRIPTION:
                        modifValue = getDescriptionScore(prop.getValue().toString());
                        break;
                    case LOCATION:
                        modifValue = getLocationScore(prop.getValue().toString());
                        break;
                    case COMMON_FOLLOWERS:
                        modifValue = getCommonFollowersScore((int) prop.getValue());
                        break;
                    default:
                        System.err.println("no function found for " + prop.getCriterion());
                }
                if(modifValue == 0 && prop.isBlocking()){
                    return 0;
                }
                score += modifValue;
            }
        }
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
        int maxDays = FollowProperties.targetProperties.getMaxDaysSinceLastTweet();
        if(maxDays<=0){
            maxDays=1;
        }
        Date now = new Date();
        if(lastUpdate!=null){
            int daysSinceLastUpdate = (int)((now.getTime()-lastUpdate.getTime()) / (24 * 60 * 60 * 1000));
            if(daysSinceLastUpdate < maxDays) {
                return maxPoints * (maxDays-daysSinceLastUpdate)/maxDays;
            }
        }
        return 0;
    }

    // @todo add last tweets
    private int getDescriptionScore(String description){
        int maxPoints = FollowProperties.scoringProperties.getProperty(Criterion.DESCRIPTION).getMaxPoints();
        String[] words = FollowProperties.targetProperties.getDescription().split(FollowProperties.ARRAY_SEPARATOR);
        String[] descriptionSplitted = description.split(" ");
        for(String s :descriptionSplitted){
            if(Arrays.stream(words).anyMatch(s.toLowerCase()::contains)){
                System.out.println("match description with '" + s +"'");
                return maxPoints;
            }
        }
        return 0;
    }



    // @todo to test
    private int getLocationScore(String location){
        int maxPoints = FollowProperties.scoringProperties.getProperty(Criterion.LOCATION).getMaxPoints();
        String targetLocation = FollowProperties.targetProperties.getLocation();
        if(targetLocation==null){
            return maxPoints;
        } else{
            String[] words = targetLocation.split(FollowProperties.ARRAY_SEPARATOR);
            if (Arrays.asList(words).contains(location)){
                return maxPoints;
            }
            return 0;
        }
    }
}
