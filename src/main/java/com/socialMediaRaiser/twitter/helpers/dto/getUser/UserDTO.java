package com.socialMediaRaiser.twitter.helpers.dto.getUser;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.JsonNode;
import com.socialMediaRaiser.AbstractBot;
import com.socialMediaRaiser.twitter.helpers.JsonHelper;
import lombok.Data;

import java.util.Date;
import java.util.logging.Logger;

@Data
public class UserDTO extends AbstractUser {
    private static final Logger LOGGER = Logger.getLogger(UserDTO.class.getName());

    private String created_at;
    private String name;
    private String location;
    private String url;
    private boolean verified;
    private JsonNode entities;
    private String profile_image_url;
    private UserStatsDTO stats;
    private String most_recent_tweet_id;
    private String pinned_tweet_id;
    private String format;

    @Override
    public Date getDateOfCreation() {
        return JsonHelper.getDateFromTwitterDateV2(this.created_at);
    }

    @Override
    public Date getLastUpdate() {
        if(this.getMostRecentTweet()==null && this.getMostRecentTweet().size()>0){
            LOGGER.severe(()->"mostRecentTweet null");
            return null;
        }
        return JsonHelper.getDateFromTwitterDateV2(this.getMostRecentTweet().get(0).getCreated_at());
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
        if(this.getMostRecentTweet()==null && this.getMostRecentTweet().size()>0){
            LOGGER.severe(()->"mostRecentTweet null");
            return null;
        }
        return this.getMostRecentTweet().get(0).getLang();
    }
}
