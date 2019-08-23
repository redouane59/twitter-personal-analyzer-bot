package com.socialMediaRaiser.twitter.helpers.dto.getUser;

import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.RandomForestAlgoritm;
import com.socialMediaRaiser.twitter.helpers.dto.IUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        if(this.getLang()==null){
            return false;
            //user.addLanguageFromLastTweet(this.getUserLastTweets(user.getId(), 3)); // really slow
        }
        return this.getLang().equals(FollowProperties.targetProperties.getLanguage());
    }

}
