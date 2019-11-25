package com.socialmediaraiser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.socialmediaraiser.twitter.impl.FollowerAnalyzer;

import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

public class TwitterGraphLauncher {

    private static final Logger LOGGER = Logger.getLogger(TwitterGraphLauncher.class.getName());

    public static void main(String[] args) throws JsonProcessingException {
        FollowerAnalyzer bot = new FollowerAnalyzer("RedouaneBali", false, false);
        List<String> mel = bot.getFollowerIds(bot.getUserFromUserName("JLMelenchon").getId());
        HashSet<FollowerAnalyzer.UserGraph> users = new HashSet<>();
        users.add(new FollowerAnalyzer.UserGraph("LaurentBouvet",1));
        users.add(new FollowerAnalyzer.UserGraph("Amk84000",1));
        users.add(new FollowerAnalyzer.UserGraph("GillesClavreul",1));
        users.add(new FollowerAnalyzer.UserGraph("ZinebElRhazoui",2));
        users.add(new FollowerAnalyzer.UserGraph("ZohraBitan",2));
        users.add(new FollowerAnalyzer.UserGraph("ivanrioufol",2));
        users.add(new FollowerAnalyzer.UserGraph("RenaudCamus",2));
        users.add(new FollowerAnalyzer.UserGraph("GWGoldnadel",2));
        users.add(new FollowerAnalyzer.UserGraph("edwyplenel",2));
        users.add(new FollowerAnalyzer.UserGraph("fabricearfi",2));
        users.add(new FollowerAnalyzer.UserGraph("davidperrotin",2));
        users.add(new FollowerAnalyzer.UserGraph("carolinedehaas",2));
        users.add(new FollowerAnalyzer.UserGraph("RokhayaDiallo",2));
        users.add(new FollowerAnalyzer.UserGraph("epelboin",2));
        users.add(new FollowerAnalyzer.UserGraph("BHL",2));
        users.add(new FollowerAnalyzer.UserGraph("Enthoven_R",2));
        users.add(new FollowerAnalyzer.UserGraph("PascalPraud",2));
        //   users.add(new FollowerAnalyzer.UserGraph("AgagBoudjahlat",2));
        users.add(new FollowerAnalyzer.UserGraph("MLP_officiel",3));
        users.add(new FollowerAnalyzer.UserGraph("MarionMarechal",3));
        users.add(new FollowerAnalyzer.UserGraph("J_Bardella",3));
        users.add(new FollowerAnalyzer.UserGraph("DamienRieu",3));
        users.add(new FollowerAnalyzer.UserGraph("JeanMessiha",3));
        users.add(new FollowerAnalyzer.UserGraph("RobertMenardFR",3));
        users.add(new FollowerAnalyzer.UserGraph("JulienOdoul",3));
        users.add(new FollowerAnalyzer.UserGraph("JLMelenchon",4));
        users.add(new FollowerAnalyzer.UserGraph("Francois_Ruffin",4));
        users.add(new FollowerAnalyzer.UserGraph("Clem_Autain",4));
        users.add(new FollowerAnalyzer.UserGraph("Deputee_Obono",4));
        users.add(new FollowerAnalyzer.UserGraph("alexiscorbiere",4));
        users.add(new FollowerAnalyzer.UserGraph("IanBrossat",4));
        users.add(new FollowerAnalyzer.UserGraph("fabien_gay",4));
        users.add(new FollowerAnalyzer.UserGraph("MadjidFalastine",4));
        users.add(new FollowerAnalyzer.UserGraph("benoithamon",5));
        users.add(new FollowerAnalyzer.UserGraph("MartineAubry",5));
        users.add(new FollowerAnalyzer.UserGraph("rglucks1",5));
        users.add(new FollowerAnalyzer.UserGraph("faureolivier",5));
        users.add(new FollowerAnalyzer.UserGraph("BalasGuillaume",5));
        users.add(new FollowerAnalyzer.UserGraph("Juanico",5));
        users.add(new FollowerAnalyzer.UserGraph("Isabel_thomasEU",5));
        users.add(new FollowerAnalyzer.UserGraph("EstherBenbassa",5));
        users.add(new FollowerAnalyzer.UserGraph("nadine__morano",6));
        users.add(new FollowerAnalyzer.UserGraph("LydiaGuirous",6));
        users.add(new FollowerAnalyzer.UserGraph("ECiotti",6));
        users.add(new FollowerAnalyzer.UserGraph("ChJacob77",6));
        users.add(new FollowerAnalyzer.UserGraph("cestrosi",6));
        users.add(new FollowerAnalyzer.UserGraph("T_Bouhafs",7));
        users.add(new FollowerAnalyzer.UserGraph("_MarwanMuhammad",7));
        users.add(new FollowerAnalyzer.UserGraph("FeizaBM",7));
        users.add(new FollowerAnalyzer.UserGraph("s_assbague",7));
        users.add(new FollowerAnalyzer.UserGraph("MarleneSchiappa",8));
        users.add(new FollowerAnalyzer.UserGraph("RichardFerrand",8));
        users.add(new FollowerAnalyzer.UserGraph("EPhilippePM",8));
        users.add(new FollowerAnalyzer.UserGraph("CCastaner",8));
        users.add(new FollowerAnalyzer.UserGraph("EmmanuelMacron",8));
        users.add(new FollowerAnalyzer.UserGraph("mounir",8));
        // Thinker_View
        bot.getJsonGraph(users);
    }
}
