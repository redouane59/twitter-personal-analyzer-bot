package com.socialMediaRaiser.twitter.helpers;

import lombok.Data;

import java.util.List;

@Data
public class URLHelper {

    private final String ROOT_URL = "https://api.twitter.com/1.1";
    private final String ROOT_URL_V2 = "https://api.twitter.com/labs/1";
    private final String IDS_JSON = "/ids.json?";
    private final String SCREEN_NAME = "screen_name";
    private final String ID = "id";
    private final String COUNT = "count";
    private final String LIST_JSON = "/list.json?";
    private final String SHOW_JSON = "/show.json?";
    private final String CREATE_JSON = "/create.json?";
    private final String DESTROY_JSON = "/destroy.json?";
    private final String RETWEETERS = "/retweeters";
    private final String FOLLOWERS = "/followers";
    private final String FRIENDS = "/friends";
    private final String STATUSES = "/statuses";
    private final String FRIENDSHIPS = "/friendships";
    private final String FAVORITES = "/favorites";
    private final String USERS = "/users";
    private static final String TWEETS = "/tweets";
    private static final String SEARCH = "/search";
    private static final String THIRTY_DAYS = "/30day";
    private static final String DEV_ENV_NAME = "/dev"; // @todo config
    private static final String ACCOUNT_ACTIVITY = "/account_activity/all";
    private static final String WEBHOOKS = "/webhooks";
    private final String USER_ID = "user_id";
    private final String LOOKUP_JSON = "/lookup.json?";
    private final String USER_TIMELINE = "/user_timeline.json?";
    private final String TRIM_USER = "trim_user=true";
    private final String EXCLUDE_RTS = "include_rts=false";
    private final String USER_FORMAT_DETAILED= "user.format=detailed";
    private final String TWEET_FORMAT_DETAILED= "tweet.format=detailed";
    private final String EXPANSIONS_RECENT_TWEET= "expansions=most_recent_tweet_id";
    private final int maxCount = 200;
    private int retweeterCount = 0;
    private int followersCount = 0;
    private int followingCount = 0;
    private int friendshipCount = 0;
    private int followCount = 0;
    private int unfollowCount = 0;
    private int userCount = 0;
    private int tweetInfoCount = 0;
    public static final int RETWEETER_MAX_CALLS = 15;
    public static final int FOLLOWER_MAX_CALLS = 15;
    public static final int FOLLOWING_MAX_CALLS = 15;
    public static final int FRIENDSHIP_MAX_CALLS = 180;
    public static final int FOLLOW_MAX_CALLS = 350;
    private static final int UNFOLLOW_MAX_CALLS = 350;
    public static int USER_MAX_CALLS = 900;
    public static int RETWEET_MAX_COUNT = 100;
    private static final int TWEET_INFO_MAX_CALLS = 900;
    private final int MAX_LOOKUP = 100;


    public String getFollowUrl(String userId) {
        this.followCount++;
        return new StringBuilder(ROOT_URL)
                .append(FRIENDSHIPS)
                .append(CREATE_JSON)
                .append(USER_ID+"=")
                .append(userId)
                .toString();
    }

    public String getUnfollowUrl(String userId) {
        this.unfollowCount++;
        return new StringBuilder(ROOT_URL)
                .append(FRIENDSHIPS)
                .append(DESTROY_JSON)
                .append(USER_ID+"=")
                .append(userId)
                .toString();
    }

    public String getFriendshipUrl(String sourceId, String targetId) {
        this.friendshipCount++;
        if(friendshipCount%50==0){
            System.out.println("friendship : " + friendshipCount + " / " + FRIENDSHIP_MAX_CALLS);
        }
        return new StringBuilder(ROOT_URL)
                .append(FRIENDSHIPS)
                .append(SHOW_JSON)
                .append("source_"+ ID +"=")
                .append(sourceId)
                .append("&target_"+ ID +"=")
                .append(targetId)
                .toString();
    }

