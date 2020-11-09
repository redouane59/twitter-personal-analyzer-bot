package com.github.redouane59.twitterbot;

import com.github.redouane59.twitter.TwitterClient;
import com.github.redouane59.twitter.dto.tweet.Tweet;
import com.github.redouane59.twitter.dto.user.User;
import com.github.redouane59.twitter.signature.TwitterCredentials;
import com.github.redouane59.twitterbot.impl.dto.Joke;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RandomAnswerTweetLauncher {

  public static TwitterClient twitterClient;
  public static String        userName = "RedTheBot_";
  public static String        template = " \uD83E\uDD16 : ";

  public static void main(String[] args) throws IOException {

    twitterClient = new TwitterClient(TwitterClient.OBJECT_MAPPER
                                          .readValue(new File("C:/Users/Perso/Documents/GitHub/twitter-credentials.json"), TwitterCredentials.class)
    );
    try {
      twitterClient.startFilteredStream(tweet -> answerTweet(tweet));
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

  public static void answerTweet(Tweet tweet) {
    try {
      if (tweet.getText() != null && !tweet.getText().startsWith("RT")) {
        System.out.println(tweet.getText());
        User user = twitterClient.getUserFromUserId(tweet.getAuthorId());
        System.out.println("answering " + user.getName());
        if (tweet.getText().toLowerCase().contains("salut") ||
            tweet.getText().toLowerCase().contains("slt ") ||
            tweet.getText().toLowerCase().contains("hello") ||
            tweet.getText().toLowerCase().contains("hi ")) {
          twitterClient.postTweet("@" + user.getName() + template + " Salut!", tweet.getId());
        } else if (tweet.getText().toLowerCase().contains("bonjour")) {
          twitterClient.postTweet("@" + user.getName() + template + " Bien le bonjour ! " + user.getName(), tweet.getId());
        } else if (tweet.getText().toLowerCase().contains("Salam")) {
          twitterClient.postTweet("@" + user.getName() + template + " Alaykoum salam!", tweet.getId());
        } else if (tweet.getText().toLowerCase().contains("cherche") && !user.getName().equals(userName)) {
          twitterClient.postTweet("@" + user.getName() + " " + findRelatedTweet(tweet.getText()), tweet.getId());
        } else if (tweet.getText().contains("?")) {
          twitterClient.postTweet("@" + user.getName() + getRandomAnswer(), tweet.getId());
        } else if (tweet.getText().contains("blague")) {
          twitterClient.postTweet("@" + user.getName() + " " + sayJoke(), tweet.getId());
        } else {
          twitterClient.postTweet("@"
                                  + user.getName()
                                  + template
                                  + " je n'ai pas compris ta demande, check https://twitter.com/RedTheBot_/status/1307314289183723521"
              , tweet.getId());
        }
      }
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }
  }

  public static String findRelatedTweet(String query) {
    query = query.replace("@" + userName + " ", "");
    query = query.replace("cherche ", "");
    query = query.replace("Cherche ", "");
    query = query.replace("\"", "");
    query = "\"" + query + "\"";
    query += " -is:reply -is:retweet lang:fr";
    List<Tweet> result = twitterClient.searchForTweetsWithin7days(query, 10, null).getTweets();
    if (result.size() == 0) {
      return template + " rien trouvé, désolé bg";
    } else if (result.size() == 1) {

    }
    Random r             = new Random();
    Tweet  selectedTweet = result.get(r.nextInt(result.size()));
    String
        tweetUrl =
        "https://twitter.com/" + twitterClient.getUserFromUserId(selectedTweet.getAuthorId()).getName() + "/status/" + selectedTweet.getId();
    return template + " " + tweetUrl;
  }

  public static String getRandomAnswer() {
    List<String>
        answers =
        List.of("Oui!", "Bien sûr que oui", "Tous les jours même", "en semaine B ouais", "Easy", "Haha oui normal", "Bientôt Insha'Allah",
                "Normaaaal", "C'est sûr même!",
                "Peut-être", "Y'a moyen", "Sûrement", "Probablement", "si t'es sage seulement", "Pas pour le moment",
                "ça dépend du virement paypal que tu vas me faire", "Euuuuuuuuuuuuuuuuuuuuuh NON", "Ahah ahah ahahahah ahahhhh non.",
                "Non.", "NON !", "JAMAIIIIIIS", "Dans tes rêves peut-être", "Demande à ta mère", "Demande à ton père", "LOL... Non");
    Random random = new Random();
    return template + answers.get(random.nextInt(answers.size()));
  }

  public static String sayJoke() {
    String                  jokeRandomUrl = "https://blague.xyz/api/joke/random";
    HashMap<String, String> parameters    = new HashMap<>();
    parameters.put("Authorization", "SON5s.M37v_kKpz4HSlleR-SEXz.VRoezy_w.0BrZ_IGVNN6_UUYiKuy.zWTX8mw");
    Optional<Joke> answer = twitterClient.getRequestHelperV2().getRequestWithHeader(jokeRandomUrl, parameters, Joke.class);
    String         result = template;
    if (answer.isPresent()) {
      result += answer.get().getJoke().getQuestion() + "\n";
      result += answer.get().getJoke().getAnswer();
    }
    return result;
  }

}
