package com.socialMediaRaiser.twitter;

import com.socialMediaRaiser.twitter.scoring.Criterion;
import com.socialMediaRaiser.twitter.scoring.UserScoringEngine;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


public class ScoringTest {

    @BeforeAll
    public static void init() {
        FollowProperties.load();
    }

    @Test
    void testScoringZero(){
        User user = new User();
        UserScoringEngine scoring = new UserScoringEngine(100);
        assertEquals(0,scoring.getUserScore(user));
    }

    @Test
    void testScoringOneMatchDescription(){
        User user = new User();
        user.setDescription("a");
        FollowProperties.targetProperties.setDescription("a,b,c");
        UserScoringEngine scoring = new UserScoringEngine(100);
        assertEquals(FollowProperties.scoringProperties.getProperty(Criterion.DESCRIPTION).getMaxPoints(), scoring.getUserScore(user));
        user.setDescription("b");
        assertEquals(FollowProperties.scoringProperties.getProperty(Criterion.DESCRIPTION).getMaxPoints(), scoring.getUserScore(user));
        user.setDescription("c");
        assertEquals(FollowProperties.scoringProperties.getProperty(Criterion.DESCRIPTION).getMaxPoints(), scoring.getUserScore(user));
    }

    @Test
    void testScoringSeveralMatchesDescription(){
        User user = new User();
        user.setDescription("a b c ");
        FollowProperties.targetProperties.setDescription("a,b,c");
        UserScoringEngine scoring = new UserScoringEngine(100);
        assertEquals(FollowProperties.scoringProperties.getProperty(Criterion.DESCRIPTION).getMaxPoints(), scoring.getUserScore(user));
    }

    @ParameterizedTest(name = "Expect score: {2} when followers: {0} followings: {1}")
    @CsvSource(value = {"1000, 1000, 20," +
                        "100, 1000, 0," +
                        "1000, 100, 0"})
    void testScoringMinMaxRatio(String nbFollowers, String nbFollowings, String exceptedResult){
        User user = new User();
        user.setFollowersCount(Integer.valueOf(nbFollowers));
        user.setFollowingsCount(Integer.valueOf(nbFollowings));
        FollowProperties.targetProperties.setMinRatio((float)0.5);
        FollowProperties.targetProperties.setMaxRatio((float)1.5);
        UserScoringEngine scoring = new UserScoringEngine(100);
        assertEquals(Integer.valueOf(exceptedResult), scoring.getUserScore(user));
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
        UserScoringEngine scoring = new UserScoringEngine(100);
        assertEquals(Integer.valueOf(exceptedResult), scoring.getUserScore(user));
    }

    @ParameterizedTest(name = "Expect score: {1} when followings: {0}")
    @CsvSource(value = {"1000, 10," +
            "100, 0," +
            "10000, 0"})
    void testScoringMinMaxFollowings(String nbFollowings, String exceptedResult){
        User user = new User();
        user.setFollowingsCount(Integer.valueOf(nbFollowings));
        FollowProperties.targetProperties.setMinNbFollowings(500);
        FollowProperties.targetProperties.setMaxNbFollowings(5000);
        UserScoringEngine scoring = new UserScoringEngine(100);
        assertEquals(Integer.valueOf(exceptedResult), scoring.getUserScore(user));
    }

    @Test
    void testLimit(){
        UserScoringEngine scoring = new UserScoringEngine(100);
        assertNotEquals(0, scoring.getLimit());
    }


}
