package com.socialMediaRaiser.twitter;

import com.socialMediaRaiser.twitter.scoring.Criterion;
import com.socialMediaRaiser.twitter.scoring.UserScoringEngine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class ScoringTest {

    private static String ownerName = "RedTheOne";

    @Test
    void testScoringZero(){
        FollowProperties.load(ownerName);
        User user = new User();
        UserScoringEngine scoring = new UserScoringEngine(100);
        assertEquals(0,scoring.getUserScore(user));
    }

    @Test
    void testScoringOneMatchDescription(){
        FollowProperties.load(ownerName);
        FollowProperties.targetProperties.setDescription("a,b,c");
        User user = new User();
        user.setDescription("a");
        FollowProperties.scoringProperties.getProperty(Criterion.DESCRIPTION).setActive(true);
        FollowProperties.scoringProperties.getProperty(Criterion.DESCRIPTION).setMaxPoints(10);
        UserScoringEngine scoring = new UserScoringEngine(100);
        assertEquals(FollowProperties.scoringProperties.getProperty(Criterion.DESCRIPTION).getMaxPoints(), scoring.getUserScore(user));
        user.setDescription("b");
        assertEquals(FollowProperties.scoringProperties.getProperty(Criterion.DESCRIPTION).getMaxPoints(), scoring.getUserScore(user));
        user.setDescription("c");
        assertEquals(FollowProperties.scoringProperties.getProperty(Criterion.DESCRIPTION).getMaxPoints(), scoring.getUserScore(user));
    }

    @Test
    void testScoringSeveralMatchesDescription(){
        FollowProperties.load(ownerName);
        User user = new User();
        user.setDescription("a b c ");
        FollowProperties.targetProperties.setDescription("a,b,c");
        FollowProperties.scoringProperties.getProperty(Criterion.DESCRIPTION).setActive(true);
        FollowProperties.scoringProperties.getProperty(Criterion.DESCRIPTION).setMaxPoints(10);
        UserScoringEngine scoring = new UserScoringEngine(100);
        assertEquals(FollowProperties.scoringProperties.getProperty(Criterion.DESCRIPTION).getMaxPoints(), scoring.getUserScore(user));
    }

    @ParameterizedTest(name = "Expect score: {2} when followers: {0} followings: {1}")
    @CsvSource(value = {"1000, 1000, 20," +
                        "100, 1000, 0," +
                        "1000, 100, 0"})
    void testScoringMinMaxRatio(String nbFollowers, String nbFollowings, String exceptedResult){
        FollowProperties.load(ownerName);
        User user = new User();
        user.setFollowersCount(Integer.valueOf(nbFollowers));
        user.setFollowingCount(Integer.valueOf(nbFollowings));
        FollowProperties.targetProperties.setMinRatio((float)0.5);
        FollowProperties.targetProperties.setMaxRatio((float)1.5);
        FollowProperties.scoringProperties.getProperty(Criterion.RATIO).setMaxPoints(20);
        FollowProperties.scoringProperties.getProperty(Criterion.RATIO).setActive(true);
        FollowProperties.scoringProperties.getProperty(Criterion.NB_FOLLOWERS).setActive(false);
        FollowProperties.scoringProperties.getProperty(Criterion.NB_FOLLOWINGS).setActive(false);
        UserScoringEngine scoring = new UserScoringEngine(100);
        assertEquals((int)Integer.valueOf(exceptedResult), scoring.getUserScore(user));
    }

    @ParameterizedTest(name = "Expect score: {1} when followers: {0}")
    @CsvSource(value = {"1000, 10," +
            "100, 0," +
            "10000, 0"})
    void testScoringMinMaxFollowers(String nbFollowers, String exceptedResult){
        User user = new User();
        user.setFollowersCount(Integer.valueOf(nbFollowers));
        FollowProperties.targetProperties.setMinNbFollowers(500);
        FollowProperties.targetProperties.setMaxNbFollowers(5000);
        FollowProperties.scoringProperties.getProperty(Criterion.NB_FOLLOWERS).setMaxPoints(10);
        UserScoringEngine scoring = new UserScoringEngine(100);
        assertEquals((int)Integer.valueOf(exceptedResult), scoring.getUserScore(user));
    }

    @ParameterizedTest(name = "Expect score: {1} when followings: {0}")
    @CsvSource(value = {"1000, 10," +
            "100, 0," +
            "10000, 0"})
    void testScoringMinMaxFollowings(String nbFollowings, String exceptedResult){
        FollowProperties.load(ownerName);
        User user = new User();
        user.setFollowingCount(Integer.valueOf(nbFollowings));
        FollowProperties.targetProperties.setMinNbFollowings(500);
        FollowProperties.targetProperties.setMaxNbFollowings(5000);
        FollowProperties.scoringProperties.getProperty(Criterion.NB_FOLLOWINGS).setMaxPoints(10);
        UserScoringEngine scoring = new UserScoringEngine(100);
        assertEquals((int)Integer.valueOf(exceptedResult), scoring.getUserScore(user));
    }

    @Test
    void testLimit(){
        FollowProperties.load(ownerName);
        UserScoringEngine scoring = new UserScoringEngine(100);
        assertNotEquals(0, scoring.getLimit());
    }

    @Test
    void testBlockingProperty(){
        FollowProperties.load(ownerName);
        User user = new User();
        user.setDescription("x");
        user.setLang("fr");
        user.setFollowersCount(100);
        FollowProperties.targetProperties.setDescription("a,b,c");
        FollowProperties.targetProperties.setMinNbFollowers(10);
        FollowProperties.targetProperties.setMaxNbFollowers(1000);
        FollowProperties.scoringProperties.getProperty(Criterion.DESCRIPTION).setActive(true);
        FollowProperties.scoringProperties.getProperty(Criterion.DESCRIPTION).setMaxPoints(10);
        FollowProperties.scoringProperties.getProperty(Criterion.DESCRIPTION).setBlocking(true);
        FollowProperties.scoringProperties.getProperty(Criterion.NB_FOLLOWERS).setActive(true);
        FollowProperties.scoringProperties.getProperty(Criterion.NB_FOLLOWERS).setMaxPoints(10);
        UserScoringEngine scoring = new UserScoringEngine(100);
        assertEquals(0, scoring.getUserScore(user));
    }


}
