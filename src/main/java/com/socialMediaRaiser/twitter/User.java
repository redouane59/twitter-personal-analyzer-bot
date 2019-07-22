package com.socialMediaRaiser.twitter;

import com.socialMediaRaiser.AbstractUser;
import com.socialMediaRaiser.twitter.scoring.UserScoringEngine;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Getter
@Setter
@NoArgsConstructor
public class User extends AbstractUser {

    private String lang;
    private int statusesCount;
    private Date dateOfCreation;
    private int commonFollowers; // nb of occurrences in followers search
    private Date dateOfFollow;
    private Date dateOfFollowBack;
    private String description;
    private int favouritesCount;
    private Date lastUpdate;
    private String location;
    private UserScoringEngine scoringEngine = new UserScoringEngine(FollowProperties.targetProperties.getMinimumPercentMatch());

    @Builder
    User(long id, String userName, int followerCout, int followingCount, String lang, int statusesCount, Date dateOfCreation, int commonFollowers,
         Date dateOfFollow, Date dateOfFollowBack, String description, int favouritesCount, Date lastUpdate, String location){
        super(id,userName, followerCout, followingCount);
        this.lang = lang;
        this.statusesCount = statusesCount;
        this.dateOfCreation = dateOfCreation;
        this.commonFollowers = commonFollowers;
        this.dateOfFollow = dateOfFollow;
        this.dateOfFollowBack = dateOfFollowBack;
        this.description = description;
        this.favouritesCount = favouritesCount;
        this.lastUpdate = lastUpdate;
        this.location = location;
    }

    @Override
    public boolean shouldBeFollowed(){
        if(this.getUserName()!=null && this.getUserName().equals(FollowProperties.USER_NAME)){
            return false;
        }
        return this.scoringEngine.shouldBeFollowed(this);
    }

    private long getNbDaysSinceLastTweet(){
        Date now = new Date();
        Date lastUpdate = this.getLastUpdate();
        return (now.getTime()-lastUpdate.getTime()) / (24 * 60 * 60 * 1000);
    }

    @Override
    public boolean shouldBeUnfollowed() {
        throw new UnsupportedOperationException();
    }

    public boolean shouldBeTakenForItsFollowers(){

        if(this.getFollowersCount()> FollowProperties.targetProperties.getMinNbFollowers()
                && this.getFollowersRatio()> FollowProperties.influencerProperties.getMinRatio()
                && this.lang.equals(FollowProperties.targetProperties.getLanguage())){
            return true;
        } else{
            return false;
        }
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

       /* if(this.getFollowersRatio()> followConfiguration.getMinRatio()
                && this.getFollowersCount()> followConfiguration.getInfluencerMinNbFollowers()
                && this.description.contains(followConfiguration.getDescription()[0]) */
                /*&& this.lang!=null && this.lang.equals(followConfiguration.getLanguage())*//*){
            return true;
        } else{
            return false;
        } */
    }

    public void incrementCommonFollowers(){
        this.commonFollowers++;
    }

    public void setDateOfFollowNow(){
        this.setDateOfFollow(new Date());
    }

    public long getDaysBetweenFollowAndLastUpdate(){
        if(dateOfFollow==null || lastUpdate==null){
            return Long.MAX_VALUE;
        }
        return (dateOfFollow.getTime()-lastUpdate.getTime()) / (24 * 60 * 60 * 1000);
    }

    public long getYearsBetweenFollowAndCreation(){
        return (dateOfFollow.getTime()-dateOfCreation.getTime()) / (365 * 24 * 60 * 60 * 1000);
    }

    public void addLanguageFromLastTweet(List<Tweet> userLastTweets){
        if(userLastTweets!=null){
            for(Tweet tweet : userLastTweets){
                if(!tweet.getLang().equals("und")){
                    this.setLang(tweet.getLang());
                    if(this.lang.equals(FollowProperties.targetProperties.getLanguage())){
                        break;
                    }
                }
            }
        }
    }

    public boolean getRandomForestPrediction(){
        this.setDateOfFollowNow();
        return RandomForestAlgoritm.getPrediction(this);
    }
}
