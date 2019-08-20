package com.socialMediaRaiser.twitter.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialMediaRaiser.twitter.Tweet;
import com.socialMediaRaiser.twitter.User;
import com.socialMediaRaiser.twitter.helpers.dto.getUser.IncludesDTO;
import com.socialMediaRaiser.twitter.helpers.dto.getUser.TweetDTO;
import com.socialMediaRaiser.twitter.helpers.dto.getUser.UserDTO;
import com.socialMediaRaiser.twitter.helpers.dto.getUser.UserObjectResponseDTO;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// @todo remove JSON from project, replace it by JsonNode

public class JsonHelper {

    @Deprecated
    private static final String STATUSES_COUNT = "statuses_count";
    private static final String TWEET_COUNT = "tweet_count";
    private static final String CREATED_AT = "created_at";
    private final String SCREEN_NAME = "screen_name";
    private final String USER = "user";
    private final String FOLLOWER_COUNT = "followers_count";
    @Deprecated
    private final String FRIENDS_COUNT = "friends_count";
    private final String FOLLOWING_COUNT = "following_count";
    private final String FAVOURITES_COUNT = "favourites_count";
    private final String FAVORITE_COUNT = "favorite_count";
    private final String RETWEET_COUNT = "retweet_count";
    private final String DESCRIPTION = "description";
    private final String TEXT = "text";
    private final String STATUS = "status";
    private final String LOCATION = "location";
    private final String ID = "id";
    private final String LANG = "lang";
    private final String NEXT_CURSOR = "next_cursor";
    public static final String FOLLOWING = "following";

