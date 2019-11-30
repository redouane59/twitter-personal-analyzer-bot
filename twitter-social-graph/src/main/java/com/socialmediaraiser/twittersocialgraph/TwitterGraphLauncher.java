package com.socialmediaraiser.twittersocialgraph;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.HashSet;
import java.util.logging.Logger;


public class TwitterGraphLauncher {

    private static final Logger LOGGER = Logger.getLogger(TwitterGraphLauncher.class.getName());

    public static void main(String[] args) throws JsonProcessingException {
        FollowerAnalyzer bot = new FollowerAnalyzer("RedouaneBali", false, false);
        HashSet<FollowerAnalyzer.UserGraph> users = new HashSet<>();
        users.add(new FollowerAnalyzer.UserGraph("MarleneSchiappa",3));
        users.add(new FollowerAnalyzer.UserGraph("RichardFerrand",3));
        users.add(new FollowerAnalyzer.UserGraph("EPhilippePM",3));
        users.add(new FollowerAnalyzer.UserGraph("CCastaner",3));
        users.add(new FollowerAnalyzer.UserGraph("mounir",3));
        users.add(new FollowerAnalyzer.UserGraph("StanGuerini",3));
        users.add(new FollowerAnalyzer.UserGraph("BGriveaux",3));
        //users.add(new UserGraph("JLMelenchon",1));
        users.add(new FollowerAnalyzer.UserGraph("Francois_Ruffin",1));
        users.add(new FollowerAnalyzer.UserGraph("Clem_Autain",1));
        users.add(new FollowerAnalyzer.UserGraph("Deputee_Obono",1));
        users.add(new FollowerAnalyzer.UserGraph("alexiscorbiere",1));
        users.add(new FollowerAnalyzer.UserGraph("IanBrossat",1));
        users.add(new FollowerAnalyzer.UserGraph("fabien_gay",1));
        users.add(new FollowerAnalyzer.UserGraph("MadjidFalastine",1));
        users.add(new FollowerAnalyzer.UserGraph("EstherBenbassa",1));
       // users.add(new UserGraph("benoithamon",2));
        users.add(new FollowerAnalyzer.UserGraph("MartineAubry",2));
        users.add(new FollowerAnalyzer.UserGraph("rglucks1",2));
        users.add(new FollowerAnalyzer.UserGraph("faureolivier",2));
        users.add(new FollowerAnalyzer.UserGraph("BalasGuillaume",2));
        users.add(new FollowerAnalyzer.UserGraph("Juanico",2));
        users.add(new FollowerAnalyzer.UserGraph("Isabel_thomasEU",2));
        users.add(new FollowerAnalyzer.UserGraph("LaurentBouvet",4));
        users.add(new FollowerAnalyzer.UserGraph("Amk84000",4));
        users.add(new FollowerAnalyzer.UserGraph("GillesClavreul",4));
        users.add(new FollowerAnalyzer.UserGraph("nadine__morano",5));
        users.add(new FollowerAnalyzer.UserGraph("LydiaGuirous",5));
        users.add(new FollowerAnalyzer.UserGraph("ECiotti",5));
        users.add(new FollowerAnalyzer.UserGraph("ChJacob77",5));
        users.add(new FollowerAnalyzer.UserGraph("cestrosi",5));
     //   users.add(new UserGraph("MLP_officiel",6));
        users.add(new FollowerAnalyzer.UserGraph("MarionMarechal",6));
        users.add(new FollowerAnalyzer.UserGraph("J_Bardella",6));
        users.add(new FollowerAnalyzer.UserGraph("DamienRieu",6));
        users.add(new FollowerAnalyzer.UserGraph("JeanMessiha",6));
        users.add(new FollowerAnalyzer.UserGraph("RobertMenardFR",6));
        users.add(new FollowerAnalyzer.UserGraph("JulienOdoul",6));
        users.add(new FollowerAnalyzer.UserGraph("ZinebElRhazoui",7));
        users.add(new FollowerAnalyzer.UserGraph("ZohraBitan",7));
        users.add(new FollowerAnalyzer.UserGraph("ivanrioufol",7));
        users.add(new FollowerAnalyzer.UserGraph("RenaudCamus",7));
        users.add(new FollowerAnalyzer.UserGraph("GWGoldnadel",7));
        users.add(new FollowerAnalyzer.UserGraph("Enthoven_R",7));
        users.add(new FollowerAnalyzer.UserGraph("PascalPraud",7));
        users.add(new FollowerAnalyzer.UserGraph("AlexDevecchio",7));
        users.add(new FollowerAnalyzer.UserGraph("MajidOukacha",7));
        users.add(new FollowerAnalyzer.UserGraph("SJallamion",7));
   //     users.add(new UserGraph("edwyplenel",8));
        users.add(new FollowerAnalyzer.UserGraph("epelboin",8));
        users.add(new FollowerAnalyzer.UserGraph("T_Bouhafs",8));
        users.add(new FollowerAnalyzer.UserGraph("_MarwanMuhammad",8));
        users.add(new FollowerAnalyzer.UserGraph("FeizaBM",8));
        users.add(new FollowerAnalyzer.UserGraph("s_assbague",8));
        users.add(new FollowerAnalyzer.UserGraph("RemyBuisine",8));
        users.add(new FollowerAnalyzer.UserGraph("fabricearfi",8));
        users.add(new FollowerAnalyzer.UserGraph("davidperrotin",8));
        users.add(new FollowerAnalyzer.UserGraph("carolinedehaas",8));
        users.add(new FollowerAnalyzer.UserGraph("RokhayaDiallo",8));
        users.add(new FollowerAnalyzer.UserGraph("BelattarYassine",8));
        users.add(new FollowerAnalyzer.UserGraph("anatolium",8));
        //   users.add(new UserGraph("AgagBoudjahlat",7));
        // Thinker_View
        bot.getJsonGraph(users);
    }
}
