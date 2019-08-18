package com.socialMediaRaiser.twitter.helpers.dto.getUser;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String created_at;
    private String name;
    private String username;
    @JsonAlias("protected")
    private String protected_;
    private String location;
    private String url;
    private String description;
    private boolean verified;
    private DescriptionEntitiesDTO entities;
    private String profile_image_url;
    private UserStatsDTO stats;
    private String most_recent_tweet_id;
    private String pinned_tweet_id;
    private String format;
}
