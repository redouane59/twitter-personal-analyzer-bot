package com.socialmediaraiser.twitter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.socialmediaraiser.twitter.helpers.TweetDeserializer;
import com.socialmediaraiser.twitter.helpers.dto.getuser.AbstractUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = TweetDeserializer.class)

public class Tweet {
    private String id;
    private String lang;
    @JsonProperty("retweet_count")
    private int retweetCount;
    @JsonProperty("favorite_count")
    private int favoriteCount;
    @JsonProperty("reply_count")
    private int replyCount;
    private String text;
    @JsonProperty("created_at")
    private Date createdAt;
    private AbstractUser user;

    public boolean matchWords(List<String> words){
        for(String word : words){
            if(this.text.contains(word)){
                return true;
            }
        }
        return false;
    }
}
