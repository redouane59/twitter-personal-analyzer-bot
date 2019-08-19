package com.socialMediaRaiser.twitter.helpers.dto.getUser;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class UserStatsDTO {
    @JsonAlias("followers_count")
    private int followersCount;
    @JsonAlias("following_count")
    private int followingCount;
    @JsonAlias("tweet_count")
    private int tweetCount;
    @JsonAlias("listed_count")
    private int listedCount;

    public UserStatsDTO(){

    }
}
