package twitter.helpers;

import org.json.JSONArray;
import twitter.User;

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

    public List<User> jsonUserArrayToList(Object jsonObject, String exceptedLanguage){
        JSONArray jArray = (JSONArray)jsonObject;
        ArrayList<User> listdata = new ArrayList<>();
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                Long id = Long.valueOf(jArray.getJSONObject(i).get(ID).toString());
                String screenName = jArray.getJSONObject(i).get(SCREEN_NAME).toString();
                int followersCount = (int)jArray.getJSONObject(i).get(FOLLOWER_COUNT);
                int friendsCount = (int)jArray.getJSONObject(i).get(FRIENDS_COUNT);
                String lang = jArray.getJSONObject(i).get(LANG).toString();
                if(lang.equals(exceptedLanguage)){
                    User user = new User(id, screenName, followersCount, friendsCount, lang);
                    listdata.add(user);
                }
            }
        }
        return listdata;
    }

}
