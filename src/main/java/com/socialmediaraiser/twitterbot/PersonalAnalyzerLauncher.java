package com.socialmediaraiser.twitterbot;

import com.socialmediaraiser.twitterbot.impl.PersonalAnalyzerBot;
import java.io.IOException;
import lombok.CustomLog;

@CustomLog
public class PersonalAnalyzerLauncher {

  public static void main(String[] args){
    if (args.length < 2) {
      LOGGER.severe(() -> "missing arguments");
    } else {
      String              userName         = args[0];
      boolean             unfollowMode     = Boolean.parseBoolean(args[1]);
      if (!unfollowMode) {
        if (args.length < 5) LOGGER.severe(() -> "missing arguments");
        boolean includeFollowers        = Boolean.parseBoolean(args[2]);
        boolean includeFollowings       = Boolean.parseBoolean(args[3]);
        boolean onlyFollowBackFollowers = Boolean.parseBoolean(args[4]);
        String tweetArchivePath         = args[5];
        PersonalAnalyzerBot bot         = new PersonalAnalyzerBot(userName, tweetArchivePath);
        bot.launch(includeFollowers, includeFollowings, onlyFollowBackFollowers);
      } else {
        String[] toUnfollow = {
            "DzzzLife","No_nam7","yassbdr","basicallyazz","LaurenceDudek","amyna133","Mel_inna91","Lauryne_Bln","FettyB7","DZ_3333","Bearmaamaa","hogwallifrey","cndsofyan","lasyn_","salmozorus","dy_nael","n__chn","rasha_rashouu","lucakows","x6testy","Moha00213","teamsuspens","_JalanSabar","tounsi_sf78","Laauryn_A","Stv_Barksdale","OceaneLovely","Youssouf__","KellyJennifer23","NoRatedTho","lspc__","bdjbt_","MaryamSabour12","213xm30","Jalilaouicmoi","l6ndy","aaron_is_bless","iliesElLoco","camilia953","ClaireNouria","PocahontasInes","Silia_Wind","A_to_the_ssia","Dylou003","imnlazone","unebinks","Algeriiennne92","AminLocoLoco","wanhedaryl","mayiswift","anissaatc","myanonyme","__kwssif","blackie9270","Sam__N_","Bilouu135_","nvss45","UnBaron_","khseiaram","anahyame","gmsandrea","sunnami31","emilieeesan","dz_srh93","Loubna_213","Benzaldo_","Vi14_","Le_Khal_","amiraaf13","AbdouSak10","tjrs_cool","mazigova","__iamnouu__","helina_ahmd","CallMequeenBieb","FerTayeb","Sahbi_Egrs","Sosofia06","maaaaissaa","etherealMSCL","KajolYuuss","Yasmine0804","joaaanneeeee","heavenwithxu","ImaneKns","linaaccessiblee","Fteh28","melkss_","charlotteevd","Solenend","KryksCdm","Nordinebkff","habibgon","mfdxbv","Imane_Kcm","ganguek_","ed_slr","coralieglb","tdlsj","catsaretrash","ChadiasarahC","alistf_","Simisco_","RaheemSterlings","LironaBerisha","alikamaty","TheNayrod","BilelMnasri","Lapegreloveya","khadyybdj","Maayssou","vucko_nina","SimoesLeaaa","Mkcnsk","isauremrcier","Messanee_","_mehmeeet","m_ineees","Valen8616","cikldn","Adriii_Wtf","camikazz__","mamimarone","n_cherryy","40_thieves_","pamplemous57","1Gars_O_Soleil2","nawslarif","krm_insaf","Faayda_Abdillah","Hffddaa","_kingluqmvn","nissa156","Unassumed_","RimeSahbi","OhKila","drissleretour","Catherineee_bhm","fendabeaute","Lulu97two","noodlejd","Kahina019","___azhr","SabinaLodbrok","fayce_b","kamso2times","t2712_iman","Khdjilli","fatkfih","leahhspp","kenza2122","yooba_ccxxi","simonagnoletti","Amandouche","Nono_1704","Offlaisse310_","TayebKospa","kanieloutis____","AminaHassen12","M_AZ93","chenis95","broly_210","Win__mood","Andily_Ibra","_Dalinaa","FrenchieAC","FabAdou","nekomamushi971","elbatataa","nwll_i","YC_945","turquirem","YSerena_","_xb2x6___","Celia_Tvbti","SecouToure","Letrendsetter","SaraLeenHayder","Mouradson","Flow91220","blaugrana_29","Luffy_Affranchi","mohamed_elhajj","Irfane_Clement","DadouDads","olivialiyung","Lillustre_Dz","Asiorak","NaelABC","NC_69_","GiuseppeParavi1","Sarah_Bcf","One4Dz","bonbouaz","Assia35583911","mohamedmoh59","shmnubia","rk_Ngadi","InesInesz","julien_Gres"
        };
        String[] whiteList = {"mohamedmoh59","NC_69_","DadouDads","fayce_b", "InesInesz", "caroletonneau", "KayiTurk_fr", "Asiorak", "Sarah_Bcf", "vivi83032363"
            , "Gabriel_Bleron", "NaelABC", "Ciudadana93", "Irfane_Clement", "OneblueTeam", "Luffy_Affranchi", "silversilvery",
                              "abdelhvmid", "souf_belkadi", "GiuseppeParavi1", "Riad7ben", "YSerena_", "Celia_Tvbti", "sombrenuance",
                              "salmaechouay", "VictorLefranc", "julien_Gres", "rk_Ngadi", "FrenchieAC", "OtmanBsh", "sof1aninho",
                              "SaraLeenHayder", "Mouradson", "OphelieEdb", "Naouffel","1Gars_O_Soleil2"};
        PersonalAnalyzerBot bot              = new PersonalAnalyzerBot(userName);
        bot.unfollow(toUnfollow, whiteList);
      }
    }
  }


}
