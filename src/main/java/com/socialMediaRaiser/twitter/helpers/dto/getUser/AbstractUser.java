package com.socialMediaRaiser.twitter.helpers.dto.getUser;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.RandomForestAlgoritm;
import com.socialMediaRaiser.twitter.helpers.dto.IUser;
import com.socialMediaRaiser.twitter.scoring.UserScoringEngine;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractUser implements IUser {

    private String id;
    private String username;
    private List<TweetDTO> mostRecentTweet;
    private String description;
    private Date dateOfFollow;
    @JsonAlias("protected")
    private boolean protectedAccount;
    private int commonFollowers; // nb of occurrences in followers search
    private Date dateOfFollowBack;
    private UserScoringEngine scoringEngine;

    @Override
    public boolean equals(Object o) {
        AbstractUser otherUser = (AbstractUser) o;
        return (otherUser).getId() == this.getId();
    }

    public double getFollowersRatio() {
        return (double) this.getFollowersCount() / (double) this.getFollowingCount();
    }

    public void setDateOfFollowNow(){
        this.setDateOfFollow(new Date());
    }

    public long getDaysBetweenFollowAndLastUpdate(){
        if(dateOfFollow==null || this.getLastUpdate()==null){
            return Long.MAX_VALUE;
        }
        return (dateOfFollow.getTime()-this.getLastUpdate().getTime()) / (24 * 60 * 60 * 1000);
    }

    public long getYearsBetweenFollowAndCreation(){
        return (dateOfFollow.getTime()-this.getDateOfCreation().getTime()) / (365 * 24 * 60 * 60 * 1000);
    }

    public boolean getRandomForestPrediction(){
        this.setDateOfFollowNow();
        return RandomForestAlgoritm.getPrediction(this);
    }

    public boolean isLanguageOK(){
        return Option.of(this.getLang())
                .map(b -> this.getLang().equals(FollowProperties.targetProperties.getLanguage())).getOrElse(false);
    }

    public boolean isInfluencer(){
        String descriptionWords = FollowProperties.targetProperties.getDescription();
        String[] descriptionWordsSplitted = descriptionWords.split(FollowProperties.ARRAY_SEPARATOR);
        String[] userDescriptionSplitted = this.getDescription().split(" ");

        String locationWords = FollowProperties.targetProperties.getLocation();
        String[] locationWordsSplitted = locationWords.split(FollowProperties.ARRAY_SEPARATOR);
        String[] userLocationSplitted = this.getLocation().split(" ");

        boolean matchDescription = false;
        boolean matchLocation = false;

        for(String s :userDescriptionSplitted){
            if(Arrays.stream(descriptionWordsSplitted).anyMatch(s::contains)){
                matchDescription = true;
            }
        }
        for(String s :userLocationSplitted){
            if(Arrays.stream(locationWordsSplitted).anyMatch(s::contains)){
                matchLocation = true;
            }
        }
        return (matchDescription&&matchLocation&&this.getFollowersRatio()>1&&this.getFollowersCount()>1000);

       /* if(this.getFollowersRatio()> followConfiguration.getMinRatio()
                && this.getFollowersCount()> followConfiguration.getInfluencerMinNbFollowers()
                && this.description.contains(followConfiguration.getDescription()[0]) */
        /*&& this.lang!=null && this.lang.equals(followConfiguration.getLanguage())*//*){
            return true;
        } else{
            return false;
        } */
    }


    // @odo remove argument ?
    public boolean shouldBeFollowed(String ownerName){
        if(this.getUsername()!=null && this.getUsername().equals(ownerName)){
            return false;
        }
        return this.scoringEngine.shouldBeFollowed(this);
    }

}
