package com.socialMediaRaiser.twitter.helpers;

import lombok.Data;

import java.util.List;

@Data
public class URLHelper {

    private final String ROOT_URL = "https://api.twitter.com/1.1";
    private final String IDS_JSON = "/ids.json?";
    private final String SCREEN_NAME = "screen_name";
    private final String ID = "id";
    private final String LIST_JSON = "/list.json?";
    private final String SHOW_JSON = "/show.json?";
    private final String CREATE_JSON = "/create.json?";
    private final String DESTROY_JSON = "/destroy.json?";
    private final String RETWEETERS = "/retweeters";
    private final String FOLLOWERS = "/followers";
    private final String FRIENDS = "/friends";
    private final String STATUSES = "/statuses";
    private final String FRIENDSHIPS = "/friendships";
    private final String USERS = "/users";
    private final String USER_ID = "user_id";
    private final String LOOKUP_JSON = "/lookup.json?";
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
    public static int RETWEET_MAX_COUNT = 90;
    private static final int TWEET_INFO_MAX_CALLS = 900;
    private final int MAX_LOOKUP = 100;

    public String getFollowUrl(String relatedName) {
        this.followCount++;
        // System.out.println("follows : " + followCount);
        return new StringBuilder(ROOT_URL)
                .append(FRIENDSHIPS)
                .append(CREATE_JSON)
                .append(SCREEN_NAME+"=")
                .append(relatedName)
                //    .append("&follow=true")
                .toString();
    }

    public String getFollowUrl(Long userId) {
        this.followCount++;
        return new StringBuilder(ROOT_URL)
                .append(FRIENDSHIPS)
                .append(CREATE_JSON)
                .append(USER_ID+"=")
                .append(userId)
                .toString();
    }

    public String getUnfollowUrl(String userName) {
        this.unfollowCount++;
        //System.out.println("unfollows : " + unfollowCount + " / " + UNFOLLOW_MAX_CALLS);
        return new StringBuilder(ROOT_URL)
                .append(FRIENDSHIPS)
                .append(DESTROY_JSON)
                .append(SCREEN_NAME+"=")
                .append(userName)
                .toString();
    }

    public String getUnfollowUrl(Long userId) {
        this.unfollowCount++;
        // System.out.println("unfollows : " + unfollowCount + " / " + UNFOLLOW_MAX_CALLS);
        return new StringBuilder(ROOT_URL)
                .append(FRIENDSHIPS)
                .append(DESTROY_JSON)
                .append(USER_ID+"=")
                .append(userId)
                .toString();
    }

