package com.github.redouane59.twitterbot.impl;

public class RankingConfiguration {

  public static int PROFILE_COEFF        = 1;
  public static int INTERACTION_COEFF    = 2;
  public static int NB_RECENT_TWEETS_MIN = 21;
  public static int NB_RECENT_TWEETS_MAX = 70;

  public static int INTERACTION_SCORE_RT_COEFF    = 4;
  public static int INTERACTION_SCORE_QUOTE_COEFF = 3;
  public static int INTERACTION_SCORE_REPLY_COEFF = 2;
  public static int INTERACTION_SCORE_LIKE_COEFF  = 1;

}
