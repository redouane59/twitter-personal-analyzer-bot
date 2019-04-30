package com.socialMediaRaiser.twitter.helpers;

import com.socialMediaRaiser.twitter.User;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonHelper {

    private static final String STATUSES_COUNT = "statuses_count";
    private static final String CREATED_AT = "created_at";
    private final String SCREEN_NAME = "screen_name";
    private final String FOLLOWER_COUNT = "followers_count";
    private final String FRIENDS_COUNT = "friends_count";
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
            String lang = jsonObject.get(LANG).toString();
            int statuses_count = (int)jsonObject.get(STATUSES_COUNT);
            String created_at = jsonObject.get(CREATED_AT).toString();
            return new User(id, screenName, followersCount, friendsCount, lang,
                    statuses_count, created_at, 1, "");
        } else{
            return null;
        }
    }

    public Long getLongFromCursorObject(JSONObject response){
        if(response.get(NEXT_CURSOR) instanceof Long){
            return (Long)response.get(NEXT_CURSOR);
        } else if(response.get(NEXT_CURSOR) instanceof Integer) {
            return Long.valueOf((Integer) response.get(NEXT_CURSOR));
        }  else{
            System.out.println("format problem");
            return null;
        }
    }

}
