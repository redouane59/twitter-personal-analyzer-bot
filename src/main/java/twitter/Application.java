package twitter;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
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

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try {
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(this.getSignedRequest(request, nonce, timestamp)).execute();
            System.out.println("executing 1 request " + url);
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
        String url = this.urlHelper.getUrl(Action.GET_FOLLOWERS, userId);
        JSONObject response = this.executeRequest(url);
        return this.jsonLongArrayToList(response.get(IDS));
    }

    @Override
    public List<String> getFollowersList(String userName) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FOLLOWERS, userName);
        JSONObject response = this.executeRequest(url);
        return this.jsonStringArrayToList(response.get(USERS));
    }

    public List<User> getFollowersUserList(String userName) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FOLLOWERS, userName);
        JSONObject response = this.executeRequest(url);
        return this.jsonUserArrayToList(response.get(USERS));
    }

    @Override
    public List<Long> getFollowingsList(Long userId) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FOLLOWING, userId);
        JSONObject response = this.executeRequest(url);
        return this.jsonLongArrayToList(response.get(IDS));
    }

    @Override
    public List<String> getFollowingsList(String name) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FOLLOWING, name);
        JSONObject response = this.executeRequest(url);
        return this.jsonStringArrayToList(response.get(USERS));
    }

    @Override
    public Boolean areFriends(Long userId1, Long userId2) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FRIENDSHIP, userId1, userId2);
        JSONObject response = this.executeRequest(url);
        JSONObject relationship = (JSONObject)response.get("relationship");
        JSONObject sourceResult = (JSONObject)relationship.get("source");
        Boolean followedBy = (Boolean)sourceResult.get("followed_by");
        Boolean following = (Boolean)sourceResult.get("following");
        return (followedBy & following);
    }

    public Boolean userFollowsOther(Long userId1, Long userId2) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FRIENDSHIP, userId1, userId2);
        JSONObject response = this.executeRequest(url);
        JSONObject relationship = (JSONObject)response.get("relationship");
        JSONObject sourceResult = (JSONObject)relationship.get("source");
        return (Boolean)sourceResult.get("following");
    }

    public Boolean userIsFollowed (Long userId1, Long userId2) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FRIENDSHIP, userId1, userId2);
        JSONObject response = this.executeRequest(url);
        JSONObject relationship = (JSONObject)response.get("relationship");
        JSONObject sourceResult = (JSONObject)relationship.get("source");
        return (Boolean)sourceResult.get("followed_by");
    }

    @Override
    public int getNbFollowers(Long userId) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FOLLOWERS, userId);
        JSONObject response = this.executeRequest(url);
        return this.jsonLongArrayToList(response.get(IDS)).size();
    }

    @Override
    public int getNbFollowings(Long userId) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FOLLOWING, userId);
        JSONObject response = this.executeRequest(url);
        return this.jsonLongArrayToList(response.get(IDS)).size();
    }

    @Override
    public int getNbFollowers(String userName) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FOLLOWERS, userName);
        JSONObject response = this.executeRequest(url);
        return this.jsonStringArrayToList(response.get(USERS)).size();    }

    @Override
    public int getNbFollowings(String userName) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FOLLOWING, userName);
        JSONObject response = this.executeRequest(url);
        return this.jsonStringArrayToList(response.get(USERS)).size();
    }

    @Override
    public List<Long> getRetweetersId(Long tweetId) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_RETWEETERS, tweetId);
        JSONObject response = this.executeRequest(url);
        return this.jsonLongArrayToList(response.get(IDS));
    }

    @Override
    public List<String> getRetweetersNames(Long tweetId) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_RETWEETERS, tweetId);
        JSONObject response = this.executeRequest(url);
        return this.jsonStringArrayToList(response.get(IDS));
    }

    public double getFollowersFollowingRatio(Long userId) throws IllegalAccessException {
        int nbFollowers = this.getNbFollowers(userId);
        int nbFollowings = this.getNbFollowings(userId);
        return (double)nbFollowers/(double)nbFollowings;
    }

    public boolean shouldFollow(Long userId) throws IllegalAccessException {
        int minNbFollowers = 100;
        int minRatio = 1;
        int maxRatio = 3;

        int nbFollowers = this.getNbFollowers(userId);
        if(nbFollowers<minNbFollowers){
            return false;
        }
        int nbFollowings = this.getNbFollowings(userId);
        double followersRatio = (double)nbFollowers/(double)nbFollowings;
        if(followersRatio>minRatio && followersRatio<maxRatio){
            return true;
        } else{
            return false;
        }
    }

    public List<Long> getPotentialFollowersFromRetweet(Long tweetId, Long userId) {

        List<Long> potentialFollowers = new ArrayList<>();
        try {
            List<Long> retweeters = this.getRetweetersId(tweetId);
            for (Long retweeterId : retweeters) {
                if (!this.userFollowsOther(userId, retweeterId)
                        && !this.userIsFollowed(userId, retweeterId)
                        && this.shouldFollow(retweeterId)) {
                    potentialFollowers.add(retweeterId);
                }
            }
            return potentialFollowers;
        } catch (Exception e) {
            return potentialFollowers;
        }
    }

    public List<String> getPotentialFollowersFromUserFollowers(List<String> otherUserNames, Long userId) throws IllegalAccessException{
        List<String> results = new ArrayList<>();
        for(String name : otherUserNames){
            results.addAll(this.getPotentialFollowersFromUserFollowers(name, userId));
        }
        return results;
    }

    public List<String> getPotentialFollowersFromUserFollowers(String otherUserName, Long userId) throws IllegalAccessException {
        List<String> potentialFollowers = new ArrayList<>();
        List<User> followers = this.getFollowersUserList(otherUserName);

        for (User follower : followers) {
            boolean alreadyFollowed = true;
            try{
                alreadyFollowed = this.userIsFollowed(userId, follower.getId());
            }
            catch(Exception e){}
            if (!alreadyFollowed && follower.shouldBeFollowed()) {
                potentialFollowers.add(follower.getScreen_name());
            }
        }
        return potentialFollowers;
    }

    public List<User> followCommonFollowers(String userName) throws IllegalAccessException {
        List<String> someFollowers = this.getFollowersList(userName);
        List<User> followedUsers = new ArrayList<>();
        for(String followerName : someFollowers){
            List<User> followers = this.getFollowersUserList(followerName);
            for (User follower : followers) {
                if (follower.shouldBeFollowed()) {
                    boolean foolowed = this.follow(follower.getScreen_name());
                    if(foolowed){
                        followedUsers.add(follower);
                    }
                }
            }
        }
        return followedUsers;
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



    public boolean follow(String userName){
     //   curl --request POST
     //   --url 'https://api.twitter.com/1.1/friendships/create.json?user_id=2244994945&follow=true'
     //           --header 'authorization: OAuth oauth_consumer_key="YOUR_CONSUMER_KEY", oauth_nonce="AUTO_GENERATED_NONCE", oauth_signature="AUTO_GENERATED_SIGNATURE", oauth_signature_method="HMAC-SHA1", oauth_timestamp="AUTO_GENERATED_TIMESTAMP", oauth_token="USERS_ACCESS_TOKEN", oauth_version="1.0"'
     //           --header 'content-type: application/json'
        String url = this.urlHelper.getUrl(Action.FOLLOW, userName);
        RequestBody reqbody = RequestBody.create(null, new byte[0]);

        Request request = new Request.Builder()
                .url(url)
                .post(reqbody)
                .build();

        try {
            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(this.getSignedRequest(request, this.getNonce(), this.getTimestamp())).execute();
            System.out.println("executing 1 request " + url);
            JSONObject jsonResponse = new JSONObject(response.body().string());
            if(response.code()==200){
                return true;
            }
        } catch(IOException e){
            e.printStackTrace();
        }

        return false;
    }

    public void unfollow(Long userId, Long userToUnfollowId){
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
        ArrayList<Long> listdata = new ArrayList<>();
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                listdata.add(jArray.getLong(i));
            }
        }
        return listdata;
    }

    public List<String> jsonStringArrayToList(Object jsonObject){
        JSONArray jArray = (JSONArray)jsonObject;
        ArrayList<String> listdata = new ArrayList<>();
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                listdata.add(jArray.getJSONObject(i).get(SCREEN_NAME).toString());
            }
        }
        return listdata;
    }

    public List<User> jsonUserArrayToList(Object jsonObject){
        JSONArray jArray = (JSONArray)jsonObject;
        ArrayList<User> listdata = new ArrayList<>();
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                Long id = Long.valueOf(jArray.getJSONObject(i).get("id").toString());
                String screenName = jArray.getJSONObject(i).get(SCREEN_NAME).toString();
                int followersCount = (int)jArray.getJSONObject(i).get("followers_count");
                int friendsCount = (int)jArray.getJSONObject(i).get("friends_count");
                User user = new User(id, screenName, followersCount, friendsCount);
                listdata.add(user);
            }
        }
        return listdata;
    }

    public Request getSignedRequest(Request request, String nonce, String timestamp) throws IOException {
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
