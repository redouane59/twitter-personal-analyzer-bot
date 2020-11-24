package com.github.redouane59.twitterbot;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.others.RequestToken;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AuthenticationLauncher {

  public static void main(String[] args) throws IOException {

    TwitterCredentials credentials = TwitterClient.OBJECT_MAPPER
        .readValue(new File("C:/Users/Perso/Documents/GitHub/twitter-credentials.json"), TwitterCredentials.class);
    TwitterClient twitterClient = new TwitterClient(credentials);

    RequestToken requestToken     = twitterClient.getOauth1Token("oob");
    RequestToken accessToken      = null;
    String       authorizationUrl = "https://twitter.com/oauth/authenticate?oauth_token=" + requestToken.getOauthToken();

    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    while (null == accessToken) {
      System.out.println("Open the following URL and grant access to your account:");
      System.out.println(authorizationUrl);
      try {
        Desktop.getDesktop().browse(new URI(authorizationUrl));
      } catch (Exception e) {
        e.printStackTrace();
      }
      System.out.print("Enter the PIN(if available) and hit enter after you granted access.[PIN]:");
      String pin = br.readLine();
      try {
        if (pin.length() > 0) {
          credentials.setAccessToken(requestToken.getOauthToken());
          credentials.setAccessTokenSecret(requestToken.getOauthTokenSecret());
          accessToken = twitterClient.getOAuth1AccessToken(requestToken, pin);
        } else {
          System.out.println("no pin");
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    credentials.setAccessToken(accessToken.getOauthToken());
    credentials.setAccessTokenSecret(accessToken.getOauthTokenSecret());
    System.out.println("access token : " + accessToken.getOauthToken());
    System.out.println("secret token : " + accessToken.getOauthTokenSecret());
    twitterClient.postTweet("test #TwitterAPI");
  }


}
