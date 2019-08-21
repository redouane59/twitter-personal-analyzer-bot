package com.socialMediaRaiser.twitter.helpers.dto.getUser;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.JsonNode;
import com.socialMediaRaiser.twitter.helpers.JsonHelper;
import com.socialMediaRaiser.twitter.helpers.dto.IUser;
import lombok.Data;

import java.util.Date;

@Data
public class UserDTO implements IUser {
    private String id;
    private String created_at;
    private String name;
    private String username;
    @JsonAlias("protected")
    private boolean protectedAccount;
    private String location;
    private String url;
    private String description;
    private boolean verified;
    private JsonNode entities;
    private String profile_image_url;
    private UserStatsDTO stats;
    private String most_recent_tweet_id;
    private TweetDTO mostRecentTweet;
    private String pinned_tweet_id;
    private String format;

    @Override
    public Date getDateOfCreation() {
        return JsonHelper.getDateFromTwitterDateV2(this.created_at);
    }

    @Override
    public Date getLastUpdate() {
        if(this.mostRecentTweet==null){
            System.err.println("mostRecentTweet null");
            return null;
        }
        return JsonHelper.getDateFromTwitterDateV2(this.mostRecentTweet.getCreated_at());
    }

    @Override
    public int getFollowersCount() {
        return this.stats.getFollowers_count();
    }

    @Override
    public int getFollowingCount() {
        return this.stats.getFollowing_count();
    }

    @Override
    public int getTweetCount() {
        return this.stats.getTweet_count();
    }

    @Override
    public String getLang() {
        return this.mostRecentTweet.getLang();
    }
}
