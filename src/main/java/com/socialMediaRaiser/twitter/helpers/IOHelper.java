package com.socialMediaRaiser.twitter.helpers;

import com.socialMediaRaiser.twitter.User;
import com.socialMediaRaiser.twitter.helpers.dto.IUser;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.socialMediaRaiser.twitter.helpers.AbstractIOHelper.DATE_FORMAT;

public class IOHelper {

    public void write(List<IUser> result) throws IOException {
        LocalDateTime now = LocalDateTime.now();
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        FileWriter writer = new FileWriter(System.getProperty("user.home") + File.separatorChar
                + "Documents" + File.separatorChar
                + "followed"
                + now.getYear()+now.getMonthValue()+now.getDayOfMonth()
                +".csv");

        for(IUser absUser : result) {
            User user = (User)absUser;
            Date followDate = user.getDateOfFollow();
            if(followDate==null){
                followDate = new Date();
            }
            writer.write(user.getId() + ";"
                    + user.getUsername() + ";"
                    + user.getFollowersCount() + ";"
                    + user.getFollowingCount()  + ";"
                    + user.getTweetCount()  + ";"
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

    public void writeFollowedWithUser(Map<IUser, Boolean> result) throws IOException {
        LocalDateTime now = LocalDateTime.now();

        FileWriter writer = new FileWriter(System.getProperty("user.home") + File.separatorChar
                + "Documents" + File.separatorChar
                + "followback"
                + now.getYear()+now.getMonthValue()+now.getDayOfMonth()
                +".csv");


        writer.write("id;name;followed\n");

        for(Map.Entry<IUser, Boolean> entry : result.entrySet()) {
            writer.write(entry.getKey().getUsername() + ";"
                    + entry.getKey().getId() + ";"
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
        }
        return content;
    }
}
