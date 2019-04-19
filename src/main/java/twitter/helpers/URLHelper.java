package twitter.helpers;

import twitter.Action;

public class URLHelper {
    private final String rootUrl = "https://api.twitter.com/1.1";
    private final String idJsonPath = "/ids.json?";
    private final String retweetersPath = "/retweeters";
    private final String followersPath = "/followers";
    private final String followingsPath = "/friends";
    private final String statusesPath = "/statuses";

    public String getUrl(Action action, Long relatedId){
        switch (action){
            case RETWEETERS:
                return this.getRetweetersListUrl(relatedId);
            case FOLLOWERS:
                return this.getFollowersListUrl(relatedId);
            case FOLLOWING:
                return this.getFollowingsListUrl(relatedId);
            default:
                return null;
        }
    }

    public String getRetweetersListUrl(Long tweetId){
        return new StringBuilder(rootUrl)
                .append(statusesPath)
                .append(retweetersPath)
                .append(idJsonPath)
                .append("id=")
                .append(tweetId)
                .toString();
    }

    public String getFollowersListUrl(Long userId){
        return new StringBuilder(rootUrl)
                .append(followersPath)
                .append(idJsonPath)
                .append("user_id=")
                .append(userId)
                .toString();
    }

    public String getFollowingsListUrl(Long userId){
        return new StringBuilder(rootUrl)
                .append(followingsPath)
                .append(idJsonPath)
                .append("user_id=")
                .append(userId).toString();
    }

    public String getLastTweetListUrl(){
        return new StringBuilder(rootUrl)
                .append(statusesPath)
                .append("/user_timeline.json?").toString();
    }
}
