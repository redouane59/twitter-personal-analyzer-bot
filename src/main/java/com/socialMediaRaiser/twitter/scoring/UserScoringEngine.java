package com.socialMediaRaiser.twitter.scoring;

import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Getter
public class UserScoringEngine {

    private int limit;
    private List<ScoringParameter> parameters = new ArrayList<>();

    public UserScoringEngine(int minimumPercentMatch){
        if(minimumPercentMatch<=100 && minimumPercentMatch>=0){
            this.limit = Criterion.getTotalMaxPoints()*minimumPercentMatch/100;
        } else{
            System.err.println("argument should be between 0 & 100");
            this.limit = 100;
        }
    }

    public boolean shouldBeFollowed(User user){
        return getUserScore(user) >= limit;
    }

    public int getUserScore(User user){
        this.buildScoringParameters(user);
        System.out.print(user.getUserName());
        return this.computeScore();
    }

    private void buildScoringParameters(User user){
        this.parameters = new ArrayList<>();
        this.parameters.add(new ScoringParameter(Criterion.NB_FOLLOWERS, user.getFollowersCount()));
        this.parameters.add(new ScoringParameter(Criterion.NB_FOLLOWINGS, user.getFollowingsCount()));
        this.parameters.add(new ScoringParameter(Criterion.RATIO, user.getFollowersRatio()));
        this.parameters.add(new ScoringParameter(Criterion.LAST_UPDATE, user.getLastUpdate()));
        this.parameters.add(new ScoringParameter(Criterion.DESCRIPTION, user.getDescription()));
        this.parameters.add(new ScoringParameter(Criterion.LOCATION, user.getLocation()));
        this.parameters.add(new ScoringParameter(Criterion.COMMON_FOLLOWERS, user.getCommonFollowers()));
    }

    private int computeScore(){
        int score = 0;
        for(ScoringParameter parameter : parameters){
            if(parameter.getCriterion().isActive() && parameter.getValue()!=null) {
                // @todo argument casts dirty
                switch (parameter.getCriterion()) {
                    case NB_FOLLOWERS:
                        score += getNbFollowersScore((int) parameter.getValue());
                        break;
                    case NB_FOLLOWINGS:
                        score += getNbFollowingsScore((int) parameter.getValue());
                        break;
                    case RATIO:
                        score += getRatioScore((double) parameter.getValue());
                        break;
                    case LAST_UPDATE:
                        score += getLastUpdateScore((Date) parameter.getValue());
                        break;
                    case DESCRIPTION:
                        score += getDescriptionScore(parameter.getValue().toString());
                        break;
                    case LOCATION:
                        score += getLocationScore(parameter.getValue().toString());
                        break;
                    case COMMON_FOLLOWERS:
                        score += getCommonFollowersScore((int) parameter.getValue());
                        break;
                }
            }
        }
        System.out.println(" : " + score +"/"+limit);
        return score;
    }

    private int getCommonFollowersScore(int value) {
        int maxPoints = Criterion.COMMON_FOLLOWERS.getMaxPoints();
        int maxFollow = FollowProperties.getIntProperty(FollowProperties.NB_BASE_FOLLOWERS);
        return maxPoints*value/maxFollow;
    }

    private int getNbFollowersScore(int nbFollowers){
        int maxPoints = Criterion.NB_FOLLOWERS.getMaxPoints();
        if(nbFollowers> FollowProperties.getIntProperty(FollowProperties.MIN_NB_FOLLOWERS)
                && nbFollowers< FollowProperties.getIntProperty(FollowProperties.MAX_NB_FOLLOWERS)){
            return maxPoints;
        }
        return 0;
    }

    private int getNbFollowingsScore(int nbFollowings){
        int maxPoints = Criterion.NB_FOLLOWINGS.getMaxPoints();
        if(nbFollowings> FollowProperties.getIntProperty(FollowProperties.MIN_NB_FOLLOWINGS)
                && nbFollowings< FollowProperties.getIntProperty(FollowProperties.MAX_NB_FOLLOWINGS)){
            return maxPoints;
        }
        return 0;
    }

    private int getRatioScore(double ratio){
        int maxPoints = Criterion.RATIO.getMaxPoints();
        if(ratio> FollowProperties.getFloatProperty(FollowProperties.MIN_RATIO)
                && ratio< FollowProperties.getFloatProperty(FollowProperties.MAX_RATIO)){
            return maxPoints;
        }
        return 0;
    }

    private int getLastUpdateScore(Date lastUpdate){
        int maxPoints = Criterion.LAST_UPDATE.getMaxPoints();
        Date now = new Date();
        if(lastUpdate!=null){
            long daysSinceLastUpdate = (now.getTime()-lastUpdate.getTime()) / (24 * 60 * 60 * 1000);
            if(daysSinceLastUpdate < FollowProperties.getIntProperty(FollowProperties.MAX_DAYS_SINCE_LAST_TWEET)) {
                return maxPoints;
            }
        }
        return 0;
    }

    private int getDescriptionScore(String description){
        int maxPoints = Criterion.DESCRIPTION.getMaxPoints();
        String[] words = FollowProperties.getStringArrayProperty(FollowProperties.DESCRIPTION);
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
        int maxPoints = Criterion.LOCATION.getMaxPoints();
        return maxPoints;
    }
}