    public String getFriendshipUrl(Long sourceId, Long targetId) {
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

    public String getFriendshipUrl(String sourceScreenName, String targetScreenName) {
        this.friendshipCount++;
        if(friendshipCount%50==0){
            System.out.println("friendship : " + friendshipCount + " / " + FRIENDSHIP_MAX_CALLS);
        }
        return new StringBuilder(ROOT_URL)
                .append(FRIENDSHIPS)
                .append(SHOW_JSON)
                .append("source_"+ SCREEN_NAME +"=")
                .append(sourceScreenName)
                .append("&target_"+ SCREEN_NAME +"=")
                .append(targetScreenName)
                .toString();
    }

    public String getRetweetersUrl(Long tweetId){
        this.retweeterCount++;
        System.out.println("Retweeters : " + retweeterCount + " / " + RETWEETER_MAX_CALLS);
        return new StringBuilder(ROOT_URL)
                .append(STATUSES)
                .append(RETWEETERS)
                .append(IDS_JSON)
                .append(ID +"=")
                .append(tweetId)
                .append("&count=")
                .append(RETWEET_MAX_COUNT)
                .toString();
    }

    public String getRetweetersUrl(Long tweetId, int cursor, int count){
        this.retweeterCount++;
        System.out.println("Retweeters : " + retweeterCount + " / " + RETWEETER_MAX_CALLS);
        return new StringBuilder(ROOT_URL)
                .append(STATUSES)
                .append(RETWEETERS)
                .append(IDS_JSON)
                .append(ID +"=")
                .append(tweetId)
                .append("&count=")
                .append(count)
                .append("&cursor=")
                .append(cursor)
                .toString();
    }

    public String getRetweetersUrl(String screenName){
        this.retweeterCount++;
        System.out.println("Retweeters : " + retweeterCount + " / " + RETWEETER_MAX_CALLS);
        return new StringBuilder(ROOT_URL)
                .append(STATUSES)
                .append(RETWEETERS)
                .append(LIST_JSON)
                .append(ID + "=")
                .append(screenName)
                .append("&count=")
                .append(maxCount)
                .toString();
    }

    public String getFollowerIdsUrl(Long userId){
        this.followersCount++;
        if(followersCount%10==0) {
            System.out.println("Followers : " + followersCount + " / " + FOLLOWER_MAX_CALLS);
        }
        return new StringBuilder(ROOT_URL)
                .append(FOLLOWERS)
                .append(IDS_JSON)
                .append(USER_ID + "=")
                .append(userId)
                .toString();
    }

    public String getFollowerIdsUrl(String screenName){
        this.followersCount++;
        if(followersCount%10==0) {
            System.out.println("Followers : " + followersCount + " / " + FOLLOWER_MAX_CALLS);
        }
        return new StringBuilder(ROOT_URL)
                .append(FOLLOWERS)
                .append(IDS_JSON)
                .append(SCREEN_NAME+"=")
                .append(screenName)
                .toString();
    }

    public String getFollowerUsersUrl(String screenName){
        this.followersCount++;
        if(followersCount%10==0) {
            System.out.println("Followers : " + followersCount + " / " + FOLLOWER_MAX_CALLS);
        }
        return new StringBuilder(ROOT_URL)
                .append(FOLLOWERS)
                .append(LIST_JSON)
                .append(SCREEN_NAME+"=")
                .append(screenName)
                .append("&count=")
                .append(maxCount)
                .toString();
    }

    public String getFollowerUsersUrl(Long userId){
        this.followersCount++;
        if(followersCount%10==0) {
            System.out.println("Followers : " + followersCount + " / " + FOLLOWER_MAX_CALLS);
        }
        return new StringBuilder(ROOT_URL)
                .append(FOLLOWERS)
                .append(LIST_JSON)
                .append(USER_ID + "=")
                .append(userId)
                .append("&count=")
                .append(maxCount)
                .toString();
    }

    public String getFollowingIdsUrl(Long userId){
        this.followingCount++;
        System.out.println("Followings : " + followingCount + " / " + FOLLOWING_MAX_CALLS);
        return new StringBuilder(ROOT_URL)
                .append(FRIENDS)
                .append(IDS_JSON)
                .append(USER_ID + "=")
                .append(userId).toString();
    }

    public String getFollowingIdsUrl(String screenName){
        this.followingCount++;
        System.out.println("Followings : " + followingCount + " / " + FOLLOWING_MAX_CALLS);
        return new StringBuilder(ROOT_URL)
                .append(FRIENDS)
                .append(IDS_JSON)
                .append(SCREEN_NAME+"=")
                .append(screenName)
                .toString();
    }

    public String getFollowingUsersUrl(String screenName){
        this.followingCount++;
        System.out.println("Followings : " + followingCount + " / " + FOLLOWING_MAX_CALLS);
        return new StringBuilder(ROOT_URL)
                .append(FRIENDS)
                .append(LIST_JSON)
                .append(SCREEN_NAME+"=")
                .append(screenName)
                .append("&count=")
                .append(maxCount)
                .toString();
    }

    public String getFollowingUsersUrl(Long userId){
        this.followingCount++;
        System.out.println("Followings : " + followingCount + " / " + FOLLOWING_MAX_CALLS);
        return new StringBuilder(ROOT_URL)
                .append(FRIENDS)
                .append(LIST_JSON)
                .append(USER_ID + "=")
                .append(userId)
                .append("&count=")
                .append(maxCount)
                .toString();
    }

    public String getLastTweetListUrl(){
        return new StringBuilder(ROOT_URL)
                .append(STATUSES)
                .append("/user_timeline.json?").toString();
    }

    public String getTweetInfoUrl(Long tweetId) {
        this.tweetInfoCount++;
        System.out.println("tweetInfoCount : " + tweetInfoCount + " / " + TWEET_INFO_MAX_CALLS);
        return new StringBuilder(ROOT_URL)
                .append(STATUSES)
                .append(SHOW_JSON)
                .append(ID+"=")
                .append(tweetId)
                .toString();
    }

    public String getUserUrl(Long userId) {
        this.userCount++;
        if(userCount%50==0) {
            System.out.println("Users : " + userCount + " / " + USER_MAX_CALLS);
        }
        return new StringBuilder(ROOT_URL)
                .append(USERS)
                .append(SHOW_JSON)
                .append(USER_ID+"=")
                .append(userId)
                .toString();
    }

    public String getUserUrl(String name) {
        this.userCount++;
        if(userCount%50==0) {
            System.out.println("Users : " + userCount + " / " + USER_MAX_CALLS);
        }
        return new StringBuilder(ROOT_URL)
                .append(USERS)
                .append(SHOW_JSON)
                .append(SCREEN_NAME+"=")
                .append(name)
                .toString();
    }

    public String getUsersUrlbyNames(List<String> names) {
        this.userCount++;
        if(userCount%50==0) {
            System.out.println("Users : " + userCount + " / " + USER_MAX_CALLS);
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

    public String getUsersUrlbyIds(List<Long> ids) {
        this.userCount++;
        if(userCount%50==0) {
            System.out.println("Users : " + userCount + " / " + USER_MAX_CALLS);
        }
        StringBuilder result = new StringBuilder(ROOT_URL)
                .append(USERS)
                .append(LOOKUP_JSON)
                .append(USER_ID+"=");
        int i=0;
        while(i<ids.size() && i<MAX_LOOKUP){
            Long id = ids.get(i);
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
}
