package twitter.helpers;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class RequestHelper {

    public JSONObject executeRequest(String url, RequestMethod method) throws IllegalAccessException {
        switch (method){
            case GET:
                return executeGetRequest(url);
            case POST:
                return executePostRequest(url);
            default:
                return null;
        }
    }


    private JSONObject executeGetRequest(String url) throws IllegalAccessException {

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try {
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(this.getSignedRequest(request, this.getNonce(), this.getTimestamp())).execute();
            System.out.println("executing 1 request " + url);
            JSONObject jsonResponse = new JSONObject(response.body().string());
            if(response.code()==200){
                return jsonResponse;
            } else if (response.code() == 429){
                throw new IllegalAccessException("error " + response.code());
            }else{
                return null;
            }
        } catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private JSONObject executePostRequest(String url) throws IllegalAccessException {

        RequestBody reqbody = RequestBody.create(null, new byte[0]);

        Request request = new Request.Builder()
                .url(url)
                .post(reqbody)
                .build();

        try {
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(this.getSignedRequest(request, this.getNonce(), this.getTimestamp())).execute();
            System.out.println("executing 1 request " + url);
            return new JSONObject(response.body().string());

        } catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private String getNonce(){
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            stringBuilder.append(secureRandom.nextInt(10));
        }
        return stringBuilder.toString();
    }

    private String getTimestamp(){
        return String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()).substring(0,10);
    }

    private Request getSignedRequest(Request request, String nonce, String timestamp) throws IOException {
        Oauth1SigningInterceptor oauth = new Oauth1SigningInterceptor.Builder()
                .consumerKey(SignatureConstants.consumerKey)
                .consumerSecret(SignatureConstants.consumerSecret)
                .accessToken(SignatureConstants.accessToken)
                .accessSecret(SignatureConstants.secretToken)
                .oauthNonce(nonce)
                .oauthTimeStamp(timestamp)
                .build();

        return oauth.signRequest(request);
    }
}
