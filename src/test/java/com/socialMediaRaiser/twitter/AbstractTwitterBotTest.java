package com.socialMediaRaiser.twitter;

import com.socialMediaRaiser.RelationType;
import com.socialMediaRaiser.twitter.scoring.UserScoringEngine;
import com.socialMediaRaiser.twitter.helpers.GoogleSheetHelper;
import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AbstractTwitterBotTest {

    private AbstractTwitterBot twitterBot = new TwitterBotByInfluencers();

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
    public void testGetFollowingsUserByName() {
        List<User> followings = twitterBot.getFollowingsUserList("LaGhostquitweet", false);
        Assert.assertTrue(followings.size()>360);
    }

    @Test
    public void testGetFollersUserByName() {
        List<User> followings = twitterBot.getFollowerUsers("LaGhostquitweet");
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
    public void testGetFollowersIdsByName() {
        List<Long> followers = twitterBot.getFollowerIds("LaGhostquitweet");
        Assert.assertTrue(followers.size()>420);
    }

    @Test
    public void testGetFollowersUsersByName() {
        List<User> followers = twitterBot.getFollowerUsers("LaGhostquitweet");
        Assert.assertTrue(followers.size()>420);
    }

    @Test
    public void testGetFollowersUsersById() {
        List<User> followers = twitterBot.getFollowerUsers(882266619115864066L, false);
        Assert.assertTrue(followers.size()>420);
    }

    @Test
    public void testFriendshipByIdYes() {
        Long userId1 = 92073489L;
        Long userId2 = 723996356L;
        RelationType result = twitterBot.getRelationType(userId1, userId2);
        Assert.assertEquals(RelationType.FRIENDS, result);
    }

    @Test
    public void getUserByUserName(){
        String userName = "RedTheOne";
        User result = twitterBot.getUserFromUserName(userName);
        Assert.assertEquals(result.getId(),92073489L);
    }

    @Test
    public void testFriendshipByIdNo() {
        Long userId1 = 92073489L;
        Long userId2 = 1976143068L;
        RelationType result = twitterBot.getRelationType(userId1, userId2);
        Assert.assertNotEquals(RelationType.FRIENDS, result);
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
    public void testShouldBeFollowedBadRatio(){
        UserScoringEngine engine = new UserScoringEngine(100);
        User user = new User();
        user.setFollowersCount(1);
        user.setFollowingCount(1000);
        user.setLastUpdate(new Date());
        user.setLang("fr");
        Assert.assertEquals(false, user.shouldBeFollowed());
        Assert.assertFalse( engine.shouldBeFollowed(user));
    }

    public void testShouldBeFollowBadLastUpdate(){
        UserScoringEngine engine = new UserScoringEngine(100);
        User user = new User();
        user.setFollowersCount(1500);
        user.setFollowingCount(1000);
        user.setLang("fr");
        user.setLastUpdate(null);
        Assert.assertEquals(false, user.shouldBeFollowed());
        Assert.assertFalse( engine.shouldBeFollowed(user));
    }

    public void testShouldBeFollowBadLastUpdate2(){
        UserScoringEngine engine = new UserScoringEngine(100);
        User user = new User();
        user.setFollowersCount(1500);
        user.setFollowingCount(1000);
        user.setLang("fr");
        user.setLastUpdate(new Date(2014,  1, 1));
        Assert.assertEquals(false, user.shouldBeFollowed());
        Assert.assertFalse( engine.shouldBeFollowed(user));
    }

    public void testShouldBeFollowedOk(){
        UserScoringEngine engine = new UserScoringEngine(100);
        User user = new User();
        user.setFollowersCount(1500);
        user.setFollowingCount(1000);
        user.setLang("fr");
        user.setLastUpdate(new Date());
        Assert.assertEquals(true, user.shouldBeFollowed());
        Assert.assertFalse( engine.shouldBeFollowed(user));
    }



    public void testReadFollowedRecently(){
        List<Long> result = twitterBot.getFollowedRecently();
        Assert.assertTrue(result.size()>100);
    }

    @Test
    public void testHashCode(){
        User user = User.builder().id(12345L).build();
        User user2 = User.builder().id(23456L).build();
        Assert.assertNotEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    @Ignore
    public void testWritingOnGoogleSheet() {
        User user = twitterBot.getUserFromUserName("RedTheOne");
        GoogleSheetHelper helper = new GoogleSheetHelper();
        helper.addNewFollowerLine(user);
    }

    @Test
    public void testRelationBetweenUsersIdFriends(){
        long userId1 = 92073489L;
        long userId2 = 723996356L;
        RelationType result = twitterBot.getRelationType(userId1, userId2);
        Assert.assertEquals(RelationType.FRIENDS, result);
    }

    @Test
    public void testRelationBetweenUsersIdNone(){
        long userId1 = 92073489L;
        long userId2 = 1976143068L;
        RelationType result = twitterBot.getRelationType(userId1, userId2);
        Assert.assertEquals(RelationType.NONE, result);
    }

    @Test
    public void testRelationBetweenUsersIdFollowing(){
        long userId1 = 92073489L;
        long userId2 = 126267113L;
        RelationType result = twitterBot.getRelationType(userId1, userId2);
        Assert.assertEquals(RelationType.FOLLOWING, result);
    }

    @Test
    public void testRelationBetweenUsersIdFollower(){
        long userId1 = 92073489L;
        long userId2 = 1128737441945464832L;
        RelationType result = twitterBot.getRelationType(userId1, userId2);
        Assert.assertEquals(RelationType.FOLLOWER, result);
    }

    @Test
    @Ignore // API KO
    public void testGetRetweetersId(){
        Long tweetId = 1078358350000205824L;
        Assert.assertTrue(twitterBot.getRetweetersId(tweetId).size()>400);
    }

    @Test
    public void getLastUpdate(){
        long userId = 92073489L;
        User user = twitterBot.getUserFromUserId(userId);
        Date now = new Date();
        Date lastUpdate = user.getLastUpdate();
        long diffDays = (now.getTime()-lastUpdate.getTime()) / (24 * 60 * 60 * 1000);
        Assert.assertTrue(  diffDays < 15);
    }

    @Test
    public void testGetPreviouslyFollowedIdsAll(){
        List<Long> result = twitterBot.getIOHelper().getPreviouslyFollowedIds();
        Assert.assertTrue(result.size()>200);
    }

    @Test
    public void testGetPreviouslyFollowedIdsByDate(){
        Date date = new Date();
        date.setDate(18);
        date.setMonth(4);
        List<Long> result = twitterBot.getIOHelper().getPreviouslyFollowedIds(true, true, date);
        Assert.assertTrue(result.size()>250);
    }

    @Test
    public void testUserDiffDate0(){
        User user = User.builder()
                .dateOfFollow(new Date())
                .lastUpdate(new Date())
                .build();
        Assert.assertEquals(0, user.getDaysBetweenFollowAndLastUpdate());
    }

    @Test
    public void testUserDiffDate7(){
        final Calendar lastUpdate = Calendar.getInstance();
        lastUpdate.add(Calendar.DATE, -7);
        User user = User.builder()
                .dateOfFollow(new Date())
                .lastUpdate(lastUpdate.getTime())
                .build();
        Assert.assertEquals(7, user.getDaysBetweenFollowAndLastUpdate());
    }

    @Test
    public void testGetLastTweetByUserName(){
        String userName = "RedTheOne";
        Tweet response = twitterBot.getUserLastTweet(userName);
        Assert.assertEquals(response.getLang(), "fr");
    }

    @Test
    public void testGetLastTweetByUserId(){
        Long userId = 92073489L;
        Tweet response = twitterBot.getUserLastTweet(userId);
        Assert.assertEquals(response.getLang(), "fr");
    }
}
