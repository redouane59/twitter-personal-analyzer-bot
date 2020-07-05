# twitter-personal-analyzer-bot
Analyze your interactions.

## Set up
In `src\main\resources\google-credentials.json`, create the following file :

```$xslt
{
"type":"service_account",
"project_id":"xxx",
"private_key_id":"xxx",
"private_key":"xxx",
"client_email":"xxx",
"client_id":"xxx",
"auth_uri":"http://accounts.google.com/o/oauth2/auth",
"token_uri":"https://oauth2.googleapis.com/token",
"auth_provider_x509_cert_url":"https://www.googleapis.com/oauth2/v1/certs",
"client_x509_cert_url":"xxx",
"sheet_id":"<yourFileId>",
"tab_name":"<yourTabName>"
}
```

## Resources :
- [Twitter API](https://developer.twitter.com/en/docs) : used to get users or tweets infos, follow & unfollow users, etc.
- [Redouane59/twitter-client](https://github.com/redouane59/twitter-client) : Custom JAVA Twitter Client
- [Google Sheets API](https://developers.google.com/sheets/api/) : used to write followed users information & statistics on an online google sheet document

