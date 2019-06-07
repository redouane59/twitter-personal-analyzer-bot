package com.socialMediaRaiser.twitter;

import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FollowProperties {

    public final static String TWEET_NAME = "RedTheOne";
    private static String fileName = TWEET_NAME +"/config.properties"; // @todo circular depedency otherwise
    @Setter @Getter
    private static Map<String, String> propertiesMap ;

    public static String MIN_NB_FOLLOWERS = "target.minNbFollowers";
    public static String MAX_NB_FOLLOWERS = "target.maxNbFollowers";
    public static String MIN_NB_FOLLOWINGS = "target.minNbFollowings";
    public static String MAX_NB_FOLLOWINGS = "target.maxNbFollowings";
    public static String MIN_RATIO = "target.minRatio";
    public static String MAX_RATIO = "target.maxRatio";
    public static String LANGUAGE = "target.language";
    public static String MAX_DAYS_SINCE_LAST_TWEET = "target.maxDaysSinceLastTweet";
    public static String DESCRIPTION = "target.description";
    public static String LOCATION = "target.location";
    public static String MIN_PERCENT_MATCH = "target.minimumPercentMatch";
    public static String TERMS = "target.terms";
    public static String NB_BASE_FOLLOWERS = "target.nbBaseFollowers";
    public static String INFLUENCER_MIN_NB_FOLLOWERS = "influencer.minNbFollowers";
    public static String INFLUENCER_MIN_RATIO = "influencer.minRatio";
    public static String IO_SHEET_IT = "io.sheet.id";
    public static String IO_SHEET_TABNAME = "io.sheet.tabName";
    public static String IO_SHEET_RESULT_COLUMN = "io.sheet.resultColumn";
    public static String IO_SHEET_FOLLOW_DATE_INDEX = "io.sheet.followDateIndex";
    public static String SCORING_IS_ACTIVE_NB_FOLLOWERS = "scoring.active.nbFollowers";
    public static String SCORING_IS_ACTIVE_NB_FOLLOWINGS = "scoring.active.nbFollowings";
    public static String SCORING_IS_ACTIVE_RATIO = "scoring.active.ratio";
    public static String SCORING_IS_ACTIVE_LAST_UPDATE = "scoring.active.lastUpdate";
    public static String SCORING_IS_ACTIVE_DESCRIPTION = "scoring.active.description";
    public static String SCORING_IS_ACTIVE_LOCATION = "scoring.active.location";
    public static String SCORING_IS_ACTIVE_COMMON_FOLLOWERS = "scoring.active.commonFollowers";
    public static String SCORING_MAX_POINTS_NB_FOLLOWERS = "scoring.maxPoints.nbFollowers";
    public static String SCORING_MAX_POINTS_NB_FOLLOWINGS = "scoring.maxPoints.nbFollowings";
    public static String SCORING_MAX_POINTS_RATIO = "scoring.maxPoints.ratio";
    public static String SCORING_MAX_POINTS_LAST_UPDATE = "scoring.maxPoints.lastUpdate";
    public static String SCORING_MAX_POINTS_DESCRIPTION = "scoring.maxPoints.description";
    public static String SCORING_MAX_POINTS_LOCATION = "scoring.maxPoints.location";
    public static String SCORING_MAX_POINTS_COMMON_FOLLOWERS = "scoring.maxPoints.commonFollowers";
    
    private static void init() {

        try (OutputStream output = new FileOutputStream("src/main/resources/"+fileName)) {

            Properties prop = new Properties();

            // set the properties value
            prop.setProperty(MIN_NB_FOLLOWERS, "1");
            prop.setProperty(MAX_NB_FOLLOWERS, "10000");
            prop.setProperty(MIN_NB_FOLLOWINGS, "1");
            prop.setProperty(MAX_NB_FOLLOWINGS, "10000");
            prop.setProperty(MIN_RATIO, "0.5");
            prop.setProperty(MAX_RATIO, "2");
            prop.setProperty(LANGUAGE, "fr");
            prop.setProperty(MAX_DAYS_SINCE_LAST_TWEET, "15");
            prop.setProperty(DESCRIPTION, "decathlon,dev,java,tech,data,software,network,omnicanal,prog,#ia,github,js,php,python,iot,startup,seo,data,machine learning,innovation"); // lowercase only
            prop.setProperty(LOCATION, "");
            prop.setProperty(MIN_PERCENT_MATCH, "85");
            prop.setProperty(TERMS, "");
            prop.setProperty(NB_BASE_FOLLOWERS, "20");
            prop.setProperty(INFLUENCER_MIN_NB_FOLLOWERS, "20");
            prop.setProperty(INFLUENCER_MIN_RATIO, "0.5");
            prop.setProperty(IO_SHEET_IT, "1rpTWqHvBFaxdHcbnHmry2quQTKhPVJ-dA2n_wep0hrs");
            prop.setProperty(IO_SHEET_TABNAME, "RedouaneBali");
            prop.setProperty(IO_SHEET_RESULT_COLUMN, "L");
            prop.setProperty(IO_SHEET_FOLLOW_DATE_INDEX, "10");

            // save properties to project root folder
            prop.store(output, null);

            propertiesMap = new HashMap<>();
            for (final String name: prop.stringPropertyNames())
                propertiesMap.put(name, prop.getProperty(name));

            System.out.println(propertiesMap);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static void load() {
        propertiesMap = new HashMap<>();

        try (InputStream input = new FileInputStream("src/main/resources/"+fileName)) {

            Properties prop = new Properties();

            // load a properties file
            prop.load(input);

            propertiesMap.put(MIN_NB_FOLLOWERS,prop.getProperty(MIN_NB_FOLLOWERS));
            propertiesMap.put(MAX_NB_FOLLOWERS,prop.getProperty(MAX_NB_FOLLOWERS));
            propertiesMap.put( MIN_NB_FOLLOWINGS, prop.getProperty(MIN_NB_FOLLOWINGS));
            propertiesMap.put(MAX_NB_FOLLOWINGS,prop.getProperty(MAX_NB_FOLLOWINGS));
            propertiesMap.put(MIN_RATIO,prop.getProperty(MIN_RATIO));
            propertiesMap.put(MAX_RATIO,prop.getProperty(MAX_RATIO));
            propertiesMap.put(LANGUAGE,prop.getProperty(LANGUAGE));
            propertiesMap.put(MAX_DAYS_SINCE_LAST_TWEET,prop.getProperty(MAX_DAYS_SINCE_LAST_TWEET));
            propertiesMap.put(DESCRIPTION,prop.getProperty(DESCRIPTION)); // lowercase only
            propertiesMap.put(LOCATION,prop.getProperty(LOCATION));
            propertiesMap.put(MIN_PERCENT_MATCH,prop.getProperty(MIN_PERCENT_MATCH));
            propertiesMap.put(TERMS,prop.getProperty(TERMS));
            propertiesMap.put(NB_BASE_FOLLOWERS,prop.getProperty(NB_BASE_FOLLOWERS));
            propertiesMap.put(INFLUENCER_MIN_NB_FOLLOWERS,prop.getProperty(INFLUENCER_MIN_NB_FOLLOWERS));
            propertiesMap.put(INFLUENCER_MIN_RATIO,prop.getProperty(INFLUENCER_MIN_RATIO));
            propertiesMap.put(IO_SHEET_IT,prop.getProperty(IO_SHEET_IT));
            propertiesMap.put(IO_SHEET_TABNAME,prop.getProperty(IO_SHEET_TABNAME));
            propertiesMap.put(IO_SHEET_RESULT_COLUMN,prop.getProperty(IO_SHEET_RESULT_COLUMN));
            propertiesMap.put(IO_SHEET_FOLLOW_DATE_INDEX, prop.getProperty(IO_SHEET_FOLLOW_DATE_INDEX));

            propertiesMap.put(SCORING_IS_ACTIVE_NB_FOLLOWERS, prop.getProperty(SCORING_IS_ACTIVE_NB_FOLLOWERS));
            propertiesMap.put(SCORING_IS_ACTIVE_NB_FOLLOWINGS, prop.getProperty(SCORING_IS_ACTIVE_NB_FOLLOWINGS));
            propertiesMap.put(SCORING_IS_ACTIVE_RATIO, prop.getProperty(SCORING_IS_ACTIVE_RATIO));
            propertiesMap.put(SCORING_IS_ACTIVE_LAST_UPDATE, prop.getProperty(SCORING_IS_ACTIVE_LAST_UPDATE));
            propertiesMap.put(SCORING_IS_ACTIVE_DESCRIPTION, prop.getProperty(SCORING_IS_ACTIVE_DESCRIPTION));
            propertiesMap.put(SCORING_IS_ACTIVE_LOCATION, prop.getProperty(SCORING_IS_ACTIVE_LOCATION));
            propertiesMap.put(SCORING_IS_ACTIVE_COMMON_FOLLOWERS, prop.getProperty(SCORING_IS_ACTIVE_COMMON_FOLLOWERS));

            propertiesMap.put(SCORING_MAX_POINTS_NB_FOLLOWERS, prop.getProperty(SCORING_MAX_POINTS_NB_FOLLOWERS));
            propertiesMap.put(SCORING_MAX_POINTS_NB_FOLLOWINGS, prop.getProperty(SCORING_MAX_POINTS_NB_FOLLOWINGS));
            propertiesMap.put(SCORING_MAX_POINTS_RATIO, prop.getProperty(SCORING_MAX_POINTS_RATIO));
            propertiesMap.put(SCORING_MAX_POINTS_LAST_UPDATE, prop.getProperty(SCORING_MAX_POINTS_LAST_UPDATE));
            propertiesMap.put(SCORING_MAX_POINTS_DESCRIPTION, prop.getProperty(SCORING_MAX_POINTS_DESCRIPTION));
            propertiesMap.put(SCORING_MAX_POINTS_LOCATION, prop.getProperty(SCORING_MAX_POINTS_LOCATION));
            propertiesMap.put(SCORING_MAX_POINTS_COMMON_FOLLOWERS, prop.getProperty(SCORING_MAX_POINTS_COMMON_FOLLOWERS));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getStringProperty(String propertyName){
        if(propertiesMap.containsKey(propertyName)) {
            return propertiesMap.get(propertyName);
        } else{
            System.err.println(propertyName + " property not found");
            return "";
        }
    }

    public static int getIntProperty(String propertyName){
        if(propertiesMap.containsKey(propertyName)){
            return Integer.valueOf(propertiesMap.get(propertyName));
        } else{
            System.err.println(propertyName + " property not found");
            return -1;
        }
    }

    public static boolean getBooleanProperty(String propertyName){
        if(propertiesMap.containsKey(propertyName)){
            return Boolean.valueOf(propertiesMap.get(propertyName));
        } else{
            System.err.println(propertyName + " property not found");
            return false;
        }
    }

    public static Long getLongProperty(String propertyName){
        if(propertiesMap.containsKey(propertyName)) {
            return Long.valueOf(propertiesMap.get(propertyName));
        }else{
            System.err.println(propertyName + " property not found");
            return 0L;
        }
    }

    public static Float getFloatProperty(String propertyName){
        if(propertiesMap.containsKey(propertyName)) {
            return Float.valueOf(propertiesMap.get(propertyName));
        }else{
            System.err.println(propertyName + " property not found");
            return (float)0;
        }
    }

    public static String[] getStringArrayProperty(String propertyName){
        if(propertiesMap.containsKey(propertyName)) {
            return propertiesMap.get(propertyName).split(",");
        }else{
            System.err.println(propertyName + " property not found");
            return new String[]{};
        }
    }
}
