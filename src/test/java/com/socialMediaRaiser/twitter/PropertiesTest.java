package com.socialMediaRaiser.twitter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PropertiesTest {
    FollowProperties followProperties = new FollowProperties();

    @BeforeAll
    public static void init(){
        FollowProperties.init();
    }

    @Test
    public void testStringProperty(){
        assertTrue(followProperties.getStringProperty(FollowProperties.TWEET_NAME).length()>0);
    }

    @Test
    public void testIntProperty(){
        assertTrue(followProperties.getIntProperty(FollowProperties.MIN_NB_FOLLOWERS)>0);
    }

    @Test
    public void testFloatProperty(){
        assertTrue(followProperties.getFloatProperty(FollowProperties.MIN_RATIO)>0);
    }

    @Test
    public void testArrayProperty(){
        assertTrue(followProperties.getStringArrayProperty(FollowProperties.DESCRIPTION).length>0);
    }

}
