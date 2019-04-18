package twitter;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public class ApplicationTest {

    @Test
    public void testRetweetersUrl(){
        Application application = new Application();
        Assert.assertEquals("https://api.twitter.com/1.1/statuses/retweeters/ids.json?", application.getUrlHelper().getRetweetersListUrl(12345L));
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
    public void testBaseSignature() throws UnsupportedEncodingException {
        String nonce = "RWMNirgMX6i";
        String timestamp = "1555592471";
        String method = "GET";
        String url = "https://api.twitter.com/1.1/followers/ids.json?user_id=952253106";
        Application application = new Application();
        String result = application.getSignatureHelper().getBaseSignature(url, method,
                application.getSignatureHelper().getStringParametersToEncrypt(nonce, timestamp));
        Assert.assertTrue(result.contains("GET&https%3A%2F%2Fapi.twitter.com%2F1.1%2Ffollowers%2Fids.json"));
        Assert.assertTrue(result.contains("oauth_consumer_key%3D"+application.getSignatureHelper().getConsumerKey()));
        Assert.assertTrue(result.contains("%26oauth_token%3D"+application.getSignatureHelper().getAccessToken()));
        Assert.assertTrue(result.contains("%26oauth_version%3D"+application.getSignatureHelper().getVersion()));
        Assert.assertTrue(result.contains("%26oauth_signature_method%3D"+application.getSignatureHelper().getSignatureMethod()));
        Assert.assertFalse(result.contains(" "));
    }

    @Test
    public void testSignature1() throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
        String nonce = "RWMNirgMX6i";
        String timestamp = "1555592471";
        String method = "GET";
        String url = "https://api.twitter.com/1.1/followers/ids.json?user_id=952253106";
        String expectedSignature = "%2FicEKfNmyFexYL0lsDiLALRs2NM%3D";
        Application application = new Application();
        String result = application.getSignatureHelper().getSignature(url, method,
                application.getSignatureHelper().getStringParametersToEncrypt(nonce, timestamp));
        Assert.assertEquals(expectedSignature, result);
    }

    @Test
    public void testSignature2() throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException {
        String nonce = "eZ2fBHWwMTL";
        String timestamp = "1555609726";
        String method = "GET";
        String url = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=RedTheOne&count=100";
        String expectedSignature = "pw6r7nVaAaBFk64KxOMU%2BVs1ot8%3D";
        Application application = new Application();
        String result = application.getSignatureHelper().getSignature(url, method,
                application.getSignatureHelper().getStringParametersToEncrypt(nonce, timestamp));
        Assert.assertEquals(expectedSignature, result);
    }

    @Test
    public void testGetFollowings() throws IOException, InterruptedException, InvalidKeyException, NoSuchAlgorithmException {
        Application application = new Application();

        List<Long> followers = application.executeRequest(Action.FOLLOWING, 952253106L, "WTbXD0lVLIA", "1555611789");
        Assert.assertTrue(followers.size()>1);
    }

    @Test
    public void testGetFollowers() throws IOException, InterruptedException, InvalidKeyException, NoSuchAlgorithmException {
        Application application = new Application();
        List<Long> followers = application.executeRequest(Action.FOLLOWERS, 952253106L, "","");
        Assert.assertTrue(followers.size()>1);
    }

    @Test
    public void testGetRetweeters() throws IOException, InterruptedException, InvalidKeyException, NoSuchAlgorithmException {
        Application application = new Application();
        List<Long> retweeters = application.executeRequest(Action.RETWEETERS, 1100473425443860481L, "", "");
        Assert.assertTrue(retweeters.size()>1);
    }
}
