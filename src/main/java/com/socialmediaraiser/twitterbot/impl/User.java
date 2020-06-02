package com.socialmediaraiser.twitterbot.impl;

import com.socialmediaraiser.twitter.dto.user.IUser;
import com.socialmediaraiser.twitter.dto.user.UserDTOv1;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class User extends UserDTOv1 {
    private int nbRetweetsReceived;
    private int nbRepliesGiven;
    private int nbRepliesReceived;
    private int nbLikesGiven;
    private int nbRetweetsGiven;
    private Date dateOfFollow;
    private Date dateOfFollowBack;
    private int commonFollowers;

    public User(IUser u){
        super(u.getId(), u.getName(), null, u.getDescription(),
        u.isProtectedAccount(),  u.getFollowersCount(), u.getFollowingCount(),
                u.getLang(), u.getTweetCount(), null, null, u.getLocation(), u.isFollowing());
    }

    public void setDateOfFollowNow(){
        this.dateOfFollow = new Date();
    }

    public double getFollowersRatio() {
        return (double) this.getFollowersCount() / (double) this.getFollowingCount();
    }
}
