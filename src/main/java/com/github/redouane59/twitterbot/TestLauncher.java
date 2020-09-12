package com.github.redouane59.twitterbot;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.dto.user.User;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import com.github.redouane59.twitterbot.io.GoogleSheetHelper;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestLauncher {

  public static TwitterClient twitterClient;

  public static void main(String[] args) throws IOException {

    twitterClient = new TwitterClient(TwitterClient.OBJECT_MAPPER
                                          .readValue(new File("C:/Users/Perso/Documents/GitHub/twitter-credentials.json"), TwitterCredentials.class)
    );
    twitterClient.startFilteredStream(tweet -> answerRanking(tweet));
  }

  public static void answerRanking(Tweet tweet) {
    if(!tweet.getText().startsWith("RT") && (tweet.getText().contains("mon classement")||tweet.getText().contains("my ranking")) && tweet.getText().contains("?")){
      User user = twitterClient.getUserFromUserId(tweet.getAuthorId());
      System.out.println("answering " + user.getName());
      int ranking = getRanking(user.getName());
      String response = "";
      if(ranking == 1){
        response = "Yasin tu es le king \uD83E\uDD47 (même si tu vesqui les five)";
      } else if(ranking>0&&ranking<=5){
        response = "Tu fais partie de l'élite ! Tu es " + ranking+"e !! \uD83D\uDE0E" ;
      } else if (ranking>5 && ranking<=15){
        response = "Au top," + ranking+"e ! \uD83D\uDCAA\uD83C\uDFFD" ;
      } else if (ranking>15 && ranking<=50){
        response = "Pas mal, tu es " + ranking + "e \uD83D\uDE09";
      } else if (ranking>50 && ranking<=100){
        response = "Peut faire mieux, " + ranking + "e \uD83D\uDE44";
      } else if (ranking>100){
        response = "Oulala vaut mieux même pas que je te dise... ("+ranking+"e) \uD83E\uDD10";
      } else if (ranking==-1){
        response = "T'es qui ??? \uD83E\uDDD0";
      }
      twitterClient.postTweet("@"+user.getName()+ " " + response + " #TwitterAPI", tweet.getId());
    }

  }

  public static void answerTweet(Tweet tweet){
    String tweetId = "1304113551167172609";
    if(!tweet.getText().startsWith("RT") && tweet.getText().contains("?")){
      System.out.println(tweet.getText());
      if(tweet.getInReplyToStatusId().equals(tweetId) || tweetId.equals(tweet.getConversationId())){
        User user = twitterClient.getUserFromUserId(tweet.getAuthorId());
        System.out.println("answering " + user.getName());
        twitterClient.postTweet("@"+user.getName()+ " " + getRandomAnswer(), tweet.getId());
      }
    }
  }

  public static String getRandomAnswer(){
    List<String> answers = List.of("Oui!", "Bien sûr que oui", "Tous les jours même", "en semaine B ouais", "Easy", "Haha oui normal", "Bientôt Insha'Allah",
                                   "Normaaaal", "C'est sûr même!",
                                   "Peut-être","Y'a moyen", "Sûrement", "Probablement", "si t'es sage seulement", "Pas pour le moment",
                                   "ça dépend du virement paypal que tu vas me faire", "Euuuuuuuuuuuuuuuuuuuuuh NON", "Ahah ahah ahahahah ahahhhh non.",
                                   "Non.", "NON !", "JAMAIIIIIIS", "Dans tes rêves peut-être", "Demande à ta mère", "Demande à ton père", "LOL... Non");
    Random random = new Random();
    return answers.get(random.nextInt(answers.size())) + " #TwitterAPI";
  }

  public static int getRanking(String userName){
    try {
      GoogleSheetHelper googleSheetHelper = new GoogleSheetHelper();
      return Integer.valueOf(googleSheetHelper.vlookup(userName,15));
    } catch (IOException e) {
      e.printStackTrace();
      return -1;
    }
  }

}
