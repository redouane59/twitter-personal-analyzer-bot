package com.socialMediaRaiser.twitter.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.signature.Oauth1SigningInterceptor;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Data
@NoArgsConstructor
public class RequestHelper {

    private int sleepTime = 15;

    @Deprecated
    public JSONObject executeRequest(String url, RequestMethod method) {
        try {
            switch (method) {
                case GET:
                    return executeGetRequest(url);
                case POST:
                    return executePostRequest(url, new HashMap<>());
                default:
                    return null;
            }
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


    private JSONObject executeGetRequest(String url) {
        try {
            Response response = this.getHttpClient(url)
                    .newCall(this.getSignedRequest(this.getRequest(url), this.getNonce(), this.getTimestamp())).execute();
            JSONObject jsonResponse = new JSONObject(response.body().string());
            if(response.code()==200){
                return jsonResponse;
            } else if (response.code()==429){
                LocalDateTime now = LocalDateTime.now();
                System.out.println(response.message() +" at "
                        + now.getHour() + ":" + now.getMinute() + ". Waiting ...");
                try {
                    TimeUnit.MINUTES.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return this.executeGetRequest(url);
            } else{
                System.err.println("(GET) not 200 return null " + response.message() + " - " + response.code());
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject executePostRequest(String url, Map<String, String> parameters) {

        try {
            String json = new ObjectMapper().writeValueAsString(parameters);

            RequestBody requestBody = RequestBody.create(null, json);

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            Request signedRequest = this.getSignedRequest(request, this.getNonce(), this.getTimestamp());

            Response response = this.getHttpClient(url)
                    .newCall(signedRequest).execute();

            if(response.code()!=200){
                System.err.println("(POST) ! not 200 " + response.message() + " - " + response.code());
            }
            String stringResposne = response.body().string();
            return new JSONObject(stringResposne);

        } catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public JSONArray executeGetRequestReturningArray(String url) {
        try {
            Response response = this.getHttpClient(url)
                    .newCall(this.getSignedRequest(this.getRequest(url), this.getNonce(), this.getTimestamp())).execute();
            if(response.code()==200){
                String stringResult = response.body().string();
                JSONArray resultArray = new JSONArray(stringResult);
                return resultArray;
            } else if (response.code() == 401){
                response.close();
                System.out.println("user private, not authorized");
            } else if (response.code()==429){
                LocalDateTime now = LocalDateTime.now();
                System.out.println(response.message() +" at "
                        + now.getHour() + ":" + now.getMinute() + ". Waiting ..."); // do a wait and return this function recursively
                try {
                    TimeUnit.MINUTES.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return this.executeGetRequestReturningArray(url);
            } else{
                System.err.println("not 200 (return null)" + response.message() + " - " + response.code());
            }
        } catch(Exception e){
            System.err.println("exception return null");
            e.printStackTrace();
        }
        return null;
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
                .consumerKey(FollowProperties.twitterCredentials.getConsumerKey())
                .consumerSecret(FollowProperties.twitterCredentials.getConsumerSecret())
                .accessToken(FollowProperties.twitterCredentials.getAccessToken())
                .accessSecret(FollowProperties.twitterCredentials.getSecretToken())
                .oauthNonce(nonce)
                .oauthTimeStamp(timestamp)
                .build();

        return oauth.signRequest(request);
    }

    private Request getRequest(String url){
        return new Request.Builder()
                .url(url)
                .get()
                .build();
    }

    private OkHttpClient getHttpClient(String url){
        int cacheSize = 500 * 1024 * 1024; // 500MB
        String path = "C:\\okhttpCache";
        File file = new File(path);
        return new OkHttpClient.Builder()
                .addNetworkInterceptor(new CacheInterceptor(this.getCacheTimeoutFromUrl(url)))
                .cache(new Cache(file, cacheSize))
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    private int getCacheTimeoutFromUrl(String url){
        int defaultCache = 48;
        if(url.contains("/friends")){
            defaultCache = 2;
        } else if (url.contains("/friendships")){
            defaultCache = 2;
        } else if (url.contains("/followers")){
            defaultCache = 72;
        } else if (url.contains("/users")){
            defaultCache = 168;
        } else if (url.contains("/user_timeline")){
            defaultCache = 168;
        }
        return defaultCache;
    }
}
