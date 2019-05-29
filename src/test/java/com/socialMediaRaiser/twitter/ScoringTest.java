package com.socialMediaRaiser.twitter;

import com.socialMediaRaiser.twitter.scoring.Criterion;
import com.socialMediaRaiser.twitter.scoring.FollowConfiguration;
import com.socialMediaRaiser.twitter.scoring.UserScoringEngine;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ScoringTest {

    @Test
    void testScoringZero(){
        User user = new User();
        UserScoringEngine scoring = new UserScoringEngine(new FollowConfiguration(),100);
        assertEquals(0,scoring.getUserScore(user));
    }

    @Test
    void testScoringOneMatchDescription(){
        User user = new User();
        user.setDescription("a");
        FollowConfiguration configuration = FollowConfiguration.builder().description(new String[]{"a", "b", "c"}).build();
        UserScoringEngine scoring = new UserScoringEngine(configuration,100);
        assertEquals(Criterion.DESCRIPTION.getMaxPoints(), scoring.getUserScore(user));
        user.setDescription("b");
        assertEquals(Criterion.DESCRIPTION.getMaxPoints(), scoring.getUserScore(user));
        user.setDescription("c");
        assertEquals(Criterion.DESCRIPTION.getMaxPoints(), scoring.getUserScore(user));
    }

    @Test
    void testScoringSeveralMatchesDescription(){
        User user = new User();
        user.setDescription("a b c ");
        FollowConfiguration configuration = FollowConfiguration.builder().description(new String[]{"a", "b", "c"}).build();
        UserScoringEngine scoring = new UserScoringEngine(configuration,100);
        assertEquals(Criterion.DESCRIPTION.getMaxPoints(), scoring.getUserScore(user));
    }

    @ParameterizedTest(name = "Expect score: {2} when followers: {0} followings: {1}")
    @CsvSource(value = {"1000, 1000, 10," +
                        "100, 1000, 0," +
                        "1000, 100, 0"})
    void testScoringMinMaxRatio(String nbFollowers, String nbFollowings, String exceptedResult){
        User user = new User();
        user.setFollowersCount(Integer.valueOf(nbFollowers));
        user.setFollowingsCount(Integer.valueOf(nbFollowings));
        FollowConfiguration configuration = FollowConfiguration.builder().minRatio((float)0.5).maxRatio((float)1.5).build();
        UserScoringEngine scoring = new UserScoringEngine(configuration,100);
        assertEquals(Integer.valueOf(exceptedResult), scoring.getUserScore(user));
    }

    @ParameterizedTest(name = "Expect score: {1} when followers: {0}")
    @CsvSource(value = {"1000, 10," +
            "100, 0," +
            "10000, 0"})
    void testScoringMinMaxFollowers(String nbFollowers, String exceptedResult){
        User user = new User();
        user.setFollowersCount(Integer.valueOf(nbFollowers));
        FollowConfiguration configuration = FollowConfiguration.builder().minNbFollowers(500).maxNbFollowers(5000).build();
        UserScoringEngine scoring = new UserScoringEngine(configuration,100);
        assertEquals(Integer.valueOf(exceptedResult), scoring.getUserScore(user));
    }

    @ParameterizedTest(name = "Expect score: {1} when followings: {0}")
    @CsvSource(value = {"1000, 10," +
            "100, 0," +
            "10000, 0"})
    void testScoringMinMaxFollowings(String nbFollowings, String exceptedResult){
        User user = new User();
        user.setFollowingsCount(Integer.valueOf(nbFollowings));
        FollowConfiguration configuration = FollowConfiguration.builder().minNbFollowings(500).maxNbFollowings(5000).build();
        UserScoringEngine scoring = new UserScoringEngine(configuration,100);
        assertEquals(Integer.valueOf(exceptedResult), scoring.getUserScore(user));
    }
}
