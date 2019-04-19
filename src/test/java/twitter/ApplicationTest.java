package twitter;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

public class ApplicationTest {

    @Test
    public void testRetweetersUrl(){
        Application application = new Application();
        Assert.assertEquals("https://api.twitter.com/1.1/statuses/retweeters/ids.json?id=12345", application.getUrlHelper().getRetweetersListUrl(12345L));
    }

    @Test
    public void testFollowersUrlById(){
        Application application = new Application();
        Assert.assertEquals("https://api.twitter.com/1.1/followers/ids.json?user_id=952253106",
                application.getUrlHelper().getFollowersListUrl(952253106L));
    }

    @Test
    public void testFollowersUrlByName(){
        Application application = new Application();
        Assert.assertEquals("https://api.twitter.com/1.1/followers/list.json?screen_name=RedTheOne",
                application.getUrlHelper().getFollowersListUrl("RedTheOne"));
    }

    @Test
    public void testFollowingsUrlById(){
        Application application = new Application();
        Assert.assertEquals("https://api.twitter.com/1.1/friends/ids.json?user_id=952253106",
                application.getUrlHelper().getFollowingsListUrl(952253106L));
    }

    @Test
    public void testFollowingsUrlByName(){
        Application application = new Application();
        Assert.assertEquals("https://api.twitter.com/1.1/friends/list.json?screen_name=RedTheOne",
                application.getUrlHelper().getFollowingsListUrl("RedTheOne"));
    }

    @Test
    public void testLastTweetUrl(){
        Application application = new Application();
        Assert.assertEquals("https://api.twitter.com/1.1/statuses/user_timeline.json?",
                application.getUrlHelper().getLastTweetListUrl());
    }

    @Test
    public void testFriendshipUrlById(){
        Application application = new Application();
        Assert.assertEquals("https://api.twitter.com/1.1/friendships/show.json?source_id=12345&target_id=67890",
                application.getUrlHelper().getFriendshipUrl(12345L,67890L));
    }

    @Test
    public void testFriendshipUrlByName(){
        Application application = new Application();
        Assert.assertEquals("https://api.twitter.com/1.1/friendships/show.json?source_screen_name=RedTheOne&target_screen_name=EmmanuelMacron",
                application.getUrlHelper().getFriendshipUrl("RedTheOne","EmmanuelMacron"));
    }

    @Test
    public void testGetFollowingsById() throws IllegalAccessException {
        Application application = new Application();
        List<Long> followings = application.getFollowingsList(952253106L);
        Assert.assertTrue(followings.size()>1);
    }

    @Test
    public void testGetFollowingsByName() throws IllegalAccessException {
        Application application = new Application();
        List<String> followings = application.getFollowingsList("RedTheOne");
        Assert.assertTrue(followings.size()>1);
    }

    @Test
    public void testGetNbFollowingsById() throws IllegalAccessException {
        Application application = new Application();
        int result = application.getNbFollowings(952253106L);
        Assert.assertTrue(result>1);
    }

    @Test
    public void testGetNbFollowingsByName() throws IllegalAccessException {
        Application application = new Application();
        int result = application.getNbFollowings("RedTheOne");
        Assert.assertTrue(result>1);
    }

    @Test
    public void testGetFollowersById() throws IllegalAccessException {
        Application application = new Application();
        String url = application.getUrlHelper().getUrl(Action.FOLLOWERS, 952253106L);
        List<Long> followers = application.getFollowersList(952253106L);
        Assert.assertTrue(followers.size()>1);
    }

    @Test
    public void testGetFollowersByName() throws IllegalAccessException {
        Application application = new Application();
        List<String> followers = application.getFollowersList("RedTheOne");
        Assert.assertTrue(followers.size()>1);
    }

    @Test
    public void testGetNbFollowersById() throws IllegalAccessException {
        Application application = new Application();
        int result = application.getNbFollowers(952253106L);
        Assert.assertTrue(result>1);
    }

    @Test
    public void testGetRetweetersId() throws IllegalAccessException {
        Application application = new Application();
        List<Long> retweeters = application.getRetweetersId( 1100473425443860481L);
        Assert.assertTrue(retweeters.size()>1);
    }

    @Test
    public void testGetRatioFollowersFollowing() throws IllegalAccessException {
        Application application = new Application();
        double ratio = application.getFollowersFollowingRatio( 92073489L);
        Assert.assertTrue(ratio>1);
    }

    @Test
    public void testFriendshipByIdYes() throws IllegalAccessException {
        Application application = new Application();
        Long userId1 = 92073489L;
        Long userId2 = 723996356L;
        boolean result = application.areFriends(userId1, userId2);
        Assert.assertTrue(result);
    }

    @Test
    public void testFriendshipByIdNo() throws IllegalAccessException {
        Application application = new Application();
        Long userId1 = 92073489L;
        Long userId2 = 1976143068L;
        boolean result = application.areFriends(userId1, userId2);
        Assert.assertFalse(result);
    }

    @Test
    @Ignore
    public void testPotentials(){
        Application application = new Application();
        List<Long> potentialFollowers = application.getPotentialFollowersFromRetweet(1100473425443860481L);
        Assert.assertTrue(potentialFollowers.size()>1);
    }


}
