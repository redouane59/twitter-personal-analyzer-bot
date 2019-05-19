package com.socialMediaRaiser.twitter.helpers;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import com.socialMediaRaiser.AbstractUser;
import com.socialMediaRaiser.twitter.User;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

public class GoogleSheetHelper extends AbstractIOHelper {
    private String SPREADSHEET_ID = "1rpTWqHvBFaxdHcbnHmry2quQTKhPVJ-dA2n_wep0hrs";
    private Sheets sheetsService;
    private final String tabName = "V2";
    private String followedBackColumn = tabName+"!L";

    public GoogleSheetHelper(){
        try {
            this.sheetsService = SheetsServiceUtil.getSheetsService();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<Long> getPreviouslyFollowedIds(boolean showFalse, boolean showTrue, Date date) {
        String startLine = "A2";
        String endLine = "L";
        int followBackResultIndex = 11; // dirty ?
        int dateOfFollowIndex = 10; // dirty ?
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

                    DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                    Date followDate = formatter.parse( String.valueOf(valueArray.get(dateOfFollowIndex)));

                    int diffInDays = -1;
                    if(date!=null && valueArray.size() > dateOfFollowIndex && date.getDate() == followDate.getDate()){
                        diffInDays = (int) ((followDate.getTime() - date.getTime()) / (1000 * 60 * 60 * 24));
                    }

                    if (date==null || diffInDays==0) {
                        String stringValue = String.valueOf(valueArray.get(0));
                        ids.add(Long.valueOf(stringValue));
                    }
                }
            }
            return ids;
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ArrayList<Long>();
    }

    public void addNewFollowerLine(AbstractUser u){
        User user = (User)u;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date followDate = user.getDateOfFollow();
        if(followDate==null){
            followDate = new Date();
        }
        // @todo add location
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
            request.execute();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    // @todo add date
    public void updateFollowBackInformation(Map<Long, Boolean> result){
        //String followedBackColumn = tabName+"!L";
        for(Map.Entry<Long, Boolean> entry : result.entrySet()) {
            Long userId = entry.getKey();
            String followedBack = String.valueOf(entry.getValue()).toUpperCase();
            System.out.println("updating " + userId + " -> " + followedBack + " ..."); // @todo call other function
            int row = getRowOfUser(userId);

            ValueRange requestBody = new ValueRange()
                    .setValues(Arrays.asList(Arrays.asList(followedBack)));

            try {
                Sheets.Spreadsheets.Values.Update request = sheetsService.spreadsheets().values()
                        .update(SPREADSHEET_ID, followedBackColumn+row, requestBody);
                request.setValueInputOption("USER_ENTERED"); // change to INPUT_VALUE_OPTION_UNSPECIFIED ?
                request.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateFollowBackInformation(Long userId, Boolean result) {
        String followedBack = String.valueOf(result).toUpperCase();
        System.out.print("updating " + userId + " -> " + followedBack + " ...");
        int row = getRowOfUser(userId);

        ValueRange requestBody = new ValueRange()
                .setValues(Arrays.asList(Arrays.asList(followedBack)));
        try {
            Sheets.Spreadsheets.Values.Update request = sheetsService.spreadsheets().values()
                    .update(SPREADSHEET_ID, followedBackColumn+row, requestBody);
            request.setValueInputOption("USER_ENTERED");
            request.execute();
        } catch (IOException e) {
            e.printStackTrace();
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
