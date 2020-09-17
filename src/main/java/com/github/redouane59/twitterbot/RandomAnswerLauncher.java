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
public class RandomAnswerLauncher {

  public static TwitterClient twitterClient;

  public static void main(String[] args) throws IOException {

    twitterClient = new TwitterClient(TwitterClient.OBJECT_MAPPER
                                          .readValue(new File("C:/Users/Perso/Documents/GitHub/twitter-credentials.json"), TwitterCredentials.class)
    );
    twitterClient.startFilteredStream(tweet -> answerTweet(tweet));
  }


  public static void answerTweet(Tweet tweet){
    String tweetId = "1305568516007956483";
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


}
