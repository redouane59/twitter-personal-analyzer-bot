package com.socialMediaRaiser.twitter;

import com.socialMediaRaiser.IMainActionsById;
import com.socialMediaRaiser.IMainActionsByName;
import com.socialMediaRaiser.RelationType;
import com.socialMediaRaiser.twitter.helpers.JsonHelper;
import com.socialMediaRaiser.twitter.helpers.RequestHelper;
import com.socialMediaRaiser.twitter.helpers.RequestMethod;
import com.socialMediaRaiser.twitter.helpers.URLHelper;
import lombok.Data;
import org.json.JSONObject;

import java.net.http.HttpClient;
import java.util.*;

@Data
public class AbstractTwitterBot implements ITwitterBot, IMainActionsByName, IMainActionsById {

    private HttpClient client = HttpClient.newHttpClient();
    private URLHelper urlHelper = new URLHelper();
    private RequestHelper requestHelper = new RequestHelper();
    private JsonHelper jsonHelper = new JsonHelper();
    private final String IDS = "ids";
    private final String USERS = "users";
    private final String RETWEET_COUNT = "retweet_count";
    private final int MAX_GET_FOLLOWERS_CALLS = 15;

    public List<Long> getUserIdsByRelation(Long userId, RelationType relationType){
        Long cursor = -1L;
        List<Long> result = new ArrayList<>();
        String url;
        if(relationType == RelationType.FOLLOWER){
            url = this.urlHelper.getFollowersUrl(userId);
        } else if (relationType == RelationType.FOLLOWING){
            url = this.urlHelper.getFollowingsUrl(userId);
        } else{
            return null;
        }
        int nbCalls = 1;
        do {
            System.out.println("looking for following with cursor = " + cursor);
            String url_with_cursor = url + "&cursor=" + cursor;
            JSONObject response = this.getRequestHelper().executeRequest(url_with_cursor, RequestMethod.GET);
            result.addAll(this.getJsonHelper().jsonLongArrayToList(response.get(IDS)));
            cursor = this.getJsonHelper().getLongFromCursorObject(response);
            nbCalls++;
        }
        while (cursor != 0 && cursor != null && nbCalls < MAX_GET_FOLLOWERS_CALLS);
        System.out.println(result.size() + " " + relationType + "S found for " + userId);
        return result;
    }

    public List<String> getUserNamesByRelation(String userName, RelationType relationType) {
        Long cursor = -1L;
        List<String> result = new ArrayList<>();
        String url;
        if(relationType == RelationType.FOLLOWER){
            url = this.urlHelper.getFollowersUrl(userName);
        } else if (relationType == RelationType.FOLLOWING){
            url = this.urlHelper.getFollowingsUrl(userName);
        } else{
            return null;
        }
        int nbCalls = 1;
        do {
            System.out.println("looking fo following with cursor = " + cursor);
            String url_with_cursor = url + "&cursor=" + cursor;
            JSONObject response = this.getRequestHelper().executeRequest(url_with_cursor, RequestMethod.GET);
            result.addAll(this.getJsonHelper().jsonStringArrayToList(response.get(USERS)));
            cursor = this.getJsonHelper().getLongFromCursorObject(response);
            nbCalls++;
        }
        while (cursor != 0 && cursor != null && nbCalls < MAX_GET_FOLLOWERS_CALLS);
        System.out.println(result.size() + " " + relationType +"S found for " + userName);
        return result;
    }

    public List<User> getUsersInfoByRelation(String userName, RelationType relationType) {
        Long cursor = -1L;
        List<User> result = new ArrayList<>();
        String url;
        if(relationType == RelationType.FOLLOWER){
            url = this.urlHelper.getFollowersUrl(userName);
        } else if (relationType == RelationType.FOLLOWING){
            url = this.urlHelper.getFollowingsUrl(userName);
        } else{
            return null;
        }
        int nbCalls = 1;
        do {
            System.out.println("looking for following with cursor = " + cursor);
            String url_with_cursor = url + "&cursor=" + cursor;
            JSONObject response = this.getRequestHelper().executeRequest(url_with_cursor, RequestMethod.GET);
            if(response==null){
                break;
            }
            result.addAll(this.getJsonHelper().jsonUserArrayToList(response.get(USERS)));
            cursor = this.getJsonHelper().getLongFromCursorObject(response);
            nbCalls++;
        }
        while (cursor != 0 && cursor != null && nbCalls < MAX_GET_FOLLOWERS_CALLS);
        System.out.println(result.size() + " " + relationType + "S found for " + userName);
        return result;
    }

