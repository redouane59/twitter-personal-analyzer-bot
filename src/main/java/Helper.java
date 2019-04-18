import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class Helper {
    private final String consumerKey = "LiMccelygYuyueOKZLaVk9N1R";
    private final String accessToken = "92073489-N6BLM48cIKk3X5ya5RiXNPYlShFF1Z1vYRug6rRiv";
    private final String rootUrl = "https://api.twitter.com/1.1";
    private final String idJsonPath = "/ids.json?";
    private final String retweetersPath = "/retweeters";
    private final String followersPath = "/followers";
    private final String followingsPath = "/friends";
    private final String statusesPath = "/statuses";

    private HttpClient client = HttpClient.newHttpClient();

    List<Long> executeRequest(Action action, Long relatedId) throws IOException, InterruptedException {
        String url = this.getUrl(action, relatedId);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers("Authorization",this.getAuthorizationHeader())
                .build();

        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        TwitterJsonResponse jsonResponse = mapper.readValue(response.body(), TwitterJsonResponse.class);

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

    int getNbFollowers(Long userId) throws IOException, InterruptedException {
        return this.executeRequest(Action.FOLLOWERS, userId).size();
    }

    int getNbFollowings(Long userId) throws IOException, InterruptedException {
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
                .append(idJsonPath).toString();
    }

    String getLastTweetListUrl(){
        return new StringBuilder(rootUrl)
                .append(statusesPath)
                .append("/user_timeline.json?").toString();
    }

    private String getAuthorizationHeader(){
        return "OAuth oauth_consumer_key" +
                "=\""+ consumerKey + "\"" +
                ",oauth_token=\""+accessToken+"\"" +
                ",oauth_signature_method=\"HMAC-SHA1\"" +
                ",oauth_timestamp=\"1555580250\"" +
                ",oauth_nonce=\"XgzhSJrv3GH\"" +
                ",oauth_version=\"1.0\"" +
                ",oauth_signature=\"xrnEdleX9w4nwG9hLFsRazzIL7E%3D\"";
    }
}
