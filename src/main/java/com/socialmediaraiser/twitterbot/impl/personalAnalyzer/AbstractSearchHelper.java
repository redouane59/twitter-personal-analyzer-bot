package com.socialmediaraiser.twitterbot.impl.personalAnalyzer;

import com.socialmediaraiser.twitter.TwitterClient;
import com.socialmediaraiser.twitter.dto.user.IUser;
import com.socialmediaraiser.twitter.dto.user.UserDTOv1;
import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public abstract class AbstractSearchHelper {
    private String userName;
    private String userId;
    private TwitterClient twitterClient = new TwitterClient();
    private List<IUser> followings;
    private List<IUser> followers;
    private Set<IUser> allUsers;

    public AbstractSearchHelper(String userName){
        this.userName = userName;
        this.userId = this.twitterClient.getUserFromUserName(userName).getId();
        this.followings = this.twitterClient.getFollowingUsers(userId);
        this.followers = this.twitterClient.getFollowerUsers(userId);
        this.allUsers = new HashSet<>() {
            {
                addAll(followings);
                addAll(followers);
            } };
    }

    public boolean isUserInList(String userId){
        if(userId==null) return false;
        UserDTOv1 retweeter = UserDTOv1.builder().id(userId).build();
        return (this.getAllUsers().contains(retweeter));
    }
}
