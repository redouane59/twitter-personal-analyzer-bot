package com.socialmediaraiser.twitterbot;

import com.socialmediaraiser.twitterbot.impl.PersonalAnalyzerBot;
import com.socialmediaraiser.twitterbot.impl.TwitterBotByInfluencers;

import java.io.IOException;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PersonalAnalyzerLauncher {

    private static final Logger LOGGER = Logger.getLogger(PersonalAnalyzerLauncher.class.getName());

    public static void main(String[] args) throws IOException, ParseException, InterruptedException {
        if(args.length<1){
            LOGGER.severe(()->"missing arguments, expecting 1 : ownerName[String]");
        } else{
            String userName = args[0];
            PersonalAnalyzerBot bot = new PersonalAnalyzerBot(userName);
         //   bot.launch();

            String[] toUnfollow = {"amiinedz78","josko3s","RiynK","Faridb59","9Madriida","SarahLdyHnz","_Niniiiiiiiii","ptitcafebrioche","CamilleKyrie2","WhiteKumaaa","clandestins_","sanaaells","sudiste213","El_P0w","Nadiaa_trore01","FaisEnUnAutre","bahweles","mrslindachibani","Dow_Jones2","da_wood7","hb_213","riihaaabk","arbitkt","Qatar_Happiness","EdouardBeyeme","fatimezzzahrae","mel79117","NassDurkio93","Mlle_batata","MarvinLakeers","jal___","Txrzan__","Coline_prchnt","Rv_Mathoux","94kg__","CaptainBooz7","dzzz_35","NassiBrown","graaldeuf","samyra75","tantinekimberly","ALGERIENNEDP","NB_Rp59","alalgerienneee","shhhut_","R0LK","OrLiliane","sslipknoot","BLLVCKSCARAIBES","BillionBix","Abdel_hz93","sheikas4","bulane20","PoetryLifeTimes","vivbrillant","IsYVEVO","sarabndry","Inaaya_93","fromMarsToPluto","ccldiaz","tiefadaouquoi","Le_FeuFoullet","InitialsDD_","Mio_Karaa","MystHumain24046","HappyYoSmile","IamYankeeBoy","risMOrisMO","plutonnique","PpaulBasse","Channel_sah","MorganeDns","Meliza_Elvz","baydou","na2s____","apresmarxavril","sameclc","ZacNasra","marine77290","NikiLaarson","elyyssa__1","wild4James","ia_sa66","otracopadevino","NamoryCoulibal2","IamNormanZ","MoussaXXVIIV","marinaa_77","Madison_gmty","ines86_","Vacensii","CarlaBZH","kriskonan","Mhra_57","grrmymy","LopezLibongo","kelsy_","vinsmooke_","TRK03BSN","henenetlb","Mlle_Ninoush","YacineMahfoufi","Sfrediin","MedMierda","amneziia91","SoxnaSi_","yasminemost","KToTheeN","ccbyeesh","IamMad__","Mchaumier","DatBoyKayLo","pmontp19","m2ldu952","moufiane1","Busoo_Joop","GazouilleurFou","missssjuriste","Ndoyamy","byminaaaa","Happy_Gnanchou","_vivelapolska_","Adam_Amrane","lunaameelo","kabylee21369","braulio_jcz","linamrni","HerBijiKurdi","sbnh___","Fuetardo_243","FranckCyril_","YenigunSevim","BoHamzouz17","MelissaPabisz","flanaa94","PRAISEDA8IGHT","AmiraDreams","lindamez2","dturotte","Ablaze191","jdereg13","Nicolas_WISSER","TheDrzy","thehaankee","libertashio_","bonbouaz","ziasiam","iya_mouhamed","jujuulagarce","fiaso78","demi_sword","JurgenTgt","Djzr__","21Draxlaaaa","pactu10","adriencuchet","Amandinee_prr","Dabebi_","MaryDedeur","nicco_tbo","WechHamzHamz","WhoTFYouR","_OncleDom","_izzb__","francksman","pluto299","africaanwhite"};
            int nbUnfollows = 0;
            for(String unfollowName : toUnfollow){
                boolean result = bot.getTwitterClient().unfollowByName(unfollowName);
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


}