    public String getRetweetersUrl(String tweetId){
        this.retweeterCount++;
        System.out.println("Retweeters : " + retweeterCount + " / " + RETWEETER_MAX_CALLS);
        return new StringBuilder(ROOT_URL)
                .append(STATUSES)
                .append(RETWEETERS)
                .append(IDS_JSON)
                .append(ID +"=")
                .append(tweetId)
                .append("&"+COUNT+"=")
                .append(RETWEET_MAX_COUNT)
                .toString();
    }

    public String getFollowerIdsUrl(String userId){
        this.followersCount++;
        if(followersCount%5==0) {
            System.out.println("Followers : " + followersCount + " / " + FOLLOWER_MAX_CALLS);
        }
        return new StringBuilder(ROOT_URL)
                .append(FOLLOWERS)
                .append(IDS_JSON)
                .append(USER_ID + "=")
                .append(userId)
                .toString();
    }


    public String getFollowerUsersUrl(String userId){
        this.followersCount++;
        if(followersCount%5==0) {
            System.out.println("Followers : " + followersCount + " / " + FOLLOWER_MAX_CALLS);
        }
        return new StringBuilder(ROOT_URL)
                .append(FOLLOWERS)
                .append(LIST_JSON)
                .append(USER_ID + "=")
                .append(userId)
                .append("&"+COUNT+"=")
                .append(maxCount)
                .toString();
    }

    public String getFollowingIdsUrl(String userId){
        this.followingCount++;
        System.out.println("Followings : " + followingCount + " / " + FOLLOWING_MAX_CALLS);
        return new StringBuilder(ROOT_URL)
                .append(FRIENDS)
                .append(IDS_JSON)
                .append(USER_ID + "=")
                .append(userId).toString();
    }

    public String getFollowingUsersUrl(String userId){
        this.followingCount++;
        System.out.println("Followings : " + followingCount + " / " + FOLLOWING_MAX_CALLS);
        return new StringBuilder(ROOT_URL)
                .append(FRIENDS)
                .append(LIST_JSON)
                .append(USER_ID + "=")
                .append(userId)
                .append("&"+COUNT+"=")
                .append(maxCount)
                .toString();
    }

    public String getLastTweetListUrl(){
        return new StringBuilder(ROOT_URL)
                .append(STATUSES)
                .append("/user_timeline.json?").toString();
    }

    public String getUserUrl(String userId) {
        this.userCount++;
        if(userCount%50==0) {
          //  System.out.println("Users : " + userCount + " / " + USER_MAX_CALLS);
        }
        return new StringBuilder(ROOT_URL_V2)
                .append(USERS)
                .append("?ids=")
                .append(userId)
                .append("&"+USER_FORMAT_DETAILED)
                .append("&"+TWEET_FORMAT_DETAILED)
                .append("&"+EXPANSIONS_RECENT_TWEET)
                .toString();
    }

    public String getUserUrlFromName(String username) {
        this.userCount++;
        if(userCount%50==0) {
           // System.out.println("Users : " + userCount + " / " + USER_MAX_CALLS);
        }
        return new StringBuilder(ROOT_URL_V2)
                .append(USERS)
                .append("?usernames=")
                .append(username)
                .append("&"+USER_FORMAT_DETAILED)
                .append("&"+TWEET_FORMAT_DETAILED)
                .append("&"+EXPANSIONS_RECENT_TWEET)
                .toString();
    }


    public String getUsersUrlbyNames(List<String> names) {
        this.userCount++;
        if(userCount%50==0) {
          //  System.out.println("Users : " + userCount + " / " + USER_MAX_CALLS);
        }
        StringBuilder result = new StringBuilder(ROOT_URL)
                .append(USERS)
                .append(LOOKUP_JSON)
                .append(SCREEN_NAME+"=");
        int i=0;
        while(i<names.size() && i<MAX_LOOKUP){
            String name = names.get(i);
            result.append(name);
            result.append(",");
            i++;
        }
        result.delete(result.length()-1,result.length());
        return result.toString();
    }

    public String getUsersUrlbyIds(List<String> ids) {
        this.userCount++;
        if(userCount%50==0) {
          //  System.out.println("Users : " + userCount + " / " + USER_MAX_CALLS);
        }
        StringBuilder result = new StringBuilder(ROOT_URL)
                .append(USERS)
                .append(LOOKUP_JSON)
                .append(USER_ID+"=");
        int i=0;
        while(i<ids.size() && i<MAX_LOOKUP){
            String id = ids.get(i);
            result.append(id);
            result.append(",");
            i++;
        }
        result.delete(result.length()-1,result.length());
        return result.toString();
    }

