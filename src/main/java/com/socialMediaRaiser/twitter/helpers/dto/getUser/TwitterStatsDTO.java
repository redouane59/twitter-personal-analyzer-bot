package com.socialMediaRaiser.twitter.helpers.dto.getUser;

import lombok.Data;

@Data
public class TwitterStatsDTO {
    private int retweet_count;
    private int reply_count;
    private int like_count;
    private int quote_count;
}
