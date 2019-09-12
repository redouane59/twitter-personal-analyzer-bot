package com.socialMediaRaiser.twitter.helpers.dto.getUser;

import lombok.Data;

@Data
public class RequestTokenDTO {
    private String oauthToken;
    private String oauthTokenSecret;
}
