package twitter.helpers;

import org.json.JSONArray;
import twitter.User;

import java.util.ArrayList;
import java.util.List;

public class JsonHelper {

    private final String SCREEN_NAME = "screen_name";

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

    public List<User> jsonUserArrayToList(Object jsonObject){
        JSONArray jArray = (JSONArray)jsonObject;
        ArrayList<User> listdata = new ArrayList<>();
        if (jArray != null) {
            for (int i=0;i<jArray.length();i++){
                Long id = Long.valueOf(jArray.getJSONObject(i).get("id").toString());
                String screenName = jArray.getJSONObject(i).get(SCREEN_NAME).toString();
                int followersCount = (int)jArray.getJSONObject(i).get("followers_count");
                int friendsCount = (int)jArray.getJSONObject(i).get("friends_count");
                User user = new User(id, screenName, followersCount, friendsCount);
                listdata.add(user);
            }
        }
        return listdata;
    }

}
