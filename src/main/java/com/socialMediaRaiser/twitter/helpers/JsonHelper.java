package com.socialMediaRaiser.twitter.helpers;

import com.socialMediaRaiser.twitter.Tweet;
import com.socialMediaRaiser.twitter.User;
import com.socialMediaRaiser.twitter.User.UserBuilder;
import lombok.Builder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JsonHelper {

    private static final String STATUSES_COUNT = "statuses_count";
    private static final String CREATED_AT = "created_at";
    private final String SCREEN_NAME = "screen_name";
    private final String FOLLOWER_COUNT = "followers_count";
    private final String FRIENDS_COUNT = "friends_count";
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

    public List<Long> jsonLongArrayToList(Object jsonObject){
        JSONArray jArray = (JSONArray)jsonObject;
        ArrayList<Long> listdata = new ArrayList<>();
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                if(jArray.get(i) instanceof Long){
                    listdata.add(jArray.getLong(i));
                } else if(jArray.get(i) instanceof Integer){
                    listdata.add(Long.valueOf(jArray.getInt(i)));
                } else if (((JSONObject)jArray.get(i)).get(ID) instanceof Long){
                    listdata.add((Long)((JSONObject)jArray.get(i)).get(ID));
                } else if (((JSONObject)jArray.get(i)).get(ID) instanceof Integer){
                    listdata.add((long) (Integer) ((JSONObject) jArray.get(i)).get(ID));
                }
            }
        }
        return listdata;
    }

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

    public User jsonResponseToUser(JSONObject jsonObject){
        if(jsonObject!=null){
            Long id = Long.valueOf(jsonObject.get(ID).toString());
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
        /*    String lang = jsonObject.get(LANG).toString(); // soon deprecated deprecated
            if(lang==null && jsonObject.has(LANG)){
                lang = ((JSONObject)jsonObject.get(STATUS)).get(LANG).toString();
            }*/
            return User.builder()
                    .id(id)
                    .userName(screenName)
                    .followerCout(followersCount)
                    .followingCount(friendsCount)
                 //   .lang(lang)
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

    public Tweet jsonResponseToTweet(JSONObject jsonObject) {

        if(jsonObject!=null){
            Long id = Long.valueOf(jsonObject.get(ID).toString());
            int retweetsCount = (int)jsonObject.get(RETWEET_COUNT);
            int favourites_count = (int)jsonObject.get(FAVORITE_COUNT);
            String text = jsonObject.get(TEXT).toString();
            String lang = jsonObject.get(LANG).toString();
            String created_at = jsonObject.get(CREATED_AT).toString();
            Tweet tweet = Tweet.builder()
                    .id(id)
                    .retweet_count(retweetsCount)
                    .favorite_count(favourites_count)
                    .text(text)
                    .lang(lang)
                    .created_at(getTwitterDate(created_at))
                    .build();
            return tweet;
        }
        return null;
    }
}
