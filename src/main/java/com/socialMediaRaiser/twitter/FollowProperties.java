package com.socialMediaRaiser.twitter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
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

    public static void load(String USER_NAME) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        try {
            URL yamlFile = FollowProperties.class.getResource("/"+USER_NAME+".yaml");
            if(yamlFile==null){
                System.err.println("yaml file not found at /"+USER_NAME+".yaml");
                return;
            }
            Map<String,Object> yaml = mapper.readValue(yamlFile, HashMap.class);
            Map<String, Object> scoringList = (Map<String, Object>)yaml.get("scoring");
            List<ScoringProperty> scoringPropertyList = new ArrayList<>();
            for(Map.Entry<String, Object> p : scoringList.entrySet()){
                ScoringProperty sp =  objectMapper.convertValue(p.getValue(), ScoringProperty.class);
                sp.setCriterion(p.getKey());
                scoringPropertyList.add(sp);
            }
            scoringProperties = new ScoringProperties(scoringPropertyList);
            targetProperties = objectMapper.convertValue(yaml.get("target"),TargetProperties.class);
            twitterCredentials = objectMapper.convertValue(yaml.get("twitter-credentials"), TwitterCredentials.class);
            googleCredentials = objectMapper.convertValue(yaml.get("google-credentials"), GoogleCredentials.class);
            influencerProperties = objectMapper.convertValue(yaml.get("influencer"),InfluencerProperties.class);
            ioProperties = objectMapper.convertValue(yaml.get("io"), IOProperties.class);
            System.out.println("properties loaded correctly");
        } catch (IOException ex) {
            System.err.println("properties could not be loaded (" + ex.getMessage()+")");
        }
    }
}
