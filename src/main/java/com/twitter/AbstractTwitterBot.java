package com.twitter;

import com.IMainActionsById;
import com.IMainActionsByName;
import com.twitter.helpers.JsonHelper;
import com.twitter.helpers.RequestHelper;
import com.twitter.helpers.RequestMethod;
import com.twitter.helpers.URLHelper;
import lombok.Data;
import org.json.JSONObject;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

@Data
public class AbstractTwitterBot implements ITwitterBot, IMainActionsByName, IMainActionsById {

    private HttpClient client = HttpClient.newHttpClient();
    private URLHelper urlHelper = new URLHelper();
    private RequestHelper requestHelper = new RequestHelper();
    private JsonHelper jsonHelper = new JsonHelper();
    private final String IDS = "ids";
    private final String USERS = "users";
    private final String expectedLanguage = "fr";
    private final String RETWEET_COUNT = "retweet_count";

    @Override
    public List<Long> getFollowersIds(Long userId) throws IllegalAccessException {
        String url = this.urlHelper.getFollowersUrl(userId);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        return this.getJsonHelper().jsonLongArrayToList(response.get(IDS));
    }

    @Override
    public List<String> getFollowerNames(String userName) throws IllegalAccessException {
        String url = this.urlHelper.getFollowersUrl(userName);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        return this.getJsonHelper().jsonStringArrayToList(response.get(USERS));
    }

    @Override
    public List<TwitterUser> getFollowersUsers(String userName) throws IllegalAccessException {
        String url = this.urlHelper.getFollowersUrl(userName);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        if(response!=null){
            return this.getJsonHelper().jsonUserArrayToList(response.get(USERS), expectedLanguage);
        } else{
            return new ArrayList<>();
        }
    }

    @Override
    public List<Long> getFollowingsIds(Long userId) throws IllegalAccessException {
        String url = this.urlHelper.getFollowingsUrl(userId);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        return this.getJsonHelper().jsonLongArrayToList(response.get(IDS));
    }

    @Override
    public List<String> getFollowingNames(String name) throws IllegalAccessException {
        String url = this.urlHelper.getFollowingsUrl(name);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        return this.getJsonHelper().jsonStringArrayToList(response.get(USERS));
    }

    @Override
    public List<TwitterUser> getFollowingsUserList(String userName) throws IllegalAccessException {
        String url = this.urlHelper.getFollowingsUrl(userName);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        if(response!=null){
            return this.getJsonHelper().jsonUserArrayToList(response.get(USERS), expectedLanguage);
        } else{
            return new ArrayList<>();
        }
    }

    @Override
    public boolean follow(Long userId) throws IllegalAccessException {
        return false;
    }

    @Override
    public boolean unfollow(Long userId) throws IllegalAccessException {
        return false;
    }

    @Override
    public Boolean areFriends(Long userId1, Long userId2) throws IllegalAccessException {
        String url = this.urlHelper.getFriendshipUrl(userId1, userId2);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        JSONObject relationship = (JSONObject)response.get("relationship");
        JSONObject sourceResult = (JSONObject)relationship.get("source");
        Boolean followedBy = (Boolean)sourceResult.get("followed_by");
        Boolean following = (Boolean)sourceResult.get("following");
        return (followedBy & following);
    }

    @Override
    public Boolean areFriends(String userName1, String userName2) throws IllegalAccessException {
        String url = this.urlHelper.getFriendshipUrl(userName1, userName2);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        JSONObject relationship = (JSONObject)response.get("relationship");
        JSONObject sourceResult = (JSONObject)relationship.get("source");
        Boolean followedBy = (Boolean)sourceResult.get("followed_by");
        Boolean following = (Boolean)sourceResult.get("following");
        return (followedBy & following);
    }

    @Override
    public int getNbFollowingsById(Long userId) throws IllegalAccessException {
        String url = this.urlHelper.getFollowingsUrl(userId);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        return this.getJsonHelper().jsonLongArrayToList(response.get(IDS)).size();
    }

