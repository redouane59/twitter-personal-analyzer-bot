package com.socialMediaRaiser.twitter;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TwitterBotTest {

    private TwitterBot twitterBot = new TwitterBot();

    /* ******************** */
    /* ***** URL tests **** */
    /* ******************** */

    @Test
    public void testUrlRetweetrs(){
        Assert.assertEquals("https://api.twitter.com/1.1/statuses/retweeters/ids.json?id=12345&count=90", twitterBot.getUrlHelper().getRetweetersUrl(12345L));
    }

    @Test
    public void testUrlFollowersById(){
        Assert.assertEquals("https://api.twitter.com/1.1/followers/ids.json?user_id=952253106",
                twitterBot.getUrlHelper().getFollowerIdsUrl(952253106L));
    }

    @Test
    public void testUrlFollowersByName(){
        Assert.assertEquals("https://api.twitter.com/1.1/followers/list.json?screen_name=RedTheOne&count=200",
                twitterBot.getUrlHelper().getFollowerUsersUrl("RedTheOne"));
    }

    @Test
    public void testUrlFollowingsById(){
        Assert.assertEquals("https://api.twitter.com/1.1/friends/ids.json?user_id=952253106",
                twitterBot.getUrlHelper().getFollowingIdsUrl(952253106L));
    }

    @Test
    public void testUrlFollowingsByName(){
        Assert.assertEquals("https://api.twitter.com/1.1/friends/list.json?screen_name=RedTheOne&count=200",
                twitterBot.getUrlHelper().getFollowingUsersUrl("RedTheOne"));
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
    public void testUrlGetUsersByNames(){
        List<String> names = new ArrayList<>();
        names.add("RedTheOne");
        names.add("Ronaldo");
        names.add("Zidane");
        Assert.assertEquals("https://api.twitter.com/1.1/users/lookup.json?screen_name=RedTheOne,Ronaldo,Zidane",
                twitterBot.getUrlHelper().getUsersUrlbyNames(names));
    }

    @Test
    public void testUrlGetUsersByIds(){
        List<Long> ids = new ArrayList<>();
        ids.add(12345L);
        ids.add(23456L);
        ids.add(34567L);
        Assert.assertEquals("https://api.twitter.com/1.1/users/lookup.json?user_id=12345,23456,34567",
                twitterBot.getUrlHelper().getUsersUrlbyIds(ids));
    }

    @Test
    public void testUrlGetTweetInfoById(){
        Assert.assertEquals("https://api.twitter.com/1.1/statuses/show.json?id=12345",
                twitterBot.getUrlHelper().getTweetInfoUrl(12345L));
    }

    /* ******************** */
    /* ** Function tests ** */
    /* ******************* */

    @Test
    public void testGetFollowingIdsById() {
        List<Long> followings = twitterBot.getFollowingIds(882266619115864066L);
        Assert.assertTrue(followings.size()>360);
    }

    @Test
    public void testGetFollowingIdsByName() {
        List<Long> followings = twitterBot.getFollowingIds("LaGhostquitweet");
        Assert.assertTrue(followings.size()>360);
    }

    @Test
    public void testGetFollowingsNamesByName() {
        List<String> followings = twitterBot.getFollowingNames("LaGhostquitweet");
        Assert.assertTrue(followings.size()>360);
    }

    @Test
    public void testGetFollowingsUserByName() {
        List<User> followings = twitterBot.getFollowingsUserList("LaGhostquitweet");
        Assert.assertTrue(followings.size()>360);
    }

  /*  @Test
    public void testGetNbFollowingsById() {
        int result = twitterBot.getNbFollowingsById(919925977777606659L);
        Assert.assertTrue(result>1 && result<500);
    } */

   /* @Test
    public void testGetNbFollowingsByName() {
        int result = twitterBot.getNbFollowingsByName("kanyewest");
        Assert.assertTrue(result>1);
    } */

    @Test
    public void testGetFollowersIdsById() {
        List<Long> followers = twitterBot.getFollowerIds(882266619115864066L);
        Assert.assertTrue(followers.size()>420);
    }

    @Test
    public void testGetFollowersNamesByName() {
        List<String> followers = twitterBot.getFollowerNames("LaGhostquitweet");
        Assert.assertTrue(followers.size()>420);
    }

    @Test
    public void testGetFollowersUsersByName() {
        List<User> followers = twitterBot.getFollowerUsers("LaGhostquitweet");
        Assert.assertTrue(followers.size()>420);
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
    @Ignore
    public void testFollowNew(){
        String userName = "RedouaneBali";
        boolean result = twitterBot.follow(userName);
        Assert.assertTrue(result);
    }

    @Test
    public void testGetUserInfo() {
        Long userId = 92073489L;
        User user = twitterBot.getUserFromUserId(userId);
        Assert.assertEquals("RedTheOne", user.getScreen_name());
    }

    @Test
    public void testGetNbRT() {
        Long tweetId = 925804518662705153L;
        int nbRT = twitterBot.getNbRT(tweetId);
        Assert.assertTrue(nbRT>1000);
    }

    @Test
    public void testGetUsersFromUserIds(){
        List<Long> ids = new ArrayList<>();
        ids.add(92073489L); // RedTheOne
        ids.add(22848599L); // Soltana
        List<User> result = twitterBot.getUsersFromUserIds(ids);
        Assert.assertEquals("RedTheOne",result.get(0).getScreen_name());
        Assert.assertEquals("Soltana",result.get(1).getScreen_name());
    }

}
