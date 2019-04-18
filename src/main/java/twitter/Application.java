package twitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import twitter.helpers.AbstractSignatureHelper;
import twitter.helpers.SignatureHelperImpl;
import twitter.helpers.URLHelper;
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
public class Application implements IApplication {

    private HttpClient client = HttpClient.newHttpClient();
    private AbstractSignatureHelper signatureHelper = new SignatureHelperImpl();
    private URLHelper urlHelper = new URLHelper();

    List<Long> executeRequest(Action action, Long relatedId) throws IOException, InterruptedException, NoSuchAlgorithmException, InvalidKeyException {
        String url = this.urlHelper.getUrl(action, relatedId);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers("Authorization",this.signatureHelper.getAuthorizationHeader("GET", url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        TwitterJsonResponse jsonResponse = new ObjectMapper().readValue(response.body(), TwitterJsonResponse.class);

        return jsonResponse.getIds();
    }

    @Override
    public int getNbFollowers(Long userId) throws IOException, InterruptedException, InvalidKeyException, NoSuchAlgorithmException {
        return this.executeRequest(Action.FOLLOWERS, userId).size();
    }

    @Override
    public int getNbFollowings(Long userId) throws IOException, InterruptedException, InvalidKeyException, NoSuchAlgorithmException {
        return this.executeRequest(Action.FOLLOWING, userId).size();
    }

    @Override
    public int getRetweeters(Long tweetId) throws IOException, InterruptedException, InvalidKeyException, NoSuchAlgorithmException {
        return this.executeRequest(Action.RETWEETERS, tweetId).size();
    }


}