    @Override
    public int getNbFollowers(String userName) throws IllegalAccessException {
        String url = this.urlHelper.getFollowersUrl(userName);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        return this.getJsonHelper().jsonStringArrayToList(response.get(USERS)).size();    }

    @Override
    public int getNbFollowings(String userName) throws IllegalAccessException {
        String url = this.urlHelper.getFollowingsUrl(userName);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        return this.getJsonHelper().jsonStringArrayToList(response.get(USERS)).size();
    }

    @Override
    public List<Long> getRetweetersId(Long tweetId) throws IllegalAccessException {
        int nbRT = this.getNbRT(tweetId);
        List<Long> retweetersIds = new ArrayList<>();
        for(int i=0; i<(nbRT/this.urlHelper.RETWEET_MAX_COUNT); i++){
            int cursor = this.urlHelper.RETWEET_MAX_COUNT*i;
            String url = this.urlHelper.getRetweetersUrl(tweetId, cursor);
            JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
            retweetersIds.addAll(this.getJsonHelper().jsonLongArrayToList(response.get(IDS)));
        }
        return retweetersIds;
    }

    public int getNbRT(Long tweetId) throws IllegalAccessException {
        String url = this.urlHelper.getTweetInfoUrl(tweetId);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        return (int)response.get(RETWEET_COUNT);
    }

    @Override
    public List<TwitterUser> getRetweetersUsers(Long tweetId) throws IllegalAccessException {
        List<Long> retweetersIds = this.getRetweetersId(tweetId);
        List<TwitterUser> result = new ArrayList<>();
        int i = 0;
        while(i<retweetersIds.size() && this.getUrlHelper().canCallGetUser()){
            Long retweeterId = retweetersIds.get(i);
            TwitterUser user = this.getUserInfoFromUserId(retweeterId);
            result.add(user);
            i++;
        }
        System.out.println("getRetweetersUsers | " + result.size() + " results" );
        return result;
    }

    @Override
    public int getNbFollowersById(Long userId) throws IllegalAccessException {
        String url = this.urlHelper.getFollowersUrl(userId);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        return this.getJsonHelper().jsonLongArrayToList(response.get(IDS)).size();
    }

    @Override
    public boolean follow(String userName){
        String url = this.urlHelper.getFollowUrl(userName);

        try {
            JSONObject jsonResponse = this.requestHelper.executeRequest(url, RequestMethod.POST);
            if(jsonResponse!=null){
                return true;
            }
        } catch(IllegalAccessException e){
            e.printStackTrace();
        }

        return false;
    }

    public List<String> follow(List<String> userNames){
        List<String> followed = new ArrayList<>();
        for(String name : userNames){
            boolean result = this.follow(name);
            if(result){
                followed.add(name);
            }
        }
        return followed;
    }

    @Override
    public boolean unfollow(String userName){
        return false;
    }

    public TwitterUser getUserInfoFromUserId(Long userId) throws IllegalAccessException {
        String url = this.getUrlHelper().getUserUrl(userId);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        return this.getJsonHelper().jsonResponseToUser(response);
    }

    public Boolean userFollowsOther(Long userId1, Long userId2) throws IllegalAccessException {
        String url = this.getUrlHelper().getFriendshipUrl(userId1, userId2);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        JSONObject relationship = (JSONObject)response.get("relationship");
        JSONObject sourceResult = (JSONObject)relationship.get("source");
        return (Boolean)sourceResult.get("following");
    }

    public Boolean userIsFollowed (Long userId1, Long userId2) throws IllegalAccessException {
        String url = this.getUrlHelper().getFriendshipUrl(userId1, userId2);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        JSONObject relationship = (JSONObject)response.get("relationship");
        JSONObject sourceResult = (JSONObject)relationship.get("source");
        return (Boolean)sourceResult.get("followed_by");
    }

    public Boolean userIsFollowed (String userName1, String userName2) throws IllegalAccessException {
        String url = this.getUrlHelper().getFriendshipUrl(userName1, userName2);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        JSONObject relationship = (JSONObject)response.get("relationship");
        JSONObject sourceResult = (JSONObject)relationship.get("source");
        return (Boolean)sourceResult.get("followed_by");
    }
}
