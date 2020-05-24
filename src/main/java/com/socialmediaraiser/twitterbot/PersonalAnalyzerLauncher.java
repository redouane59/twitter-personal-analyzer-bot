package com.socialmediaraiser.twitterbot;

import com.socialmediaraiser.twitterbot.impl.PersonalAnalyzerBot;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Logger;

public class PersonalAnalyzerLauncher {

    private static final Logger LOGGER = Logger.getLogger(PersonalAnalyzerLauncher.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        if(args.length<2){
            LOGGER.severe(()->"missing arguments, expecting 2 : ownerName[String], unfollowMode[boolean]");
        } else{
            String userName = args[0];
            boolean unfollowMode = Boolean.parseBoolean(args[1]);
            PersonalAnalyzerBot bot = new PersonalAnalyzerBot(userName);
            if(!unfollowMode){
                boolean includeFollowers = true;
                boolean includeFollowings = true;
                boolean onlyFollowBackFollowers = true;
                String tweetArchivePath = userName.toLowerCase()+"-tweet-history.json";
                if(args.length>5) {
                    includeFollowers = Boolean.parseBoolean(args[2]);
                    includeFollowings = Boolean.parseBoolean(args[3]);
                    onlyFollowBackFollowers = Boolean.parseBoolean(args[4]);
                    tweetArchivePath = args[5];
                }
                bot.launch(includeFollowers, includeFollowings, onlyFollowBackFollowers, tweetArchivePath);
            } else{
                String[] toUnfollow = {"InesInesz"};
                // @todo KO
                String[] whiteList = {"InesInesz","caroletonneau","KayiTurk_fr","Asiorak","Sarah_Bcf","vivi83032363"
                        ,"Gabriel_Bleron","NaelABC","Ciudadana93","Irfane_Clement","OneblueTeam","Luffy_Affranchi","silversilvery",
                        "abdelhvmid","souf_belkadi","GiuseppeParavi1","Riad7ben","YSerena_","Celia_Tvbti","sombrenuance",
                        "salmaechouay","VictorLefranc","julien_Gres","rk_Ngadi"};
                bot.unfollow(toUnfollow, whiteList);
            }
        }
    }


}
