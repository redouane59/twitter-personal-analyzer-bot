package com.socialMediaRaiser.twitter.helpers;

import com.socialMediaRaiser.AbstractUser;
import com.socialMediaRaiser.twitter.AbstractTwitterBot;
import com.socialMediaRaiser.twitter.User;
import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class IOHelper {

    public void write(List<? extends AbstractUser> result) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        FileWriter writer = new FileWriter(System.getProperty("user.home") + File.separatorChar
                + "Documents" + File.separatorChar
                + "followed"
                + now.getYear()+now.getMonthValue()+now.getDayOfMonth()
                +".csv");

        for(AbstractUser absUser : result) {
            User user = (User)absUser;
            Date followDate = user.getDateOfFollow();
            if(followDate==null){
                followDate = new Date();
            }
            writer.write(user.getId() + ";"
                    + user.getUserName() + ";"
                    + user.getFollowersCount() + ";"
                    + user.getFollowingCount()  + ";"
                    + user.getStatusesCount()  + ";"
                    + user.getFavouritesCount() + ";"
                    + user.getDescription().
                    replaceAll("\"","")
                    .replaceAll(";"," ")
                    .replaceAll("\n","") + ";"
                    + dateFormat.format(user.getLastUpdate()) + ";"
                    + dateFormat.format(user.getDateOfCreation())  + ";"
                    + user.getCommonFollowers()  + ";"
                    + dateFormat.format(followDate)
                    + "\n");
        }

        writer.close();
    }

    public void writeFollowedWithUserName(Map<String, Boolean> result) throws IOException {
        LocalDateTime now = LocalDateTime.now();

        FileWriter writer = new FileWriter(System.getProperty("user.home") + File.separatorChar
                + "Documents" + File.separatorChar
                + "followback"
                + now.getYear()+now.getMonthValue()+now.getDayOfMonth()
                +".csv");

        writer.write("name;followed\n");

        for(Map.Entry<String, Boolean> entry : result.entrySet()) {
            writer.write(entry.getKey() + ";"
                    + entry.getValue()
                    + "\n");
        }

        writer.close();
    }

    public void writeFollowedWithUserId(Map<Long, Boolean> result) throws IOException {
        LocalDateTime now = LocalDateTime.now();

        FileWriter writer = new FileWriter(System.getProperty("user.home") + File.separatorChar
                + "Documents" + File.separatorChar
                + "followback"
                + now.getYear()+now.getMonthValue()+now.getDayOfMonth()
                +".csv");

        writer.write("name;followed\n");

        for(Map.Entry<Long, Boolean> entry : result.entrySet()) {
            writer.write(entry.getKey() + ";"
                    + entry.getValue()
                    + "\n");
        }

        writer.close();
    }

    public void writeFollowedWithUser(Map<AbstractUser, Boolean> result) throws IOException {
        LocalDateTime now = LocalDateTime.now();

        FileWriter writer = new FileWriter(System.getProperty("user.home") + File.separatorChar
                + "Documents" + File.separatorChar
                + "followback"
                + now.getYear()+now.getMonthValue()+now.getDayOfMonth()
                +".csv");


        writer.write("id;name;followed\n");

        for(Map.Entry<AbstractUser, Boolean> entry : result.entrySet()) {
            writer.write(entry.getKey().getUserName() + ";"
                    + entry.getKey().getId() + ";"
                    + entry.getValue()
                    + "\n");
        }

        writer.close();
    }

    public void writeId(Map<AbstractUser, Boolean> result) throws IOException {
        LocalDateTime now = LocalDateTime.now();

        FileWriter writer = new FileWriter(System.getProperty("user.home") + File.separatorChar
                + "Documents" + File.separatorChar
                + "ids"
                + now.getYear()+now.getMonthValue()+now.getDayOfMonth()
                +".csv");


        writer.write("name;id;followed\n");
        AbstractTwitterBot twitterBot = new TwitterBotByInfluencers();
        for(Map.Entry<AbstractUser, Boolean> entry : result.entrySet()) {
            writer.write(entry.getKey().getUserName() + ";"
                    + twitterBot.getUserFromUserName(entry.getKey().getUserName()).getId() + ";"
                    + entry.getValue()
                    + "\n");
        }

        writer.close();
    }

    public List<String[]> readData(String filePath) throws IOException {
        int count = 0;
        List<String[]> content = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                content.add(line.split(";"));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //Some error logging
        }
        return content;
    }
}
