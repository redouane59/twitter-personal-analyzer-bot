package com.github.redouane59.twitterbot.io;

import com.github.redouane59.twitterbot.impl.User;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvHelper implements IOHelper{

  @Override
  public void addUserLine(final List<User> users) {
    FileWriter writer;
    try {
      writer = new FileWriter(System.getProperty("user.home") + File.separatorChar
                              + "Documents" + File.separatorChar
                              + "user-analyze"
                              + ".csv");
      for(User user : users) {
        // @todo maybe we can have an abstract method returning a List of fields to avoid code duplication
        writer.write(user.getId() + ";"
                     + user.getName() + ";"
                     + user.getFollowersCount() + ";"
                     + user.getFollowingCount() + ";"
                     + user.getTweetCount() + ";"
                     + user.getDescription().
            replaceAll("\"","")
                           .replaceAll(";"," ")
                           .replaceAll("\n","") + ";"
                     + Optional.ofNullable(user.getLocation()).orElse("") + ";"
                     + user.getNbRepliesReceived() + ";"
                     + user.getNbRetweetsReceived() + ";"
                     + user.getNbRepliesGiven() + ";"
                     + user.getNbRetweetsGiven() + ";"
                     + user.getNbLikesGiven() + ";"
                     + user.isFollowing() + ";"
                     + "\n");
      }
      LOGGER.info("file generated with success in Documents folder");
      writer.close();
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
    }
  }

}
