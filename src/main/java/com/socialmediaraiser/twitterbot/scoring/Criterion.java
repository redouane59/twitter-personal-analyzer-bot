package com.socialmediaraiser.twitterbot.scoring;

import lombok.Getter;

@Getter
public enum Criterion {

  NB_FOLLOWERS,
  NB_FOLLOWINGS,
  RATIO,
  LAST_UPDATE,
  DESCRIPTION,
  LOCATION,
  NB_TWEETS,
  COMMON_FOLLOWERS;
}
