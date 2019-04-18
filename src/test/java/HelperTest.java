import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
        Assert.assertEquals("https://api.twitter.com/1.1/friends/ids.json?user_id=952253106", helper.getFollowingsListUrl(952253106L));
    }

    @Test
    public void testLastTweetUrl(){
        Helper helper = new Helper();
        Assert.assertEquals("https://api.twitter.com/1.1/statuses/user_timeline.json?", helper.getLastTweetListUrl());
    }

    @Test
    public void testSignature() throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
        String nonce = "RWMNirgMX6i";
        String timestamp = "1555592471";
        String method = "GET";
        String url = "https://api.twitter.com/1.1/followers/ids.json?user_id=952253106";
        String expectedSignature = "%2FicEKfNmyFexYL0lsDiLALRs2NM%3D";
        Helper helper = new Helper();
        String result = helper.getSignatureHelper().getSignature(url, method,
                helper.getSignatureHelper().getStringParametersToEncrypt(nonce, timestamp));
        Assert.assertEquals(expectedSignature, result);
    }

    @Test
    public void testGetFollowings() throws IOException, InterruptedException, InvalidKeyException, NoSuchAlgorithmException {
        Helper helper = new Helper();
        List<Long> followers = helper.executeRequest(Action.FOLLOWING, 952253106L);
        Assert.assertTrue(followers.size()>1);
    }

    @Test
    public void testGetFollowers() throws IOException, InterruptedException, InvalidKeyException, NoSuchAlgorithmException {
        Helper helper = new Helper();
        List<Long> followers = helper.executeRequest(Action.FOLLOWERS, 952253106L);
        Assert.assertTrue(followers.size()>1);
    }
}
