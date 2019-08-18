package com.socialMediaRaiser.twitter.helpers.dto.getUser;

import lombok.Data;

@Data
public class TwitterUrl {
    private int start;
    private int end;
    private String url;
    private String expanded_url;
    private String display_url;
}
