package com.socialmediaraiser.twittersocialgraph;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmediaraiser.twittersocialgraph.impl.GroupEnum;
import com.socialmediaraiser.twittersocialgraph.impl.JsonGraph;
import com.socialmediaraiser.twittersocialgraph.impl.UserGraph;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;


public class Launcher {

    private static final Logger LOGGER = Logger.getLogger(Launcher.class.getName());

    public static void main(String[] args) throws IOException, URISyntaxException {
        FollowerAnalyzer bot = new FollowerAnalyzer();
        HashSet<UserGraph> users = new HashSet<>();
        // List<UserGraph> users = new ArrayList<>();
        users.add(new UserGraph("MarleneSchiappa", GroupEnum.LREM));
        users.add(new UserGraph("GabrielAttal", GroupEnum.LREM));
        users.add(new UserGraph("RichardFerrand", GroupEnum.LREM));
        users.add(new UserGraph("EPhilippePM", GroupEnum.LREM));
        users.add(new UserGraph("CCastaner", GroupEnum.LREM));
        users.add(new UserGraph("mounir", GroupEnum.LREM));
        users.add(new UserGraph("StanGuerini", GroupEnum.LREM));
        users.add(new UserGraph("BGriveaux", GroupEnum.LREM));
        //  users.add(new UserGraph("AgnesCerighelli", GroupEnum.LREM));
        users.add(new UserGraph("delevoye", GroupEnum.LREM));
        users.add(new UserGraph("agnesbuzyn", GroupEnum.LREM));
        users.add(new UserGraph("SibethNdiaye", GroupEnum.LREM));
        users.add(new UserGraph("ABenalla_", GroupEnum.LREM));
        users.add(new UserGraph("GDarmanin", GroupEnum.LREM));
        users.add(new UserGraph("EmmanuelMacron", GroupEnum.LREM));
        users.add(new UserGraph("JLMelenchon", GroupEnum.EX_GAUCHE));
        users.add(new UserGraph("Francois_Ruffin", GroupEnum.EX_GAUCHE));
        users.add(new UserGraph("Clem_Autain", GroupEnum.EX_GAUCHE));
        users.add(new UserGraph("Deputee_Obono", GroupEnum.EX_GAUCHE));
        users.add(new UserGraph("alexiscorbiere", GroupEnum.EX_GAUCHE));
        users.add(new UserGraph("AQuatennens", GroupEnum.EX_GAUCHE));
        users.add(new UserGraph("IanBrossat", GroupEnum.EX_GAUCHE));
        users.add(new UserGraph("fabien_gay", GroupEnum.EX_GAUCHE));
        users.add(new UserGraph("MadjidFalastine", GroupEnum.EX_GAUCHE));
        users.add(new UserGraph("EstherBenbassa", GroupEnum.EX_GAUCHE));
        users.add(new UserGraph("UPR_Asselineau", GroupEnum.EX_GAUCHE));
        users.add(new UserGraph("MaximeCochard_", GroupEnum.EX_GAUCHE));
        users.add(new UserGraph("olbesancenot", GroupEnum.EX_GAUCHE));
        users.add(new UserGraph("GuiraudInd", GroupEnum.EX_GAUCHE));
        users.add(new UserGraph("ericcoquerel", GroupEnum.EX_GAUCHE));
        users.add(new UserGraph("benoithamon", GroupEnum.PS));
        users.add(new UserGraph("MartineAubry", GroupEnum.PS));
        users.add(new UserGraph("rglucks1", GroupEnum.PS));
        users.add(new UserGraph("faureolivier", GroupEnum.PS));
        users.add(new UserGraph("BalasGuillaume", GroupEnum.PS));
        users.add(new UserGraph("Juanico", GroupEnum.PS));
        users.add(new UserGraph("Isabel_thomasEU", GroupEnum.PS));
        users.add(new UserGraph("RemiFeraud", GroupEnum.PS));
        users.add(new UserGraph("LaurentBouvet", GroupEnum.PR));
        users.add(new UserGraph("Amk84000", GroupEnum.PR));
        users.add(new UserGraph("GillesClavreul", GroupEnum.PR));
        users.add(new UserGraph("nadine__morano", GroupEnum.LR));
        users.add(new UserGraph("LydiaGuirous", GroupEnum.LR));
        users.add(new UserGraph("ECiotti", GroupEnum.LR));
        users.add(new UserGraph("ChJacob77", GroupEnum.LR));
        users.add(new UserGraph("cestrosi", GroupEnum.LR));
        users.add(new UserGraph("vpecresse", GroupEnum.LR));
        users.add(new UserGraph("Meyer_Habib", GroupEnum.LR));
        users.add(new UserGraph("xavierbertrand", GroupEnum.LR));
        users.add(new UserGraph("valerieboyer13", GroupEnum.LR));
        users.add(new UserGraph("JulienAubert84", GroupEnum.LR));
        users.add(new UserGraph("FabienDiFilippo", GroupEnum.LR));
        users.add(new UserGraph("BrunoRetailleau", GroupEnum.LR));
        users.add(new UserGraph("datirachida", GroupEnum.LR));
        users.add(new UserGraph("jeannettebougra", GroupEnum.LR));
        users.add(new UserGraph("ClaudeGoasguen", GroupEnum.LR));
        users.add(new UserGraph("sonjoachim", GroupEnum.LR));
        users.add(new UserGraph("MLP_officiel", GroupEnum.EX_DROITE));
        users.add(new UserGraph("MarionMarechal", GroupEnum.EX_DROITE));
        users.add(new UserGraph("J_Bardella", GroupEnum.EX_DROITE));
        users.add(new UserGraph("DamienRieu", GroupEnum.EX_DROITE));
        users.add(new UserGraph("JeanMessiha", GroupEnum.EX_DROITE));
        users.add(new UserGraph("RobertMenardFR", GroupEnum.EX_DROITE));
        users.add(new UserGraph("JulienOdoul", GroupEnum.EX_DROITE));
        users.add(new UserGraph("dupontaignan", GroupEnum.EX_DROITE));
        users.add(new UserGraph("GilbertCollard", GroupEnum.EX_DROITE));
        users.add(new UserGraph("NicolasBay_", GroupEnum.EX_DROITE));
        users.add(new UserGraph("f_philippot", GroupEnum.EX_DROITE));
        users.add(new UserGraph("edwyplenel", GroupEnum.JOURNALISTES_GAUCHE));
        users.add(new UserGraph("T_Bouhafs", GroupEnum.JOURNALISTES_GAUCHE));
        users.add(new UserGraph("_MarwanMuhammad", GroupEnum.JOURNALISTES_GAUCHE));
        users.add(new UserGraph("FeizaBM", GroupEnum.JOURNALISTES_GAUCHE));
        users.add(new UserGraph("s_assbague", GroupEnum.JOURNALISTES_GAUCHE));
        users.add(new UserGraph("RemyBuisine", GroupEnum.JOURNALISTES_GAUCHE));
        users.add(new UserGraph("fabricearfi", GroupEnum.JOURNALISTES_GAUCHE));
        users.add(new UserGraph("davidperrotin", GroupEnum.JOURNALISTES_GAUCHE));
        users.add(new UserGraph("carolinedehaas", GroupEnum.JOURNALISTES_GAUCHE));
        users.add(new UserGraph("RokhayaDiallo", GroupEnum.JOURNALISTES_GAUCHE));
        users.add(new UserGraph("BelattarYassine", GroupEnum.JOURNALISTES_GAUCHE));
        users.add(new UserGraph("anatolium", GroupEnum.JOURNALISTES_GAUCHE));
        users.add(new UserGraph("N_Henin", GroupEnum.JOURNALISTES_GAUCHE));
        users.add(new UserGraph("marineturchi", GroupEnum.JOURNALISTES_GAUCHE));
        users.add(new UserGraph("ellensalvi", GroupEnum.JOURNALISTES_GAUCHE));
        users.add(new UserGraph("IdrissSihamedi", GroupEnum.JOURNALISTES_GAUCHE));
        users.add(new UserGraph("RomainCaillet", GroupEnum.JOURNALISTES_GAUCHE));
        users.add(new UserGraph("clemovitch", GroupEnum.JOURNALISTES_GAUCHE));
        users.add(new UserGraph("LeBjrTristesse", GroupEnum.JOURNALISTES_GAUCHE));
        users.add(new UserGraph("ncadene", GroupEnum.JOURNALISTES_GAUCHE));
        users.add(new UserGraph("J_Rodrigues_Off", GroupEnum.JOURNALISTES_GAUCHE));
        users.add(new UserGraph("AnasseKazib", GroupEnum.JOURNALISTES_GAUCHE));
        users.add(new UserGraph("askolovitchC", GroupEnum.JOURNALISTES_GAUCHE));
        users.add(new UserGraph("jmaphatie", GroupEnum.JOURNALISTES_GAUCHE));
        //users.add(new UserGraph("TariqRamadan", GroupEnum.JOURNALISTES_GAUCHE));
        users.add(new UserGraph("ZinebElRhazoui", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("ZohraBitan", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("ivanrioufol", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("RenaudCamus", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("GWGoldnadel", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("Enthoven_R", GroupEnum.JOURNALISTES_DROITE));
        // users.add(new UserGraph("PascalPraud", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("AlexDevecchio", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("MajidOukacha", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("SJallamion", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("Sifaoui", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("AgagBoudjahlat", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("AlloucheNader", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("julienbahloul", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("arnoklarsfeld", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("lauhaim", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("ELevyCauseur", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("LeaSalame", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("FrancisKalifat", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("frhaz", GroupEnum.JOURNALISTES_DROITE));
        //users.add(new UserGraph("BHL", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("CarolineFourest", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("W_Alhusseini", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("alexdelvalle3", GroupEnum.JOURNALISTES_DROITE));

   /*     HashSet<UserGraph> others;
        for(UserGraph user : users){
            others = new HashSet<>();
            others.add(user);
            JsonGraph result = bot.getJsonGraph(others, users);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File("public/users/"+user.getId()+".json"), result);
        } */

       /* HashSet<UserGraph> others = new HashSet<>();
        UserGraph user = new UserGraph("GuiraudInd", GroupEnum.JOURNALISTES_DROITE);
        others.add(user);
        JsonGraph result = bot.getJsonGraph(others, users);
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("public/users/"+user.getId()+".json"), result);

        bot.getJsonGraph(others, users);*/

       bot.getJsonGraph(users);
        //   bot.getCsvArray(users);
        return;
    }
}

