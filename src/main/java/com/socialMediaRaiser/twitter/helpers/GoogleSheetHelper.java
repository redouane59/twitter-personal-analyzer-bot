package com.socialMediaRaiser.twitter.helpers;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import com.socialMediaRaiser.AbstractUser;
import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.User;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class GoogleSheetHelper extends AbstractIOHelper {
    private Sheets sheetsService;
    private String followedBackColumn;
    private String sheetId;
    private String tabName;
    private String resultColumn;
    public GoogleSheetHelper(){
        FollowProperties.init();
        this.sheetId = FollowProperties.getStringProperty(FollowProperties.IO_SHEET_IT);
        this.tabName = FollowProperties.getStringProperty(FollowProperties.IO_SHEET_TABNAME);
        this.resultColumn = FollowProperties.getStringProperty(FollowProperties.IO_SHEET_RESULT_COLUMN);
        this.followedBackColumn = this.tabName+"!"+FollowProperties.getStringProperty(FollowProperties.IO_SHEET_RESULT_COLUMN);
        try {
            this.sheetsService = SheetsServiceUtil.getSheetsService();
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<Long> getPreviouslyFollowedIds(boolean showFalse, boolean showTrue, Date date) {
        String startLine = "A2";
        int followBackResultIndex = resultColumn.toLowerCase().toCharArray()[0] - 'a';
        List<String> ranges = Arrays.asList(this.tabName +"!"+startLine+":"+resultColumn);
        try{
            BatchGetValuesResponse readResult = sheetsService.spreadsheets().values()
                    .batchGet(this.sheetId)
                    .setRanges(ranges)
                    .execute();
            List<List<Object>> result = readResult.getValueRanges().get(0).getValues();
            List<Long> ids = new ArrayList<>();
            for(List<Object> valueArray : result){
                if((showFalse && showTrue)
                        || valueArray.size()<=followBackResultIndex
                        || (valueArray.size()>followBackResultIndex && showFalse && String.valueOf(valueArray.get(followBackResultIndex)).toLowerCase().equals("false"))
                        || (valueArray.size()>followBackResultIndex && showTrue && String.valueOf(valueArray.get(followBackResultIndex)).toLowerCase().equals("true"))){

                    DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
                    Date followDate = formatter.parse( String.valueOf(valueArray.get(FollowProperties.getIntProperty(FollowProperties.IO_SHEET_FOLLOW_DATE_INDEX))));

                    int diffInDays = -1;
                    if(date!=null && valueArray.size() > FollowProperties.getIntProperty(FollowProperties.IO_SHEET_FOLLOW_DATE_INDEX) && date.getDate() == followDate.getDate()){
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
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
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
                        user.getFollowingsCount(),
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
                    sheetsService.spreadsheets().values().append(this.sheetId, this.tabName+"!A1", body);
            request.setValueInputOption("RAW");
            request.execute();
        } catch(Exception e){
            e.printStackTrace();
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
                    .update(this.sheetId, followedBackColumn+row, requestBody);
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
