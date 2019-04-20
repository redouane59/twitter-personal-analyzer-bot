package twitter.helpers;

import twitter.Action;

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
    private final String USER_ID = "user_id";
    private final int maxCount = 200;
    private int retweeterCount = 0;
    private int followersCount = 0;
    private int followingCount = 0;
    private int friendshipCount = 0;
    private int followCount = 0;
    public static int RETWEETER_MAX_CALLS = 15;
    public static int FOLLOWER_MAX_CALLS = 15;
    public static int FOLLOWING_MAX_CALLS = 15;
    public static int FRIENDSHIP_MAX_CALLS = 180;
    public static int FOLLOW_MAX_CALLS = 400;

    public String getUrl(Action action, Long relatedId){
        return this.getUrl(action, relatedId, null);
    }

    public String getUrl(Action action, String relatedName){
        return this.getUrl(action, relatedName, null);
    }

    public String getUrl(Action action, Long relatedId, Long relatedId2){
        String resultUrl = null;
        switch (action){
            case GET_RETWEETERS:
                this.retweeterCount++;
                resultUrl = this.getRetweetersListUrl(relatedId);
                break;
            case GET_FOLLOWERS:
                this.followersCount++;
                resultUrl = this.getFollowersListUrl(relatedId);
                break;
            case GET_FOLLOWING:
                this.followingCount++;
                resultUrl = this.getFollowingsListUrl(relatedId);
                break;
            case GET_FRIENDSHIP:
                this.friendshipCount++;
                resultUrl = this.getFriendshipUrl(relatedId, relatedId2);
                break;
            case FOLLOW:
                this.followCount++;
                resultUrl = this.getFollowUrl(relatedId);
                break;
            }

        this.displayRateLimits();
        return resultUrl;

    }

    public String getUrl(Action action, String relatedName1, String relatedName2){
        String resultUrl = null;
        switch (action){
            case GET_RETWEETERS:
                this.retweeterCount++;
                resultUrl =  this.getRetweetersListUrl(relatedName1);
                break;
            case GET_FOLLOWERS:
                this.followersCount++;
                resultUrl =  this.getFollowersListUrl(relatedName1);
                break;
            case GET_FOLLOWING:
                this.followingCount++;
                resultUrl =  this.getFollowingsListUrl(relatedName1);
                break;
            case GET_FRIENDSHIP:
                this.friendshipCount++;
                resultUrl =  this.getFriendshipUrl(relatedName1, relatedName2);
                break;
            case FOLLOW:
                this.followCount++;
                resultUrl =  this.getFollowUrl(relatedName1);
        }
        this.displayRateLimits();
        return resultUrl;
    }

    public String getFollowUrl(String relatedName) {
        return new StringBuilder(ROOT_URL)
                .append(FRIENDSHIPS)
                .append(CREATE_JSON)
                .append(SCREEN_NAME+"=")
                .append(relatedName)
                .append("&follow=true")
                .toString();
    }

    public String getFollowUrl(Long relatedId) {
        return new StringBuilder(ROOT_URL)
                .append(FRIENDSHIPS)
                .append(CREATE_JSON)
                .append(USER_ID+"=")
                .append(relatedId)
                .append("&follow=true")
                .toString();
    }

    public String getFriendshipUrl(Long sourceId, Long targetId) {
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
        return new StringBuilder(ROOT_URL)
                .append(FRIENDSHIPS)
                .append(SHOW_JSON)
                .append("source_"+ SCREEN_NAME +"=")
                .append(sourceScreenName)
                .append("&target_"+ SCREEN_NAME +"=")
                .append(targetScreenName)
                .toString();
    }

    public String getRetweetersListUrl(Long tweetId){
        return new StringBuilder(ROOT_URL)
                .append(STATUSES)
                .append(RETWEETERS)
                .append(IDS_JSON)
                .append(ID +"=")
                .append(tweetId)
                .toString();
    }

    public String getRetweetersListUrl(String screenName){
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

    public String getFollowersListUrl(Long userId){
        return new StringBuilder(ROOT_URL)
                .append(FOLLOWERS)
                .append(IDS_JSON)
                .append(USER_ID + "=")
                .append(userId)
                .toString();
    }

    public String getFollowersListUrl(String screenName){
        return new StringBuilder(ROOT_URL)
                .append(FOLLOWERS)
                .append(LIST_JSON)
                .append(SCREEN_NAME+"=")
                .append(screenName)
                .append("&count=")
                .append(maxCount)
                .toString();
    }

    public String getFollowingsListUrl(Long userId){
        return new StringBuilder(ROOT_URL)
                .append(FRIENDS)
                .append(IDS_JSON)
                .append(USER_ID + "=")
                .append(userId).toString();
    }

    public String getFollowingsListUrl(String screenName){
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
                .append(followCount);
        System.out.println("current rates : " + s);
    }

    public boolean canCall(){
        if(this.retweeterCount>=RETWEETER_MAX_CALLS
            || this.followCount >= FOLLOW_MAX_CALLS
            || this.followingCount >= FOLLOWING_MAX_CALLS
            || this.followersCount >= FOLLOWER_MAX_CALLS
            || this.friendshipCount >= FRIENDSHIP_MAX_CALLS){
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

    public void resetQuarterCounters() {
        this.retweeterCount = 0;
        this.followersCount = 0;
        this.followingCount = 0;
        this.friendshipCount = 0;
    }
}
