package com.socialMediaRaiser.twitter.scoring;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.util.HashMap;

@Data
@Builder
@AllArgsConstructor
public class FollowConfiguration {

    private String tweetName;
    private int minNbFollowers;
    private int maxNbFollowers;
    private int minNbFollowings;
    private int maxNbFollowings;
    private float minRatio;
    private float maxRatio;
    private String language;
    private int maxDaysSinceLastTweet; // be careful about 7 days cache !
    private int minimumPercentMatch;
    private String[] description;
    private String[] location;
    private int nbBaseFollowers;
    private int influencerMinNbFollowers = 2500;
    private float influencerMinRatio = (float)2;

    public FollowConfiguration(){
        this.loadConfiguration();
    }

    private void loadConfiguration() {
        ObjectMapper mapper = new ObjectMapper();
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File from = new File(classLoader.getResource("follow-config.json").getFile());
        TypeReference<HashMap<String,Object>> typeRef = new TypeReference<>() {};
        try{
            HashMap<String,Object> o = mapper.readValue(from, typeRef);
            this.tweetName = o.get("tweetName").toString();
            this.minNbFollowers = Integer.valueOf(o.get("minNbFollowers").toString());
            this.maxNbFollowers = Integer.valueOf(o.get("maxNbFollowers").toString());
            this.minNbFollowings = Integer.valueOf(o.get("minNbFollowings").toString());
            this.maxNbFollowings = Integer.valueOf(o.get("maxNbFollowings").toString());
            this.minRatio = Float.valueOf(o.get("minRatio").toString());
            this.maxRatio = Float.valueOf(o.get("maxRatio").toString());
            this.language = o.get("language").toString(); // @todo array
            this.maxDaysSinceLastTweet = Integer.valueOf(o.get("maxDaysSinceLastTweet").toString());
            this.description = mapper.readValue(o.get("description").toString(), String[].class);
            this.location = mapper.readValue(o.get("location").toString(), String[].class);
            this.minimumPercentMatch = Integer.valueOf(o.get("minimumPercentMatch").toString());
            this.nbBaseFollowers = Integer.valueOf(o.get("nbBaseFollowers").toString());
            this.influencerMinNbFollowers = Integer.valueOf(o.get("influencerMinNbFollowers").toString());
            this.influencerMinRatio = Float.valueOf(o.get("influencerMinRatio").toString());
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
