package com.socialMediaRaiser.twitter;

import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UrlHelperTest {

    private static String ownerName = "RedTheOne";
    private AbstractTwitterBot twitterBot = new TwitterBotByInfluencers(ownerName);

    @BeforeAll
    public static void init(){
        FollowProperties.load(ownerName);
    }

    @Test
    public void testUrlRetweetrs(){
        assertTrue("https://api.twitter.com/1.1/statuses/retweeters/ids.json?id=12345&count=100".equals(twitterBot.getUrlHelper().getRetweetersUrl(12345L)));
    }

    @Test
    public void testUrlFollowersById(){
        assertTrue("https://api.twitter.com/1.1/followers/ids.json?user_id=952253106".equals(
                twitterBot.getUrlHelper().getFollowerIdsUrl(952253106L)));
    }

    @Test
    public void testUrlFollowersByName(){
        assertTrue("https://api.twitter.com/1.1/followers/list.json?screen_name=RedTheOne&count=200".equals(
                twitterBot.getUrlHelper().getFollowerUsersUrl("RedTheOne")));
    }

    @Test
    public void testUrlFollowingsById(){
        assertTrue("https://api.twitter.com/1.1/friends/ids.json?user_id=952253106".equals(twitterBot.getUrlHelper().getFollowingIdsUrl(952253106L)));
    }

    @Test
    public void testUrlFollowingsByName(){
        assertTrue("https://api.twitter.com/1.1/friends/list.json?screen_name=RedTheOne&count=200".equals(
                twitterBot.getUrlHelper().getFollowingUsersUrl("RedTheOne")));
    }

    @Test
    public void testUrlLastTweet(){
        assertTrue("https://api.twitter.com/1.1/statuses/user_timeline.json?".equals(
                twitterBot.getUrlHelper().getLastTweetListUrl()));
    }

    @Test
    public void testUrlFriendshipById(){
        assertTrue("https://api.twitter.com/1.1/friendships/show.json?source_id=12345&target_id=67890".equals(
                twitterBot.getUrlHelper().getFriendshipUrl(12345L,67890L)));
    }

    @Test
    public void testUrlFriendshipByName(){
        assertTrue("https://api.twitter.com/1.1/friendships/show.json?source_screen_name=RedTheOne&target_screen_name=EmmanuelMacron".equals(
                twitterBot.getUrlHelper().getFriendshipUrl("RedTheOne","EmmanuelMacron")));
    }

    @Test
    public void testUrlFollowByName(){
        assertTrue("https://api.twitter.com/1.1/friendships/create.json?screen_name=RedTheOne".equals(
                twitterBot.getUrlHelper().getFollowUrl("RedTheOne")));
    }

    @Test
    public void testUrlUnfollowByName(){
        assertTrue("https://api.twitter.com/1.1/friendships/destroy.json?screen_name=RedTheOne".equals(
                twitterBot.getUrlHelper().getUnfollowUrl("RedTheOne")));
    }

    @Test
    public void testUrlFollowById(){
        assertTrue("https://api.twitter.com/1.1/friendships/create.json?user_id=12345".equals(
                twitterBot.getUrlHelper().getFollowUrl(12345L)));
    }

    @Test
    public void testUrlGetUserByIdV2(){
        assertTrue("https://api.twitter.com/labs/1/users?ids=12345&user.format=detailed&tweet.format=detailed&expansions=most_recent_tweet_id".equals(
                twitterBot.getUrlHelper().getUserUrl(12345L)));
    }

    @Test
    public void testUrlGetUsersByNames(){
        List<String> names = new ArrayList<>();
        names.add("RedTheOne");
        names.add("Ronaldo");
        names.add("Zidane");
        assertTrue("https://api.twitter.com/1.1/users/lookup.json?screen_name=RedTheOne,Ronaldo,Zidane".equals(
                twitterBot.getUrlHelper().getUsersUrlbyNames(names)));
    }

    @Test
    public void testUrlGetUsersByIds(){
        List<Long> ids = new ArrayList<>();
        ids.add(12345L);
        ids.add(23456L);
        ids.add(34567L);
        assertTrue("https://api.twitter.com/1.1/users/lookup.json?user_id=12345,23456,34567".equals(
                twitterBot.getUrlHelper().getUsersUrlbyIds(ids)));
    }

    @Test
    public void testUrlGetTweetInfoById(){
        assertTrue("https://api.twitter.com/1.1/statuses/show.json?id=12345".equals(
                twitterBot.getUrlHelper().getTweetInfoUrl(12345L)));
    }

    @Test
    public void testUrlGetRateLimitStatus(){
        assertTrue("https://api.twitter.com/1.1/application/rate_limit_status.json".equals(
                twitterBot.getUrlHelper().getRateLimitUrl()));
    }

    @Test
    public void testGetUserTweetUrlByName(){
        assertTrue("https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=twitterdev&count=1&trim_user=true&include_rts=false".equals(
                twitterBot.getUrlHelper().getUserTweetsUrl("twitterdev",1)));
    }

    @Test
    public void testGetUserTweetUrlById(){
        assertTrue("https://api.twitter.com/1.1/statuses/user_timeline.json?user_id=12345&count=1&trim_user=true&include_rts=false".equals(
                twitterBot.getUrlHelper().getUserTweetsUrl(12345L,1)));
    }

    @Test
    public void testSearchTweetsUrl(){
        //https://api.twitter.com/1.1/tweets/search/30day/DevImproveMyTwitter.json
        assertTrue("https://api.twitter.com/1.1/tweets/search/30day/dev.json".equals(
                twitterBot.getUrlHelper().getSearchTweetsUrl()));
    }

    @Test
    public void testLiveEventUrl(){
        //https://api.twitter.com/1.1/account_activity/all/:env_name/webhooks.json
        assertTrue("https://api.twitter.com/1.1/account_activity/all/dev/webhooks.json".equals(
                twitterBot.getUrlHelper().getLiveEventUrl()));
    }

    @Test
    public void testLikeUrl(){
        //https://api.twitter.com/1.1/favorites/create.json?id=TWEET_ID_TO_FAVORITE
        Long tweetId = 12345L;
        assertTrue(("https://api.twitter.com/1.1/favorites/create.json?id="+tweetId).equals(
                twitterBot.getUrlHelper().getLikeUrl(tweetId)));
    }
}