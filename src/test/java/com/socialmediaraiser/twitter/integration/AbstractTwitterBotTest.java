package com.socialmediaraiser.twitter.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.socialmediaraiser.RelationType;
import com.socialmediaraiser.twitter.AbstractTwitterBot;
import com.socialmediaraiser.twitter.FollowProperties;
import com.socialmediaraiser.twitter.Tweet;
import com.socialmediaraiser.twitter.User;
import com.socialmediaraiser.twitter.helpers.dto.getuser.AbstractUser;
import com.socialmediaraiser.twitter.impl.FollowerAnalyzer;
import com.socialmediaraiser.twitter.impl.TwitterBotByInfluencers;
import com.socialmediaraiser.twitter.scoring.UserScoringEngine;
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

    @Test
    public void testFollowerAnalyzer(){
        FollowerAnalyzer bot = new FollowerAnalyzer("laurentbouvet", false, false);
        Map<String, Integer> result = bot.analyzeBios("RedouaneBali", 100);
        assertNotNull(result);
    }

      @Test
    public void testLinks(){
        FollowerAnalyzer.Link link = new FollowerAnalyzer.Link("A","B",0);
        FollowerAnalyzer.Link link2 = new FollowerAnalyzer.Link("B","A",1);
        assertTrue(link.equals(link2));
        assertTrue(link2.equals(link));
        Set<FollowerAnalyzer.Link> links = new HashSet<>();
        links.add(link);
        links.add(link2);
        assertTrue(links.contains(new FollowerAnalyzer.Link("A","B",-1)));
        assertTrue(links.contains(new FollowerAnalyzer.Link("B","A",-1)));
    }


    @Test
    public void testArray() {
        FollowerAnalyzer bot = new FollowerAnalyzer("RedouaneBali", false, false);

        List<String> users = new ArrayList<>();
        users.add("LaurentBouvet");
        users.add("Amk84000");
      users.add("GillesClavreul");
        users.add("ZinebElRhazoui");
          //  users.add("printempsrepub");
        users.add("ZohraBitan");
        //  users.add("Jo_delb");
        //  users.add("Nicolaszeminus");
        users.add("ivanrioufol");
        users.add("RenaudCamus");
        //  users.add("Valeurs");
        users.add("GWGoldnadel");
        users.add("edwyplenel");
        users.add("fabricearfi");
        users.add("davidperrotin");
        users.add("carolinedehaas");
        //   users.add("BHL");
        users.add("Enthoven_R");
        //   users.add("AgagBoudjahlat");
        //    users.add("RNational_off");
        //     users.add("MLP_officiel");
        users.add("MarionMarechal");
        users.add("J_Bardella");
        users.add("DamienRieu");
        users.add("JeanMessiha");
        //  users.add("F_Desouche");
        users.add("RobertMenardFR");
        users.add("JulienOdoul");
        //  users.add("FranceInsoumise");
        //  users.add("JLMelenchon");
        users.add("Francois_Ruffin");
        users.add("Clem_Autain");
        users.add("Deputee_Obono");
        users.add("alexiscorbiere");
        users.add("IanBrossat");
        //   users.add("pcf");
        users.add("fabien_gay");
        users.add("MadjidFalastine");
        //    users.add("PartiIndigenes");
        //    users.add("partisocialiste");
        users.add("benoithamon");
        users.add("MartineAubry");
        //  users.add("GenerationsMvt");
        users.add("rglucks1");
        users.add("faureolivier");
        users.add("BalasGuillaume");
        users.add("Juanico");
        users.add("Isabel_thomasEU");
        users.add("EstherBenbassa");
        //   users.add("lesRepublicains");
        users.add("nadine__morano");
        users.add("LydiaGuirous");
        users.add("ECiotti");
        users.add("ChJacob77");
        users.add("cestrosi");
        users.add("T_Bouhafs");
        users.add("_MarwanMuhammad");
        users.add("FeizaBM");
        //  users.add("ccif");
        users.add("s_assbague");
     /*   users.add("MarleneSchiappa");
        users.add("RichardFerrand");
        users.add("EPhilippePM");
        users.add("CCastaner");
        users.add("EmmanuelMacron");
        users.add("mounir"); */
// Thinker_View
        bot.getArray(users);
    }


}