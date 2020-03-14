package com.socialmediaraiser.twitterbot;

import com.socialmediaraiser.twitterbot.impl.PersonalAnalyzerBot;
import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Logger;

public class PersonalAnalyzerLauncher {

    private static final Logger LOGGER = Logger.getLogger(PersonalAnalyzerLauncher.class.getName());

    public static void main(String[] args) throws IOException, InterruptedException {
        if(args.length<2){
            LOGGER.severe(()->"missing arguments, expecting 2 : ownerName[String], mode[boolean]");
        } else{
            String userName = args[0];
            boolean unfollowMode = Boolean.parseBoolean(args[1]);
            PersonalAnalyzerBot bot = new PersonalAnalyzerBot(userName);
            if(!unfollowMode){
                bot.launch(false, false, true);
            } else{
                String[] toUnfollow = {"ju_myn","Jazaslayr","aminedrif","Dz_Miraa","payass78","chickenvie225","PtitCongolais","caroulouche","_kaskou","mylenouush","Liiiiaa_","SaraTestino","yazid_b13","cmni_","Aminata_Bil","shyba_","acmjds","AriielleY","Sorahiia","ethanlldr","Loura_54","EnVeritecLouise","Ya2sOujdi","beetuussss","Al_Jawchan","Dadilazog","pandapinpo","Naaj___","escarlethvill","_esengo","youngniggaa509","T_Challa_______","Ro0__","sesouu_6","honeyjea__","balladounss","Nassimiinho","elise_decaux","rbmghr","6peperoni","Mactar_Diouf","gharbi_riad","ElheniRamia","dreezy_a","tasvuquoi","mahugnxn47","mik_251","Lvcas_B0rges","houd_t","jeremi1409","sophiaktir","Celia_Tvbti","rxbase","turc0ss","God_Schizo","Civ_225","_McHeinz","astrr__","sombrenuance","SouzRivera","ilham75019","rozciceklidal","_kdhm","issamlacisaille","Kofykof__","TheCrazy_Styler","lisaaa201","sc_934","CherryFa_","safa_fllg","queenbibou","El_Touz","Louly13_","iines_sd","edaaaa____","anissa_aln","louisedrevillon","sara_tld","DaAdOo_KhAliFaa","iDamien_H","Gaviria2_0","claralvzd","iliass_laiss","Girondinho_","noubdn","yacinhovic","GabeeetSteven","NmBoss_","niniilourson","frz_nmch","iamstmack","kylwsgd","OrlaneEllama","shdmbg","jmen_fou_","DjietoHermine","elrv92","45Gate","Jacques_ronnie","solenesoty","QueenKunta_","Lm_ZaZask0","YSerena_","solenetnn","VitianaVie","medndiayy","irinafromtheblk","rifi_shl","estel_cl","iam_hochea","ryesling","Nwl_dz","fvtihvbdi","lci_rvr","Riri_Z3","victordesmauges","monkeyDsoso","HGwendo","sugaroukht","InoriX2","LisaBretagne","marinebhh","ZakiTastyx","BaartSiim","BilalBangBang","Moha_b75","khadiija_Barry","Ahmed_Rossonero","Rimzer_","meMentionnePas_","linspic2lo","QuavoKb9","97one216","abire_e","Beleking_","ubwolves","_iLvks","FinistEver","Robinho75_","ecrink___","Chamsia_b","okemeck","axelite","malekbarcelona","undzzz_","IsaTarricone","scoborov","bokossyNjoke01","imanekkk","Ismailzerr","insssk","liseblk78","HRajja","BoubaSeck_","xNiroh","callmesheik","190516th","LeilaPruijs","Riad7ben","julienprt","Kamichto","AlphyTowowo","mehdiHmd92","pa_2non","salmaechouay","sihame_okd","TaReeum_","dypfzz","Sneezy__m","Reealbibi","AxelTeddyG","_ShaMokonzi_","onekk_","islamo_rebeu","loserxoftheyear","GiuseppeParavi1","YassJoestar","AntoninEdoire","juju_vyrr","Gabriel_Bleron","Youssmks","itaelmrtn","nikomouuk","diopsenpai","l_institE","souf_belkadi","meeruemm","RyadBenaidji","Jujudicael_","_emna__m","TY970","dilki87","NWAR_ludba","mxdou_","s_blma","gustavo185_","Luffy_Affranchi","Kadidia92","_richhrich","Giovannidjossou","JaiUnAvis","abdelhvmid","Kenzaa__s","OneblueTeam","Femme_Saiyan","alizaudinooot","Mdmu_","cccestmoii","SherihanB","BatsLesHanches","ZakAitOuaid","VictorLefranc","Irfane_Clement","NaelABC","vivi83032363","Sarah_Bcf","Asiorak","KayiTurk_fr","caroletonneau","InesInesz"};
                String[] whiteList = {"InesInesz","caroletonneau","KayiTurk_fr","Asiorak","Sarah_Bcf","vivi83032363",
                        "Gabriel_Bleron","NaelABC","Ciudadana93","Irfane_Clement","OneblueTeam","Luffy_Affranchi","silversilvery",
                        "abdelhvmid","souf_belkadi","GiuseppeParavi1","Riad7ben","YSerena_","Celia_Tvbti","sombrenuance",
                        "salmaechouay","VictorLefranc"};
                bot.unfollow(toUnfollow, whiteList);
            }
        }
    }


}
