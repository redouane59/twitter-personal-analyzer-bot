package com.twitter;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class TwitterBotTest {

    private TwitterBot twitterBot = new TwitterBot();

    @Test
    public void testUrlRetweetrs(){
        Assert.assertEquals("https://api.twitter.com/1.1/statuses/retweeters/ids.json?id=12345&count=90", twitterBot.getUrlHelper().getRetweetersUrl(12345L));
    }

    @Test
    public void testUrlFollowersById(){
        Assert.assertEquals("https://api.twitter.com/1.1/followers/ids.json?user_id=952253106",
                twitterBot.getUrlHelper().getFollowersUrl(952253106L));
    }

    @Test
    public void testUrlFollowersByName(){
        Assert.assertEquals("https://api.twitter.com/1.1/followers/list.json?screen_name=RedTheOne&count=200",
                twitterBot.getUrlHelper().getFollowersUrl("RedTheOne"));
    }

    @Test
    public void testUrlFollowingsById(){
        Assert.assertEquals("https://api.twitter.com/1.1/friends/ids.json?user_id=952253106",
                twitterBot.getUrlHelper().getFollowingsUrl(952253106L));
    }

    @Test
    public void testUrlFollowingsByName(){
        Assert.assertEquals("https://api.twitter.com/1.1/friends/list.json?screen_name=RedTheOne&count=200",
                twitterBot.getUrlHelper().getFollowingsUrl("RedTheOne"));
    }

    @Test
    public void testUrlLastTweet(){
        Assert.assertEquals("https://api.twitter.com/1.1/statuses/user_timeline.json?",
                twitterBot.getUrlHelper().getLastTweetListUrl());
    }

    @Test
    public void testUrlFriendshipById(){
        Assert.assertEquals("https://api.twitter.com/1.1/friendships/show.json?source_id=12345&target_id=67890",
                twitterBot.getUrlHelper().getFriendshipUrl(12345L,67890L));
    }

    @Test
    public void testUrlFriendshipByName(){
        Assert.assertEquals("https://api.twitter.com/1.1/friendships/show.json?source_screen_name=RedTheOne&target_screen_name=EmmanuelMacron",
                twitterBot.getUrlHelper().getFriendshipUrl("RedTheOne","EmmanuelMacron"));
    }

    @Test
    public void testUrlFollowByName(){
        Assert.assertEquals("https://api.twitter.com/1.1/friendships/create.json?screen_name=RedTheOne&follow=true",
                twitterBot.getUrlHelper().getFollowUrl("RedTheOne"));
    }

    @Test
    public void testUrlUnfollowByName(){
        Assert.assertEquals("https://api.twitter.com/1.1/friendships/destroy.json?screen_name=RedTheOne",
                twitterBot.getUrlHelper().getUnfollowUrl("RedTheOne"));
    }

    @Test
    public void testUrlFollowById(){
        Assert.assertEquals("https://api.twitter.com/1.1/friendships/create.json?user_id=12345&follow=true",
                twitterBot.getUrlHelper().getFollowUrl(12345L));
    }

    @Test
    public void testUrlGetUserById(){
        Assert.assertEquals("https://api.twitter.com/1.1/users/show.json?user_id=12345",
                twitterBot.getUrlHelper().getUserUrl(12345L));
    }

    @Test
    public void testUrlGetTweetInfoById(){
        Assert.assertEquals("https://api.twitter.com/1.1/statuses/show.json?id=12345",
                twitterBot.getUrlHelper().getTweetInfoUrl(12345L));
    }

    @Test
    public void testGetFollowingsById() {
        List<Long> followings = twitterBot.getFollowingsIds(92073489L);
        Assert.assertTrue(followings.size()>1);
    }

    @Test
    public void testGetFollowingsByName() {
        List<String> followings = twitterBot.getFollowingNames("kanyewest");
        Assert.assertTrue(followings.size()>1);
    }

    @Test
    public void testGetFollowingsUserByName() {
        List<User> followings = twitterBot.getFollowingsUserList("davidguetta");
        Assert.assertTrue(followings.size()>1);
    }

    @Test
    public void testGetNbFollowingsById() {
        int result = twitterBot.getNbFollowingsById(919925977777606659L);
        Assert.assertTrue(result>1 && result<500);
    }

    @Test
    public void testGetNbFollowingsByName() {
        int result = twitterBot.getNbFollowings("kanyewest");
        Assert.assertTrue(result>1);
    }

    @Test
    public void testGetFollowersById() {
        String url = twitterBot.getUrlHelper().getFollowersUrl(952253106L);
        List<Long> followers = twitterBot.getFollowersIds(952253106L);
        Assert.assertTrue(followers.size()>1);
    }

    @Test
    public void testGetFollowersByName() {
        List<String> followers = twitterBot.getFollowerNames("kanyewest");
        Assert.assertTrue(followers.size()>1);
    }

    @Test
    public void testGetNbFollowersByName() {
        int result = twitterBot.getNbFollowers("kanyewest");
        Assert.assertTrue(result>1);
    }

    @Test
    public void testGetNbFollowersById() {
        int result = twitterBot.getNbFollowersById(919925977777606659L);
        Assert.assertTrue(result>4999);
    }

    @Test
    public void testGetRetweetersId() {
        List<Long> retweeters = twitterBot.getRetweetersId( 1100473425443860481L);
        Assert.assertTrue(retweeters.size()>1);
    }

    @Test
    public void testFriendshipByIdYes() {
        Long userId1 = 92073489L;
        Long userId2 = 723996356L;
        boolean result = twitterBot.areFriends(userId1, userId2);
        Assert.assertTrue(result);
    }

    @Test
    public void testFriendshipByIdNo() {
        Long userId1 = 92073489L;
        Long userId2 = 1976143068L;
        boolean result = twitterBot.areFriends(userId1, userId2);
        Assert.assertFalse(result);
    }

    @Test
    public void testFollowNew(){
        String userName = "RedouaneBali";
        boolean result = twitterBot.follow(userName);
        Assert.assertTrue(result);
    }

    @Test
    public void testGetUserInfo() {
        Long userId = 92073489L;
        User user = twitterBot.getUserInfoFromUserId(userId);
        Assert.assertEquals("RedTheOne", user.getScreen_name());
    }

    @Test
    public void testGetNbRT() {
        Long tweetId = 925804518662705153L;
        int nbRT = twitterBot.getNbRT(tweetId);
        Assert.assertTrue(nbRT>1000);
    }

}
