package com.twitter.helpers;

import com.twitter.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class IOHelper {

    public void write(List<User> result) throws IOException {
        LocalDateTime now = LocalDateTime.now();

        FileWriter writer = new FileWriter(System.getProperty("user.home") + File.separatorChar
                + "Documents" + File.separatorChar
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
                    + now.getDayOfMonth() + "/"+now.getMonthValue() + " " + now.getHour() + ":"+now.getMinute()+";"
                    + "\n");
        }

        writer.close();
    }
}