    public String getRateLimitUrl(){
        // https://api.twitter.com/1.1/application/rate_limit_status.json
        return new StringBuilder(ROOT_URL)
                .append("/application")
                .append("/rate_limit_status.json")
                .toString();
    }

    @Deprecated
    public String getTweetInfoUrl(String tweetId) {
        this.tweetInfoCount++;
        if(this.tweetInfoCount%10==0){
            System.out.println("*** tweetInfoCount : " + tweetInfoCount + " / " + TWEET_INFO_MAX_CALLS + " ***");
        }
        return new StringBuilder(ROOT_URL)
                .append(STATUSES)
                .append(SHOW_JSON)
                .append(ID+"=")
                .append(tweetId)
                .toString();
    }

    public String getUserTweetsUrl(String userId, int count){
        this.tweetInfoCount++;
        if(this.tweetInfoCount%10==0){
            System.out.println("*** tweetInfoCount : " + tweetInfoCount + " / " + TWEET_INFO_MAX_CALLS + " ***");
        }
        return new StringBuilder(ROOT_URL)
                .append(STATUSES)
                .append(USER_TIMELINE)
                .append(USER_ID+"=")
                .append(userId)
                .append("&"+COUNT+"=")
                .append(count)
                .append("&"+TRIM_USER)
                .append("&"+EXCLUDE_RTS)
                .toString();
    }

    // https://api.twitter.com/labs/1/tweets?ids=1067094924124872705
    public String getUserTweetsUrlV2(String userId, int count){
        this.tweetInfoCount++;
        if(this.tweetInfoCount%10==0){
            System.out.println("*** tweetInfoCount : " + tweetInfoCount + " / " + TWEET_INFO_MAX_CALLS + " ***");
        }
        return new StringBuilder(ROOT_URL)
                .append(STATUSES)
                .append(USER_TIMELINE)
                .append(USER_ID+"=")
                .append(userId)
                .append("&"+COUNT+"=")
                .append(count)
                .append("&"+TRIM_USER)
                .append("&"+EXCLUDE_RTS)
                .toString();
    }

    public String getSearchTweetsUrl() {
        return new StringBuilder(ROOT_URL)
                .append(TWEETS)
                .append(SEARCH)
                .append(THIRTY_DAYS)
                .append(DEV_ENV_NAME)
                .append(".json")
                .toString();
    }


    public String getLiveEventUrl() {
        return new StringBuilder(ROOT_URL)
                .append(ACCOUNT_ACTIVITY)
                .append(DEV_ENV_NAME)
                .append(WEBHOOKS)
                .append(".json")
                .toString();
    }


    public boolean canCallGetFollowers(){
        if(this.followersCount<FOLLOWER_MAX_CALLS){
            return true;
        }
        return false;
    }

    public boolean canCallUserInfo(){
        if(this.userCount<USER_MAX_CALLS){
            return true;
        }
        return false;
    }

    public void displayRateLimits(){
        StringBuilder s = new StringBuilder();
        s.append("retweeters : ")
                .append(retweeterCount)
                .append(" | ")
                .append("followers : ")
                .append(followersCount)
                .append(" | ")
                .append("following : ")
                .append(followingCount)
                .append(" | ")
                .append("friendship : ")
                .append(friendshipCount)
                .append(" | ")
                .append("follow : ")
                .append(followCount)
                .append(" | ")
                .append("user : ")
                .append(userCount);
        System.out.println("current rates : " + s);
    }


    public String getLikeUrl(String tweetId) {
        // curl --request POST
        //--url 'https://api.twitter.com/1.1/favorites/create.json?id=TWEET_ID_TO_FAVORITE'
        return new StringBuilder(ROOT_URL)
                .append(FAVORITES)
                .append(CREATE_JSON)
                .append(ID+"=")
                .append(tweetId)
                .toString();
    }
}
