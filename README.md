# social-graph
Demo available here : https://twitter-social-graph.firebaseapp.com/index.html

To use it with your own [twitter developper account](https://developer.twitter.com/en/apply-for-access), add a .yaml file in `social-graph/src/main/resources/twitter-credentials.yaml` of this type 
```$xslt
twitter-credentials:
  consumerKey: xxx
  consumerSecret: xxx
  accessToken: xxx
  secretToken: xxx
```
Launch first `mvn clean install` in social-graph repository then launch `com.socialmediaraiser.twittersocialgraph.Launcher`, editing inside the class the accounts you want to analyse.

## Resources :
- [D3JS (Force Directed Graph with Labels)](https://bl.ocks.org/heybignick/3faf257bbbbc7743bb72310d03b86ee8) : Graph representation.


# bot
Use API & IA to bring growth to your twitter account !

## List of available criteria to filter profiles
- Keyword on profile description
- Keyword on tweets
- Location
- Language
- Time since last update
- Common followers
- Minimum number of followers
- Maximum number of followers
- Minimum number of followings
- Maximum number of followings
- Minimum ratio followers/followings
- Maximum ratio followers/followings
- Tweet counts

## 3 different implementations

### TwitterBotByInfluencers
Based on your current relations with some high ranked profiles, this bot will search for users trying to optimize the probability of follow back sorting them by number of common friends.

### TwitterBotByLastActivity
Based on your last days activities, this bot will search for all users you were in contact with to create relation.

### TwitterBotByLiveKeyWords
Using the Twitter Stream API, this bot will look for live tweets speaking about some keywords you defined previously and will create relation with their authors.

### Yaml config file example 
To put in `resources` folder.
```
google-credentials:
  type: xxx
  project_id: xxx
  private_key_id: xxx
  private_key: "xxx"
  client_email: xxx
  client_id: xxx
  auth_uri: https://accounts.google.com/o/oauth2/auth
  token_uri: https://oauth2.googleapis.com/token
  auth_provider_x509_cert_url: https://www.googleapis.com/oauth2/v1/certs
  client_x509_cert_url: xxx
target:
  minNbFollowers: 50
  maxNbFollowers: 10000
  minNbFollowings: 50
  maxNbFollowings: 10000
  description: "JavaScript,Python,Java,j2e,C++,PHP,C#"
  unwantedKeywords: xxx, RT, patriote
  minRatio: 0.5
  maxRatio: 2
  maxDaysSinceLastTweet: 5
  nbBaseFollowers: 20
  terms:
  location: "France,Paris,Lyon,Marseille,Lille,Nice,Toulouse,Bordeaux,Rouen,Strasbourg,Nantes,Metz,Grenoble,Toulon,Montpellier"
  language: fr
  minimumPercentMatch: 50
influencer:
  minRatio: 1.2
  minNbFollowers: 4000
  baseList:
scoring:
  nbFollowers:
    active: true
    maxPoints: 10
    blocking: false
  nbFollowings:
    active: true
    maxPoints: 10
    blocking: false
  ratio:
    active: true
    maxPoints: 10
    blocking: true
  lastUpdate:
    active: true
    maxPoints: 20
    blocking: true
  description:
    active: true
    maxPoints: 20
    blocking: false
  location:
    active: true
    maxPoints: 10
    blocking: false
  commonFollowers:
    active: true
    maxPoints: 20
    blocking: false
  tweetCount:
    active: false
    maxPoints: 0
    blocking: false
io:
  resultColumn: O
  tabName: RedouaneBali
  followDateIndex: 11
  id: 1rpTWqHvBFaxdHcbnHmry2quQTKhPVJ-dA2n_wep0hrs
  useRFA: false
  rfaRange: RedouaneBali RFA!A2:I
twitter-credentials:
  consumerKey: xxx
  consumerSecret: xxx
  accessToken: xxx
  secretToken: xxx
``` 
# core
Core functions used to call Twitter API.

## Resources :
- [Twitter API](https://developer.twitter.com/en/docs) : used to get users or tweets infos, follow & unfollow users, etc.
- [Google Sheets API](https://developers.google.com/sheets/api/) : used to write followed users information & statistics on an online google sheet document
- [Twitter4j](http://twitter4j.org/en/) : used to consume live streaming twitter API

