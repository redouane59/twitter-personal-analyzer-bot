package com.socialMediaRaiser.twitter.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.socialMediaRaiser.twitter.helpers.dto.getUser.*;
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
    private static final String TWEET_COUNT = "tweetCount";
    private static final String CREATED_AT = "dateOfCreation";
    private final String SCREEN_NAME = "screen_name";
    private final String USER = "user";
    private final String FOLLOWER_COUNT = "followersCount";
    @Deprecated
    private final String FRIENDS_COUNT = "friends_count";
    private final String FOLLOWING_COUNT = "followingCount";
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

    public UserDTO jsonResponseToUserV2(String response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode obj = objectMapper.readValue(response, JsonNode.class);
        UserDTO user = objectMapper.treeToValue(obj.get("data").get(0), UserDTO.class);
        TweetDTO lastTweet = objectMapper.treeToValue(obj.get("includes").get("tweets").get(0), TweetDTO.class);
        user.setMostRecentTweet(lastTweet);
        return user;
    }

    public List<TweetDTO> jsonResponseToTweetList(Object o){
        // [{"in_reply_to_status_id_str":"1162977652812275712","in_reply_to_status_id":1162977652812275712,"coordinates":null,"created_at":"Sun Aug 18 06:41:49 +0000 2019","truncated":false,"in_reply_to_user_id_str":"910189037968855042","source":"<a href=\"http://twitter.com/download/android\" rel=\"nofollow\">Twitter for Android<\/a>","retweet_count":0,"retweeted":false,"geo":null,"in_reply_to_screen_name":"baki2439","is_quote_status":false,"entities":{"urls":[],"hashtags":[],"user_mentions":[{"indices":[0,9],"screen_name":"baki2439","id_str":"910189037968855042","name":"🇷\u200b🇴\u200b🇦\u200b🇩\u200b🇹\u200b🇴バキ🧢","id":910189037968855042}],"symbols":[]},"id_str":"1162977884648198145","in_reply_to_user_id":910189037968855042,"favorite_count":1,"id":1162977884648198145,"text":"@baki2439 J'avais pas reçu la notif sorry","place":null,"contributors":null,"lang":"fr","user":{"id_str":"92073489","id":92073489},"favorited":false},{"in_reply_to_status_id_str":"1162973948885307392","in_reply_to_status_id":1162973948885307392,"coordinates":null,"created_at":"Sun Aug 18 06:39:42 +0000 2019","truncated":false,"in_reply_to_user_id_str":"910189037968855042","source":"<a href=\"http://twitter.com/download/android\" rel=\"nofollow\">Twitter for Android<\/a>","retweet_count":0,"retweeted":false,"geo":null,"in_reply_to_screen_name":"baki2439","is_quote_status":false,"entities":{"urls":[],"hashtags":[],"user_mentions":[],"symbols":[]},"id_str":"1162977351820632064","in_reply_to_user_id":910189037968855042,"favorite_count":0,"id":1162977351820632064,"text":"@baki2439 ?","place":null,"contributors":null,"lang":"und","user":{"id_str":"92073489","id":92073489},"favorited":false},{"in_reply_to_status_id_str":"1162798251533373442","in_reply_to_status_id":1162798251533373442,"coordinates":null,"created_at":"Sat Aug 17 18:53:50 +0000 2019","truncated":false,"in_reply_to_user_id_str":"1024594135","source":"<a href=\"https://mobile.twitter.com\" rel=\"nofollow\">Twitter Web App<\/a>","retweet_count":0,"retweeted":false,"geo":null,"in_reply_to_screen_name":"DelrieuLoic","is_quote_status":false,"entities":{"urls":[],"hashtags":[],"user_mentions":[{"indices":[0,12],"screen_name":"DelrieuLoic","id_str":"1024594135","name":"loic delrieu","id":1024594135}],"symbols":[]},"id_str":"1162799716544987136","in_reply_to_user_id":1024594135,"favorite_count":0,"id":1162799716544987136,"text":"@DelrieuLoic comment elle est arrivée là ptdr","place":null,"contributors":null,"lang":"fr","user":{"id_str":"92073489","id":92073489},"favorited":false}]
        List<TweetDTO> tweets = new ObjectMapper().readValuee(o, new TypeReference<List<TweetDTO>>(){}.class);
        System.out.println("not implemented");
        return new ArrayList<>();
    }

    public List<UserDTO> jsonUserArrayToList(Object o){
        System.out.println("not implemented");
        return new ArrayList<>();
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

}
