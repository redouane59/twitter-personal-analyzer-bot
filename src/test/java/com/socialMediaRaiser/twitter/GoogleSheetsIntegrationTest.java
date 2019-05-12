package com.socialMediaRaiser.twitter;

import com.google.api.services.sheets.v4.Sheets;
import com.socialMediaRaiser.twitter.helpers.GoogleSheetHelper;
import com.socialMediaRaiser.twitter.helpers.SheetsServiceUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

public class GoogleSheetsIntegrationTest {
    private static Sheets sheetsService;
    private GoogleSheetHelper helper = new GoogleSheetHelper();

    @BeforeClass
    public static void setup() throws GeneralSecurityException, IOException {
        sheetsService = SheetsServiceUtil.getSheetsService();
    }

    @Test
    public void testReading() throws IOException {
        Assert.assertTrue(helper.getPreviouslyFollowedIds().size()>1);
    }

    @Test
    public void testWriting() {
        helper.addNewFollowerLine(User.builder()
                .id(816708441477677056L).userName("Red").build());
    }

    @Test
    public void testUpdate(){
        Map<Long, Boolean> result = new HashMap<>();
        result.put(12345L, false);
        result.put(23456L, true);
        helper.updateFollowBackInformation(result);
    }

    @Test
    public void testGetRow(){
        Long userId = 816708441477677056L;
        int result = helper.getRowOfUser(userId);
        Assert.assertEquals(2, result);
    }
}