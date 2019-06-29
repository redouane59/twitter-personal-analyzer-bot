package com.socialMediaRaiser.twitter;

import com.socialMediaRaiser.AbstractUser;
import com.socialMediaRaiser.twitter.helpers.RequestHelper;
import com.socialMediaRaiser.twitter.helpers.URLHelper;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwitterAccountEventHelper extends AbstractTwitterBot {

    public TwitterAccountEventHelper(){
        String url = this.getUrlHelper().getLiveEventUrl();
        JSONObject response = this.getRequestHelper().executePostRequest(url,new HashMap<String,String>());

    }

    @Override
    public List<? extends AbstractUser> getPotentialFollowers(Long ownerId, int count, boolean follow, boolean saveResults) {
        return null;
    }
}
