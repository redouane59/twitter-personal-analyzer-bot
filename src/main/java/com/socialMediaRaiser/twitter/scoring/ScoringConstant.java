package com.socialMediaRaiser.twitter.scoring;

// @todo to remove
public class ScoringConstant {

    public static int MIN_NB_FOLLOWERS = 100;
    public static int MAX_NB_FOLLOWERS = 10000;
    public static int MIN_NB_FOLLOWINGS = 1;
    public static int MAX_NB_FOLLOWINGS = 10000;
    public static int MIN_RATIO = 1;
    public static int MAX_RATIO = 4;
    public static String LANGUAGE = "fr";
    public static int MAX_DAYS_SINCE_LAST_TWEET = 15; // be careful about 7 days cache !
    public static String[] DESCRIPTION; // @todo to implement
    public static String LOCATION; // @todo to implement

    public static int INFLUENCER_MIN_NB_FOLLOWERS = 2500;
    public static float INFLUENCER_MIN_RATIO = (float)2;
}
