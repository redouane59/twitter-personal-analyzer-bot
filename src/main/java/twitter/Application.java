package twitter;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;
import twitter.helpers.Oauth1SigningInterceptor;
import twitter.helpers.SignatureConstants;
import twitter.helpers.URLHelper;

import java.io.IOException;
import java.net.http.HttpClient;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class Application implements IApplication {

    private HttpClient client = HttpClient.newHttpClient();
    private URLHelper urlHelper = new URLHelper();
    private final String IDS = "ids";
    private final String USERS = "users";
    private final String SCREEN_NAME = "screen_name";

    JSONObject executeRequest(String url) throws IllegalAccessException {
        return this.executeRequest(url, this.getNonce(), this.getTimestamp());
    }

    JSONObject executeRequest(String url, String nonce, String timestamp) throws IllegalAccessException {

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

        try {
            Request signed = oauth1.signRequest(request);
            OkHttpClient client = new OkHttpClient();

            Response response = client.newCall(signed).execute();

            JSONObject jsonResponse = new JSONObject(response.body().string());

            if(response.code()==200){
                return jsonResponse;
            } else{
                throw new IllegalAccessException("error " + response.code());
            }
        } catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Long> getFollowersList(Long userId) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.FOLLOWERS, userId);
        JSONObject response = this.executeRequest(url);
        return this.jsonLongArrayToList(response.get(IDS));
    }

    @Override
    public List<String> getFollowersList(String userName) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.FOLLOWERS, userName);
        JSONObject response = this.executeRequest(url);
        return this.jsonStringArrayToList(response.get(USERS));
    }

    @Override
    public List<Long> getFollowingsList(Long userId) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.FOLLOWING, userId);
        JSONObject response = this.executeRequest(url);
        return this.jsonLongArrayToList(response.get(IDS));
    }

    @Override
    public List<String> getFollowingsList(String name) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.FOLLOWING, name);
        JSONObject response = this.executeRequest(url);
        return this.jsonStringArrayToList(response.get(USERS));
    }

    @Override
    public Boolean areFriends(Long userId1, Long userId2) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.FRIENDSHIP, userId1, userId2);
        JSONObject response = this.executeRequest(url);
        JSONObject relationship = (JSONObject)response.get("relationship");
        JSONObject sourceResult = (JSONObject)relationship.get("source");
        Boolean followedBy = (Boolean)sourceResult.get("followed_by");
        Boolean following = (Boolean)sourceResult.get("following");
        return (followedBy & following);
    }

    @Override
    public int getNbFollowers(Long userId) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.FOLLOWERS, userId);
        JSONObject response = this.executeRequest(url);
        return this.jsonLongArrayToList(response.get(IDS)).size();
    }

    @Override
    public int getNbFollowings(Long userId) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.FOLLOWING, userId);
        JSONObject response = this.executeRequest(url);
        return this.jsonLongArrayToList(response.get(IDS)).size();
    }

    @Override
    public int getNbFollowers(String userName) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.FOLLOWERS, userName);
        JSONObject response = this.executeRequest(url);
        return this.jsonStringArrayToList(response.get(USERS)).size();    }

    @Override
    public int getNbFollowings(String userName) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.FOLLOWING, userName);
        JSONObject response = this.executeRequest(url);
        return this.jsonStringArrayToList(response.get(USERS)).size();
    }

    @Override
    public List<Long> getRetweetersId(Long tweetId) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.RETWEETERS, tweetId);
        JSONObject response = this.executeRequest(url);
        return this.jsonLongArrayToList(response.get(IDS));
    }

    @Override
    public List<String> getRetweetersNames(Long tweetId) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.RETWEETERS, tweetId);
        JSONObject response = this.executeRequest(url);
        return this.jsonStringArrayToList(response.get(IDS));
    }

    public double getFollowersFollowingRatio(Long userId) throws IllegalAccessException {
        int nbFollowers = this.getNbFollowers(userId);
        int nbFollowings = this.getNbFollowings(userId);
        return (double)nbFollowers/(double)nbFollowings;
    }

    public boolean shouldFollow(Long userId) throws IllegalAccessException {
        double followersRatio = this.getFollowersFollowingRatio(userId);
        int minRatio = 1;
        int maxRatio = 3;

        if(followersRatio>minRatio && followersRatio<maxRatio){
            return true;
        } else{
            return false;
        }
    }

    public List<Long> getPotentialFollowersFromRetweet(Long tweetId) {

        List<Long> potentialFollowers = new ArrayList<>();
        try{
            List<Long> retweeters = this.getRetweetersId(tweetId);
            for(Long userId : retweeters){
                if(this.shouldFollow(userId)){
                    potentialFollowers.add(userId);
                }
            }
            return potentialFollowers;
        } catch(Exception e){
            return potentialFollowers;
        }
    }


    public List<Long> getUsersNotFollowingBack(Long userId) throws IllegalAccessException {
        List<Long> notFollowingsBackUsers = new ArrayList<>();
        List<Long> followersList = this.getFollowersList(userId);
        for(Long followerId : followersList){
            // @TODO to finish
            notFollowingsBackUsers.add(followerId);
        }
        return notFollowingsBackUsers;
    }

    public void unfollow(Long userId, Long userToUnfollowId){
        return;
    }

    public void follow(Long userId, Long userToUnfollowId){
        return;
    }

    public String getNonce(){
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            stringBuilder.append(secureRandom.nextInt(10));
        }
        return stringBuilder.toString();
    }

    public String getTimestamp(){
        return String.valueOf(new Timestamp(System.currentTimeMillis()).getTime()).substring(0,10);
    }

    public List<Long> jsonLongArrayToList(Object jsonObject){
        JSONArray jArray = (JSONArray)jsonObject;
        ArrayList<Long> listdata = new ArrayList<Long>();
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                listdata.add(jArray.getLong(i));
            }
        }
        return listdata;
    }

    public List<String> jsonStringArrayToList(Object jsonObject){
        JSONArray jArray = (JSONArray)jsonObject;
        ArrayList<String> listdata = new ArrayList<String>();
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                listdata.add(jArray.getJSONObject(i).get(SCREEN_NAME).toString());
            }
        }
        return listdata;
    }


}
