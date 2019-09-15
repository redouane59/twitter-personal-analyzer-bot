package com.socialmediaraiser.twitter.helpers.dto.getuser;

import lombok.Data;

@Data
public class TwitterUrl {
    private int start;
    private int end;
    private String url;
    private String expanded_url;
    private String display_url;
}
