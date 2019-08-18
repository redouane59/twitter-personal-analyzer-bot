package com.socialMediaRaiser.twitter.helpers.dto.getUser;

import lombok.Data;

@Data
public class UserStatsDTO {
    private int followers_count;
    private int following_count;
    private int tweet_count;
    private int listed_count;
}
