package com.socialMediaRaiser.twitter.helpers;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import com.socialMediaRaiser.twitter.User;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class GoogleSheetHelper {
    private String SPREADSHEET_ID = "1rpTWqHvBFaxdHcbnHmry2quQTKhPVJ-dA2n_wep0hrs";
    private Sheets sheetsService;
    private final String tabName = "V2";

    public GoogleSheetHelper(){
        try {
            this.sheetsService = SheetsServiceUtil.getSheetsService();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<Long> getPreviouslyFollowedIds() {
        return this.getPreviouslyFollowedIds(true, true);
    }

    // @todo filter by date or by index ?
    public List<Long> getPreviouslyFollowedIds(boolean showFalse, boolean showTrue) {
        String startLine = "A2";
        String endLine = "M";
        int followBackResultIndex = 11;
        List<String> ranges = Arrays.asList(tabName+"!"+startLine+":"+endLine);
        try{
            BatchGetValuesResponse readResult = sheetsService.spreadsheets().values()
                    .batchGet(SPREADSHEET_ID)
                    .setRanges(ranges)
                    .execute();
            List<List<Object>> result = readResult.getValueRanges().get(0).getValues();
            List<Long> ids = new ArrayList<>();
            for(List<Object> valueArray : result){
                if((showFalse && showTrue)
                        || valueArray.size()<=followBackResultIndex
                        || (valueArray.size()>followBackResultIndex && showFalse && String.valueOf(valueArray.get(followBackResultIndex)).toLowerCase().equals("false"))
                        || (valueArray.size()>followBackResultIndex && showTrue && String.valueOf(valueArray.get(followBackResultIndex)).toLowerCase().equals("true"))){
                    String stringValue = String.valueOf(valueArray.get(0));
                    ids.add(Long.valueOf(stringValue));
                }
            }
            return ids;
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ArrayList<Long>();
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
                    sheetsService.spreadsheets().values().append(SPREADSHEET_ID, tabName+"!A1", body);
            request.setValueInputOption("RAW");
            AppendValuesResponse response = request.execute();
            // System.out.println(response);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void updateFollowBackInformation(Map<Long, Boolean> result){
        String followedBackColumn = tabName+"!L";
        for(Map.Entry<Long, Boolean> entry : result.entrySet()) {
            Long userId = entry.getKey();
            String followedBack = String.valueOf(entry.getValue()).toUpperCase();
            int row = getRowOfUser(userId);

            ValueRange requestBody = new ValueRange()
                    .setValues(Arrays.asList(Arrays.asList(followedBack)));

            try {
                Sheets.Spreadsheets.Values.Update request = sheetsService.spreadsheets().values()
                        .update(SPREADSHEET_ID, followedBackColumn+row, requestBody);
                request.setValueInputOption("RAW");
                UpdateValuesResponse response = request.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }



        }
    }

    public int getRowOfUser(Long userId){
        List<Long> ids = this.getPreviouslyFollowedIds(true, true);
        int startIndex = 2; // sheet starts at line 1 + header 1
        int i=0;
        while (i<ids.size()){
            if(ids.get(i).equals(userId)){
                return i + startIndex;
            }
            i++;
        }
        return -1;
    }
}
