package com.socialMediaRaiser.twitter.helpers;

import com.socialMediaRaiser.twitter.User;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IOHelper {

    public void write(List<User> result) throws IOException {
        LocalDateTime now = LocalDateTime.now();

        FileWriter writer = new FileWriter(System.getProperty("user.home") + File.separatorChar
                + "Documents" + File.separatorChar
                + "followed"
                + now.getYear()+now.getMonthValue()+now.getDayOfMonth()+now.getHour()+now.getMinute()
                +".csv");

        writer.write("name;followers;followings;nbTweets;creation;commonFollowers;followDate\n");

        for(User user : result) {
            writer.write(user.getScreen_name() + ";"
                    + user.getFollowers_count() + ";"
                    + user.getFriends_count()  + ";"
                    + user.getStatuses_count()  + ";"
                    + user.getCreated_at()  + ";"
                    + user.getCommonFollowers()  + ";"
                    + user.getDateOfFollow()
                    + "\n");
        }

        writer.close();
    }

    public void writeFollowed(Map<String, Boolean> result) throws IOException {
        LocalDateTime now = LocalDateTime.now();

        FileWriter writer = new FileWriter(System.getProperty("user.home") + File.separatorChar
                + "Documents" + File.separatorChar
                + "followback"
                + now.getYear()+now.getMonthValue()+now.getDayOfMonth()+now.getHour()+now.getMinute()
                +".csv");

        writer.write("name;followed\n");

        for(Map.Entry<String, Boolean> entry : result.entrySet()) {
            writer.write(entry.getKey() + ";"
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
            System.out.println(e);
            //Some error logging
        }
        return content;
    }
}
