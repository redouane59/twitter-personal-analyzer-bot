package com.socialMediaRaiser.twitter.helpers.dto.getUser;

import lombok.Data;

import java.util.List;

@Data
public class TweetDTO {
    private String id;
    private String created_at;
    private String text;
    private String author_id;
    private String in_reply_to_user_id;
    private List<ReferencedTweetDTO> referenced_tweets;
    private MentionEntitiesDTO entities;
    private TwitterStatsDTO stats;
    private boolean possibly_sensitive;
    private String lang;
    private String source;
    private String format;
}
