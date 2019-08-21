package com.socialMediaRaiser.twitter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialMediaRaiser.AbstractBot;
import com.socialMediaRaiser.RelationType;
import com.socialMediaRaiser.twitter.helpers.*;
import com.socialMediaRaiser.twitter.helpers.dto.IUser;
import com.socialMediaRaiser.twitter.helpers.dto.getRelationship.RelationshipDTO;
import com.socialMediaRaiser.twitter.helpers.dto.getRelationship.RelationshipObjectResponseDTO;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import lombok.Data;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Data
public abstract class AbstractTwitterBot extends AbstractBot implements ITwitterBot{

    private String ownerName;
    private URLHelper urlHelper = new URLHelper();
    private RequestHelper requestHelper = new RequestHelper();
    private JsonHelper jsonHelper = new JsonHelper();
    private final String IDS = "ids";
    private final String USERS = "users";
    private final String CURSOR = "cursor";
    private final String NEXT = "next";
    private final String RETWEET_COUNT = "retweet_count";
    private final String RELATIONSHIP = "relationship";
    private final String FOLLOWING = "following";
    private final String FOLLOWED_BY = "followed_by";
    private final String SOURCE = "source";
    private final int MAX_GET_F_CALLS = 30;

    public AbstractTwitterBot(String ownerName){
        super(new GoogleSheetHelper(ownerName));
        this.ownerName = ownerName;
    }

    // can manage up to 5000 results / call . Max 15 calls / 15min ==> 75.000 results max. / 15min
    private List<String> getUserIdsByRelation(String url){
        Long cursor = -1L;
        List<String> result = new ArrayList<>();
        int nbCalls = 1;
        do {
            String url_with_cursor = url + "&"+CURSOR+"=" + cursor;
            JSONObject response = this.getRequestHelper().executeGetRequest(url_with_cursor);
            if(response!=null && response.has(IDS)){
                List<String> ids = this.getJsonHelper().jsonLongArrayToList(response.get(IDS));
                if(ids!=null){
                    result.addAll(ids);
                }
            } else{
                System.err.println("response null or ids not found !");
            }

            cursor = this.getJsonHelper().getLongFromCursorObject(response);
            nbCalls++;
        }
        while (cursor != null && cursor != 0 && nbCalls < MAX_GET_F_CALLS);
        return result;
    }

    // can manage up to 200 results/call . Max 15 calls/15min ==> 3.000 results max./15min
    private List<IUser> getUsersInfoByRelation(String url) {
        Long cursor = -1L;
        List<IUser> result = new ArrayList<>();
        int nbCalls = 1;
        System.out.print("users : ");
        do {
            String url_with_cursor = url + "&"+CURSOR+"=" + cursor;
            JSONObject response = this.getRequestHelper().executeGetRequest(url_with_cursor);
            if(response==null){
                break;
            }
            List<IUser> users = this.getJsonHelper().jsonUserArrayToList(response.get(USERS));
            result.addAll(users);
            cursor = this.getJsonHelper().getLongFromCursorObject(response);
            nbCalls++;
            System.out.print(result.size() + " | ");
        } while (cursor != 0 && cursor!=null && nbCalls < MAX_GET_F_CALLS);
        System.out.print("\n");
        return result;
    }

    private List<String> getUserIdsByRelation(String userId, RelationType relationType){
        String url = null;
        if(relationType == RelationType.FOLLOWER){
            url = this.urlHelper.getFollowerIdsUrl(userId);
        } else if (relationType == RelationType.FOLLOWING){
            url = this.urlHelper.getFollowingIdsUrl(userId);
        }
        return this.getUserIdsByRelation(url);
    }

    private List<IUser> getUsersInfoByRelation(String userId, RelationType relationType) {
        String url = null;
        if(relationType == RelationType.FOLLOWER){
            url = this.urlHelper.getFollowerUsersUrl(userId);
        } else if (relationType == RelationType.FOLLOWING){
            url = this.urlHelper.getFollowingUsersUrl(userId);
        }
        return this.getUsersInfoByRelation(url);
    }

