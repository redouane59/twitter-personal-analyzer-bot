package com.socialmediaraiser.twitterbot.impl;

import com.socialmediaraiser.RelationType;
import com.socialmediaraiser.twitter.TwitterClient;
import com.socialmediaraiser.twitter.helpers.ConverterHelper;
import com.socialmediaraiser.twitter.dto.tweet.ITweet;
import com.socialmediaraiser.twitter.dto.tweet.TweetDataDTO;
import com.socialmediaraiser.twitter.IUser;
import com.socialmediaraiser.twitterbot.AbstractIOHelper;
import com.socialmediaraiser.twitterbot.GoogleSheetHelper;
import com.socialmediaraiser.twitterbot.PersonalAnalyzerLauncher;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Getter
@Setter
public class PersonalAnalyzerBot {

    private String userName;
    private static final Logger LOGGER = Logger.getLogger(PersonalAnalyzerLauncher.class.getName());
    private AbstractIOHelper ioHelper;
    private TwitterClient twitterClient = new TwitterClient();

    public PersonalAnalyzerBot(String userName){
        this.userName = userName;
        this.ioHelper = new GoogleSheetHelper(userName);
    }

    public void launch() throws IOException, InterruptedException {
        UserInteractions interactions = this.getNbInterractions(ConverterHelper.dayBeforeNow(7));
        String userId = this.twitterClient.getUserFromUserName(userName).getId();
        List<IUser> followingsUsers = this.twitterClient.getFollowingsUsers(userId);
        for(IUser user : followingsUsers){
            User customUser = new User(user);
            customUser.setNbInteractions(interactions.get(user.getId()).getTotalNbInteractions());
            // add RT and/or likes
            if(this.twitterClient.getRelationType(userId, customUser.getId()).equals(RelationType.FRIENDS)){
                this.ioHelper.addNewFollowerLineSimple(customUser);
                TimeUnit.MILLISECONDS.sleep(600); // @todo manage the limit better with Less calls but bigger objects
                LOGGER.info("adding " + user.getName() + "...");
            } else{
                LOGGER.info("NOT adding " + user.getName() + " (not following back)");
            }
        }
        LOGGER.info("finish with success");
    }

    public UserInteractions getNbInterractions(Date initDate) throws IOException {
        File file = new File(getClass().getClassLoader().getResource("tweet-history.json").getFile());
        //  List<TweetDataDTO> tweets = twitterClient.readTwitterDataFile(file);
        UserInteractions userInteractions = new UserInteractions();
        // this.countRepliesToAndRT(tweets, initDate, userInteractions);
        this.countRecentRepliesFrom(userInteractions, true);
        return userInteractions;
    }

    public void countRecentRepliesFrom(UserInteractions userInteractions, boolean onlyRecent) {
        Date toDate =  onlyRecent ? ConverterHelper.minutesBeforeNow(60) : ConverterHelper.dayBeforeNow(7);
        toDate = DateUtils.truncate(toDate, Calendar.HOUR);
        Date fromDate = onlyRecent ? DateUtils.addDays(toDate, -7) : DateUtils.addDays(toDate, -30);
        fromDate = DateUtils.ceiling(fromDate, Calendar.HOUR);

        List<ITweet> tweetWithReplies;
        String query = "@"+userName+" -is:retweet";
        if(onlyRecent){
            tweetWithReplies= this.twitterClient.searchForTweetsWithin7days(query, fromDate, toDate);
        } else{
            tweetWithReplies= this.twitterClient.searchForTweetsWithin30days(query, fromDate, toDate);
        }
        for(ITweet tweet : tweetWithReplies){
            UserInteractions.UserInteraction userInteraction = userInteractions.get(tweet.getAuthorId());
            userInteraction.incrementNbRepliesFrom();
            userInteractions.getValues().add(userInteraction);
        }
    }

    public void countRepliesToAndRT(List<TweetDataDTO> tweets, Date initDate, UserInteractions userInteractions){
        Date tweetDate;

        for(TweetDataDTO tweetDataDTO : tweets){
            ITweet tweet = tweetDataDTO.getTweet();
            // checking the reply I gave to other users
            String inReplyUserId = tweet.getInReplyToUserId();
            tweetDate = tweet.getCreatedAt();
            if(inReplyUserId!=null){
                if(tweetDate!=null && tweetDate.compareTo(initDate)>0) {
                    UserInteractions.UserInteraction userInteraction = userInteractions.get(inReplyUserId);
                    userInteraction.incrementNbRepliesTo();
                }
            }

            if(tweetDate!=null && tweetDate.compareTo(initDate)>0){
                if(tweetDataDTO.getTweet().getRetweetCount()>0){
                    this.countRetweets(tweetDataDTO, userInteractions);
                }
            }
        }
    }

