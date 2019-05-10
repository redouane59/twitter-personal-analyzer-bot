package com.socialMediaRaiser.twitter;

import com.google.api.client.util.DateTime;
import com.socialMediaRaiser.AbstractUser;
import com.socialMediaRaiser.twitter.config.FollowParameters;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

@Data
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

    @Builder
    User(long id, String userName, int followerCout, int followingCount, String lang, int statusesCount, Date dateOfCreation, int commonFollowers,
         Date dateOfFollow, String description, int favouritesCount, Date lastUpdate){
        super(id,userName, followerCout, followingCount);
        this.lang = lang;
        this.statusesCount = statusesCount;
        this.dateOfCreation = dateOfCreation;
        this.commonFollowers = commonFollowers;
        this.dateOfFollow = dateOfFollow;
        this.description = description;
        this.favouritesCount = favouritesCount;
        this.lastUpdate = lastUpdate;
    }

    @Override
    public boolean shouldBeFollowed(){

        if(this.getFollowersCount()> FollowParameters.MIN_NB_FOLLOWERS
                && this.getFollowersCount()<FollowParameters.MAX_NB_FOLLOWERS
                && this.getFollowersRatio()>FollowParameters.MIN_RATIO
                && this.getFollowersRatio()<FollowParameters.MAX_RATIO
                && this.lang.equals(FollowParameters.LANGUAGE)
                && this.lastUpdate!=null){
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
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        this.setDateOfFollow(new Date());
    }

    // @TODO why do I need to put it here too ?
    @Override
    public int hashCode() {
        return Long.hashCode(this.getId());
    }

}