    @Deprecated
    public List<String> jsonLongArrayToList(Object jsonObject){
        JSONArray jArray = (JSONArray)jsonObject;
        ArrayList<String> listdata = new ArrayList<>();
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                if(jArray.get(i) instanceof Long){
                    listdata.add(String.valueOf(jArray.getLong(i)));
                } else if(jArray.get(i) instanceof Integer){
                    listdata.add(String.valueOf(jArray.getInt(i)));
                } else if (((JSONObject)jArray.get(i)).get(ID) instanceof Long){
                    listdata.add((String)((JSONObject)jArray.get(i)).get(ID));
                } else if (((JSONObject)jArray.get(i)).get(ID) instanceof Integer){
                    listdata.add((String)((JSONObject) jArray.get(i)).get(ID));
                }
            }
        }
        return listdata;
    }

    @Deprecated
    public List<String> jsonStringArrayToList(Object jsonObject){
        JSONArray jArray = (JSONArray)jsonObject;
        ArrayList<String> listdata = new ArrayList<>();
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                listdata.add(jArray.getJSONObject(i).get(SCREEN_NAME).toString());
            }
        }
        return listdata;
    }

    @Deprecated
    public List<User> jsonUserArrayToList(Object jsonObject){
        JSONArray jArray = (JSONArray)jsonObject;
        ArrayList<User> listdata = new ArrayList<>();
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                listdata.add(this.jsonResponseToUser(jArray.getJSONObject(i)));
            }
        }
        return listdata;
    }

    @Deprecated
    public User jsonResponseToUser(JSONObject jsonObject){
        if(jsonObject!=null){
            String id = jsonObject.get(ID).toString();
            String screenName = jsonObject.get(SCREEN_NAME).toString();
            int followersCount = (int)jsonObject.get(FOLLOWER_COUNT);
            int friendsCount = (int)jsonObject.get(FRIENDS_COUNT);
            int statuses_count = (int)jsonObject.get(STATUSES_COUNT);
            String created_at = jsonObject.get(CREATED_AT).toString();
            String description = jsonObject.get(DESCRIPTION).toString();
            int favourites_count = (int)jsonObject.get(FAVOURITES_COUNT);
            String location = "";
            if(jsonObject.has(LOCATION)){
                location = jsonObject.get(LOCATION).toString();
            }
            String lastUpdate = null;
            if(jsonObject.has(STATUS)){
                lastUpdate = ((JSONObject)jsonObject.get(STATUS)).get(CREATED_AT).toString();
            }
            return User.builder()
                    .id(id)
                    .userName(screenName)
                    .followerCout(followersCount)
                    .followingCount(friendsCount)
                    .statusesCount(statuses_count)
                    .dateOfCreation(getTwitterDate(created_at))
                    .description(description)
                    .favouritesCount(favourites_count)
                    .dateOfFollow(null)
                    .lastUpdate(getTwitterDate(lastUpdate))
                    .location(location)
                    .build();
        } else{
            return null;
        }
    }

    public User jsonResponseToUserV2(String response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        UserObjectResponseDTO obj = objectMapper.readValue(response, UserObjectResponseDTO.class);
        UserDTO data = obj.getData().get(0);
        IncludesDTO includes = obj.getIncludes();
        List<TweetDTO> mostRecentTweet = null;
        String lang = null;
        Date lastUpdate = null;
        if(!data.isProtectedAccount() && includes!=null){
            mostRecentTweet = includes.getTweets();
            lang = includes.getTweets().get(0).getLang();
            lastUpdate = getTwitterDateV2(includes.getTweets().get(0).getCreated_at());
        }
        return User.builder()
                .id(data.getId())
                .userName(data.getUsername())
                .followerCout(data.getStats().getFollowers_count())
                .followingCount(data.getStats().getFollowing_count())
                .statusesCount(data.getStats().getTweet_count())
                .dateOfCreation(getTwitterDateV2(data.getCreated_at()))
                .description(data.getDescription())
                .dateOfFollow(null)
                .location(data.getLocation())
                .mostRecentTweet(mostRecentTweet)
                .lang(lang)
                .lastUpdate(lastUpdate)
                .protectedAccount(data.isProtectedAccount())
                .build();
    }

    @Deprecated
    public Long getLongFromCursorObject(JSONObject response){
        if(response==null){
            System.err.println("result null");
            return null;
        }
        if(response.get(NEXT_CURSOR) instanceof Long){
            return (Long)response.get(NEXT_CURSOR);
        } else if(response.get(NEXT_CURSOR) instanceof Integer) {
            return Long.valueOf((Integer) response.get(NEXT_CURSOR));
        }  else{
            System.err.println("format problem");
            return null;
        }
    }

    public static Date getTwitterDate(String date)
    {
        if(date==null){
            return null;
        }

        try {
            final String TWITTER = "EEE MMM dd HH:mm:ss Z yyyy";
            SimpleDateFormat sf = new SimpleDateFormat(TWITTER, Locale.ENGLISH);
            sf.setLenient(true);
            return sf.parse(date);
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Date getTwitterDateV2(String date)
    {
        if(date==null){
            return null;
        }

        try {
            return Date.from(Instant.parse(date));
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Deprecated
    public List<Tweet> jsonResponseToTweetList(JSONArray jsonArray) {
        List<Tweet> tweets = new ArrayList<>();
        if(jsonArray!=null){
            for(Object o : jsonArray){
                JSONObject jsonObject = (JSONObject)o;
                Long id = Long.valueOf(jsonObject.get(ID).toString());
                int retweetsCount = (int)jsonObject.get(RETWEET_COUNT);
                int favourites_count = (int)jsonObject.get(FAVORITE_COUNT);
                String text = jsonObject.get(TEXT).toString();
                String lang = jsonObject.get(LANG).toString();
                Date createdAtDate = getTwitterDate(jsonObject.get(CREATED_AT).toString());
                User user = null;
                try{
                    user = jsonResponseToUser((JSONObject)jsonObject.get(USER));
                    user.setLastUpdate(createdAtDate);
                } catch (Exception e){
                 //   System.err.println(e);
                }
                Tweet tweet = Tweet.builder()
                        .id(id)
                        .retweet_count(retweetsCount)
                        .favorite_count(favourites_count)
                        .text(text)
                        .lang(lang)
                        .created_at(createdAtDate)
                        .user(user)
                        .build();
                tweets.add(tweet);
            }
        }
        return tweets;
    }

}
