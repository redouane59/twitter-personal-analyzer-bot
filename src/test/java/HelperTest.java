import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class HelperTest {

    @Test
    public void testRetweetersUrl(){
        Helper helper = new Helper();
        Assert.assertEquals("https://api.twitter.com/1.1/statuses/retweeters/ids.json?", helper.getRetweetersListUrl(12345L));
    }

    @Test
    public void testFollowersUrl(){
        Helper helper = new Helper();
        Assert.assertEquals("https://api.twitter.com/1.1/followers/ids.json?user_id=952253106",
                helper.getFollowersListUrl(Long.valueOf("952253106")));
    }

    @Test
    public void testFollowingsUrl(){
        Helper helper = new Helper();
        Assert.assertEquals("https://api.twitter.com/1.1/friends/ids.json?", helper.getFollowingsListUrl(12345L));
    }

    @Test
    public void testLastTweetUrl(){
        Helper helper = new Helper();
        Assert.assertEquals("https://api.twitter.com/1.1/statuses/user_timeline.json?", helper.getLastTweetListUrl());
    }

    @Test
    public void testGetFollowers() throws IOException, InterruptedException {
        Helper helper = new Helper();
        List<Long> followers = helper.executeRequest(Action.FOLLOWERS, 952253106L);
        Assert.assertTrue(followers.size()>100);
    }

    @Test
    public void testGetFollowings() throws IOException, InterruptedException {
        Helper helper = new Helper();
        List<Long> followers = helper.executeRequest(Action.FOLLOWING, 952253106L);
        Assert.assertTrue(followers.size()>1);
    }
}
