package com.socialMediaRaiser.twitter.helpers;

import com.socialMediaRaiser.AbstractUser;
import com.socialMediaRaiser.twitter.User;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IOHelper {

    public void write(List<? extends AbstractUser> result) throws IOException {
        LocalDateTime now = LocalDateTime.now();

        FileWriter writer = new FileWriter(System.getProperty("user.home") + File.separatorChar
                + "Documents" + File.separatorChar
                + "followed"
                + now.getYear()+now.getMonthValue()+now.getDayOfMonth()
                +".csv");

        for(AbstractUser absUser : result) {
            User user = (User)absUser;
            writer.write(user.getId() + ";"
                    + user.getUserName() + ";"
                    + user.getFollowersCount() + ";"
                    + user.getFollowingCount()  + ";"
                    + user.getStatusesCount()  + ";"
                    + user.getFavouritesCount() + ";"
                    + user.getDescription() + ";"
                    + user.getLastUpdate() + ";"
                    + user.getDateOfCreation()  + ";"
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
