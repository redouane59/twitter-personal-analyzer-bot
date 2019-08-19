package com.socialMediaRaiser.twitter;

import com.socialMediaRaiser.RelationType;
import com.socialMediaRaiser.twitter.helpers.GoogleSheetHelper;
import com.socialMediaRaiser.twitter.helpers.dto.getUser.TweetDTO;
import com.socialMediaRaiser.twitter.helpers.dto.getUser.UserDTO;
import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;
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
        List<Long> followings = twitterBot.getFollowingIds(882266619115864066L);
        assertTrue(followings.size() > 200);
    }

    @Test
    public void testGetFollowingIdsByName() {
        List<Long> followings = twitterBot.getFollowingIds("LaGhostquitweet");
        assertTrue(followings.size() > 200);
    }

    @Test
    public void testGetFollowingsUserByName() {
        List<UserDTO> followings = twitterBot.getFollowingsUsers("LaGhostquitweet");
        assertTrue(followings.size() > 200);
    }

    @Test
    public void testGetFollowersUserByName() {
        List<UserDTO> followings = twitterBot.getFollowerUsers("LaGhostquitweet");
        assertTrue(followings.size() > 360);
    }

  /*  @Test
    public void testGetNbFollowingsById() {
        int result = twitterBot.getNbFollowingsById(919925977777606659L);
        assertTrue(result>1 && result<500);
    } */

   /* @Test
    public void testGetNbFollowingsByName() {
        int result = twitterBot.getNbFollowingsByName("kanyewest");
        assertTrue(result>1);
    } */

    @Test
    public void testGetFollowersIdsById() {
        List<Long> followers = twitterBot.getFollowerIds(882266619115864066L);
        assertTrue(followers.size() > 420);
    }

    @Test
    public void testGetFollowersIdsByName() {
        List<Long> followers = twitterBot.getFollowerIds("LaGhostquitweet");
        assertTrue(followers.size() > 420);
    }

    @Test
    public void testGetFollowersUsersByName() {
        List<UserDTO> followers = twitterBot.getFollowerUsers("LaGhostquitweet");
        assertTrue(followers.size() > 420);
    }

    @Test
    public void testGetFollowersUsersById() {
        List<UserDTO> followers = twitterBot.getFollowerUsers(882266619115864066L);
        assertTrue(followers.size() > 420);
    }

    @Test
    public void testFriendshipByIdYes() {
        Long userId1 = 92073489L;
        Long userId2 = 723996356L;
        RelationType result = twitterBot.getRelationType(userId1, userId2);
        assertEquals(RelationType.FRIENDS, result);
    }

    @Test
    public void getUserByUserName() {
        String userName = "RedTheOne";
        UserDTO result = twitterBot.getUserFromUserName(userName);
        assertTrue(result.getId().equals("92073489"));
    }

    @Test
    public void testFriendshipByIdNo() {
        Long userId1 = 92073489L;
        Long userId2 = 1976143068L;
        RelationType result = twitterBot.getRelationType(userId1, userId2);
        assertNotEquals(RelationType.FRIENDS, result);
    }

    @Test
    @Disabled
    public void testFollowNew() {
        String userName = "RedTheOne";
        boolean result = twitterBot.follow(userName);
        assertTrue(result);
    }

    @Test
    public void testGetUserInfoName() {
        long userId = 92073489L;
        UserDTO user = twitterBot.getUserFromUserId(userId);
        assertEquals("RedTheOne", user.getUsername());
    }

    @Test
    public void testGetUserInfoId() {
        String userId = "92073489";
        UserDTO user = twitterBot.getUserFromUserId(Long.valueOf(userId));
        assertTrue(userId.equals(user.getId()));
    }

    @Test
    @Disabled
    public void testGetUserInfoFavouritesCount() {
        long userId = 92073489L;
        UserDTO user = twitterBot.getUserFromUserId(userId);
    //    assertTrue(user.getFavouritesCount() > 0);
    }

    @Test
    public void testGetUserInfoFavouritesDateOfCreation() {
        long userId = 92073489L;
        UserDTO user = twitterBot.getUserFromUserId(userId);
        assertTrue(user.getDateOfCreation() != null);
    }

    @Test
    public void testGetUserInfoStatusesCount() {
        long userId = 92073489L;
        UserDTO user = twitterBot.getUserFromUserId(userId);
        assertTrue(user.getStatusesCount() > 0);
    }

    @Test
    public void testGetUserInfoLang() {
        long userId = 92073489L;
        UserDTO user = twitterBot.getUserFromUserId(userId);
       // user.addLanguageFromLastTweet(twitterBot.getUserLastTweets(userId, 3));
        assertEquals("fr", user.getLang());
    }

    @Test
    public void testGetUserInfoLastUpdate() {
        String userId = "92073489";
        UserDTO user = twitterBot.getUserFromUserId(Long.valueOf(userId));
        assertTrue(userId.equals(user.getId()));
        assertTrue(user.getLastUpdate() != null);
    }

    @Test
    public void testGetUserInfoFollowingRatio() {
        long userId = 92073489L;
        UserDTO user = twitterBot.getUserFromUserId(userId);
        assertEquals(userId, user.getId());
        assertTrue(user.getFollowersRatio() > 1);
    }

    @Test
    public void testGetUserWithCache() {
        Long userId = 92073489L;
        UserDTO user = twitterBot.getUserFromUserId(userId);
        assertEquals("RedTheOne", user.getUsername());
        user = twitterBot.getUserFromUserId(userId);
        assertEquals("RedTheOne", user.getUsername());
    }

    @Test
    public void testGetUsersFromUserIds() {
        List<Long> ids = new ArrayList<>();
        ids.add(92073489L); // RedTheOne
        ids.add(22848599L); // Soltana
        List<UserDTO> result = twitterBot.getUsersFromUserIds(ids);
        assertEquals("RedTheOne", result.get(0).getUsername());
        assertEquals("Soltana", result.get(1).getUsername());
    }

    @Test
    public void testGetRateLimitStatus() {
        assertNotEquals(null, twitterBot.getRateLimitStatus());
    }

    /*
    @Test
    public void testShouldBeFollowedBadRatio() {
        UserScoringEngine engine = new UserScoringEngine(100);
        UserDTO user = new User();
        user.setFollowersCount(1);
        user.setFollowingsCount(1000);
        user.setLastUpdate(new Date());
        user.setLang("fr");
        assertEquals(false, user.shouldBeFollowed(ownerName));
        assertFalse(engine.shouldBeFollowed(user));
    }

    public void testShouldBeFollowBadLastUpdate() {
        UserScoringEngine engine = new UserScoringEngine(100);
        UserDTO user = new User();
        user.setFollowersCount(1500);
        user.setFollowingsCount(1000);
        user.setLang("fr");
        user.setLastUpdate(null);
        assertEquals(false, user.shouldBeFollowed(ownerName));
        assertFalse(engine.shouldBeFollowed(user));
    }

    public void testShouldBeFollowBadLastUpdate2() {
        UserScoringEngine engine = new UserScoringEngine(100);
        UserDTO user = new User();
        user.setFollowersCount(1500);
        user.setFollowingsCount(1000);
        user.setLang("fr");
        user.setLastUpdate(new Date(2014, 1, 1));
        assertEquals(false, user.shouldBeFollowed(ownerName));
        assertFalse(engine.shouldBeFollowed(user));
    }

    public void testShouldBeFollowedOk() {
        UserScoringEngine engine = new UserScoringEngine(100);
        UserDTO user = new User();
        user.setFollowersCount(1500);
        user.setFollowingsCount(1000);
        user.setLang("fr");
        user.setLastUpdate(new Date());
        assertEquals(true, user.shouldBeFollowed(ownerName));
        assertFalse(engine.shouldBeFollowed(user));
    } */

    @Test
    public void testHashCode() {
        UserDTO user = UserDTO.builder().id("12345").build();
        UserDTO user2 = UserDTO.builder().id("23456").build();
        assertNotEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    @Disabled
    public void testWritingOnGoogleSheet() {
        UserDTO user = twitterBot.getUserFromUserName("RedTheOne");
        GoogleSheetHelper helper = new GoogleSheetHelper(ownerName);
        helper.addNewFollowerLine(user);
    }

    @Test
    public void testRelationBetweenUsersIdFriends() {
        long userId1 = 92073489L;
        long userId2 = 723996356L;
        RelationType result = twitterBot.getRelationType(userId1, userId2);
        assertEquals(RelationType.FRIENDS, result);
    }

    @Test
    public void testRelationBetweenUsersIdNone() {
        long userId1 = 92073489L;
        long userId2 = 1976143068L;
        RelationType result = twitterBot.getRelationType(userId1, userId2);
        assertEquals(RelationType.NONE, result);
    }

    @Test
    public void testRelationBetweenUsersIdFollowing() {
        long userId1 = 92073489L;
        long userId2 = 126267113L;
        RelationType result = twitterBot.getRelationType(userId1, userId2);
        assertEquals(RelationType.FOLLOWING, result);
    }

    @Test
    public void testRelationBetweenUsersIdFollower() {
        long userId1 = 92073489L;
        long userId2 = 1128737441945464832L;
        RelationType result = twitterBot.getRelationType(userId1, userId2);
        assertEquals(RelationType.FOLLOWER, result);
    }

    @Test
    @Disabled // API KO
    public void testGetRetweetersId() {
        Long tweetId = 1078358350000205824L;
        assertTrue(twitterBot.getRetweetersId(tweetId).size() > 400);
    }

    @Test
    public void getLastUpdate() {
        long userId = 92073489L;
        UserDTO user = twitterBot.getUserFromUserId(userId);
        Date now = new Date();
        Date lastUpdate = user.getLastUpdate();
        long diffDays = (now.getTime() - lastUpdate.getTime()) / (24 * 60 * 60 * 1000);
        assertTrue(diffDays < 15);
    }

    @Test
    public void testGetMostRecentTweets(){
        long userId = 92073489L;
        UserDTO user = twitterBot.getUserFromUserId(userId);
        assertNotNull(user.getMostRecentTweet());
    }


    @Test
    public void testUserDiffDate0() {
        UserDTO user = UserDTO.builder()
                .dateOfFollow(new Date())
                .mostRecentTweet(TweetDTO.builder()
                        .created_at(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm.sss'Z'").format(new Date()))
                        .build())
                .build();
        assertEquals(0, user.getDaysBetweenFollowAndLastUpdate());
    }

    @Test
    public void testUserDiffDate7() {
        final Calendar lastUpdate = Calendar.getInstance();
        lastUpdate.add(Calendar.DATE, -7);
        UserDTO user = UserDTO.builder()
                .dateOfFollow(new Date())
                .build();
        assertEquals(7, user.getDaysBetweenFollowAndLastUpdate());
    }

    @Test
    public void testGetLastTweetByUserName() {
        String userName = "RedTheOne";
        List<TweetDTO> response = twitterBot.getUserLastTweets(userName, 2);
        assertTrue(response.get(0).getLang().equals("fr")
                || response.get(1).getLang().equals("fr"));
    }

    @Test
    public void testGetLastTweetByUserId() {
        Long userId = 92073489L;
        List<TweetDTO> response = twitterBot.getUserLastTweets(userId, 3);
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
        List<TweetDTO> results = null;
        try {
            results = twitterBot.searchForTweets("redtheone", count, dateformat.parse(strdate1), dateformat.parse(strdate2));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertNotNull(results);
        assertTrue(results.size() == count);
    }
}