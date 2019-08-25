package com.socialMediaRaiser.twitter.integration;

import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.RandomForestAlgoritm;
import com.socialMediaRaiser.twitter.helpers.dto.getUser.AbstractUser;
import com.socialMediaRaiser.twitter.impl.TwitterBotByInfluencers;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RandomForestAlgorithmTest {

    @Test
    void testFalse() {
        String userName = "RedouaneBali";
        FollowProperties.load(userName);
        AbstractUser user = new TwitterBotByInfluencers(userName).getUserFromUserName("blevy90");
        assertEquals(false, user.getRandomForestPrediction());
    }

    @Test
    void testTrue() {
        String userName = "RedouaneBali";
        FollowProperties.load(userName);
        AbstractUser user = new TwitterBotByInfluencers(userName).getUserFromUserName("Caromontenot");
        assertEquals(false, user.getRandomForestPrediction());
    }




}