    @Override
    public List<String> getFollowerIds(String userId)  {
        return this.getUserIdsByRelation(userId, RelationType.FOLLOWER);
    }

    @Override
    public List<IUser> getFollowerUsers(String userId) {
        return this.getUsersInfoByRelation(userId, RelationType.FOLLOWER);
    }

    @Override
    public List<String> getFollowingIds(String userId) {
        return this.getUserIdsByRelation(userId, RelationType.FOLLOWING);
    }

    @Override
    public List<IUser> getFollowingsUsers(String userId) {
        return this.getUsersInfoByRelation(userId, RelationType.FOLLOWING);
    }

    @Override
    public RelationType getRelationType(String userId1, String userId2){
        String url = this.urlHelper.getFriendshipUrl(userId1, userId2);
        String response = this.getRequestHelper().executeGetRequestV2(url);
        if(response!=null) {
            try {
                RelationshipDTO relationshipDTO = new ObjectMapper().readValue(this.getRequestHelper().executeGetRequestV2(url), RelationshipObjectResponseDTO.class).getRelationship();
                Boolean followedBy = relationshipDTO.getSource().isFollowed_by();
                Boolean following = relationshipDTO.getSource().isFollowing();
                if(followedBy && !following){
                    return RelationType.FOLLOWER;
                } else if (!followedBy && following){
                    return RelationType.FOLLOWING;
                } else if (!followedBy && !following){
                    return RelationType.NONE;
                } else if (followedBy && following){
                    return RelationType.FRIENDS;
                }
            } catch (IOException e) {
                System.err.print(e.getMessage() + " response = " + response);
            }
        }
        System.err.print("areFriends was null for " + userId2 + "! -> false ");
        return null;
    }

    // API KO
    @Override
    public List<String> getRetweetersId(String tweetId) {
        String url = this.urlHelper.getRetweetersUrl(tweetId);
        return this.getUserIdsByRelation(url);
    }

    @Override
    public boolean follow(String userId) {
        String url = this.urlHelper.getFollowUrl(userId);
        JSONObject jsonResponse = this.requestHelper.executePostRequest(url, new HashMap<>());
        if(jsonResponse!=null) {
            if (jsonResponse.has(JsonHelper.FOLLOWING)) {
                System.out.println(userId + " followed ! ");
                return true;
            } else{
                System.err.println("following property not found :(  " + userId + " not followed !");
            }
        }
        System.err.println("jsonResponse was null for user  " + userId);
        return false;
    }

    @Override
    public boolean unfollow(String userId) {
        String url = this.urlHelper.getUnfollowUrl(userId);
        JSONObject jsonResponse = this.requestHelper.executePostRequest(url, new HashMap<>());
        if(jsonResponse!=null){
            System.out.println(userId + " unfollowed");
            return true;
        }
        System.err.println(userId + " not unfollowed");
        return false;
    }

    public IUser getUserFromUserId(String userId)  {
        String url = this.getUrlHelper().getUserUrl(userId);
        String response = this.getRequestHelper().executeGetRequestV2(url);
        if(response!=null){
            try{
                return this.getJsonHelper().jsonResponseToUserV2(response);
            } catch(Exception e){
                System.err.print(e.getMessage() + " response = " + response);
                e.printStackTrace();
            }
        }
        System.err.println("getUserFromUserId return null for " + userId);
        return null;
    }

    @Override
    public IUser getUserFromUserName(String userName) {
        String url = this.getUrlHelper().getUserUrlFromName(ownerName);
        String response = this.getRequestHelper().executeGetRequestV2(url);
        if (response != null) {
            try {
              //  UserObjectResponseDTO userObjectResponseDTO = new ObjectMapper().readValue(response, UserObjectResponseDTO.class);
                IUser user = this.getJsonHelper().jsonResponseToUserV2(response); // @todo find solution to use the dto directly
                return user;
            } catch (IOException e) {
                System.err.print(e.getMessage() + " response = " + response);
            }
        }
        return null;
    }

