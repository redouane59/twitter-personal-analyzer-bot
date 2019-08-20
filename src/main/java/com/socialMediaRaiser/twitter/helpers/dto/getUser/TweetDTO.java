package com.socialMediaRaiser.twitter.helpers.dto.getUser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class TweetDTO {
    private String id;
    private String created_at;
    private String text;
    private String author_id;
    private String in_reply_to_user_id;
    private String in_reply_to_status_id;
    private List<ReferencedTweetDTO> referenced_tweets;
    private JsonNode entities;
    private TwitterStatsDTO stats;
    private boolean possibly_sensitive;
    private String lang;
    private String source;
    private String format;
    private JsonNode attachments;
    private JsonNode geo;

    public TweetDTO(){

    }

    public boolean matchWords(List<String> words){
        for(String word : words){
            if(this.text.contains(word)){
                return true;
            }
        }
        return false;
    }
}
