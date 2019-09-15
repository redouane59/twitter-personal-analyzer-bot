package com.socialmediaraiser.twitter;

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
    private int retweet_count;
    private int favorite_count;
    private int reply_count;
    private String text;
    private Date created_at;
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
