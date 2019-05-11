package com.socialMediaRaiser.twitter;

import com.socialMediaRaiser.twitter.helpers.GoogleSheetHelper;
import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AbstractTwitterBotTest {

    private AbstractTwitterBot twitterBot = new TwitterBotByInfluencers();

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
        Assert.assertEquals("https://api.twitter.com/1.1/friendships/create.json?screen_name=RedTheOne",
                twitterBot.getUrlHelper().getFollowUrl("RedTheOne"));
    }

    @Test
    public void testUrlUnfollowByName(){
        Assert.assertEquals("https://api.twitter.com/1.1/friendships/destroy.json?screen_name=RedTheOne",
                twitterBot.getUrlHelper().getUnfollowUrl("RedTheOne"));
    }

    @Test
    public void testUrlFollowById(){
        Assert.assertEquals("https://api.twitter.com/1.1/friendships/create.json?user_id=12345",
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

    @Test
    public void testUrlGetRateLimitStatus(){
        Assert.assertEquals("https://api.twitter.com/1.1/application/rate_limit_status.json",
                twitterBot.getUrlHelper().getRateLimitUrl());
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
    public void testGetUserInfoName() {
        long userId = 92073489L;
        User user = twitterBot.getUserFromUserId(userId);
        Assert.assertEquals("RedTheOne", user.getUserName());
    }

    @Test
    public void testGetUserInfoId() {
        long userId = 92073489L;
        User user = twitterBot.getUserFromUserId(userId);
        Assert.assertEquals(userId, user.getId());
    }

    @Test
    public void testGetUserInfoFavouritesCount() {
        long userId = 92073489L;
        User user = twitterBot.getUserFromUserId(userId);
        Assert.assertTrue(user.getFavouritesCount()>0);
    }

    @Test
    public void testGetUserInfoFavouritesDateOfCreation() {
        long userId = 92073489L;
        User user = twitterBot.getUserFromUserId(userId);
        Assert.assertTrue(user.getDateOfCreation()!=null);
    }

    @Test
    public void testGetUserInfoStatusesCount() {
        long userId = 92073489L;
        User user = twitterBot.getUserFromUserId(userId);
        Assert.assertTrue(user.getStatusesCount()>0);
    }

    @Test
    public void testGetUserInfoLang() {
        long userId = 92073489L;
        User user = twitterBot.getUserFromUserId(userId);
        Assert.assertEquals("en",user.getLang());
    }

    @Test
    public void testGetUserInfoLastUpdate() {
        long userId = 92073489L;
        User user = twitterBot.getUserFromUserId(userId);
        Assert.assertEquals(userId, user.getId());
        Assert.assertTrue(user.getLastUpdate()!=null);
    }

    @Test
    public void testGetUserInfoFollowingRatio() {
        long userId = 92073489L;
        User user = twitterBot.getUserFromUserId(userId);
        Assert.assertEquals(userId, user.getId());
        Assert.assertTrue(user.getFollowersRatio()>1);
    }

    @Test
    public void testGetUserWithCache() {
        Long userId = 92073489L;
        User user = twitterBot.getUserFromUserId(userId);
        Assert.assertEquals("RedTheOne", user.getUserName());
        user = twitterBot.getUserFromUserId(userId);
        Assert.assertEquals("RedTheOne", user.getUserName());
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
        Assert.assertEquals("RedTheOne",result.get(0).getUserName());
        Assert.assertEquals("Soltana",result.get(1).getUserName());
    }

    @Test
    public void testGetRateLimitStatus(){
       Assert.assertNotEquals(null,twitterBot.getRateLimitStatus() );
    }

    @Test
    public void testShouldBeFollowed(){
        User user = new User();
        user.setFollowersCount(1);
        user.setFollowingCount(1000);
        Assert.assertEquals(false, user.shouldBeFollowed());
        User user2 = new User();
        user2.setFollowersCount(1500);
        user2.setFollowingCount(1000);
        user2.setLang("fr");
        user2.setLastUpdate(new Date());
        Assert.assertEquals(true, user2.shouldBeFollowed());
    }

  //  @Test @TODO
 /*   public void testReadFollowedRecently(){
        List<Long> result = twitterBot.getFollowedRecently();
        Assert.assertTrue(result.size()>100);
    } */

    @Test
    public void testHashCode(){
        User user = User.builder().id(12345L).build();
        User user2 = User.builder().id(23456L).build();
        Assert.assertNotEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    public void testWritingOnGoogleSheet() {
        User user = twitterBot.getUserFromUserName("RedTheOne");
        GoogleSheetHelper helper = new GoogleSheetHelper();
        helper.addNewFollowerLine(user);
    }
}
