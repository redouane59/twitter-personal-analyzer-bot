package com.socialMediaRaiser.twitter;

import com.google.api.client.util.DateTime;
import com.socialMediaRaiser.AbstractUser;
import com.socialMediaRaiser.twitter.config.FollowParameters;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class User extends AbstractUser {


    private String lang;
    private int statusesCount;
    private String dateOfCreation;
    private int commonFollowers; // nb of occurrences in followers search
    private String dateOfFollow; // @todo change type
    private DateTime lastUpdate; // @todo to add using GET statuses/user_timeline
    private String description; // @todo to add
    private int favouritesCount; // @todo to add

    public User(){

    }

    public User(Long id, String user_name, int followers_count, int following_count, String lang,
                int statusesCount, String dateOfCreation, int commonFollowers, String dateOfFollow){
        super(id, user_name, followers_count, following_count);
        this.lang = lang;
        this.statusesCount = statusesCount;
        this.dateOfCreation = dateOfCreation;
        this.commonFollowers = commonFollowers;
        this.dateOfFollow = dateOfFollow;
    }

    @Override
    public boolean shouldBeFollowed(){

        if(this.getFollowersCount()> FollowParameters.MIN_NB_FOLLOWERS
                && this.getFollowersCount()<FollowParameters.MAX_NB_FOLLOWERS
                && this.getFollowersRatio()>FollowParameters.MIN_RATIO
                && this.getFollowersRatio()<FollowParameters.MAX_RATIO
                && this.lang.equals(FollowParameters.LANGUAGE)){
            return true;
        } else{
            return false;
        }
    }

    @Override
    public boolean shouldBeUnfollowed() {
        throw new UnsupportedOperationException();
    }

    public boolean shouldBeTakenForItsFollowers(){

        if(this.getFollowersCount()> FollowParameters.MIN_NB_FOLLOWERS
                && this.getFollowersRatio()>FollowParameters.MIN_RATIO
                && this.lang.equals(FollowParameters.LANGUAGE)){
            return true;
        } else{
            return false;
        }
    }

    public boolean isInfluencer(){
        if(this.getFollowersRatio()>FollowParameters.INFLUENCER_MIN_RATIO
                && this.getFollowersCount()>FollowParameters.INFLUENCER_MIN_NB_FOLLOWERS
                && this.lang.equals(FollowParameters.LANGUAGE)){
            return true;
        } else{
            return false;
        }
    }

    public void incrementCommonFollowers(){
        this.commonFollowers++;
    }

    public void setDateOfFollowNow(){
        LocalDateTime now = LocalDateTime.now();
        this.setDateOfFollow(now.getDayOfMonth() + "/" + now.getMonthValue()
                + " " + now.getHour() + ":" + now.getMinute());
    }

}
