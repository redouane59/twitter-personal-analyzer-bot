package twitter;

import lombok.Data;
import org.json.JSONObject;
import twitter.helpers.JsonHelper;
import twitter.helpers.RequestHelper;
import twitter.helpers.RequestMethod;
import twitter.helpers.URLHelper;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

@Data
public class Application implements IApplication {

    private HttpClient client = HttpClient.newHttpClient();
    private URLHelper urlHelper = new URLHelper();
    private RequestHelper requestHelper = new RequestHelper();
    private JsonHelper jsonHelper = new JsonHelper();
    private final String IDS = "ids";
    private final String USERS = "users";

    @Override
    public List<Long> getFollowersList(Long userId) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FOLLOWERS, userId);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        return this.getJsonHelper().jsonLongArrayToList(response.get(IDS));
    }

    @Override
    public List<String> getFollowersList(String userName) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FOLLOWERS, userName);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        return this.getJsonHelper().jsonStringArrayToList(response.get(USERS));
    }

    @Override
    public List<User> getFollowersUserList(String userName) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FOLLOWERS, userName);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        if(response!=null){
            return this.getJsonHelper().jsonUserArrayToList(response.get(USERS));
        } else{
            return new ArrayList<>();
        }
    }

    @Override
    public List<Long> getFollowingsList(Long userId) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FOLLOWING, userId);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        return this.getJsonHelper().jsonLongArrayToList(response.get(IDS));
    }

    @Override
    public List<String> getFollowingsList(String name) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FOLLOWING, name);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        return this.getJsonHelper().jsonStringArrayToList(response.get(USERS));
    }

    @Override
    public List<User> getFollowingsUserList(String userName) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FOLLOWING, userName);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        if(response!=null){
            return this.getJsonHelper().jsonUserArrayToList(response.get(USERS));
        } else{
            return new ArrayList<>();
        }
    }

    @Override
    public Boolean areFriends(Long userId1, Long userId2) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FRIENDSHIP, userId1, userId2);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        JSONObject relationship = (JSONObject)response.get("relationship");
        JSONObject sourceResult = (JSONObject)relationship.get("source");
        Boolean followedBy = (Boolean)sourceResult.get("followed_by");
        Boolean following = (Boolean)sourceResult.get("following");
        return (followedBy & following);
    }

    @Override
    public Boolean areFriends(String userName1, String userName2) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FRIENDSHIP, userName1, userName2);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        JSONObject relationship = (JSONObject)response.get("relationship");
        JSONObject sourceResult = (JSONObject)relationship.get("source");
        Boolean followedBy = (Boolean)sourceResult.get("followed_by");
        Boolean following = (Boolean)sourceResult.get("following");
        return (followedBy & following);
    }

    public Boolean userFollowsOther(Long userId1, Long userId2) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FRIENDSHIP, userId1, userId2);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        JSONObject relationship = (JSONObject)response.get("relationship");
        JSONObject sourceResult = (JSONObject)relationship.get("source");
        return (Boolean)sourceResult.get("following");
    }

    public Boolean userIsFollowed (Long userId1, Long userId2) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FRIENDSHIP, userId1, userId2);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        JSONObject relationship = (JSONObject)response.get("relationship");
        JSONObject sourceResult = (JSONObject)relationship.get("source");
        return (Boolean)sourceResult.get("followed_by");
    }

    public Boolean userIsFollowed (String userName1, String userName2) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FRIENDSHIP, userName1, userName2);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        JSONObject relationship = (JSONObject)response.get("relationship");
        JSONObject sourceResult = (JSONObject)relationship.get("source");
        return (Boolean)sourceResult.get("followed_by");
    }

    @Override
    public int getNbFollowers(Long userId) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FOLLOWERS, userId);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        return this.getJsonHelper().jsonLongArrayToList(response.get(IDS)).size();
    }

    @Override
    public int getNbFollowings(Long userId) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FOLLOWING, userId);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        return this.getJsonHelper().jsonLongArrayToList(response.get(IDS)).size();
    }

    @Override
    public int getNbFollowers(String userName) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FOLLOWERS, userName);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        return this.getJsonHelper().jsonStringArrayToList(response.get(USERS)).size();    }

    @Override
    public int getNbFollowings(String userName) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_FOLLOWING, userName);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        return this.getJsonHelper().jsonStringArrayToList(response.get(USERS)).size();
    }

    @Override
    public List<Long> getRetweetersId(Long tweetId) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_RETWEETERS, tweetId);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        return this.getJsonHelper().jsonLongArrayToList(response.get(IDS));
    }

    @Override
    public List<String> getRetweetersNames(Long tweetId) throws IllegalAccessException {
        String url = this.urlHelper.getUrl(Action.GET_RETWEETERS, tweetId);
        JSONObject response = this.getRequestHelper().executeRequest(url, RequestMethod.GET);
        return this.getJsonHelper().jsonStringArrayToList(response.get(IDS));
    }

    private List<String> getPotentialFollowersFromUserFollowers(List<User> users, String userName) throws IllegalAccessException{
        List<String> results = new ArrayList<>();
        for(User user : users){
            if(this.getUrlHelper().canCall()) {
                results.addAll(this.getPotentialFollowersFromUserFollowers(user.getScreen_name(), userName));
            }
        }
        return results;
    }


    private List<String> getPotentialFollowersFromUserFollowers(String otherUserName, String userName) throws IllegalAccessException {
        List<String> potentialFollowers = new ArrayList<>();
        List<User> followers = this.getFollowersUserList(otherUserName);

        for (User follower : followers) {
            boolean canMakeCall = this.getUrlHelper().canCall();
            if(canMakeCall){
                boolean alreadyFollowed = true;
                try{
                    alreadyFollowed = this.userIsFollowed(userName, follower.getScreen_name());
                }
                catch(Exception e){}
                if (!alreadyFollowed && follower.shouldBeFollowed()) {
                    potentialFollowers.add(follower.getScreen_name());
                }
            }
        }
        return potentialFollowers;
    }


    public List<String> followAllPotentialFollowersFromCommon(String userName){
        String tweetName = "RedTheOne";
        List<String> followed = new ArrayList<>();
        List<String> potentialFollowers = new ArrayList<>();
        try {
            List<User> followers = this.getFollowersUserList(tweetName);
            List<User> followings = this.getFollowingsUserList(tweetName);
            List<User> allUsers = new ArrayList<>();
            allUsers.addAll(followers);
            allUsers.addAll(followings);
            potentialFollowers = this.getPotentialFollowersFromUserFollowers(allUsers, tweetName);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        for(String name : potentialFollowers){
            boolean result = this.follow(name);
            if(result){
                followed.add(name);
            }
        }

        return followed;
    }

    public List<String> getUsersNotFollowingBack(String userName) throws IllegalAccessException {
        List<String> notFollowingsBackUsers = new ArrayList<>();
        List<User> followings = this.getFollowingsUserList(userName);
        boolean canCall = true;
        for(User following : followings){
            canCall = this.getUrlHelper().canCall();
            if(canCall && !this.areFriends(userName, following.getScreen_name()) && following.shouldBeFollowed()){
                notFollowingsBackUsers.add(following.getScreen_name());
            };
        }
        return notFollowingsBackUsers;
    }

    public List<String> unfollowAllNotFollowingBackUsers(String userName) throws IllegalAccessException {
        List<String> notFollowingBack = this.getUsersNotFollowingBack(userName);
        List<String> unfollowed = new ArrayList<>();

        for(String name : notFollowingBack){
            boolean result = this.unfollow(name);
            if(result){
                unfollowed.add(name);
            }
        }
        return unfollowed;
    }



        @Override
    public boolean follow(String userName){
        String url = this.urlHelper.getUrl(Action.FOLLOW, userName);

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

    @Override
    public boolean unfollow(String userName){
        return true;
    }

}
