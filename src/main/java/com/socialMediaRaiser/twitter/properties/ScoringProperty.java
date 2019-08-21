package com.socialMediaRaiser.twitter.properties;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.socialMediaRaiser.twitter.scoring.Criterion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@Builder
public class ScoringProperty {
    private Criterion criterion;
    private boolean active;
    private int maxPoints;
    private Object value;
    private boolean blocking;

    public ScoringProperty(){

    }

    public void setCriterion(String s){
        switch (s){
            case "nbFollowers":
                this.criterion = Criterion.NB_FOLLOWERS;
                break;
            case "nbFollowings":
                this.criterion = Criterion.NB_FOLLOWINGS;
                break;
            case "description":
                this.criterion = Criterion.DESCRIPTION;
                break;
            case "location":
                this.criterion = Criterion.LOCATION;
                break;
            case "commonFollowers":
                this.criterion = Criterion.COMMON_FOLLOWERS;
                break;
            case "lastUpdate":
                this.criterion = Criterion.LAST_UPDATE;
                break;
            case "ratio":
                this.criterion = Criterion.RATIO;
                break;
            case "favouritesCount":
                this.criterion = Criterion.NB_FAVS;
                break;
            case "tweetCount":
                this.criterion = Criterion.NB_TWEETS;
                break;
            default:
                System.err.println("criterion not found");
                break;
        }
    }
}
