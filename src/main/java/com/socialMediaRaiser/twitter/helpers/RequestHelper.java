package com.socialMediaRaiser.twitter.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.helpers.dto.getUser.RequestTokenDTO;
import com.socialMediaRaiser.twitter.signature.Oauth1SigningInterceptor;
import lombok.Data;
import lombok.NoArgsConstructor;
import okhttp3.*;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Data
@NoArgsConstructor
public class RequestHelper {

    private int sleepTime = 5;

    public JsonNode executeGetRequest(String url) {
        try {
            Response response = this.getHttpClient(url)
                    .newCall(this.getSignedRequest(this.getRequest(url), this.getNonce(), this.getTimestamp()))
                    .execute();
            JsonNode node = JsonHelper.OBJECT_MAPPER.readTree(response.body().string());
            if(response.code()==200){
                return node;
            } else if (response.code()==429){
                LocalDateTime now = LocalDateTime.now();
                System.out.println("\n" + response.message() +" at "
                        + now.getHour() + ":" + now.getMinute() + ". Waiting ... " + url);
                try {
                    TimeUnit.MINUTES.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return this.executeGetRequest(url);
            } else{
                System.err.println("(GET) not calling " + url + " 200 return null " + response.message() + " - " + response.code());
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public String executeGetRequestV2(String url) {
        try {
            Response response = this.getHttpClient(url)
                    .newCall(this.getSignedRequest(this.getRequest(url), this.getNonce(), this.getTimestamp())).execute();
            if(response.code()==200){
                String result = response.body().string();
                response.close();
                return result;
            } else if (response.code()==429){
                response.close();
                LocalDateTime now = LocalDateTime.now();
                System.out.println("\n" + response.message() +" at "
                        + now.getHour() + ":" + now.getMinute() + ". Waiting ... " + url);
                try {
                    TimeUnit.MINUTES.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return this.executeGetRequestV2(url);
            } else{
                System.err.println("(GET) not calling " + url + " 200 return null " + response.message() + " - " + response.code());
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public JsonNode executePostRequest(String url, Map<String, String> parameters) {
        try {
            String json = JsonHelper.OBJECT_MAPPER.writeValueAsString(parameters);

            RequestBody requestBody = RequestBody.create(null, json);

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            Request signedRequest = this.getSignedRequest(request, this.getNonce(), this.getTimestamp());

            Response response = this.getHttpClient(url)
                    .newCall(signedRequest).execute();

            if(response.code()!=200){
                System.err.println("(POST) ! not 200 calling " + url + " " + response.message() + " - " + response.code());
            }
            String stringResponse = response.body().string();
            return JsonHelper.OBJECT_MAPPER.readTree(stringResponse);

        } catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    public RequestTokenDTO executeTokenRequest(String url){
        try {
            Request request = new Request.Builder()
                    .url(url)
                    .post(RequestBody.create(null, "{}"))
                    .build();

            Request signedRequest = this.getSignedRequest(request, this.getNonce(), this.getTimestamp());

            Response response = this.getHttpClient(url).newCall(signedRequest).execute();

            if(response.code()!=200){
                System.err.println("(POST) ! not 200 calling " + url + " " + response.message() + " - " + response.code());
            }

            String stringResponse = response.body().string();

            List<NameValuePair> params = URLEncodedUtils.parse(new URI("twitter.com?"+stringResponse), Charset.forName("UTF-8").name());

            RequestTokenDTO requestTokenDTO = new RequestTokenDTO();

            for (NameValuePair param : params) {
                if(param.getName().equals("oauth_token")){
                    requestTokenDTO.setOauthToken(param.getValue());
                } else if (param.getName().equals("oauth_token_secret")){
                    requestTokenDTO.setOauthTokenSecret(param.getValue());
                }
            }

            return requestTokenDTO;
        } catch(IOException | URISyntaxException e){
            e.printStackTrace();
            return null;
        }
    }

    @Deprecated
    public JsonNode executeGetRequestReturningArray(String url) {
        try {
            Response response = this.getHttpClient(url)
                    .newCall(this.getSignedRequest(this.getRequest(url), this.getNonce(), this.getTimestamp())).execute();
            if(response.code()==200){
                String stringResult = response.body().string();
                JsonNode resultArray = JsonHelper.OBJECT_MAPPER.readTree(stringResult);
                return resultArray;
            } else if (response.code() == 401){
                response.close();
                System.out.println("user private, not authorized");
            } else if (response.code()==429){
                LocalDateTime now = LocalDateTime.now();
                System.out.println("\n" +           response.message() +" at "
                        + now.getHour() + ":" + now.getMinute() + ". Waiting ... " + url); // do a wait and return this function recursively
                try {
                    TimeUnit.MINUTES.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return this.executeGetRequestReturningArray(url);
            } else{
                System.err.println("not 200 (return null) calling " + url + " " + response.message() + " - " + response.code());
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
        String path = "okhttpCache";
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
            defaultCache = 12;
        } else if (url.contains("/friendships")){
            defaultCache = 0;
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
