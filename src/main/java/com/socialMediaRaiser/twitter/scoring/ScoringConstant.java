package com.socialMediaRaiser.twitter.scoring;

public class ScoringConstant {

    public static final int MIN_NB_FOLLOWERS = 100;
    public static final int MAX_NB_FOLLOWERS = 10000;
    public static final int MIN_NB_FOLLOWINGS = 1;
    public static final int MAX_NB_FOLLOWINGS = 10000;
    public static final int MIN_RATIO = 1;
    public static final int MAX_RATIO = 4;
    public static final String LANGUAGE1 = "fr"; // @todo array
    public static final String LANGUAGE2 = "en";
    public static final String LANGUAGE3 = "es";
    public static final int MAX_DAYS_SINCE_LAST_TWEET = 15; // be careful about 7 days cache !
    public static final String[] DESCRIPTION = new String[1]; // @todo to implement
    public static final String[] LOCATION = new String[1]; // @todo to implement

    public static final int INFLUENCER_MIN_NB_FOLLOWERS = 2500;
    public static final float INFLUENCER_MIN_RATIO = (float)2;
}
