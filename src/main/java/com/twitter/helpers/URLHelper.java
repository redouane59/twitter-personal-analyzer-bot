package com.twitter.helpers;

public class URLHelper {
    private final String ROOT_URL = "https://api.twitter.com/1.1";
    private final String IDS_JSON = "/ids.json?";
    private final String SCREEN_NAME = "screen_name";
    private final String ID = "id";
    private final String LIST_JSON = "/list.json?";
    private final String SHOW_JSON = "/show.json?";
    private final String CREATE_JSON = "/create.json?";
    private final String RETWEETERS = "/retweeters";
    private final String FOLLOWERS = "/followers";
    private final String FRIENDS = "/friends";
    private final String STATUSES = "/statuses";
    private final String FRIENDSHIPS = "/friendships";
    private final String USERS = "/users";
    private final String USER_ID = "user_id";
    private final int maxCount = 200;
    private int retweeterCount = 0;
    private int followersCount = 0;
    private int followingCount = 0;
    private int friendshipCount = 0;
    private int followCount = 0;
    private int userCount = 0;
    private int tweetInfoCount = 0;
    public static int RETWEETER_MAX_CALLS = 15;
    public static int FOLLOWER_MAX_CALLS = 15;
    public static int FOLLOWING_MAX_CALLS = 15;
    public static int FRIENDSHIP_MAX_CALLS = 180;
    public static int FOLLOW_MAX_CALLS = 350;
    public static int USER_MAX_CALLS = 900;
    public static int RETWEET_MAX_COUNT = 100;
    private static final int TWEET_INFO_MAX_CALLS = 900;

    public String getFollowUrl(String relatedName) {
        this.followCount++;
        System.out.println("follows : " + followCount + " / " + FOLLOW_MAX_CALLS);
        return new StringBuilder(ROOT_URL)
                .append(FRIENDSHIPS)
                .append(CREATE_JSON)
                .append(SCREEN_NAME+"=")
                .append(relatedName)
                .append("&follow=true")
                .toString();
    }

    public String getFollowUrl(Long relatedId) {
        this.followCount++;
        System.out.println("follows : " + followCount + " / " + FOLLOW_MAX_CALLS);
        return new StringBuilder(ROOT_URL)
                .append(FRIENDSHIPS)
                .append(CREATE_JSON)
                .append(USER_ID+"=")
                .append(relatedId)
                .append("&follow=true")
                .toString();
    }

    public String getFriendshipUrl(Long sourceId, Long targetId) {
        this.friendshipCount++;
        System.out.println("friendship : " + friendshipCount + " / " + FRIENDSHIP_MAX_CALLS);
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
        System.out.println("friendship : " + friendshipCount + " / " + FRIENDSHIP_MAX_CALLS);
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
                .append("&count=100")
                .toString();
    }

    public String getRetweetersUrl(Long tweetId, int cursor){
        this.retweeterCount++;
        System.out.println("Retweeters : " + retweeterCount + " / " + RETWEETER_MAX_CALLS);
        return new StringBuilder(ROOT_URL)
                .append(STATUSES)
                .append(RETWEETERS)
                .append(IDS_JSON)
                .append(ID +"=")
                .append(tweetId)
                .append("&count=100")
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

    public String getFollowersUrl(Long userId){
        this.followersCount++;
        System.out.println("Followers : " + followersCount + " / " + FOLLOWER_MAX_CALLS);
        return new StringBuilder(ROOT_URL)
                .append(FOLLOWERS)
                .append(IDS_JSON)
                .append(USER_ID + "=")
                .append(userId)
                .toString();
    }

    public String getFollowersUrl(String screenName){
        this.followersCount++;
        System.out.println("Followers : " + followersCount + " / " + FOLLOWER_MAX_CALLS);
        return new StringBuilder(ROOT_URL)
                .append(FOLLOWERS)
                .append(LIST_JSON)
                .append(SCREEN_NAME+"=")
                .append(screenName)
                .append("&count=")
                .append(maxCount)
                .toString();
    }

    public String getFollowingsUrl(Long userId){
        this.followingCount++;
        System.out.println("Followings : " + followingCount + " / " + FOLLOWING_MAX_CALLS);
        return new StringBuilder(ROOT_URL)
                .append(FRIENDS)
                .append(IDS_JSON)
                .append(USER_ID + "=")
                .append(userId).toString();
    }

    public String getFollowingsUrl(String screenName){
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

    public String getLastTweetListUrl(){
        return new StringBuilder(ROOT_URL)
                .append(STATUSES)
                .append("/user_timeline.json?").toString();
    }

    public String getTweetInfoUrl(Long tweetId) {
        this.tweetInfoCount++;
        System.out.println("Users : " + tweetInfoCount + " / " + TWEET_INFO_MAX_CALLS);
        return new StringBuilder(ROOT_URL)
                .append(STATUSES)
                .append(SHOW_JSON)
                .append(ID+"=")
                .append(tweetId)
                .toString();
    }

    public String getUserUrl(Long relatedId) {
        this.userCount++;
        System.out.println("Users : " + userCount + " / " + USER_MAX_CALLS);
        return new StringBuilder(ROOT_URL)
                .append(USERS)
                .append(SHOW_JSON)
                .append(USER_ID+"=")
                .append(relatedId)
                .toString();
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

    public boolean canCall(){
        if(this.retweeterCount>=RETWEETER_MAX_CALLS
            || this.followCount >= FOLLOW_MAX_CALLS
            || this.followingCount >= FOLLOWING_MAX_CALLS
            || this.followersCount >= FOLLOWER_MAX_CALLS
            || this.friendshipCount >= FRIENDSHIP_MAX_CALLS
            || this.userCount >= USER_MAX_CALLS){
            return false;
        } else{
            return true;
        }
    }

    public boolean canCallFriendship(){
        if(this.friendshipCount < FRIENDSHIP_MAX_CALLS){
            return true;
        }
        System.out.println("max calls getFriendship reached");
        return false;
    }

    public boolean canCallGetFollowers(){
        if(this.followersCount < FOLLOWER_MAX_CALLS){
            return true;
        }
        System.out.println("max calls getFollowers reached");
        return false;
    }

    public boolean canCallGetFollowings(){
        if(this.followingCount < FOLLOWING_MAX_CALLS){
            return true;
        }
        System.out.println("max calls getFollowings reached");
        return false;
    }

    public boolean canCallGetUser(){
        if(this.userCount < USER_MAX_CALLS){
            return true;
        }
        System.out.println("max calls getUser reached");
        return false;
    }

    public void resetQuarterCounters() {
        this.retweeterCount = 0;
        this.followersCount = 0;
        this.followingCount = 0;
        this.friendshipCount = 0;
        this.userCount = 0;
    }


}
