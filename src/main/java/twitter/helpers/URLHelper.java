package twitter.helpers;

import twitter.Action;

public class URLHelper {
    private final String ROOT_URL = "https://api.twitter.com/1.1";
    private final String IDS_JSON = "/ids.json?";
    private final String SCREEN_NAME = "screen_name";
    private final String ID = "id";
    private final String LIST_JSON = "/list.json?";
    private final String SHOW_JSON = "/show.json?";
    private final String RETWEETERS = "/retweeters";
    private final String FOLLOWERS = "/followers";
    private final String FRIENDS = "/friends";
    private final String STATUSES = "/statuses";
    private final String FRIENDSHIPS = "/friendships";

    public String getUrl(Action action, Long relatedId){
        return this.getUrl(action, relatedId, null);
    }

    public String getUrl(Action action, String relatedName){
        return this.getUrl(action, relatedName, null);
    }

    public String getUrl(Action action, Long relatedId, Long relatedId2){
        switch (action){
            case RETWEETERS:
                return this.getRetweetersListUrl(relatedId);
            case FOLLOWERS:
                return this.getFollowersListUrl(relatedId);
            case FOLLOWING:
                return this.getFollowingsListUrl(relatedId);
            case FRIENDSHIP:
                return this.getFriendshipUrl(relatedId, relatedId2);
            default:
                return null;
        }
    }

    public String getUrl(Action action, String relatedName1, String relatedName2){
        switch (action){
            case RETWEETERS:
                return this.getRetweetersListUrl(relatedName1);
            case FOLLOWERS:
                return this.getFollowersListUrl(relatedName1);
            case FOLLOWING:
                return this.getFollowingsListUrl(relatedName1);
            case FRIENDSHIP:
                return this.getFriendshipUrl(relatedName1, relatedName2);
            default:
                return null;
        }
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
                .toString();
    }

    public String getFollowersListUrl(Long userId){
        return new StringBuilder(ROOT_URL)
                .append(FOLLOWERS)
                .append(IDS_JSON)
                .append("user_id=")
                .append(userId)
                .toString();
    }

    public String getFollowersListUrl(String screenName){
        return new StringBuilder(ROOT_URL)
                .append(FOLLOWERS)
                .append(LIST_JSON)
                .append(SCREEN_NAME+"=")
                .append(screenName)
                .toString();
    }

    public String getFollowingsListUrl(Long userId){
        return new StringBuilder(ROOT_URL)
                .append(FRIENDS)
                .append(IDS_JSON)
                .append("user_id=")
                .append(userId).toString();
    }

    public String getFollowingsListUrl(String screenName){
        return new StringBuilder(ROOT_URL)
                .append(FRIENDS)
                .append(LIST_JSON)
                .append(SCREEN_NAME+"=")
                .append(screenName).toString();
    }

    public String getLastTweetListUrl(){
        return new StringBuilder(ROOT_URL)
                .append(STATUSES)
                .append("/user_timeline.json?").toString();
    }
}
