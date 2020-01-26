package com.socialmediaraiser.twitterbot;

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
            TwitterBotByInfluencers bot = new TwitterBotByInfluencers(userName, false, true);
        /*    Map<String, Integer> interractions = bot.getNbInterractions("2019-09-24", userName);
            List<AbstractUser> followers = bot.getTwitterHelper().getFollowingsUsers(bot.getTwitterHelper().getUserFromUserName(userName).getId());
            for(AbstractUser user : followers){
                user.setNbInteractions(interractions.getOrDefault(user.getId(),0));
                // add RT and/or likes
                bot.getIoHelper().addNewFollowerLineSimple(user);
                TimeUnit.MILLISECONDS.sleep(700);
                LOGGER.info("adding " + user.getUsername() + "...");
            }
            LOGGER.info("finish with success");*/


            String[] toUnfollow = {"moonlvghtt","BroSalifKilla","massi934","sarah_tffr","xavyflor","AZouzou69","Jey_kosi243","selooumaa","lbenbnine","user_yass","jmenblcptdr","IvoirienSympa","_dlrxacrgl_","DiMachaviel21","Safwane212","ynaiamaya","wildwolfnews","Inapercue","efoulon1","ZonujaySan_2p","cestdz","Bnfyou","tissemeboss","Zak_man93","JndIchetraki","54Yanbar","mohamed_elhajj","Myriame630","christian300658","LeilaXHunter","13010H","baljeetkaur900","Anne_L0rii","meguyyyyy","xi_IMPULSE","myrabeelle","unez19","saaaliiihaaa","fatselzr","SpriXIII","ab_djigal","destailleurs","soraya1eredunom","Moha2Ketama","Pepitaslamd","ar_Riffi","Lawbna","ComteMaintenon","KndD_","soudjzr06","Navy4Liiife","AbdoulayeSall01","LaigleDuNorth","_porlanoche","randakbch","losako243","mlkmnl31","DanielSoulama","Ludo9498","Mehboula_","gaambass","LeCommentateu12","youyou2A","ThaRealMitch","SALIOU6_M","nadial2511","3amoTony","mike_cdl","riam_ad","wildeandciea","raayaane75","widadm212","Hillamba","fatima_bent","DabreIsm","jibijumbo","jsmnzk","nxrova_2","DwayneSama","shaneservr","lenaa_soso","tnss__","biib_06","JeromeAttalOff","EditionSpecial2","SebNormand76","Davilson93i","rimkvss","hugwharold","AlexisNoiron","_Carine_C","agadiroisee","cedric_4real","putainzebi_","Younsbou","MiniiBN_","Turkoss_","3labalek","tifosi76","Hassouuuun","stl_gbr","DMougeon","abde_alg","ImenLaDanse","Ymbld92","asmakzrn","mahautldz","katsaandeye","anth30o","le_sslyn","_chahinezeDz","leilooooooo","petimiti","tuconnaispsliza","yohan375","Tama_Frasich","Jikzer","T_Malvezin","DalBndl","Chems_Sahili","jfgangsta","san0ou","Samyra_PECORY","Fatchouha71","nina92tn","Rodouawn","DhikronEnsemble","Nami_zle","RizouC2t","larouquiineGi","Sorayaaa_216","jocelynedongoh","Docey6","onlyeds","alban_yoann","yellanamontana","medlihtam__","aysianj_","Jacxson__","TropViive","So_Abiba","mhb2_i","arsene_baley","iamdcrz","Words_failed","carrel_tob9","tvnisiana","aymenGTAV","kelahlws","AllhassaneSimon","fiers2nous","flm2tt","faithendek","afo_galle","S_Kbreizh","iissm35","ousseynou__baye","SUSIC2CS","sam_ray07","fatimakk_","Mehdicament28","Serrrawin","sovndes","RobZabra","enkaa_b","hype72","mohamed_abdoul","marianamndes","MMassiMM","ShaeCald1","Mamounette0512","balla_221","amrovic_","ITACHIDZ38","3arbi69","iProtego","212weldRbat","ninaa2107","NepheritesRain","36cerveau","TiParisien440","Josi_Jaber","stz216","Mirouche_","loubna78700","madein986","Baswa_","Lenalvtt","soumnarif_","Jordaan4real","ouardiadz13","bellavita__","bnlaes","2h13_","Alfoussen_94","98DRUGOFSHAWN","mcdoftdiego","LAsBarcelonaise","HlcChariisme","gbhjkv","AngeNabil","JLng_84","inaaDjb","Cro__co","snkm_","LenPharaon","ineessshm","alainparis142","sss_sirine","nwlkrt","lulaurent13","lc_pinknbt","mwbloem","Tounsia__93","seroplex_5mg","Kvdy00","Naza_Congolais_","Sbl_xb","jspencoretg","_westernie","Fumier_10","Hamzooo_Amg","Sombre__Negro","NisSouCote","sademaf","TDOH666","YeahCallSiggy","DzPower_28","i_morotsir","Thib_LP","93_220","ryanafoutre","Jahestbon","Gxlx_","OneThugOneStyle","Auroraaa_972","johnsonights","nawel_pxris","Alg_00213","Bigheart357","Sarraa____","Igirly_","S_OUKii","james_honore","_IsmaDar","bilkis__","chameau213","migo931","sxlxfxx","YChaumien","wyslaurent","KayiTurk_fr","wshci","youboox","Marine_Onfroy","ctricot","MelLmne","JsuisPasLa_"};
            int nbUnfollows = 0;
            for(String unfollowName : toUnfollow){
                boolean result = bot.getTwitterHelper().unfollowByName(unfollowName);
                if(result){
                    nbUnfollows++;
                    LOGGER.info(unfollowName + " unfollowed...");
                } else{
                    LOGGER.severe(unfollowName + " not unfollowed !!");
                }
                TimeUnit.MILLISECONDS.sleep(500);
            }

            LOGGER.info(nbUnfollows + " users unfollowed with success !");

        }
    }


}
