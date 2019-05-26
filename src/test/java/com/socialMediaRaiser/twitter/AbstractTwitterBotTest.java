package com.socialMediaRaiser.twitter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialMediaRaiser.RelationType;
import com.socialMediaRaiser.twitter.scoring.ScoringConstant;
import com.socialMediaRaiser.twitter.scoring.UserScoringEngine;
import com.socialMediaRaiser.twitter.helpers.GoogleSheetHelper;
import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
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
        List<User> followings = twitterBot.getFollowingsUserList("LaGhostquitweet");
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
        List<User> followers = twitterBot.getFollowerUsers(882266619115864066L);
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
        user.addLanguageFromLastTweet(twitterBot.getUserLastTweets(userId,2));
        Assert.assertEquals("fr",user.getLang());
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
        List<Tweet> response = twitterBot.getUserLastTweets(userName,2 );
        Assert.assertTrue(response.get(0).getLang().equals("fr")
        || response.get(1).getLang().equals("fr"));
    }

    @Test
    public void testGetLastTweetByUserId(){
        Long userId = 92073489L;
        List<Tweet> response = twitterBot.getUserLastTweets(userId, 2);
        Assert.assertTrue(response.get(0).getLang().equals("fr")
                || response.get(1).getLang().equals("fr"));
    }

    @Test
    public void testLoadFollowConfiguration(){
        ScoringConstant scoringConstant = new ScoringConstant();
        Assert.assertNotEquals(0,scoringConstant.getMinNbFollowers());
    }

    // @todo à déplacer
    @Test
    public void testCastTweetFromString() throws IOException {
        String tweet = "{\"created_at\":\"Sun May 26 16:37:48 +0000 2019\",\"id\":1132687286708178945,\"id_str\":\"1132687286708178945\",\"text\":\"Right @stewartdonald3 , so can you let JR know that we've been here before but our expectations now are 100+ points\\u2026 https:\\/\\/t.co\\/pzuPKcttBk\",\"source\":\"\\u003ca href=\\\"http:\\/\\/twitter.com\\/download\\/android\\\" rel=\\\"nofollow\\\"\\u003eTwitter for Android\\u003c\\/a\\u003e\",\"truncated\":true,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":2897854199,\"id_str\":\"2897854199\",\"name\":\"Tracy\",\"screen_name\":\"tracysweettweet\",\"location\":\"The Edge of Reality\",\"url\":null,\"description\":\"Mam to 2 kids and 1 labrador, pretty happy with life\",\"translator_type\":\"none\",\"protected\":false,\"verified\":false,\"followers_count\":29,\"friends_count\":283,\"listed_count\":0,\"favourites_count\":1589,\"statuses_count\":347,\"created_at\":\"Sat Nov 29 18:42:44 +0000 2014\",\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":false,\"lang\":null,\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"C0DEED\",\"profile_background_image_url\":\"http:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_image_url_https\":\"https:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_tile\":false,\"profile_link_color\":\"1DA1F2\",\"profile_sidebar_border_color\":\"C0DEED\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"profile_image_url\":\"http:\\/\\/pbs.twimg.com\\/profile_images\\/771135820199854083\\/BPszH9Z9_normal.jpg\",\"profile_image_url_https\":\"https:\\/\\/pbs.twimg.com\\/profile_images\\/771135820199854083\\/BPszH9Z9_normal.jpg\",\"profile_banner_url\":\"https:\\/\\/pbs.twimg.com\\/profile_banners\\/2897854199\\/1462485811\",\"default_profile\":true,\"default_profile_image\":false,\"following\":null,\"follow_request_sent\":null,\"notifications\":null},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"is_quote_status\":false,\"extended_tweet\":{\"full_text\":\"Right @stewartdonald3 , so can you let JR know that we've been here before but our expectations now are 100+ points next season and some entertaining bloody football! #STID\",\"display_text_range\":[0,172],\"entities\":{\"hashtags\":[{\"text\":\"STID\",\"indices\":[167,172]}],\"urls\":[],\"user_mentions\":[{\"screen_name\":\"stewartdonald3\",\"name\":\"stewart donald\",\"id\":577978827,\"id_str\":\"577978827\",\"indices\":[6,21]}],\"symbols\":[]}},\"quote_count\":0,\"reply_count\":3,\"retweet_count\":1,\"favorite_count\":2,\"entities\":{\"hashtags\":[],\"urls\":[{\"url\":\"https:\\/\\/t.co\\/pzuPKcttBk\",\"expanded_url\":\"https:\\/\\/twitter.com\\/i\\/web\\/status\\/1132687286708178945\",\"display_url\":\"twitter.com\\/i\\/web\\/status\\/1\\u2026\",\"indices\":[117,140]}],\"user_mentions\":[{\"screen_name\":\"stewartdonald3\",\"name\":\"stewart donald\",\"id\":577978827,\"id_str\":\"577978827\",\"indices\":[6,21]}],\"symbols\":[]},\"favorited\":false,\"retweeted\":false,\"filter_level\":\"low\",\"lang\":\"en\",\"timestamp_ms\":\"1558888668120\"}\n";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Tweet result = objectMapper.readValue(tweet, Tweet.class);
        // @todo complete
        Assert.assertNotNull(result);
    }

    @Test
    public void testStreaming() throws IOException, InterruptedException {
        TwitterStreamCollector collector = new TwitterStreamCollector();
        collector.collect();
    }
}
