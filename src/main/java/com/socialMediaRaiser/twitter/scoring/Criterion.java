package com.socialMediaRaiser.twitter.scoring;

import lombok.Getter;

@Getter
public enum Criterion {

    NB_FOLLOWERS,
    NB_FOLLOWINGS,
    RATIO,
    LAST_UPDATE,
    DESCRIPTION,
    LOCATION,
    COMMON_FOLLOWERS;

}
