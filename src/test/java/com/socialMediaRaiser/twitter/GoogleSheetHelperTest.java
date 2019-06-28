package com.socialMediaRaiser.twitter;

import com.socialMediaRaiser.twitter.helpers.GoogleSheetHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GoogleSheetHelperTest {

    private GoogleSheetHelper googleSheetHelper = new GoogleSheetHelper();

    @BeforeAll
    static void init(){
        FollowProperties.load();
    }

    @Test
    void testGetPreviouslyFollowedIdsAll(){
        List<Long> result = googleSheetHelper.getPreviouslyFollowedIds();
        assertTrue(result.size()>200);
    }

    @Test
    void testGetPreviouslyFollowedIdsByDate(){
        Date date = new Date();
        date.setDate(11);
        date.setMonth(04);
        List<Long> result = googleSheetHelper.getPreviouslyFollowedIds(true, true, date);
        assertTrue(result.size()>50);
    }

    @Test
    void testRowOfUser(){
        int result = googleSheetHelper.getUserRows().get(925955978L);
        assertEquals(10, result);
        result = googleSheetHelper.getUserRows().get(1719824233L);
        assertEquals(3493, result);
    }

    @Test
    void testGetRandomForestData() throws Exception {
        RandomForestAlgoritm.process();
        List<List<Object>> result = googleSheetHelper.getRandomForestData();
        assertTrue(result.size()>0);
    }
}
