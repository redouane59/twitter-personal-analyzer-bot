package com.socialMediaRaiser.twitter.helpers;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.socialMediaRaiser.twitter.User;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class GoogleSheetHelper {
    private String SPREADSHEET_ID = "1rpTWqHvBFaxdHcbnHmry2quQTKhPVJ-dA2n_wep0hrs";
    private Sheets sheetsService;
    private final String tabName = "Followed 2";
    private final String tabNameTest = "Test";


    public GoogleSheetHelper(){
        try {
            this.sheetsService = SheetsServiceUtil.getSheetsService();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<Long> getPreviouslyFollowedIds() throws IOException {
        String startLine = "A2";
        String endLine = "A";
        List<String> ranges = Arrays.asList(tabName+"!"+startLine+":"+endLine);
        BatchGetValuesResponse readResult = sheetsService.spreadsheets().values()
                .batchGet(SPREADSHEET_ID)
                .setRanges(ranges)
                .execute();

        List<List<Object>> result = readResult.getValueRanges().get(0).getValues();
        List<Long> ids = new ArrayList<>();
        for(List<Object> valueArray : result){
            String stringValue = String.valueOf(valueArray.get(0));
            ids.add(Long.valueOf(stringValue));
        }
        return ids;
    }

    public void addNewFollowerLine(User user){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date followDate = user.getDateOfFollow();
        if(followDate==null){
            followDate = new Date();
        }
        ValueRange body = new ValueRange()
                .setValues(Arrays.asList(Arrays.asList(
                        String.valueOf(user.getId()),
                        user.getUserName(),
                        user.getFollowersCount(),
                        user.getFollowingCount(),
                        user.getStatusesCount(),
                        user.getFavouritesCount(),
                        user.getDescription().
                                replaceAll("\"","")
                                .replaceAll(";"," ")
                                .replaceAll("\n",""),
                        dateFormat.format(user.getLastUpdate()),
                        dateFormat.format(user.getDateOfCreation()),
                        user.getCommonFollowers(),
                        dateFormat.format(followDate)
                        )));
        try{
            Sheets.Spreadsheets.Values.Append request =
                    sheetsService.spreadsheets().values().append(SPREADSHEET_ID, tabNameTest+"!A1", body);
            request.setValueInputOption("RAW");
            AppendValuesResponse response = request.execute();
            System.out.println(response);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    // @todo
    public void filFollowBackInformation(Map<String, Boolean> result){

    }
}