    @Override
    public List<Long> getFollowerIds(Long userId)  {
        return this.getUserIdsByRelation(userId, RelationType.FOLLOWER);
    }

    @Override
    public List<String> getFollowerNames(String userName) {
        return this.getUserNamesByRelation(userName, RelationType.FOLLOWER);
    }

    @Override
    public List<User> getFollowerUsers(String userName) {
        return this.getUsersInfoByRelation(userName, RelationType.FOLLOWER);
    }

    @Override
    public List<Long> getFollowingIds(Long userId) {
        return this.getUserIdsByRelation(userId, RelationType.FOLLOWING);
    }

    @Override
    public List<Long> getFollowingIdsByName(String userName) {
        Long cursor = -1L;
        List<Long> result = new ArrayList<>();
        String url = this.urlHelper.getFollowingsUrl(userName);
        int nbCalls = 1;
        do {
            System.out.println("looking for following with cursor = " + cursor);
            String url_with_cursor = url + "&cursor=" + cursor;
            JSONObject response = this.getRequestHelper().executeRequest(url_with_cursor, RequestMethod.GET);
            result.addAll(this.getJsonHelper().jsonLongArrayToList(response.get("users")));
            cursor = this.getJsonHelper().getLongFromCursorObject(response);
            nbCalls++;
        }
        while (cursor != 0 && cursor != null && nbCalls < MAX_GET_FOLLOWERS_CALLS);
        System.out.println(result.size() + " followings found for " + userName);
        return result;
    }

    @Override
    public List<String> getFollowingNames(String userName) {
        return this.getUserNamesByRelation(userName, RelationType.FOLLOWING);
    }

    @Override
    public List<User> getFollowingsUserList(String userName) {
        return this.getUsersInfoByRelation(userName, RelationType.FOLLOWING);
    }

    @Override
    public boolean follow(Long userId) {
        return false;
    }

    @Override
    public boolean unfollow(Long userId) {
        return false;
    }

    @Override
    public Boolean areFriends(Long userId1, Long userId2) {
        String url = this.urlHelper.getFriendshipUrl(userId1, userId2);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        if(response!=null){
            JSONObject relationship = (JSONObject)response.get("relationship");
            JSONObject sourceResult = (JSONObject)relationship.get("source");
            Boolean followedBy = (Boolean)sourceResult.get("followed_by");
            Boolean following = (Boolean)sourceResult.get("following");
            return (followedBy & following);
        } else{
            return null;
        }
    }

    public LinkedHashMap<String, Boolean> areFriends(String userName1, List<String> otherUsers){
        LinkedHashMap<String, Boolean> result = new LinkedHashMap<>();
        for(String otherUserName : otherUsers){
            Boolean areFriends = this.areFriends(userName1, otherUserName);
            if(areFriends==null){
                System.out.println("areFriends was null for " + otherUserName + "! -> false");
                areFriends = false;
            }
            result.put(otherUserName, areFriends);

        }
        return result;
    }

    @Override
    public Boolean areFriends(String userName1, String userName2) {
        String url = this.urlHelper.getFriendshipUrl(userName1, userName2);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        if(response!=null) {
            JSONObject relationship = (JSONObject) response.get("relationship");
            JSONObject sourceResult = (JSONObject) relationship.get("source");
            Boolean followedBy = (Boolean) sourceResult.get("followed_by");
            Boolean following = (Boolean) sourceResult.get("following");
            return (followedBy & following);
        } else{
            return null;
        }
    }

