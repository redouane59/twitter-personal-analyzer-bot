package com.socialMediaRaiser.twitter;

import com.socialMediaRaiser.RelationType;
import com.socialMediaRaiser.twitter.helpers.GoogleSheetHelper;
import com.socialMediaRaiser.twitter.helpers.dto.IUser;
import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;
import com.socialMediaRaiser.twitter.scoring.UserScoringEngine;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AbstractTwitterBotTest {

    private static String ownerName = "RedTheOne";
    private AbstractTwitterBot twitterBot = new TwitterBotByInfluencers(ownerName);

    @BeforeAll
    public static void init() {
        FollowProperties.load(ownerName);
    }

    @Test
    public void testGetFollowingIdsById() {
        List<String> followings = twitterBot.getFollowingIds("882266619115864066");
        assertTrue(followings.size() > 200);
    }


    @Test
    public void testGetFollowingsUserByName() {
        List<IUser> followings = twitterBot.getFollowingsUsers("LaGhostquitweet");
        assertTrue(followings.size() > 200);
    }

    @Test
    public void testGetFollersUserByName() {
        List<IUser> followings = twitterBot.getFollowerUsers("LaGhostquitweet");
        assertTrue(followings.size() > 360);
    }

    @Test
    public void testGetFollowersIdsById() {
        List<String> followers = twitterBot.getFollowerIds("882266619115864066");
        assertTrue(followers.size() > 420);
    }

    @Test
    public void testGetFollowersUsersByName() {
        List<IUser> followers = twitterBot.getFollowerUsers("LaGhostquitweet");
        assertTrue(followers.size() > 420);
    }

    @Test
    public void testGetFollowersUsersById() {
        List<IUser> followers = twitterBot.getFollowerUsers("882266619115864066");
        assertTrue(followers.size() > 420);
    }

    @Test
    public void testFriendshipByIdYes() {
        String userId1 = "92073489";
        String userId2 = "723996356";
        RelationType result = twitterBot.getRelationType(userId1, userId2);
        assertEquals(RelationType.FRIENDS, result);
    }

    @Test
    public void getUserByUserName() {
        String userName = "RedTheOne";
        IUser result = twitterBot.getUserFromUserName(userName);
        assertEquals(result.getId(), "92073489");
    }

    @Test
    public void testFriendshipByIdNo() {
        String userId1 = "92073489";
        String userId2 = "1976143068";
        RelationType result = twitterBot.getRelationType(userId1, userId2);
        assertNotEquals(RelationType.FRIENDS, result);
    }

    @Test
    public void testGetUserInfoName() {
        String userId = "92073489";
        IUser user = twitterBot.getUserFromUserId(userId);
        assertEquals("RedTheOne", user.getUsername());
    }

    @Test
    public void testGetUserInfoId() {
        String userId = "92073489";
        IUser user = twitterBot.getUserFromUserId(userId);
        assertEquals(userId, user.getId());
    }

    @Test
    public void testGetUserInfoFavouritesDateOfCreation() {
        String userId = "92073489";
        IUser user = twitterBot.getUserFromUserId(userId);
        assertTrue(user.getDateOfCreation() != null);
    }

    @Test
    public void testGetUserInfoStatusesCount() {
        String userId = "92073489";
        IUser user = twitterBot.getUserFromUserId(userId);
        assertTrue(user.getTweetCount() > 0);
    }

    @Test
    public void testGetUserInfoLang() {
        String userId = "92073489";
        User user = (User)twitterBot.getUserFromUserId(userId);
        user.addLanguageFromLastTweet(twitterBot.getUserLastTweets(userId, 3));
        assertEquals("fr", user.getLang());
    }

    @Test
    public void testGetUserInfoLastUpdate() {
        String userId = "92073489";
        IUser user = twitterBot.getUserFromUserId(userId);
        assertEquals(userId, user.getId());
        assertTrue(user.getLastUpdate() != null);
    }

    @Test
    public void testGetUserInfoFollowingRatio() {
        String userId = "92073489";
        User user = (User)twitterBot.getUserFromUserId(userId);
        assertEquals(userId, user.getId());
        assertTrue(user.getFollowersRatio() > 1);
    }

    @Test
    public void testGetUserWithCache() {
        String userId = "92073489";
        IUser user = twitterBot.getUserFromUserId(userId);
        assertEquals("RedTheOne", user.getUsername());
        user = twitterBot.getUserFromUserId(userId);
        assertEquals("RedTheOne", user.getUsername());
    }

    @Test
    public void testGetUsersFromUserIds() {
        List<String> ids = new ArrayList<>();
        ids.add("92073489"); // RedTheOne
        ids.add("22848599"); // Soltana
        List<IUser> result = twitterBot.getUsersFromUserIds(ids);
        assertEquals("RedTheOne", result.get(0).getUsername());
        assertEquals("Soltana", result.get(1).getUsername());
    }

    @Test
    public void testGetRateLimitStatus() {
        assertNotEquals(null, twitterBot.getRateLimitStatus());
    }

    @Test
    public void testShouldBeFollowedBadRatio() {
        UserScoringEngine engine = new UserScoringEngine(100);
        User user = new User();
        user.setFollowersCount(1);
        user.setFollowingCount(1000);
        user.setLastUpdate(new Date());
        user.setLang("fr");
        assertEquals(false, user.shouldBeFollowed(ownerName));
        assertFalse(engine.shouldBeFollowed(user));
    }

    public void testShouldBeFollowBadLastUpdate() {
        UserScoringEngine engine = new UserScoringEngine(100);
        User user = new User();
        user.setFollowersCount(1500);
        user.setFollowingCount(1000);
        user.setLang("fr");
        user.setLastUpdate(null);
        assertEquals(false, user.shouldBeFollowed(ownerName));
        assertFalse(engine.shouldBeFollowed(user));
    }

    public void testShouldBeFollowBadLastUpdate2() {
        UserScoringEngine engine = new UserScoringEngine(100);
        User user = new User();
        user.setFollowersCount(1500);
        user.setFollowingCount(1000);
        user.setLang("fr");
        user.setLastUpdate(new Date(2014, 1, 1));
        assertEquals(false, user.shouldBeFollowed(ownerName));
        assertFalse(engine.shouldBeFollowed(user));
    }

    public void testShouldBeFollowedOk() {
        UserScoringEngine engine = new UserScoringEngine(100);
        User user = new User();
        user.setFollowersCount(1500);
        user.setFollowingCount(1000);
        user.setLang("fr");
        user.setLastUpdate(new Date());
        assertEquals(true, user.shouldBeFollowed(ownerName));
        assertFalse(engine.shouldBeFollowed(user));
    }

    @Test
    public void testHashCode() {
        User user = User.builder().id("12345").build();
        User user2 = User.builder().id("23456").build();
        assertNotEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    @Disabled
    public void testWritingOnGoogleSheet() {
        User user = (User)twitterBot.getUserFromUserName("RedTheOne");
        GoogleSheetHelper helper = new GoogleSheetHelper(ownerName);
        helper.addNewFollowerLine(user);
    }

    @Test
    public void testRelationBetweenUsersIdFriends() {
        String userId1 = "92073489";
        String userId2 = "723996356";
        RelationType result = twitterBot.getRelationType(userId1, userId2);
        assertEquals(RelationType.FRIENDS, result);
    }

    @Test
    public void testRelationBetweenUsersIdNone() {
        String userId1 = "92073489";
        String userId2 = "1976143068";
        RelationType result = twitterBot.getRelationType(userId1, userId2);
        assertEquals(RelationType.NONE, result);
    }

    @Test
    public void testRelationBetweenUsersIdFollowing() {
        String userId1 = "92073489";
        String userId2 = "126267113";
        RelationType result = twitterBot.getRelationType(userId1, userId2);
        assertEquals(RelationType.FOLLOWING, result);
    }

    @Test
    public void testRelationBetweenUsersIdFollower() {
        String userId1 = "92073489";
        String userId2 = "1128737441945464832";
        RelationType result = twitterBot.getRelationType(userId1, userId2);
        assertEquals(RelationType.FOLLOWER, result);
    }

    @Test
    @Disabled // API KO
    public void testGetRetweetersId() {
        String tweetId = "1078358350000205824";
        assertTrue(twitterBot.getRetweetersId(tweetId).size() > 400);
    }

    @Test
    public void getLastUpdate() {
        String userId = "92073489";
        IUser user = twitterBot.getUserFromUserId(userId);
        Date now = new Date();
        Date lastUpdate = user.getLastUpdate();
        long diffDays = (now.getTime() - lastUpdate.getTime()) / (24 * 60 * 60 * 1000);
        assertTrue(diffDays < 15);
    }

    @Test
    public void getMostRecentTweets(){
        String userId = "92073489";
        User user = (User)twitterBot.getUserFromUserId(userId);
        assertTrue(user.getMostRecentTweet().size()>0);
    }


    @Test
    public void testUserDiffDate0() {
        User user = User.builder()
                .dateOfFollow(new Date())
                .lastUpdate(new Date())
                .build();
        assertEquals(0, user.getDaysBetweenFollowAndLastUpdate());
    }

    @Test
    public void testUserDiffDate7() {
        final Calendar lastUpdate = Calendar.getInstance();
        lastUpdate.add(Calendar.DATE, -7);
        User user = User.builder()
                .dateOfFollow(new Date())
                .lastUpdate(lastUpdate.getTime())
                .build();
        assertEquals(7, user.getDaysBetweenFollowAndLastUpdate());
    }

    @Test
    public void testGetLastTweetByUserName() {
        String userName = "RedTheOne";
        List<Tweet> response = twitterBot.getUserLastTweets(userName, 2);
        assertTrue(response.get(0).getLang().equals("fr")
                || response.get(1).getLang().equals("fr"));
    }

    @Test
    public void testGetLastTweetByUserId() {
        String userId = "92073489";
        List<Tweet> response = twitterBot.getUserLastTweets(userId, 3);
        assertTrue(response.get(0).getLang().equals("fr")
                || response.get(1).getLang().equals("fr"));
    }

    @Test
    @Disabled
    public void testSearchForTweets() {
        int count = 10;
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmm");
        String strdate1 = "201906010000";
        String strdate2 = "201906011200";
        List<Tweet> results = null;
        try {
            results = twitterBot.searchForTweets("redtheone", count, dateformat.parse(strdate1), dateformat.parse(strdate2));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertNotNull(results);
        assertTrue(results.size() == count);
    }

    @Test
    public void testIsUserInfluencer(){
        FollowProperties.targetProperties.setDescription("java");
        FollowProperties.targetProperties.setLocation("France");
        User user = User.builder().location("France").description("java").build();
        assertTrue(user.isInfluencer());
        user = User.builder().location("Senegal").description("java").build();
        assertFalse(user.isInfluencer());
        user = User.builder().location("Senegal").description("cool").build();
        assertFalse(user.isInfluencer());
        user = User.builder().location("France").description("cool").build();
        assertFalse(user.isInfluencer());
    }
}