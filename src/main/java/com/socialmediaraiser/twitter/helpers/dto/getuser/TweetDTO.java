package com.socialmediaraiser.twitter.helpers.dto.getuser;

import com.fasterxml.jackson.databind.JsonNode;
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
    private JsonNode entities;
    private TwitterStatsDTO stats;
    private boolean possibly_sensitive;
    private String lang;
    private String source;
    private String format;
    private JsonNode attachments;
    private JsonNode geo;
}
