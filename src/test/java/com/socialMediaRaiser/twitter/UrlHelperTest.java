package com.socialMediaRaiser.twitter;

import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UrlHelperTest {

    private AbstractTwitterBot twitterBot = new TwitterBotByInfluencers();

    @BeforeAll
    public static void init(){
        FollowProperties.load();
    }

    @Test
    public void testUrlRetweetrs(){
        assertEquals("https://api.twitter.com/1.1/statuses/retweeters/ids.json?id=12345&count=100", twitterBot.getUrlHelper().getRetweetersUrl(12345L));
    }

    @Test
    public void testUrlFollowersById(){
        assertEquals("https://api.twitter.com/1.1/followers/ids.json?user_id=952253106",
                twitterBot.getUrlHelper().getFollowerIdsUrl(952253106L));
    }

    @Test
    public void testUrlFollowersByName(){
        assertEquals("https://api.twitter.com/1.1/followers/list.json?screen_name=RedTheOne&count=200",
                twitterBot.getUrlHelper().getFollowerUsersUrl("RedTheOne"));
    }

    @Test
    public void testUrlFollowingsById(){
        assertEquals("https://api.twitter.com/1.1/friends/ids.json?user_id=952253106",
                twitterBot.getUrlHelper().getFollowingIdsUrl(952253106L));
    }

    @Test
    public void testUrlFollowingsByName(){
        assertEquals("https://api.twitter.com/1.1/friends/list.json?screen_name=RedTheOne&count=200",
                twitterBot.getUrlHelper().getFollowingUsersUrl("RedTheOne"));
    }

    @Test
    public void testUrlLastTweet(){
        assertEquals("https://api.twitter.com/1.1/statuses/user_timeline.json?",
                twitterBot.getUrlHelper().getLastTweetListUrl());
    }

    @Test
    public void testUrlFriendshipById(){
        assertEquals("https://api.twitter.com/1.1/friendships/show.json?source_id=12345&target_id=67890",
                twitterBot.getUrlHelper().getFriendshipUrl(12345L,67890L));
    }

    @Test
    public void testUrlFriendshipByName(){
        assertEquals("https://api.twitter.com/1.1/friendships/show.json?source_screen_name=RedTheOne&target_screen_name=EmmanuelMacron",
                twitterBot.getUrlHelper().getFriendshipUrl("RedTheOne","EmmanuelMacron"));
    }

    @Test
    public void testUrlFollowByName(){
        assertEquals("https://api.twitter.com/1.1/friendships/create.json?screen_name=RedTheOne",
                twitterBot.getUrlHelper().getFollowUrl("RedTheOne"));
    }

    @Test
    public void testUrlUnfollowByName(){
        assertEquals("https://api.twitter.com/1.1/friendships/destroy.json?screen_name=RedTheOne",
                twitterBot.getUrlHelper().getUnfollowUrl("RedTheOne"));
    }

    @Test
    public void testUrlFollowById(){
        assertEquals("https://api.twitter.com/1.1/friendships/create.json?user_id=12345",
                twitterBot.getUrlHelper().getFollowUrl(12345L));
    }

    @Test
    public void testUrlGetUserById(){
        assertEquals("https://api.twitter.com/1.1/users/show.json?user_id=12345",
                twitterBot.getUrlHelper().getUserUrl(12345L));
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
        List<Long> ids = new ArrayList<>();
        ids.add(12345L);
        ids.add(23456L);
        ids.add(34567L);
        assertEquals("https://api.twitter.com/1.1/users/lookup.json?user_id=12345,23456,34567",
                twitterBot.getUrlHelper().getUsersUrlbyIds(ids));
    }

    @Test
    public void testUrlGetTweetInfoById(){
        assertEquals("https://api.twitter.com/1.1/statuses/show.json?id=12345",
                twitterBot.getUrlHelper().getTweetInfoUrl(12345L));
    }

    @Test
    public void testUrlGetRateLimitStatus(){
        assertEquals("https://api.twitter.com/1.1/application/rate_limit_status.json",
                twitterBot.getUrlHelper().getRateLimitUrl());
    }

    @Test
    public void testGetUserTweetUrlByName(){
        assertEquals("https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=twitterdev&count=1&trim_user=true&include_rts=false",
        twitterBot.getUrlHelper().getUserTweetsUrl("twitterdev",1));
    }

    @Test
    public void testGetUserTweetUrlById(){
        assertEquals("https://api.twitter.com/1.1/statuses/user_timeline.json?user_id=12345&count=1&trim_user=true&include_rts=false",
                twitterBot.getUrlHelper().getUserTweetsUrl(12345L,1));
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
        Long tweetId = 12345L;
        assertEquals("https://api.twitter.com/1.1/favorites/create.json?id="+tweetId,
                twitterBot.getUrlHelper().getLikeUrl(tweetId));
    }


}