    public List<IUser> getUsersFromUserNames(List<String> userNames)  {
        String url = this.getUrlHelper().getUsersUrlbyNames(userNames);
        JSONArray response = this.getRequestHelper().executeGetRequestReturningArray(url);
        if(response!=null){
            return this.getJsonHelper().jsonUserArrayToList(response);
        } else{
            return null;
        }
    }

    List<IUser> getUsersFromUserIds(List<String> userIds)  {
        String url = this.getUrlHelper().getUsersUrlbyIds(userIds);
        JSONArray response = this.getRequestHelper().executeGetRequestReturningArray(url);
        if(response!=null){
            return this.getJsonHelper().jsonUserArrayToList(response);
        } else{
            return null;
        }
    }

    // @TODO to implement
    public String getRateLimitStatus(){
        String url = this.getUrlHelper().getRateLimitUrl();
        return this.getRequestHelper().executeGetRequestV2(url);
    }

    public void checkNotFollowBack(String ownerName, boolean unfollow, boolean writeInSheet, Date date, boolean override) {
        List<String> followedPreviously = this.getIOHelper().getPreviouslyFollowedIds(override, override, date);
        if(followedPreviously!=null && followedPreviously.size()>0){
            IUser user = this.getUserFromUserName(ownerName);
            this.areFriends(user.getId(), followedPreviously, unfollow, writeInSheet);
        } else{
            System.err.println("no followers found at this date");
        }
    }

    public List<Tweet> getUserLastTweets(String userId, int count){
        String url = this.getUrlHelper().getUserTweetsUrl(userId, count);
        JSONArray response = this.getRequestHelper().executeGetRequestReturningArray(url);
        if(response!=null && response.length()>0){
            return this.getJsonHelper().jsonResponseToTweetList(response);
        }
        return null;
    }

    @Override
    public void likeTweet(String tweetId) {
        String url = this.getUrlHelper().getLikeUrl(tweetId);
        this.getRequestHelper().executePostRequest(url, null);
    }

    @Override
    public void retweetTweet(String tweetId) {

    }

    // @todo remove count
    @Override
    public List<Tweet> searchForTweets(String query, int count, Date fromDate, Date toDate){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");

        if(count<10){
            count = 10;
            System.err.println("count minimum = 10");
        }
        if(count>100){
            count = 100;
            System.err.println("count maximum = 100");
        }
        String url = this.getUrlHelper().getSearchTweetsUrl();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("query",query);
        parameters.put("maxResults",String.valueOf(count));
        parameters.put("fromDate",dateFormat.format(fromDate));
        parameters.put("toDate",dateFormat.format(toDate));


        String next;
        List<Tweet> result = new ArrayList<>();
        int nbCalls = 1;
        do {
            JSONObject response = this.getRequestHelper().executePostRequest(url,parameters);
            JSONArray responseArray = (JSONArray)response.get("results");

            if(response!=null && response.length()>0){
                result.addAll(this.getJsonHelper().jsonResponseToTweetList(responseArray));
            } else{
                System.err.println("response null or ids not found !");
            }

            if(!response.has(NEXT)){
                break;
            }
            next = response.get(NEXT).toString();
            parameters.put(NEXT, next);
            nbCalls++;
        }
        while (next!= null && nbCalls < MAX_GET_F_CALLS && result.size()<count);
        return result;
    }

    // @todo to put in user
    public boolean isLanguageOK(User user){
        if(user.getLang()==null){
            user.addLanguageFromLastTweet(this.getUserLastTweets(user.getId(), 3)); // really slow
        }
        return (user.getLang()!=null && user.getLang().equals(FollowProperties.targetProperties.getLanguage()));
    }

    protected Authentication getAuthentication(){
        return new OAuth1(
                FollowProperties.twitterCredentials.getConsumerKey(),
                FollowProperties.twitterCredentials.getConsumerSecret(),
                FollowProperties.twitterCredentials.getAccessToken(),
                FollowProperties.twitterCredentials.getSecretToken());
    }
}
