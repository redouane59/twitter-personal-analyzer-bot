package com.socialMediaRaiser.twitter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PropertiesTest {
    FollowProperties followProperties = new FollowProperties();
    static String userName = "RedTheOne";

    @BeforeAll
    public static void init(){
        FollowProperties.load(userName);
    }

    @Test
    public void testIntProperty(){
        assertTrue(followProperties.targetProperties.getMinNbFollowers()>0);
    }

    @Test
    public void testFloatProperty(){
        assertTrue(followProperties.targetProperties.getMinRatio()>0);
    }

    @Test
    public void testArrayProperty(){
        assertTrue(followProperties.targetProperties.getDescription().length()>0);
    }

}
