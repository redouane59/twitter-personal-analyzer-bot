package integration;

import com.socialmediaraiser.core.RelationType;
import com.socialmediaraiser.core.twitter.AbstractTwitterBot;
import com.socialmediaraiser.core.twitter.FollowProperties;
import com.socialmediaraiser.core.twitter.Tweet;
import com.socialmediaraiser.core.twitter.User;
import com.socialmediaraiser.core.twitter.helpers.dto.getuser.AbstractUser;
import com.socialmediaraiser.twitterbot.impl.TwitterBotByInfluencers;
import com.socialmediaraiser.core.twitter.scoring.UserScoringEngine;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AbstractTwitterBotTest {

    private static String ownerName = "RedouaneBali";
    private AbstractTwitterBot twitterBot = new TwitterBotByInfluencers(ownerName, false, false);

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
    @Disabled
    public void testGetFollowingsUserByName() {
        List<AbstractUser> followings = twitterBot.getFollowingsUsers("LaGhostquitweet");
        assertTrue(followings.size() > 200);
    }

    @Test
    @Disabled
    public void testGetFollersUserByName() {
        List<AbstractUser> followings = twitterBot.getFollowerUsers("LaGhostquitweet");
        assertTrue(followings.size() > 360);
    }

    @Test
    @Disabled
    public void testGetFollowersIdsById() {
        List<String> followers = twitterBot.getFollowerIds("882266619115864066");
        assertTrue(followers.size() > 420);
    }

    @Test
    public void testGetFollowersUsersByName() {
        List<AbstractUser> followers = twitterBot.getFollowerUsers("LaGhostquitweet");
        assertTrue(followers.size() > 420);
    }

    @Test
    @Disabled
    public void testGetFollowersUsersById() {
        List<AbstractUser> followers = twitterBot.getFollowerUsers("882266619115864066");
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
        AbstractUser result = twitterBot.getUserFromUserName(userName);
        assertEquals("92073489", result.getId());
        userName = "RedouaneBali";
        result = twitterBot.getUserFromUserName(userName);
        assertEquals("RedouaneBali", result.getUsername());
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
        AbstractUser user = twitterBot.getUserFromUserId(userId);
        assertEquals("RedTheOne", user.getUsername());
    }

    @Test
    public void testGetUserInfoId() {
        String userId = "92073489";
        AbstractUser user = twitterBot.getUserFromUserId(userId);
        assertEquals(userId, user.getId());
    }

    @Test
    public void testGetUserInfoFavouritesDateOfCreation() {
        String userId = "92073489";
        AbstractUser user = twitterBot.getUserFromUserId(userId);
        assertTrue(user.getDateOfCreation() != null);
    }

    @Test
    public void testGetUserInfoStatusesCount() {
        String userId = "92073489";
        AbstractUser user = twitterBot.getUserFromUserId(userId);
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
        AbstractUser user = twitterBot.getUserFromUserId(userId);
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
        AbstractUser user = twitterBot.getUserFromUserId(userId);
        assertEquals("RedTheOne", user.getUsername());
        user = twitterBot.getUserFromUserId(userId);
        assertEquals("RedTheOne", user.getUsername());
    }

    @Test
    public void testGetUsersFromUserIds() {
        List<String> ids = new ArrayList<>();
        ids.add("92073489"); // RedTheOne
        ids.add("22848599"); // Soltana
        List<AbstractUser> result = twitterBot.getUsersFromUserIds(ids);
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
        User user = User.builder()
                .followersCout(1)
                .followingCount(1000)
                .lastUpdate(new Date())
                .lang("fr")
                .build();
        assertEquals(false, user.shouldBeFollowed(ownerName));
        assertFalse(engine.shouldBeFollowed(user));
    }

    @Test
    public void testShouldBeFollowBadLastUpdate() {
        UserScoringEngine engine = new UserScoringEngine(100);
        User user = User.builder()
                .followersCout(1500)
                .followingCount(1000)
                .lang("fr")
                .lastUpdate(null)
                .build();
        user.setScoringEngine(new UserScoringEngine(65));
        assertEquals(false, user.shouldBeFollowed(ownerName));
        assertFalse(engine.shouldBeFollowed(user));
    }


    @Test
    public void testShouldBeFollowedOk() {
        UserScoringEngine engine = new UserScoringEngine(100);
        User user = new User();
        user.setFollowersCount(1500);
        user.setFollowingCount(1000);
        user.setLang("fr");
        user.setLastUpdate(new Date());
        user.setScoringEngine(engine);
        user.setLocation("Paris");
        user.setCommonFollowers(20); // @todo not use redtheone config
        assertTrue(user.shouldBeFollowed(ownerName));
        assertTrue(engine.shouldBeFollowed(user));
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
        AbstractUser user = twitterBot.getUserFromUserId(userId);
        Date now = new Date();
        Date lastUpdate = user.getLastUpdate();
        long diffDays = (now.getTime() - lastUpdate.getTime()) / (24 * 60 * 60 * 1000);
        assertTrue(diffDays < 15);
    }

    @Test
    public void getMostRecentTweets(){
        String userId = "92073489";
        AbstractUser user = twitterBot.getUserFromUserId(userId);
        assertFalse(user.getMostRecentTweet().isEmpty());
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
        results = twitterBot.searchForTweets("redtheone", count, strdate1, strdate2);
        assertNotNull(results);
        assertTrue(results.size() == count);
    }
}