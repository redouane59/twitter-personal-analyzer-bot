package com.socialMediaRaiser.twitter.helpers.dto.getUser;

import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TwitterStatsDTO {
    private int retweet_count;
    private int reply_count;
    private int like_count;
    private int quote_count;

    public TwitterStatsDTO(){

    }
}