    public void countRetweets(TweetDataDTO tweetDataDTO, UserInteractions userInteractions){
        List<String> retweeterIds = this.twitterClient.getRetweetersId(tweetDataDTO.getTweet().getId());
        for(String retweeterId : retweeterIds){
            UserInteractions.UserInteraction userInteraction = userInteractions.get(retweeterId);
            userInteraction.incrementNbRetweets();
        }
    }

    @SneakyThrows
    public void unfollow(String[] toUnfollow){
        //String[] toUnfollow = {"amiinedz78","josko3s","RiynK","Faridb59","9Madriida","SarahLdyHnz","_Niniiiiiiiii","ptitcafebrioche","CamilleKyrie2","WhiteKumaaa","clandestins_","sanaaells","sudiste213","El_P0w","Nadiaa_trore01","FaisEnUnAutre","bahweles","mrslindachibani","Dow_Jones2","da_wood7","hb_213","riihaaabk","arbitkt","Qatar_Happiness","EdouardBeyeme","fatimezzzahrae","mel79117","NassDurkio93","Mlle_batata","MarvinLakeers","jal___","Txrzan__","Coline_prchnt","Rv_Mathoux","94kg__","CaptainBooz7","dzzz_35","NassiBrown","graaldeuf","samyra75","tantinekimberly","ALGERIENNEDP","NB_Rp59","alalgerienneee","shhhut_","R0LK","OrLiliane","sslipknoot","BLLVCKSCARAIBES","BillionBix","Abdel_hz93","sheikas4","bulane20","PoetryLifeTimes","vivbrillant","IsYVEVO","sarabndry","Inaaya_93","fromMarsToPluto","ccldiaz","tiefadaouquoi","Le_FeuFoullet","InitialsDD_","Mio_Karaa","MystHumain24046","HappyYoSmile","IamYankeeBoy","risMOrisMO","plutonnique","PpaulBasse","Channel_sah","MorganeDns","Meliza_Elvz","baydou","na2s____","apresmarxavril","sameclc","ZacNasra","marine77290","NikiLaarson","elyyssa__1","wild4James","ia_sa66","otracopadevino","NamoryCoulibal2","IamNormanZ","MoussaXXVIIV","marinaa_77","Madison_gmty","ines86_","Vacensii","CarlaBZH","kriskonan","Mhra_57","grrmymy","LopezLibongo","kelsy_","vinsmooke_","TRK03BSN","henenetlb","Mlle_Ninoush","YacineMahfoufi","Sfrediin","MedMierda","amneziia91","SoxnaSi_","yasminemost","KToTheeN","ccbyeesh","IamMad__","Mchaumier","DatBoyKayLo","pmontp19","m2ldu952","moufiane1","Busoo_Joop","GazouilleurFou","missssjuriste","Ndoyamy","byminaaaa","Happy_Gnanchou","_vivelapolska_","Adam_Amrane","lunaameelo","kabylee21369","braulio_jcz","linamrni","HerBijiKurdi","sbnh___","Fuetardo_243","FranckCyril_","YenigunSevim","BoHamzouz17","MelissaPabisz","flanaa94","PRAISEDA8IGHT","AmiraDreams","lindamez2","dturotte","Ablaze191","jdereg13","Nicolas_WISSER","TheDrzy","thehaankee","libertashio_","bonbouaz","ziasiam","iya_mouhamed","jujuulagarce","fiaso78","demi_sword","JurgenTgt","Djzr__","21Draxlaaaa","pactu10","adriencuchet","Amandinee_prr","Dabebi_","MaryDedeur","nicco_tbo","WechHamzHamz","WhoTFYouR","_OncleDom","_izzb__","francksman","pluto299","africaanwhite"};
        int nbUnfollows = 0;
        for(String unfollowName : toUnfollow){
            boolean result = this.getTwitterClient().unfollowByName(unfollowName);
            if(result){
                nbUnfollows++;

            } else{
                LOGGER.severe(unfollowName + " not unfollowed !!");
            }
            TimeUnit.MILLISECONDS.sleep(500);
        }

        LOGGER.info(nbUnfollows + " users unfollowed with success !");
    }
}
