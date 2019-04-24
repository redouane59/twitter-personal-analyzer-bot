package com.twitter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

    private long id;
    private String screen_name;
    private int followers_count;
    private int friends_count;
    private String lang;
    private int statuses_count;
    private String created_at;
    private int commonFollowers; // nb of occurrences in followers search


    public double getFollowersRatio(){
        return (double)this.followers_count/(double)this.friends_count;
    }

    public boolean shouldBeFollowed(){
        int minNbFollowers = 300;
        int maxNbFollowers = 30000;
        int minRatio = 1;
        int maxRatio = 3;
        String french = "fr";

        if(this.followers_count>minNbFollowers
                && this.followers_count<maxNbFollowers
                && this.getFollowersRatio()>minRatio
                && this.getFollowersRatio()<maxRatio
                && this.lang.equals(french)){
            return true;
        } else{
            return false;
        }
    }

    public void incrementCommonFollowers(){
        this.commonFollowers++;
    }

}
