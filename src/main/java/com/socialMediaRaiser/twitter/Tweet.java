package com.socialMediaRaiser.twitter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.socialMediaRaiser.twitter.helpers.TweetDeserializer;
import com.socialMediaRaiser.twitter.helpers.dto.IUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

import static java.util.TimeZone.getDefault;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = TweetDeserializer.class)

public class Tweet {
    private long id;
    private String lang;
    private int retweet_count;
    private int favorite_count;
    private int reply_count;
    private String text;
    private Date created_at;
    private IUser user;

    public boolean matchWords(List<String> words){
        for(String word : words){
            if(this.text.contains(word)){
                return true;
            }
        }
        return false;
    }
}
