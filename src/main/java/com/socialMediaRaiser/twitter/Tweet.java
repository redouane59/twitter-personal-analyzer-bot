package com.socialMediaRaiser.twitter;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class Tweet {
    private long id;
    private String lang;
    private int retweet_count;
    private int favorite_count;
    private String text;
    private Date created_at;

}
