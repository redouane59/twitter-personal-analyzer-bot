package com.socialMediaRaiser.twitter;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.socialMediaRaiser.twitter.helpers.GoogleSheetHelper;
import com.socialMediaRaiser.twitter.helpers.SheetsServiceUtil;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GoogleSheetsIntegrationTest {
    private static Sheets sheetsService;
    private GoogleSheetHelper helper;

    @BeforeClass
    public static void setup() throws GeneralSecurityException, IOException {
        sheetsService = SheetsServiceUtil.getSheetsService();
    }

    @Test
    public void testReading() throws IOException, GeneralSecurityException {
        helper = new GoogleSheetHelper();
        Assert.assertTrue(helper.getPreviouslyFollowedIds().size()>1);
    }

    @Test
    public void testWriting() throws IOException, GeneralSecurityException {
        helper = new GoogleSheetHelper();
        helper.addNewFollowerLine(User.builder()
                .id(816708441477677056L).userName("Red").build());
    }
}