package com.socialMediaRaiser.twitter.scoring;

import com.socialMediaRaiser.twitter.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
public class UserScoringEngine {

    private int limit;
    private List<ScoringParameter> parameters = new ArrayList<>();

    public UserScoringEngine(int minimumMatch){
        if(!(minimumMatch<=100 && minimumMatch>=0)){
            System.err.println("minimumMatch should be between 0 & 100");
        }
        this.limit = Criterion.getTotalMaxPoints()*minimumMatch/100;
    }

    public int getUserScore(User user){
        this.buildScoringParameters(user);
        return this.computeScore();
    }

    private void buildScoringParameters(User user){
        this.parameters.add(new ScoringParameter(Criterion.NB_FOLLOWERS, user.getFollowersCount()));
        this.parameters.add(new ScoringParameter(Criterion.NB_FOLLOWINGS, user.getFollowingCount()));
        this.parameters.add(new ScoringParameter(Criterion.RATIO, user.getFollowersRatio()));
        this.parameters.add(new ScoringParameter(Criterion.LANGUAGE, user.getLang()));
        this.parameters.add(new ScoringParameter(Criterion.LAST_UPDATE, user.getLastUpdate()));
        this.parameters.add(new ScoringParameter(Criterion.DESCRIPTION, user.getDescription()));
        this.parameters.add(new ScoringParameter(Criterion.LOCATION, user.getLocation()));
    }

    public int computeScore(){
        int score = 0;
        for(ScoringParameter parameter : parameters){
            if(parameter.getValue()!=null) {
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
                    case LANGUAGE:
                        score += getLangScore(parameter.getValue().toString());
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
                }
            }
        }
        return score;
    }

    public boolean shouldBeFollowed(User user){
        return getUserScore(user) >= limit;
    }

    private int getNbFollowersScore(int nbFollowers){
        int maxPoints = Criterion.NB_FOLLOWERS.getMaxPoints();
        if(nbFollowers> ScoringConstant.MIN_NB_FOLLOWERS
                && nbFollowers< ScoringConstant.MAX_NB_FOLLOWERS){
            return maxPoints;
        }
        return 0;
    }

    private int getNbFollowingsScore(int nbFollowings){
        int maxPoints = Criterion.NB_FOLLOWINGS.getMaxPoints();
        if(nbFollowings> ScoringConstant.MIN_NB_FOLLOWINGS
                && nbFollowings< ScoringConstant.MAX_NB_FOLLOWINGS){
            return maxPoints;
        }
        return 0;
    }

    private int getRatioScore(double ratio){
        int maxPoints = Criterion.RATIO.getMaxPoints();
        if(ratio> ScoringConstant.MIN_RATIO
                && ratio< ScoringConstant.MAX_RATIO){
            return maxPoints;
        }
        return 0;
    }

    private int getLastUpdateScore(Date lastUpdate){
        int maxPoints = Criterion.LAST_UPDATE.getMaxPoints();
        Date now = new Date();
        if(lastUpdate!=null){
            long daysSinceLastUpdate = (now.getTime()-lastUpdate.getTime()) / (24 * 60 * 60 * 1000);
            if(daysSinceLastUpdate < ScoringConstant.MAX_DAYS_SINCE_LAST_TWEET) {
                return maxPoints;
            }
        }
        return 0;
    }

    private int getLangScore(String lang){
        int maxPoints = Criterion.LANGUAGE.getMaxPoints();
        if(lang!=null && lang.equals(ScoringConstant.LANGUAGE)){
            return maxPoints;
        }
        return 0;
    }

    private int getDescriptionScore(String description){
        int maxPoints = Criterion.DESCRIPTION.getMaxPoints();
        return maxPoints;
    }

    private int getLocationScore(String location){
        int maxPoints = Criterion.LOCATION.getMaxPoints();
        return maxPoints;
    }
}
