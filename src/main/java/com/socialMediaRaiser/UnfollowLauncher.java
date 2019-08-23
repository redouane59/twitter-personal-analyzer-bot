package com.socialMediaRaiser;

import com.socialMediaRaiser.twitter.AbstractTwitterBot;
import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.RandomForestAlgoritm;
import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;

import java.util.Calendar;
import java.util.Date;

public class UnfollowLauncher {

    private static AbstractTwitterBot twitterBot;

    public static void main(String[] args) throws Exception {
        if(args.length<1){
            System.err.println("missing arguments, expecting 1 : ownerName[String]");
        } else{
            String ownerName = args[0];
            System.out.println("start working for " + ownerName + " for unfollows. ");
            twitterBot = new TwitterBotByInfluencers(ownerName);
            twitterBot.checkNotFollowBack(ownerName,true, true, yesterday(), false);
            System.out.println("end program");
        }
    }

    private static Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

}
