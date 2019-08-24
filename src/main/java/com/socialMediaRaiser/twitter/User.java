package com.socialMediaRaiser.twitter;

import com.socialMediaRaiser.twitter.helpers.dto.getUser.AbstractUser;
import com.socialMediaRaiser.twitter.helpers.dto.getUser.TweetDTO;
import com.socialMediaRaiser.twitter.scoring.UserScoringEngine;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class User extends AbstractUser {

    private int followersCount;
    private int followingCount;
    private String lang;
    private int tweetCount;
    private Date dateOfCreation;
    @Deprecated
    private int favouritesCount;
    private Date lastUpdate;
    private String location;

    @Builder
    User(String id, String userName, int followersCout, int followingCount, String lang, int statusesCount, Date dateOfCreation,
         int commonFollowers, Date dateOfFollow, Date dateOfFollowBack, String description, int favouritesCount,
         Date lastUpdate, String location, List<TweetDTO> mostRecentTweet, boolean protectedAccount){
        super(id, userName, mostRecentTweet, description, dateOfFollow, protectedAccount, commonFollowers, dateOfFollowBack, new UserScoringEngine(FollowProperties.targetProperties.getMinimumPercentMatch()));
        this.followersCount = followersCout;
        this.followingCount = followingCount;
        this.lang = lang;
        this.tweetCount = statusesCount;
        this.dateOfCreation = dateOfCreation;
        this.favouritesCount = favouritesCount;
        this.lastUpdate = lastUpdate;
        this.location = location;
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
}
