package com.socialMediaRaiser.twitter.unit;

import com.socialMediaRaiser.twitter.AbstractTwitterBot;
import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UrlHelperTest {

    private static String ownerName = "RedTheOne";
    private AbstractTwitterBot twitterBot = new TwitterBotByInfluencers(ownerName);

    @BeforeAll
    public static void init(){
        FollowProperties.load(ownerName);
    }

    @Test
    public void testUrlRetweetrs(){
        assertEquals("https://api.twitter.com/1.1/statuses/retweeters/ids.json?id=12345&count=100", twitterBot.getUrlHelper().getRetweetersUrl("12345"));
    }

    @Test
    public void testUrlFollowersById(){
        assertEquals("https://api.twitter.com/1.1/followers/ids.json?user_id=952253106",
                twitterBot.getUrlHelper().getFollowerIdsUrl("952253106"));
    }

    @Test
    public void testUrlFollowingsById(){
        assertEquals("https://api.twitter.com/1.1/friends/ids.json?user_id=952253106",
                twitterBot.getUrlHelper().getFollowingIdsUrl("952253106"));
    }

    @Test
    public void testUrlLastTweet(){
        assertEquals("https://api.twitter.com/1.1/statuses/user_timeline.json?",
                twitterBot.getUrlHelper().getLastTweetListUrl());
    }

    @Test
    public void testUrlFriendshipById(){
        assertEquals("https://api.twitter.com/1.1/friendships/show.json?source_id=12345&target_id=67890",
                twitterBot.getUrlHelper().getFriendshipUrl("12345","67890"));
    }

    @Test
    public void testUrlFollowById(){
        assertEquals("https://api.twitter.com/1.1/friendships/create.json?user_id=12345",
                twitterBot.getUrlHelper().getFollowUrl("12345"));
    }

    @Test
    public void testUrlGetUserByIdV2(){
        assertEquals("https://api.twitter.com/labs/1/users?ids=12345&user.format=detailed&tweet.format=detailed&expansions=most_recent_tweet_id",
                twitterBot.getUrlHelper().getUserUrl("12345"));
    }

    @Test
    public void testUrlGetUsersByNames(){
        List<String> names = new ArrayList<>();
        names.add("RedTheOne");
        names.add("Ronaldo");
        names.add("Zidane");
        assertEquals("https://api.twitter.com/1.1/users/lookup.json?screen_name=RedTheOne,Ronaldo,Zidane",
                twitterBot.getUrlHelper().getUsersUrlbyNames(names));
    }

    @Test
    public void testUrlGetUsersByIds(){
        List<String> ids = new ArrayList<>();
        ids.add("12345");
        ids.add("23456");
        ids.add("34567");
        assertEquals("https://api.twitter.com/1.1/users/lookup.json?user_id=12345,23456,34567",
                twitterBot.getUrlHelper().getUsersUrlbyIds(ids));
    }

    @Test
    public void testUrlGetTweetInfoById(){
        assertEquals("https://api.twitter.com/1.1/statuses/show.json?id=12345",
                twitterBot.getUrlHelper().getTweetInfoUrl("12345"));
    }

    @Test
    public void testUrlGetRateLimitStatus(){
        assertEquals("https://api.twitter.com/1.1/application/rate_limit_status.json",
                twitterBot.getUrlHelper().getRateLimitUrl());
    }

    @Test
    public void testGetUserTweetUrlById(){
        assertEquals("https://api.twitter.com/1.1/statuses/user_timeline.json?user_id=12345&count=1&trim_user=true&include_rts=false",
                twitterBot.getUrlHelper().getUserTweetsUrl("12345",1));
    }

    @Test
    public void testSearchTweetsUrl(){
        //https://api.twitter.com/1.1/tweets/search/30day/DevImproveMyTwitter.json
        assertEquals("https://api.twitter.com/1.1/tweets/search/30day/dev.json",
                twitterBot.getUrlHelper().getSearchTweetsUrl());
    }

    @Test
    public void testLiveEventUrl(){
        //https://api.twitter.com/1.1/account_activity/all/:env_name/webhooks.json
        assertEquals("https://api.twitter.com/1.1/account_activity/all/dev/webhooks.json",
                twitterBot.getUrlHelper().getLiveEventUrl());
    }

    @Test
    public void testLikeUrl(){
        //https://api.twitter.com/1.1/favorites/create.json?id=TWEET_ID_TO_FAVORITE
        String tweetId = "12345";
        assertEquals("https://api.twitter.com/1.1/favorites/create.json?id="+tweetId,
                twitterBot.getUrlHelper().getLikeUrl(tweetId));
    }

}
