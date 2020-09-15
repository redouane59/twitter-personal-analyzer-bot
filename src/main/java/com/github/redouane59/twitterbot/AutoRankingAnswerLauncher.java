package com.github.redouane59.twitterbot;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.dto.user.User;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import com.github.redouane59.twitterbot.io.GoogleSheetHelper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AutoRankingAnswerLauncher {

  public static TwitterClient twitterClient;
  public static GoogleSheetHelper googleSheetHelper;

  public static void main(String[] args) throws IOException {

    twitterClient = new TwitterClient(TwitterClient.OBJECT_MAPPER
                                          .readValue(new File("C:/Users/Perso/Documents/GitHub/twitter-credentials.json"), TwitterCredentials.class));
    googleSheetHelper  = new GoogleSheetHelper();
    twitterClient.startFilteredStream(tweet -> answerRanking(tweet));
  }

  public static void answerRanking(Tweet tweet) {
    if(!tweet.getText().startsWith("RT") && (tweet.getText().contains("mon classement")||tweet.getText().contains("my ranking")) && tweet.getText().contains("?")){
      User user = twitterClient.getUserFromUserId(tweet.getAuthorId());
      System.out.println("answering " + user.getName());
      twitterClient.postTweet("@" + user.getName() + " " + getText(user.getName()) + " #TwitterAPI", tweet.getId());
    }

  }

  public static String getText(String userName) {
    List<Object> userData = googleSheetHelper.getUserData(userName);
    StringBuilder response = new StringBuilder();
    if (userData.size() == 0) {
      response = new StringBuilder("T'es qui ??? \uD83E\uDDD0");
    } else {
      int           ranking  = Integer.parseInt(String.valueOf(userData.get(15)));
      if (ranking == 1) {
        response = new StringBuilder("Yasin tu es le king \uD83E\uDD47 (même si tu vesqui les five)");
      } else if (ranking > 0 && ranking <= 5) {
        response = new StringBuilder("Tu fais partie de l'élite ! Tu es " + ranking + "e !! \uD83D\uDE0E");
      } else if (ranking > 5 && ranking <= 15) {
        response = new StringBuilder("Au top," + ranking + "e ! \uD83D\uDCAA\uD83C\uDFFD");
      } else if (ranking > 15 && ranking <= 50) {
        response = new StringBuilder("Pas mal, tu es " + ranking + "e \uD83D\uDE09");
      } else if (ranking > 50 && ranking <= 100) {
        response = new StringBuilder("Peut faire mieux, " + ranking + "e \uD83D\uDE44");
      } else if (ranking > 100) {
        response = new StringBuilder("Oulala vaut mieux même pas que je te dise... (" + ranking + "e) \uD83E\uDD10");
      } else if (ranking == -1) {
        response = new StringBuilder("T'es qui ??? \uD83E\uDDD0");
      }
    }
    try {
      double profileStars     = Double.parseDouble(String.valueOf(userData.get(16)).replace(",","."));
      double interactionStars = Double.parseDouble(String.valueOf(userData.get(17)).replace(",","."));
      response.append("\nNote profil : ");
      for (int i = 0; i < profileStars; i++) {
        response.append("⭐");
      }
      response.append("\nNote interactions : ");
      for (int i = 0; i < interactionStars; i++) {
        response.append("⭐");
      }
      response.append("\n");
    } catch(Exception e){
      System.out.println(e.getMessage());
    }
    return response.toString();
  }

}
