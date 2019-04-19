package twitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import lombok.Data;
import twitter.helpers.Oauth1SigningInterceptor;
import twitter.helpers.SignatureConstants;
import twitter.helpers.URLHelper;

import java.io.IOException;
import java.net.http.HttpClient;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.List;

@Data
public class Application implements IApplication {

    private HttpClient client = HttpClient.newHttpClient();
     private URLHelper urlHelper = new URLHelper();

    List<Long> executeRequest(Action action, Long relatedId) throws InterruptedException, NoSuchAlgorithmException, InvalidKeyException, IOException {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            stringBuilder.append(secureRandom.nextInt(10));
        }
        String nonce = stringBuilder.toString();
        String timestamp = String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()).substring(0,10);
        return this.executeRequest(action, relatedId, nonce, timestamp);
    }

    List<Long> executeRequest(Action action, Long relatedId, String nonce, String timestamp) throws IOException, InterruptedException, NoSuchAlgorithmException, InvalidKeyException {
        String url = this.urlHelper.getUrl(action, relatedId);

        Oauth1SigningInterceptor oauth1 = new Oauth1SigningInterceptor.Builder()
                .consumerKey(SignatureConstants.consumerKey)
                .consumerSecret(SignatureConstants.consumerSecret)
                .accessToken(SignatureConstants.accessToken)
                .accessSecret(SignatureConstants.secretToken)
                .oauthNonce(nonce)
                .oauthTimeStamp(timestamp)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Request signed = oauth1.signRequest(request);
        OkHttpClient client = new OkHttpClient();

        Response response = client.newCall(signed).execute();

        TwitterJsonResponse jsonResponse = new ObjectMapper().readValue(response.body().string(), TwitterJsonResponse.class);

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
    public List<Long> getRetweeters(Long tweetId) throws IOException, InterruptedException, InvalidKeyException, NoSuchAlgorithmException {
        return this.executeRequest(Action.RETWEETERS, tweetId);
    }


}
