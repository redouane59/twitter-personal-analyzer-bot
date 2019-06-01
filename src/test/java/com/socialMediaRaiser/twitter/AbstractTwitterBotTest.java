package com.socialMediaRaiser.twitter;

import com.socialMediaRaiser.RelationType;
import com.socialMediaRaiser.twitter.helpers.GoogleSheetHelper;
import com.socialMediaRaiser.twitter.helpers.IOHelper;
import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;
import com.socialMediaRaiser.twitter.scoring.UserScoringEngine;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbstractTwitterBotTest {

    private AbstractTwitterBot twitterBot = new TwitterBotByInfluencers();

    @BeforeAll
    private static void init(){
        FollowProperties.load();
    }

    @Test
    private void testGetFollowingIdsById() {
        List<Long> followings = twitterBot.getFollowingIds(882266619115864066L);
        assertTrue(followings.size()>360);
    }

    @Test
    private void testGetFollowingIdsByName() {
        List<Long> followings = twitterBot.getFollowingIds("LaGhostquitweet");
        assertTrue(followings.size()>360);
    }

    @Test
    private void testGetFollowingsUserByName() {
        List<User> followings = twitterBot.getFollowingsUsers("LaGhostquitweet");
        assertTrue(followings.size()>360);
    }

    @Test
    private void testGetFollersUserByName() {
        List<User> followings = twitterBot.getFollowerUsers("LaGhostquitweet");
        assertTrue(followings.size()>360);
    }

  /*  @Test
    private void testGetNbFollowingsById() {
        int result = twitterBot.getNbFollowingsById(919925977777606659L);
        assertTrue(result>1 && result<500);
    } */

   /* @Test
    private void testGetNbFollowingsByName() {
        int result = twitterBot.getNbFollowingsByName("kanyewest");
        assertTrue(result>1);
    } */

    @Test
    private void testGetFollowersIdsById() {
        List<Long> followers = twitterBot.getFollowerIds(882266619115864066L);
        assertTrue(followers.size()>420);
    }

    @Test
    private void testGetFollowersIdsByName() {
        List<Long> followers = twitterBot.getFollowerIds("LaGhostquitweet");
        assertTrue(followers.size()>420);
    }

    @Test
    private void testGetFollowersUsersByName() {
        List<User> followers = twitterBot.getFollowerUsers("LaGhostquitweet");
        assertTrue(followers.size()>420);
    }

    @Test
    private void testGetFollowersUsersById() {
        List<User> followers = twitterBot.getFollowerUsers(882266619115864066L);
        assertTrue(followers.size()>420);
    }

    @Test
    private void testFriendshipByIdYes() {
        Long userId1 = 92073489L;
        Long userId2 = 723996356L;
        RelationType result = twitterBot.getRelationType(userId1, userId2);
        assertEquals(RelationType.FRIENDS, result);
    }

    @Test
    private void getUserByUserName(){
        String userName = "RedTheOne";
        User result = twitterBot.getUserFromUserName(userName);
        assertEquals(result.getId(),92073489L);
    }

    @Test
    private void testFriendshipByIdNo() {
        Long userId1 = 92073489L;
        Long userId2 = 1976143068L;
        RelationType result = twitterBot.getRelationType(userId1, userId2);
        assertNotEquals(RelationType.FRIENDS, result);
    }

    @Test
    @Disabled
    private void testFollowNew(){
        String userName = "RedouaneBali";
        boolean result = twitterBot.follow(userName);
        assertTrue(result);
    }

    @Test
    private void testGetUserInfoName() {
        long userId = 92073489L;
        User user = twitterBot.getUserFromUserId(userId);
        assertEquals("RedTheOne", user.getUserName());
    }

    @Test
    private void testGetUserInfoId() {
        long userId = 92073489L;
        User user = twitterBot.getUserFromUserId(userId);
        assertEquals(userId, user.getId());
    }

    @Test
    private void testGetUserInfoFavouritesCount() {
        long userId = 92073489L;
        User user = twitterBot.getUserFromUserId(userId);
        assertTrue(user.getFavouritesCount()>0);
    }

    @Test
    private void testGetUserInfoFavouritesDateOfCreation() {
        long userId = 92073489L;
        User user = twitterBot.getUserFromUserId(userId);
        assertTrue(user.getDateOfCreation()!=null);
    }

    @Test
    private void testGetUserInfoStatusesCount() {
        long userId = 92073489L;
        User user = twitterBot.getUserFromUserId(userId);
        assertTrue(user.getStatusesCount()>0);
    }

    @Test
    private void testGetUserInfoLang() {
        long userId = 92073489L;
        User user = twitterBot.getUserFromUserId(userId);
        user.addLanguageFromLastTweet(twitterBot.getUserLastTweets(userId,2));
        assertEquals("fr",user.getLang());
    }

    @Test
    private void testGetUserInfoLastUpdate() {
        long userId = 92073489L;
        User user = twitterBot.getUserFromUserId(userId);
        assertEquals(userId, user.getId());
        assertTrue(user.getLastUpdate()!=null);
    }

    @Test
    private void testGetUserInfoFollowingRatio() {
        long userId = 92073489L;
        User user = twitterBot.getUserFromUserId(userId);
        assertEquals(userId, user.getId());
        assertTrue(user.getFollowersRatio()>1);
    }

    @Test
    private void testGetUserWithCache() {
        Long userId = 92073489L;
        User user = twitterBot.getUserFromUserId(userId);
        assertEquals("RedTheOne", user.getUserName());
        user = twitterBot.getUserFromUserId(userId);
        assertEquals("RedTheOne", user.getUserName());
    }

    @Test
    private void testGetNbRT() {
        Long tweetId = 925804518662705153L;
        int nbRT = twitterBot.getNbRT(tweetId);
        assertTrue(nbRT>1000);
    }

    @Test
    private void testGetUsersFromUserIds(){
        List<Long> ids = new ArrayList<>();
        ids.add(92073489L); // RedTheOne
        ids.add(22848599L); // Soltana
        List<User> result = twitterBot.getUsersFromUserIds(ids);
        assertEquals("RedTheOne",result.get(0).getUserName());
        assertEquals("Soltana",result.get(1).getUserName());
    }

    @Test
    private void testGetRateLimitStatus(){
       assertNotEquals(null,twitterBot.getRateLimitStatus() );
    }

    @Test
    private void testShouldBeFollowedBadRatio(){
        UserScoringEngine engine = new UserScoringEngine(100);
        User user = new User();
        user.setFollowersCount(1);
        user.setFollowingsCount(1000);
        user.setLastUpdate(new Date());
        user.setLang("fr");
        assertEquals(false, user.shouldBeFollowed());
        assertFalse( engine.shouldBeFollowed(user));
    }

    private void testShouldBeFollowBadLastUpdate(){
        UserScoringEngine engine = new UserScoringEngine(100);
        User user = new User();
        user.setFollowersCount(1500);
        user.setFollowingsCount(1000);
        user.setLang("fr");
        user.setLastUpdate(null);
        assertEquals(false, user.shouldBeFollowed());
        assertFalse( engine.shouldBeFollowed(user));
    }

    private void testShouldBeFollowBadLastUpdate2(){
        UserScoringEngine engine = new UserScoringEngine(100);
        User user = new User();
        user.setFollowersCount(1500);
        user.setFollowingsCount(1000);
        user.setLang("fr");
        user.setLastUpdate(new Date(2014,  1, 1));
        assertEquals(false, user.shouldBeFollowed());
        assertFalse( engine.shouldBeFollowed(user));
    }

    private void testShouldBeFollowedOk(){
        UserScoringEngine engine = new UserScoringEngine(100);
        User user = new User();
        user.setFollowersCount(1500);
        user.setFollowingsCount(1000);
        user.setLang("fr");
        user.setLastUpdate(new Date());
        assertEquals(true, user.shouldBeFollowed());
        assertFalse( engine.shouldBeFollowed(user));
    }



    private void testReadFollowedRecently(){
        List<Long> result = twitterBot.getFollowedRecently();
        assertTrue(result.size()>100);
    }

    @Test
    private void testHashCode(){
        User user = User.builder().id(12345L).build();
        User user2 = User.builder().id(23456L).build();
        assertNotEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    @Disabled
    private void testWritingOnGoogleSheet() {
        User user = twitterBot.getUserFromUserName("RedTheOne");
        GoogleSheetHelper helper = new GoogleSheetHelper();
        helper.addNewFollowerLine(user);
    }

    @Test
    private void testRelationBetweenUsersIdFriends(){
        long userId1 = 92073489L;
        long userId2 = 723996356L;
        RelationType result = twitterBot.getRelationType(userId1, userId2);
        assertEquals(RelationType.FRIENDS, result);
    }

    @Test
    private void testRelationBetweenUsersIdNone(){
        long userId1 = 92073489L;
        long userId2 = 1976143068L;
        RelationType result = twitterBot.getRelationType(userId1, userId2);
        assertEquals(RelationType.NONE, result);
    }

    @Test
    private void testRelationBetweenUsersIdFollowing(){
        long userId1 = 92073489L;
        long userId2 = 126267113L;
        RelationType result = twitterBot.getRelationType(userId1, userId2);
        assertEquals(RelationType.FOLLOWING, result);
    }

    @Test
    private void testRelationBetweenUsersIdFollower(){
        long userId1 = 92073489L;
        long userId2 = 1128737441945464832L;
        RelationType result = twitterBot.getRelationType(userId1, userId2);
        assertEquals(RelationType.FOLLOWER, result);
    }

    @Test
    @Disabled // API KO
    private void testGetRetweetersId(){
        Long tweetId = 1078358350000205824L;
        assertTrue(twitterBot.getRetweetersId(tweetId).size()>400);
    }

    @Test
    private void getLastUpdate(){
        long userId = 92073489L;
        User user = twitterBot.getUserFromUserId(userId);
        Date now = new Date();
        Date lastUpdate = user.getLastUpdate();
        long diffDays = (now.getTime()-lastUpdate.getTime()) / (24 * 60 * 60 * 1000);
        assertTrue(  diffDays < 15);
    }


    @Test
    private void testUserDiffDate0(){
        User user = User.builder()
                .dateOfFollow(new Date())
                .lastUpdate(new Date())
                .build();
        assertEquals(0, user.getDaysBetweenFollowAndLastUpdate());
    }

    @Test
    private void testUserDiffDate7(){
        final Calendar lastUpdate = Calendar.getInstance();
        lastUpdate.add(Calendar.DATE, -7);
        User user = User.builder()
                .dateOfFollow(new Date())
                .lastUpdate(lastUpdate.getTime())
                .build();
        assertEquals(7, user.getDaysBetweenFollowAndLastUpdate());
    }

    @Test
    private void testGetLastTweetByUserName(){
        String userName = "RedTheOne";
        List<Tweet> response = twitterBot.getUserLastTweets(userName,2 );
        assertTrue(response.get(0).getLang().equals("fr")
        || response.get(1).getLang().equals("fr"));
    }

    @Test
    private void testGetLastTweetByUserId(){
        Long userId = 92073489L;
        List<Tweet> response = twitterBot.getUserLastTweets(userId, 2);
        assertTrue(response.get(0).getLang().equals("fr")
                || response.get(1).getLang().equals("fr"));
    }

    @Test
    @Disabled
    private void testStreaming() throws IOException, InterruptedException {
        TwitterStreamCollector collector = new TwitterStreamCollector();
        collector.collect();
    }

    @Test
    public void testSearchForTweets(){
        int count = 10;
        List<Tweet> results = twitterBot.searchForTweets("redtheone", count, "201906010000","201906011200");
        assertNotNull(results);
        assertTrue(results.size()==count);
    }

}