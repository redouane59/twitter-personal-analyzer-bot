package com.socialMediaRaiser.twitter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.socialMediaRaiser.twitter.helpers.TweetDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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
    private User user;
}
