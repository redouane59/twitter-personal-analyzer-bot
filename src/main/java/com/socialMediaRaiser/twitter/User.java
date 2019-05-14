package com.socialMediaRaiser.twitter;

import com.socialMediaRaiser.AbstractUser;
import com.socialMediaRaiser.twitter.config.FollowParameters;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

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

    // @TODO create ranking
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
        this.setDateOfFollow(new Date());
    }

}
