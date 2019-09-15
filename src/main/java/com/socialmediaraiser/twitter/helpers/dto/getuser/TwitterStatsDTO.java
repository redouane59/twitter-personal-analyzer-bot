package com.socialmediaraiser.twitter.helpers.dto.getuser;

import lombok.Data;

@Data
public class TwitterStatsDTO {
    private int retweet_count;
    private int reply_count;
    private int like_count;
    private int quote_count;
}
