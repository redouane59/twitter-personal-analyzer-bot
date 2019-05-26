package com.socialMediaRaiser.twitter;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.socialMediaRaiser.AbstractUser;
import com.socialMediaRaiser.twitter.helpers.JsonHelper;
import com.socialMediaRaiser.twitter.scoring.ScoringConstant;
import com.socialMediaRaiser.twitter.scoring.UserScoringEngine;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class User extends AbstractUser {

    private String lang;
    private int statusesCount;
    private Date dateOfCreation;
    private int commonFollowers; // nb of occurrences in followers search
    private Date dateOfFollow;
    private String description;
    private int favouritesCount;
    private Date lastUpdate;
    private String location;
    private ScoringConstant scoringConstant = new ScoringConstant();
    private UserScoringEngine scoringEngine = new UserScoringEngine(scoringConstant.getMinimumPercentMatch());

    @Builder
    User(long id, String userName, int followerCout, int followingCount, String lang, int statusesCount, Date dateOfCreation, int commonFollowers,
         Date dateOfFollow, String description, int favouritesCount, Date lastUpdate, String location){
        super(id,userName, followerCout, followingCount);
        this.lang = lang;
        this.statusesCount = statusesCount;
        this.dateOfCreation = dateOfCreation;
        this.commonFollowers = commonFollowers;
        this.dateOfFollow = dateOfFollow;
        this.description = description;
        this.favouritesCount = favouritesCount;
        this.lastUpdate = lastUpdate;
        this.location = location;
    }

    @Override
    public boolean shouldBeFollowed(){
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

        if(this.getFollowersCount()> scoringConstant.getMinNbFollowers()
                && this.getFollowersRatio()> scoringConstant.getMinRatio()
                && this.lang.equals(scoringConstant.getLanguage())){
            return true;
        } else{
            return false;
        }
    }

    public boolean isInfluencer(){
        if(this.getFollowersRatio()> ScoringConstant.INFLUENCER_MIN_RATIO
                && this.getFollowersCount()> ScoringConstant.INFLUENCER_MIN_NB_FOLLOWERS
                /*&& this.lang.equals(ScoringConstant.LANGUAGE)*/){
            return true;
        } else{
            return false;
        }
    }

    public void incrementCommonFollowers(){
        this.commonFollowers++;
    }

    public void setDateOfFollowNow(){
        this.setDateOfFollow(new Date());
    }

    public long getDaysBetweenFollowAndLastUpdate(){
        return (dateOfFollow.getTime()-lastUpdate.getTime()) / (24 * 60 * 60 * 1000);
    }

    public void addLanguageFromLastTweet(List<Tweet> userLastTweets){
        if(userLastTweets!=null){
            for(Tweet tweet : userLastTweets){
                if(!tweet.getLang().equals("und")){
                    this.setLang(tweet.getLang());
                    if(this.lang.equals(scoringConstant.getLanguage())){
                        break;
                    }
                }
            }
        }
    }
}
