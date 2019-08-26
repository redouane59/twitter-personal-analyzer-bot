package com.socialMediaRaiser.twitter.integration;

import com.socialMediaRaiser.UnfollowLauncher;
import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.RandomForestAlgoritm;
import com.socialMediaRaiser.twitter.User;
import com.socialMediaRaiser.twitter.helpers.dto.getUser.AbstractUser;
import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RandomForestAlgorithmTest {

    String userName = "RedouaneBali";

    @BeforeEach
    void init() throws Exception {
        FollowProperties.load(userName);
        RandomForestAlgoritm.process();
    }
    @Test
    void testFalse() {
        AbstractUser user = new TwitterBotByInfluencers(userName).getUserFromUserName("blevy90");
        assertEquals(false, user.getRandomForestPrediction());
    }

    @Test
    void testTrue() {
        // Followers	Followings	NbDaySinceLastTweet	CommonFollowers	DateOfFollow	Tweets	YearsSinceCreation
        // 4837	2448	4	2	2019/05/28 21:32	8426	8
        Calendar cal = Calendar.getInstance();
        cal.set(2019, 4, 28, 21, 32);
        Date followDate = cal.getTime();
        User user = User.builder()
                .followersCout(4837)
                .followingCount(2448)
                .lastUpdate(UnfollowLauncher.yesterday(4))
                .commonFollowers(2)
                .dateOfFollow(followDate)
                .statusesCount(8426)
                .dateOfCreation(UnfollowLauncher.yesterday(8*365))
                .build();
        assertEquals(true, user.getRandomForestPrediction());
    }




}
