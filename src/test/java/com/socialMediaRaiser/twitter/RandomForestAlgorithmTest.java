package com.socialMediaRaiser.twitter;

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
    void test1() {
        String userName = "RedTheOne";
        FollowProperties.load(userName);
        RandomForestAlgoritm rfa = new RandomForestAlgoritm();
        AbstractUser user = new TwitterBotByInfluencers(userName).getUserFromUserName("GhizleneBessai4");
        assertEquals(false, user.getRandomForestPrediction());
    }




}
