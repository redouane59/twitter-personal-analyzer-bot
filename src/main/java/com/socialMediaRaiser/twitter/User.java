package com.socialMediaRaiser.twitter;

import com.socialMediaRaiser.twitter.config.FollowParameters;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User{

    private long id;
    private String screen_name;
    private int followers_count;
    private int friends_count;
    private String lang;
    private int statuses_count;
    private String created_at;
    private int commonFollowers; // nb of occurrences in followers search
    private String dateOfFollow;

    public double getFollowersRatio(){
        return (double)this.followers_count/(double)this.friends_count;
    }

    public boolean shouldBeFollowed(){

        if(this.followers_count> FollowParameters.MIN_NB_FOLLOWERS
                && this.followers_count<FollowParameters.MAX_NB_FOLLOWERS
                && this.getFollowersRatio()>FollowParameters.MIN_RATIO
                && this.getFollowersRatio()<FollowParameters.MAX_RATIO
                && this.lang.equals(FollowParameters.LANGUAGE)){
            return true;
        } else{
            return false;
        }
    }

    public void incrementCommonFollowers(){
        this.commonFollowers++;
    }

}