    @Override
    public List<Long> getRetweetersId(Long tweetId) {
        int nbRT = this.getNbRT(tweetId);
        List<Long> retweetersIds = new ArrayList<>();
        int count = this.urlHelper.RETWEET_MAX_COUNT;
        int countSum = 0;
        for(int i=0; i<(nbRT/this.urlHelper.RETWEET_MAX_COUNT+1); i++){ //@TODO add condition Retweeters : 11 / 15
            if(countSum+count>nbRT){
                count = nbRT-countSum;
            }
            int cursor = this.urlHelper.RETWEET_MAX_COUNT*i;
            String url = this.urlHelper.getRetweetersUrl(tweetId, cursor, count);
            JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
            if(response!=null) {
                retweetersIds.addAll(this.getJsonHelper().jsonLongArrayToList(response.get(IDS)));
            } else{
                break;
            }
            countSum += count;
        }
        return retweetersIds;
    }

    public int getNbRT(Long tweetId) {
        String url = this.urlHelper.getTweetInfoUrl(tweetId);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        if(response!=null){
            return (int)response.get(RETWEET_COUNT);
        } else{
            return -1;
        }
    }

    @Override
    public List<User> getRetweetersUsers(Long tweetId) {
        List<Long> retweetersIds = this.getRetweetersId(tweetId);
        List<User> result = new ArrayList<>();
        int i = 0;
        while(i<retweetersIds.size()){
            Long retweeterId = retweetersIds.get(i);
            User user = this.getUserInfoFromUserId(retweeterId);
            result.add(user);
            i++;
        }
        System.out.println("getRetweetersUsers | " + result.size() + " results" );
        return result;
    }

/*    @Override
    public int getNbFollowersById(Long userId)  {
        String url = this.urlHelper.getFollowersUrl(userId);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        if(response!=null){
            return this.getJsonHelper().jsonLongArrayToList(response.get(IDS)).size();
        } else{
            return -1;
        }
    } */

    @Override
    public boolean follow(String userName){
        String url = this.urlHelper.getFollowUrl(userName);
        JSONObject jsonResponse = this.requestHelper.executeRequest(url, RequestMethod.POST);
        if(jsonResponse!=null) {
            System.out.println(userName + " followed ! ");
            return true;
        }
        return false;
    }

    public List<User> follow(List<User> users) {
        List<User> followed = new ArrayList<>();
        for (User user : users) {
            this.follow(user.getScreen_name());
            followed.add(user);
        }
        return followed;
    }

    @Override
    public boolean unfollow(String userName){
        String url = this.urlHelper.getUnfollowUrl(userName);
        JSONObject jsonResponse = this.requestHelper.executeRequest(url, RequestMethod.POST);
        if(jsonResponse!=null){
            return true;
        }
        return false;
    }

    public User getUserInfoFromUserId(Long userId)  {
        String url = this.getUrlHelper().getUserUrl(userId);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        if(response!=null){
            return this.getJsonHelper().jsonResponseToUser(response);
        } else{
            return null;
        }
    }

    public User getUserInfoFromUserName(String userName)  {
        String url = this.getUrlHelper().getUserUrl(userName);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        if(response!=null){
            return this.getJsonHelper().jsonResponseToUser(response);
        } else{
            return null;
        }
    }

    public Boolean userFollowsOther(Long userId1, Long userId2)  {
        String url = this.getUrlHelper().getFriendshipUrl(userId1, userId2);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        JSONObject relationship = (JSONObject)response.get("relationship");
        JSONObject sourceResult = (JSONObject)relationship.get("source");
        return (Boolean)sourceResult.get("following");
    }

    public Boolean userIsFollowed (Long userId1, Long userId2)  {
        String url = this.getUrlHelper().getFriendshipUrl(userId1, userId2);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        JSONObject relationship = (JSONObject)response.get("relationship");
        JSONObject sourceResult = (JSONObject)relationship.get("source");
        return (Boolean)sourceResult.get("followed_by");
    }

    public Boolean userIsFollowed (String userName1, String userName2)  {
        String url = this.getUrlHelper().getFriendshipUrl(userName1, userName2);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        if(response != null){
            JSONObject relationship = (JSONObject)response.get("relationship");
            JSONObject sourceResult = (JSONObject)relationship.get("source");
            return (Boolean)sourceResult.get("followed_by");
        } else{
            return null;
        }
    }

    public List<String> unfollow(List<String> userNames) {
        List<String> unfollowed = new ArrayList<>();
        for(String userName : userNames){
            boolean result = this.unfollow(userName);
            if(result){
                unfollowed.add(userName);
            }
        }
        return unfollowed;
    }
}
