package com.socialmediaraiser.twittersocialgraph;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.socialmediaraiser.twittersocialgraph.impl.GroupEnum;
import com.socialmediaraiser.twittersocialgraph.impl.UserGraph;

import java.util.HashSet;
import java.util.logging.Logger;


public class Launcher {

    private static final Logger LOGGER = Logger.getLogger(Launcher.class.getName());

    public static void main(String[] args) throws JsonProcessingException {
        FollowerAnalyzer bot = new FollowerAnalyzer("RedouaneBali");
        HashSet<UserGraph> users = new HashSet<>();
        users.add(new UserGraph("MarleneSchiappa", GroupEnum.LREM));
        users.add(new UserGraph("GabrielAttal", GroupEnum.LREM));
        users.add(new UserGraph("RichardFerrand", GroupEnum.LREM));
        users.add(new UserGraph("EPhilippePM", GroupEnum.LREM));
        users.add(new UserGraph("CCastaner", GroupEnum.LREM));
        users.add(new UserGraph("mounir", GroupEnum.LREM));
        users.add(new UserGraph("StanGuerini", GroupEnum.LREM));
        users.add(new UserGraph("BGriveaux", GroupEnum.LREM));
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
        users.add(new UserGraph("benoithamon", GroupEnum.PS));
        users.add(new UserGraph("MartineAubry", GroupEnum.PS));
        users.add(new UserGraph("rglucks1", GroupEnum.PS));
        users.add(new UserGraph("faureolivier", GroupEnum.PS));
        users.add(new UserGraph("BalasGuillaume", GroupEnum.PS));
        users.add(new UserGraph("Juanico", GroupEnum.PS));
        users.add(new UserGraph("Isabel_thomasEU", GroupEnum.PS));
        users.add(new UserGraph("LaurentBouvet", GroupEnum.PR));
        users.add(new UserGraph("Amk84000", GroupEnum.PR));
        users.add(new UserGraph("GillesClavreul", GroupEnum.PR));
        users.add(new UserGraph("nadine__morano", GroupEnum.LR));
        users.add(new UserGraph("LydiaGuirous", GroupEnum.LR));
        users.add(new UserGraph("ECiotti", GroupEnum.LR));
        users.add(new UserGraph("ChJacob77", GroupEnum.LR));
        users.add(new UserGraph("cestrosi", GroupEnum.LR));
        users.add(new UserGraph("vpecresse", GroupEnum.LR));
        users.add(new UserGraph("MLP_officiel", GroupEnum.EX_DROITE));
        users.add(new UserGraph("MarionMarechal", GroupEnum.EX_DROITE));
        users.add(new UserGraph("J_Bardella", GroupEnum.EX_DROITE));
        users.add(new UserGraph("DamienRieu", GroupEnum.EX_DROITE));
        users.add(new UserGraph("JeanMessiha", GroupEnum.EX_DROITE));
        users.add(new UserGraph("RobertMenardFR", GroupEnum.EX_DROITE));
        users.add(new UserGraph("JulienOdoul", GroupEnum.EX_DROITE));
        users.add(new UserGraph("ZinebElRhazoui", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("ZohraBitan", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("ivanrioufol", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("RenaudCamus", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("GWGoldnadel", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("Enthoven_R", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("PascalPraud", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("AlexDevecchio", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("MajidOukacha", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("SJallamion", GroupEnum.JOURNALISTES_DROITE));
        users.add(new UserGraph("AgagBoudjahlat", GroupEnum.JOURNALISTES_DROITE));
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
        // Thinker_View
        bot.getJsonGraph(users);
    }
}
