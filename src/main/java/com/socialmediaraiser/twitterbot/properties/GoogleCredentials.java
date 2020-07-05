package com.socialmediaraiser.twitterbot.properties;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GoogleCredentials {

  private String type;
  private String project_id;
  private String private_key_id;
  private String private_key;
  private String client_email;
  private String client_id;
  private String auth_uri;
  private String token_uri;
  private String auth_provider_x509_cert_url;
  private String client_x509_cert_url;
  @JsonProperty("sheet_id")
  private String sheetId;
  @JsonProperty("tab_name")
  private String tabName;
}
