package com.github.redouane59.twitterbot.io;

import com.github.redouane59.twitterbot.impl.InteractiveUser;
import com.github.redouane59.twitterbot.impl.RankedUser;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvHelper implements IOHelper {

  @Override
  public void addUserLine(final List<RankedUser> users) {
    FileWriter writer;
    try {
      writer = new FileWriter(System.getProperty("user.home") + File.separatorChar
                              + "Documents" + File.separatorChar
                              + "user-analyze"
                              + ".csv");
      for (InteractiveUser user : users) {
        // @todo maybe we can have an abstract method returning a List of fields to avoid code duplication
        writer.write(user.getId() + ";"
                     + user.getName() + ";"
                     + user.getFollowersCount() + ";"
                     + user.getFollowingCount() + ";"
                     + user.getTweetCount() + ";"
                     + user.getDescription().
            replaceAll("\"", "")
                           .replaceAll(";", " ")
                           .replaceAll("\n", "") + ";"
                     + Optional.ofNullable(user.getLocation()).orElse("") + ";"
                     + user.getUserInteraction().getAnsweredIds().size() + ";"
                     + user.getUserInteraction().getRetweetedIds().size() + ";"
                     + user.getUserInteraction().getAnsweredIds().size() + ";"
                     + user.getUserInteraction().getRetweetedIds().size() + ";"
                     + user.getUserInteraction().getLikedIds().size() + ";"
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
