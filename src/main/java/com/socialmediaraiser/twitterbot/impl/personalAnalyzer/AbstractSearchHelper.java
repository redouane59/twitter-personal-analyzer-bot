package com.socialmediaraiser.twitterbot.impl.personalAnalyzer;

import com.socialmediaraiser.twitter.TwitterClient;
import lombok.Getter;

@Getter
public abstract class AbstractSearchHelper {
    private String userName;
    private TwitterClient twitterClient = new TwitterClient();

    public AbstractSearchHelper(String userName){
        this.userName = userName;
    }
}
