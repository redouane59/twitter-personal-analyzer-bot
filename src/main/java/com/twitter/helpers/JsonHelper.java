package com.twitter.helpers;

import com.twitter.TwitterUser;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonHelper {

    private final String SCREEN_NAME = "screen_name";
    private final String FOLLOWER_COUNT = "followers_count";
    private final String FRIENDS_COUNT = "friends_count";
    private final String ID = "id";
    private final String LANG = "lang";

    public List<Long> jsonLongArrayToList(Object jsonObject){
        JSONArray jArray = (JSONArray)jsonObject;
        ArrayList<Long> listdata = new ArrayList<>();
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                listdata.add(jArray.getLong(i));
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

    public List<TwitterUser> jsonUserArrayToList(Object jsonObject, String exceptedLanguage){
        JSONArray jArray = (JSONArray)jsonObject;
        ArrayList<TwitterUser> listdata = new ArrayList<>();
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                Long id = Long.valueOf(jArray.getJSONObject(i).get(ID).toString());
                String screenName = jArray.getJSONObject(i).get(SCREEN_NAME).toString();
                int followersCount = (int)jArray.getJSONObject(i).get(FOLLOWER_COUNT);
                int friendsCount = (int)jArray.getJSONObject(i).get(FRIENDS_COUNT);
                String lang = jArray.getJSONObject(i).get(LANG).toString();
                if(lang.equals(exceptedLanguage)){
                    TwitterUser twitterUser = new TwitterUser(id, screenName, followersCount, friendsCount, lang);
                    listdata.add(twitterUser);
                }
            }
        }
        return listdata;
    }

    public TwitterUser jsonResponseToUser(JSONObject jsonObject){
        Long id = Long.valueOf(jsonObject.get(ID).toString());
        String screenName = jsonObject.get(SCREEN_NAME).toString();
        int followersCount = (int)jsonObject.get(FOLLOWER_COUNT);
        int friendsCount = (int)jsonObject.get(FRIENDS_COUNT);
        String lang = jsonObject.get(LANG).toString();
        return new TwitterUser(id, screenName, followersCount, friendsCount, lang);
    }

}
