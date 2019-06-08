package com.socialMediaRaiser.twitter.properties;

import lombok.Data;

@Data
public class CredentialsProperties {
    private String consumerKey;
    private String consumerSecret;
    private String accessToken;
    private String secretToken;
}
