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
import lombok.Data;
import lombok.SneakyThrows;
import org.apache.commons.lang.time.DateUtils;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Data
public class PersonalAnalyzerBot {

    private String userName;
    private static final Logger LOGGER = Logger.getLogger(PersonalAnalyzerLauncher.class.getName());
    private AbstractIOHelper ioHelper;
    private TwitterClient twitterClient = new TwitterClient();

    public PersonalAnalyzerBot(String userName){
        this.userName = userName;
        this.ioHelper = new GoogleSheetHelper(userName);
    }
    public void launch() throws IOException, ParseException, InterruptedException {
        Map<String, Integer> interractions = this.getNbInterractions(ConverterHelper.getDateFromString("20200101"));
        String userId = this.twitterClient.getUserFromUserName(userName).getId();
        List<IUser> followingsUsers = this.twitterClient.getFollowingsUsers(userId);
        for(IUser user : followingsUsers){
            User customUser = new User(user);
            customUser.setNbInteractions(interractions.getOrDefault(user.getId(),0));
            // add RT and/or likes
            if(this.twitterClient.getRelationType(userId, customUser.getId()).equals(RelationType.FRIENDS)){
                this.ioHelper.addNewFollowerLineSimple(customUser); // @todo how to manage better the limit ? Less calls to implement
                TimeUnit.MILLISECONDS.sleep(600);
                LOGGER.info("adding " + user.getName() + "...");
            } else{
                LOGGER.info("NOT adding " + user.getName() + " (not following back)");
            }
        }
        LOGGER.info("finish with success");
    }

    public Map<String, Integer> getNbInterractions(Date initDate) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("tweet.json").getFile());
        List<TweetDataDTO> tweets = twitterClient.readTwitterDataFile(file);
        // @todo add condition on followers
        Map<String, Integer> result = new HashMap<>();
        Date tweetDate;
        int repliesGivenCount = 0;
        int repliesReceivedCount = 0;
        int retweetCount = 0;
        for(TweetDataDTO tweetDataDTO : tweets){
            ITweet tweet = tweetDataDTO.getTweet();
            // checking the reply I gave to other users
            String inReplyUserId = tweet.getInReplyToUserId();
            tweetDate = tweet.getCreatedAt();
            if(inReplyUserId!=null){
                if(tweetDate!=null && tweetDate.compareTo(initDate)>0) {
                    result.put(inReplyUserId, 1+result.getOrDefault(inReplyUserId, 0));
                    repliesGivenCount++;
                }
            }
            if(tweetDate!=null && tweetDate.compareTo(initDate)>0){
                // checking the user who retweeted me
                if(tweetDataDTO.getTweet().getRetweetCount()>0){
                    List<String> retweeterIds = this.twitterClient.getRetweetersId(tweetDataDTO.getTweet().getId());
                    for(String retweeterId : retweeterIds){
                        result.put(retweeterId, 1+result.getOrDefault(retweeterId, 0));
                        retweetCount++;
                    }
                }
            }
        }

        System.out.println(result.size() + " users found : " + repliesGivenCount + " given replies & " + retweetCount + " retweets");

        Date currentDate = ConverterHelper.minutesBeforeNow(60);
        // 10 requests for 10 weeks
        for(int i=1; i<=10;i++){
            Date fromDate = DateUtils.addDays(currentDate,-2);
            // checking the reply other gave me (40 days)
            List<ITweet> tweetWithReplies = this.twitterClient.searchForTweetsWithin30days("@"+userName,
                    DateUtils.truncate(fromDate, Calendar.HOUR),
                    DateUtils.truncate(currentDate, Calendar.HOUR));
            for(ITweet tweet : tweetWithReplies){
                if(!tweet.getText().contains("RT")){
                    result.put(tweet.getUser().getId(), 1+result.getOrDefault(tweet.getUser().getId(), 0));
                    repliesReceivedCount++;
                }
                currentDate = tweet.getCreatedAt();
            }
        }
        System.out.println(result.size() + " users found : "
                + repliesGivenCount + " given replies & "
                + retweetCount + " retweets & "
                + repliesReceivedCount + " received replies");
        return result;
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
