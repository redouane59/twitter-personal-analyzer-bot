package com.socialmediaraiser.twittersocialgraph;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.HashSet;
import java.util.logging.Logger;

import static com.socialmediaraiser.twittersocialgraph.FollowerAnalyzer.*;


public class TwitterGraphLauncher {

    private static final Logger LOGGER = Logger.getLogger(TwitterGraphLauncher.class.getName());

    public static void main(String[] args) throws JsonProcessingException {
        FollowerAnalyzer bot = new FollowerAnalyzer("RedTheOne");
        HashSet<UserGraph> users = new HashSet<>();
        users.add(new UserGraph("MarleneSchiappa",3));
        users.add(new UserGraph("RichardFerrand",3));
        users.add(new UserGraph("EPhilippePM",3));
        users.add(new UserGraph("CCastaner",3));
        users.add(new UserGraph("mounir",3));
        users.add(new UserGraph("StanGuerini",3));
        users.add(new UserGraph("BGriveaux",3));
        users.add(new UserGraph("JLMelenchon",1));
        users.add(new UserGraph("Francois_Ruffin",1));
        users.add(new UserGraph("Clem_Autain",1));
        users.add(new UserGraph("Deputee_Obono",1));
        users.add(new UserGraph("alexiscorbiere",1));
        users.add(new UserGraph("IanBrossat",1));
        users.add(new UserGraph("fabien_gay",1));
        users.add(new UserGraph("MadjidFalastine",1));
        users.add(new UserGraph("EstherBenbassa",1));
        users.add(new UserGraph("benoithamon",2));
        users.add(new UserGraph("MartineAubry",2));
        users.add(new UserGraph("rglucks1",2));
        users.add(new UserGraph("faureolivier",2));
        users.add(new UserGraph("BalasGuillaume",2));
        users.add(new UserGraph("Juanico",2));
        users.add(new UserGraph("Isabel_thomasEU",2));
        users.add(new UserGraph("LaurentBouvet",4));
        users.add(new UserGraph("Amk84000",4));
        users.add(new UserGraph("GillesClavreul",4));
        users.add(new UserGraph("nadine__morano",5));
        users.add(new UserGraph("LydiaGuirous",5));
        users.add(new UserGraph("ECiotti",5));
        users.add(new UserGraph("ChJacob77",5));
        users.add(new UserGraph("cestrosi",5));
        users.add(new UserGraph("MLP_officiel",6));
        users.add(new UserGraph("MarionMarechal",6));
        users.add(new UserGraph("J_Bardella",6));
        users.add(new UserGraph("DamienRieu",6));
        users.add(new UserGraph("JeanMessiha",6));
        users.add(new UserGraph("RobertMenardFR",6));
        users.add(new UserGraph("JulienOdoul",6));
        users.add(new UserGraph("ZinebElRhazoui",7));
        users.add(new UserGraph("ZohraBitan",7));
        users.add(new UserGraph("ivanrioufol",7));
        users.add(new UserGraph("RenaudCamus",7));
        users.add(new UserGraph("GWGoldnadel",7));
        users.add(new UserGraph("Enthoven_R",7));
        users.add(new UserGraph("PascalPraud",7));
        users.add(new UserGraph("AlexDevecchio",7));
        users.add(new UserGraph("MajidOukacha",7));
        users.add(new UserGraph("SJallamion",7));
        users.add(new UserGraph("edwyplenel",8));
        users.add(new UserGraph("epelboin",8));
        users.add(new UserGraph("T_Bouhafs",8));
        users.add(new UserGraph("_MarwanMuhammad",8));
        users.add(new UserGraph("FeizaBM",8));
        users.add(new UserGraph("s_assbague",8));
        users.add(new UserGraph("RemyBuisine",8));
        users.add(new UserGraph("fabricearfi",8));
        users.add(new UserGraph("davidperrotin",8));
        users.add(new UserGraph("carolinedehaas",8));
        users.add(new UserGraph("RokhayaDiallo",8));
        users.add(new UserGraph("BelattarYassine",8));
        users.add(new UserGraph("anatolium",8));
        users.add(new UserGraph("AgagBoudjahlat",7));
        // Thinker_View
        bot.getJsonGraph(users);
    }
}
