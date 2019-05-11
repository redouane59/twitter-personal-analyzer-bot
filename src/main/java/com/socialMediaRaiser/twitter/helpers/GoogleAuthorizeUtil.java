package com.socialMediaRaiser.twitter.helpers;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.sheets.v4.SheetsScopes;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;


public class GoogleAuthorizeUtil {
    public static GoogleCredential authorize() throws IOException, GeneralSecurityException {
        GoogleCredential credential = GoogleCredential.fromStream(GoogleAuthorizeUtil.class.getResourceAsStream("/google-sheets-client-secret.json"))
                .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
        return credential;
    }
}
