package com.socialMediaRaiser.twitter.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.properties.GoogleCredentials;

import java.io.*;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.util.Collections;


public class GoogleAuthorizeUtil {
    public static GoogleCredential authorize() throws IOException, GeneralSecurityException {

        String jsonInString = new ObjectMapper().writeValueAsString(FollowProperties.googleCredentials);

        InputStream inputStream = new ByteArrayInputStream(jsonInString.getBytes());

        return GoogleCredential
                .fromStream(inputStream)
                .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
    }
}
