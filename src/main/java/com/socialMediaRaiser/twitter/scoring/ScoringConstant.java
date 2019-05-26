package com.socialMediaRaiser.twitter.scoring;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.File;
import java.util.HashMap;

@Data
public class ScoringConstant {

    public ScoringConstant(){
        this.loadConfiguration();
    }

    private int minNbFollowers;
    private int maxNbFollowers;
    private int minNbFollowings;
    private int maxNbFollowings;
    private int minRatio;
    private int maxRatio;
    private String language;
    private int maxDaysSinceLastTweet; // be careful about 7 days cache !
    private int minimumPercentMatch;
    public String[] description; // @todo to implement
    public String[] location; // @todo to implement

    public static final int INFLUENCER_MIN_NB_FOLLOWERS = 2500;
    public static final float INFLUENCER_MIN_RATIO = (float)2;

    private void loadConfiguration() {
        ObjectMapper mapper = new ObjectMapper();
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File from = new File(classLoader.getResource("follow-config.json").getFile());
        TypeReference<HashMap<String,Object>> typeRef = new TypeReference<>() {};
        try{
            HashMap<String,Object> o = mapper.readValue(from, typeRef);
            this.minNbFollowers = Integer.valueOf(o.get("minNbFollowers").toString()); // @Todo créer constantes
            this.maxNbFollowers = Integer.valueOf(o.get("maxNbFollowers").toString());
            this.minNbFollowings = Integer.valueOf(o.get("minNbFollowings").toString());
            this.maxNbFollowings = Integer.valueOf(o.get("maxNbFollowings").toString());
            this.minRatio = Integer.valueOf(o.get("minRatio").toString());
            this.maxRatio = Integer.valueOf(o.get("maxRatio").toString());
            this.language = o.get("language").toString();
            this.maxDaysSinceLastTweet = Integer.valueOf(o.get("maxDaysSinceLastTweet").toString());
            this.description = mapper.readValue(o.get("description").toString(), String[].class);
            this.location = mapper.readValue(o.get("location").toString(), String[].class);
            this.minimumPercentMatch = Integer.valueOf(o.get("minimumPercentMatch").toString());
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
