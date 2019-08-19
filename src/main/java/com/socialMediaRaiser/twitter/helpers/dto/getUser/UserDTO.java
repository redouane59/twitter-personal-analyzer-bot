package com.socialMediaRaiser.twitter.helpers.dto.getUser;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.RandomForestAlgoritm;
import com.socialMediaRaiser.twitter.helpers.JsonHelper;
import com.socialMediaRaiser.twitter.scoring.UserScoringEngine;
import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Arrays;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class UserDTO {
    private String id;
    @JsonProperty("created_at")
    private String dateOfCreation;
    private String name;
    private String username;
    @JsonProperty("protected")
    private boolean protectedAccount;
    private String location;
    private String url;
    private String description;
    private boolean verified;
    private JsonNode entities;
    @JsonAlias("profile_image_url")
    private String profileImageUrl;
    private UserStatsDTO stats;
    @JsonAlias("most_recent_tweet_id")
    private String most_recent_tweet_id;
    private TweetDTO mostRecentTweet;
    @JsonAlias("pinned_tweet_id")
    private String pinnedTweet;
    private String format;
    // not from DTO
    @JsonIgnore
    private UserScoringEngine scoringEngine = new UserScoringEngine(FollowProperties.targetProperties.getMinimumPercentMatch());
    private int commonFollowers; // nb of occurrences in followers search
    private Date dateOfFollow;
    private Date dateOfFollowBack;

    public UserDTO(){

    }

    public int getStatusesCount(){
        return this.getStats().getTweetCount();
    }

    public int getFollowersCount(){
        return this.getStats().getFollowersCount();
    }

    public int getFollowingCount(){
        return this.getStats().getFollowingCount();
    }

    @Override
    public boolean equals(Object o) {
        UserDTO otherUser = (UserDTO) o;
        return otherUser.getId().equals(this.getId());
    }

    // @odo remove argument ?
    public boolean shouldBeFollowed(String ownerName){
        if(this.username!=null && this.username.equals(ownerName)){
            return false;
        }
        return this.scoringEngine.shouldBeFollowed(this);
    }

    public boolean shouldBeTakenForItsFollowers(){

        if(this.getFollowersCount()> FollowProperties.targetProperties.getMinNbFollowers()
                && this.getFollowersRatio()> FollowProperties.influencerProperties.getMinRatio()
                && this.getLang().equals(FollowProperties.targetProperties.getLanguage())){
            return true;
        } else{
            return false;
        }
    }

    public double getFollowersRatio() {
        return (double) this.getFollowersCount() / (double) this.getFollowingCount();
    }

    public String getLang(){
        return Option.of(this.mostRecentTweet.getLang()).getOrNull();
    }

    public boolean isInfluencer(){
        String words = FollowProperties.targetProperties.getDescription();
        String[] wordsSplitted = words.split(FollowProperties.ARRAY_SEPARATOR);
        String[] descriptionSplitted = this.getDescription().split(" ");
        for(String s :descriptionSplitted){
            if(Arrays.stream(wordsSplitted).anyMatch(s::contains)){
                return true;
            }
        }
        return false;
    }

    public void incrementCommonFollowers(){
        this.commonFollowers++;
    }

    public void setDateOfFollowNow(){
        this.setDateOfFollow(new Date());
    }

    public long getDaysBetweenFollowAndLastUpdate(){
        if(dateOfFollow==null || getLastUpdate()==null){
            return Long.MAX_VALUE;
        }
        return (dateOfFollow.getTime()-getLastUpdate().getTime()) / (24 * 60 * 60 * 1000);
    }

    public Date getLastUpdate(){
        if(this.mostRecentTweet!=null) {
            return JsonHelper.getTwitterDateV2(this.mostRecentTweet.getCreated_at());
        } else{
            return null;
        }
    }

    public long getYearsBetweenFollowAndCreation(){
        return (dateOfFollow.getTime()-JsonHelper.getTwitterDateV2(dateOfCreation).getTime()) / (365 * 24 * 60 * 60 * 1000);
    }

    public boolean getRandomForestPrediction(){
        this.setDateOfFollowNow();
        return RandomForestAlgoritm.getPrediction(this);
    }

}
