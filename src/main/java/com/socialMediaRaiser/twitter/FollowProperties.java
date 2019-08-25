package com.socialMediaRaiser.twitter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.socialMediaRaiser.twitter.helpers.JsonHelper;
import com.socialMediaRaiser.twitter.properties.*;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class FollowProperties {

    //public static String USER_NAME = "RedouaneBali";
    public static TargetProperties targetProperties;
    public static ScoringProperties scoringProperties;
    public static InfluencerProperties influencerProperties;
    public static IOProperties ioProperties;
    public static TwitterCredentials twitterCredentials;
    public static GoogleCredentials googleCredentials;
    public static String ARRAY_SEPARATOR = ",";

    public static boolean load(String userName) {

        if(userName==null) return false;

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        JsonHelper.OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        try {
            URL yamlFile = FollowProperties.class.getResource("/"+userName+".yaml");
            if(yamlFile==null){
                System.err.println("yaml file not found at /"+userName+".yaml");
                return false;
            }
            Map<String,Object> yaml = mapper.readValue(yamlFile, HashMap.class);
            Map<String, Object> scoringList = (Map<String, Object>)yaml.get("scoring");
            List<ScoringProperty> scoringPropertyList = new ArrayList<>();
            for(Map.Entry<String, Object> p : scoringList.entrySet()){
                ScoringProperty sp =  JsonHelper.OBJECT_MAPPER.convertValue(p.getValue(), ScoringProperty.class);
                sp.setCriterion(p.getKey());
                scoringPropertyList.add(sp);
            }
            scoringProperties = new ScoringProperties(scoringPropertyList);
            targetProperties = JsonHelper.OBJECT_MAPPER.convertValue(yaml.get("target"),TargetProperties.class);
            twitterCredentials = JsonHelper.OBJECT_MAPPER.convertValue(yaml.get("twitter-credentials"), TwitterCredentials.class);
            googleCredentials = JsonHelper.OBJECT_MAPPER.convertValue(yaml.get("google-credentials"), GoogleCredentials.class);
            influencerProperties = JsonHelper.OBJECT_MAPPER.convertValue(yaml.get("influencer"),InfluencerProperties.class);
            ioProperties = JsonHelper.OBJECT_MAPPER.convertValue(yaml.get("io"), IOProperties.class);
            System.out.println("properties loaded correctly");
            return true;
        } catch (IOException ex) {
            System.err.println("properties could not be loaded (" + ex.getMessage()+")");
            return false;
        }
    }
}
