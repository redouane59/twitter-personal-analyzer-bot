import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Data
public class Helper {
    private final String rootUrl = "https://api.twitter.com/1.1";
    private final String idJsonPath = "/ids.json?";
    private final String retweetersPath = "/retweeters";
    private final String followersPath = "/followers";
    private final String followingsPath = "/friends";
    private final String statusesPath = "/statuses";

    private HttpClient client = HttpClient.newHttpClient();
    private AbstractSignatureHelper signatureHelper = new SignatureHelperImpl();

    List<Long> executeRequest(Action action, Long relatedId) throws IOException, InterruptedException, NoSuchAlgorithmException, InvalidKeyException {
        String url = this.getUrl(action, relatedId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers("Authorization",this.getAuthorizationHeader("GET", url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        TwitterJsonResponse jsonResponse = new ObjectMapper().readValue(response.body(), TwitterJsonResponse.class);

        return jsonResponse.getIds();
    }

    String getUrl(Action action, Long relatedId){
        switch (action){
            case RETWEETERS:
                return this.getRetweetersListUrl(relatedId);
            case FOLLOWERS:
                return this.getFollowersListUrl(relatedId);
            case FOLLOWING:
                return this.getFollowingsListUrl(relatedId);
            default:
                return null;
        }
    }

    int getNbFollowers(Long userId) throws IOException, InterruptedException, InvalidKeyException, NoSuchAlgorithmException {
        return this.executeRequest(Action.FOLLOWERS, userId).size();
    }

    int getNbFollowings(Long userId) throws IOException, InterruptedException, InvalidKeyException, NoSuchAlgorithmException {
        return this.executeRequest(Action.FOLLOWING, userId).size();
    }

    String getRetweetersListUrl(Long tweetId){
        return new StringBuilder(rootUrl)
                .append(statusesPath)
                .append(retweetersPath)
                .append(idJsonPath).toString();
    }

    String getFollowersListUrl(Long userId){
        return new StringBuilder(rootUrl)
                .append(followersPath)
                .append(idJsonPath)
                .append("user_id=")
                .append(userId)
                .toString();
    }

    String getFollowingsListUrl(Long userId){
        return new StringBuilder(rootUrl)
                .append(followingsPath)
                .append(idJsonPath)
                .append("user_id=")
                .append(userId).toString();
    }

    String getLastTweetListUrl(){
        return new StringBuilder(rootUrl)
                .append(statusesPath)
                .append("/user_timeline.json?").toString();
    }

    private String getAuthorizationHeader(String method, String url) throws IOException, InvalidKeyException, NoSuchAlgorithmException {

        return "OAuth oauth_consumer_key" +
                "=\""+ this.getSignatureHelper().getConsumerKey() + "\"" +
                ",oauth_token=\""+this.getSignatureHelper().getAccessToken()+"\"" +
                ",oauth_signature_method=\"HMAC-SHA1\"" +
                ",oauth_timestamp=\"1555592471\"" +
                ",oauth_nonce=\"RWMNirgMX6i\"" +
                ",oauth_version=\"1.0\"" +
                ",oauth_signature=\"" + this.getSignatureHelper().getSignature(url, method, this.getSignatureHelper().getStringParametersToEncrypt()) + "\"";
    }



}
