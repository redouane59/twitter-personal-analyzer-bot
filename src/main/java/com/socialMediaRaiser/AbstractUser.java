package com.socialMediaRaiser;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class AbstractUser {
    private long id;
    private String userName;
    private int followersCount;
    private int followingCount;

    public AbstractUser(){

    }

    public AbstractUser(long id){
        this.id = id;
    }

    public double getFollowersRatio(){
        return (double)this.followersCount /(double)this.followingCount;
    }

    public abstract boolean shouldBeFollowed();
    public abstract boolean shouldBeUnfollowed();

}
