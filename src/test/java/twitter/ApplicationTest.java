package twitter;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class ApplicationTest {

    @Test
    public void testRetweetersUrl(){
        Application application = new Application();
        Assert.assertEquals("https://api.twitter.com/1.1/statuses/retweeters/ids.json?id=12345", application.getUrlHelper().getRetweetersListUrl(12345L));
    }

    @Test
    public void testFollowersUrl(){
        Application application = new Application();
        Assert.assertEquals("https://api.twitter.com/1.1/followers/ids.json?user_id=952253106",
                application.getUrlHelper().getFollowersListUrl(Long.valueOf("952253106")));
    }

    @Test
    public void testFollowingsUrl(){
        Application application = new Application();
        Assert.assertEquals("https://api.twitter.com/1.1/friends/ids.json?user_id=952253106", application.getUrlHelper().getFollowingsListUrl(952253106L));
    }

    @Test
    public void testLastTweetUrl(){
        Application application = new Application();
        Assert.assertEquals("https://api.twitter.com/1.1/statuses/user_timeline.json?", application.getUrlHelper().getLastTweetListUrl());
    }

    @Test
    public void testGetFollowings() throws IOException, InterruptedException, InvalidKeyException, NoSuchAlgorithmException {
        Application application = new Application();
        List<Long> followers = application.executeRequest(Action.FOLLOWING, 952253106L, "WTbXD0lVLIA", "1555611789");
        Assert.assertTrue(followers.size()>1);
    }

    @Test
    public void testGetNbFollowings() throws InterruptedException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        Application application = new Application();
        int result = application.getNbFollowings(952253106L);
        Assert.assertTrue(result>1);
    }

    @Test
    public void testGetFollowers() throws IOException, InterruptedException, InvalidKeyException, NoSuchAlgorithmException {
        Application application = new Application();
        List<Long> followers = application.executeRequest(Action.FOLLOWERS, 952253106L,"WTbXD0lVLIB" ,"1555611789");
        Assert.assertTrue(followers.size()>1);
    }

    @Test
    public void testGetNbFollowers() throws InterruptedException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        Application application = new Application();
        int result = application.getNbFollowers(952253106L);
        Assert.assertTrue(result>1);
    }

    @Test
    public void testGetRetweeters() throws IOException, InterruptedException, InvalidKeyException, NoSuchAlgorithmException {
        Application application = new Application();
        List<Long> retweeters = application.getRetweeters( 1100473425443860481L);
        Assert.assertTrue(retweeters.size()>1);
    }
}
