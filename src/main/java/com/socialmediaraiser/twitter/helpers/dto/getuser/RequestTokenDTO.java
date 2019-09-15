package com.socialmediaraiser.twitter.helpers.dto.getuser;

import lombok.Data;

@Data
public class RequestTokenDTO {
    private String oauthToken;
    private String oauthTokenSecret;
}
