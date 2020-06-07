package com.socialmediaraiser.twitterbot;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.socialmediaraiser.twitter.TwitterClient;
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

    URL yamlFile = GoogleAuthorizeUtil.class.getResource("/RedTheOne.yaml");
    if (yamlFile == null) {
      yamlFile = GoogleAuthorizeUtil.class.getResource("/RedTheOne.yaml");
    }
    if (yamlFile == null) {
      LOGGER.severe(() -> "yaml file not found at /RedTheOne.yaml");
      return null;
    }
    ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    TwitterClient.OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    Map<String, Object> yaml        = mapper.readValue(yamlFile, HashMap.class);
    Map<String, Object> scoringList = (Map<String, Object>) yaml.get("scoring");
    GoogleCredentials googleCredentials    = TwitterClient.OBJECT_MAPPER.convertValue(yaml.get("google-credentials"), GoogleCredentials.class);

    String jsonInString = TwitterClient.OBJECT_MAPPER.writeValueAsString(googleCredentials);

    InputStream inputStream = new ByteArrayInputStream(jsonInString.getBytes());

    return GoogleCredential
        .fromStream(inputStream)
        .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
  }
}
