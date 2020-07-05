package com.socialmediaraiser.twitterbot;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.socialmediaraiser.twitter.TwitterClient;
import com.socialmediaraiser.twitter.signature.TwitterCredentials;
import com.socialmediaraiser.twitterbot.properties.GoogleCredentials;
import com.socialmediaraiser.twitterbot.properties.TargetProperties;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.CustomLog;

@CustomLog
public class GoogleAuthorizeUtil {

  private GoogleAuthorizeUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static GoogleCredential authorize() throws IOException {
    // @todo use a local json instead
    URL googleCredentialsFile = GoogleAuthorizeUtil.class.getClassLoader().getResource("google-credentials.json");
    if (googleCredentialsFile == null) {
      LOGGER.severe(() -> "file not found");
    }
    ObjectMapper mapper = new ObjectMapper();
    TwitterClient.OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    HashMap           credentialMap     = mapper.readValue(googleCredentialsFile, HashMap.class);
    GoogleCredentials googleCredentials = TwitterClient.OBJECT_MAPPER.convertValue(credentialMap, GoogleCredentials.class);

    String jsonInString = TwitterClient.OBJECT_MAPPER.writeValueAsString(googleCredentials);

    InputStream inputStream = new ByteArrayInputStream(jsonInString.getBytes());

    return GoogleCredential
        .fromStream(inputStream)
        .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
  }
}
