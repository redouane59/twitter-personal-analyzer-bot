package com.socialmediaraiser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.socialmediaraiser.twitter.impl.FollowerAnalyzer;

import java.util.HashSet;
import java.util.List;
import java.util.logging.Logger;

import static com.socialmediaraiser.twitter.impl.FollowerAnalyzer.*;

public class TwitterGraphLauncher {

    private static final Logger LOGGER = Logger.getLogger(TwitterGraphLauncher.class.getName());

    public static void main(String[] args) throws JsonProcessingException {
        FollowerAnalyzer bot = new FollowerAnalyzer("RedouaneBali", false, false);
        HashSet<UserGraph> users = new HashSet<>();
        users.add(new UserGraph("LaurentBouvet",1));
        users.add(new UserGraph("Amk84000",1));
        users.add(new UserGraph("GillesClavreul",1));
        users.add(new UserGraph("ZinebElRhazoui",2));
        users.add(new UserGraph("ZohraBitan",2));
        users.add(new UserGraph("ivanrioufol",2));
        users.add(new UserGraph("RenaudCamus",2));
        users.add(new UserGraph("GWGoldnadel",2));
        users.add(new UserGraph("edwyplenel",2));
        users.add(new UserGraph("fabricearfi",2));
        users.add(new UserGraph("davidperrotin",2));
        users.add(new UserGraph("carolinedehaas",2));
        users.add(new UserGraph("RokhayaDiallo",2));
        users.add(new UserGraph("epelboin",2));
        users.add(new UserGraph("BHL",2));
        users.add(new UserGraph("Enthoven_R",2));
        users.add(new UserGraph("PascalPraud",2));
        //   users.add(new UserGraph("AgagBoudjahlat",2));
        users.add(new UserGraph("MLP_officiel",3));
        users.add(new UserGraph("MarionMarechal",3));
        users.add(new UserGraph("J_Bardella",3));
        users.add(new UserGraph("DamienRieu",3));
        users.add(new UserGraph("JeanMessiha",3));
        users.add(new UserGraph("RobertMenardFR",3));
        users.add(new UserGraph("JulienOdoul",3));
        users.add(new UserGraph("JLMelenchon",4));
        users.add(new UserGraph("Francois_Ruffin",4));
        users.add(new UserGraph("Clem_Autain",4));
        users.add(new UserGraph("Deputee_Obono",4));
        users.add(new UserGraph("alexiscorbiere",4));
        users.add(new UserGraph("IanBrossat",4));
        users.add(new UserGraph("fabien_gay",4));
        users.add(new UserGraph("MadjidFalastine",4));
        users.add(new UserGraph("benoithamon",5));
        users.add(new UserGraph("MartineAubry",5));
        users.add(new UserGraph("rglucks1",5));
        users.add(new UserGraph("faureolivier",5));
        users.add(new UserGraph("BalasGuillaume",5));
        users.add(new UserGraph("Juanico",5));
        users.add(new UserGraph("Isabel_thomasEU",5));
        users.add(new UserGraph("EstherBenbassa",5));
        users.add(new UserGraph("nadine__morano",6));
        users.add(new UserGraph("LydiaGuirous",6));
        users.add(new UserGraph("ECiotti",6));
        users.add(new UserGraph("ChJacob77",6));
        users.add(new UserGraph("cestrosi",6));
        users.add(new UserGraph("T_Bouhafs",7));
        users.add(new UserGraph("_MarwanMuhammad",7));
        users.add(new UserGraph("FeizaBM",7));
        users.add(new UserGraph("s_assbague",7));
        users.add(new UserGraph("MarleneSchiappa",8));
        users.add(new UserGraph("RichardFerrand",8));
        users.add(new UserGraph("EPhilippePM",8));
        users.add(new UserGraph("CCastaner",8));
        users.add(new UserGraph("mounir",8));
        // Thinker_View
        bot.getJsonGraph(users);
    }
}
