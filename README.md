# twitter-bot
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
``` 

## Resources :
- [Twitter API](https://developer.twitter.com/en/docs) : used to get users or tweets infos, follow & unfollow users, etc.
- [Redouane59/twitter-client](https://github.com/redouane59/twitter-client) : Custom JAVA Twitter Client
- [Google Sheets API](https://developers.google.com/sheets/api/) : used to write followed users information & statistics on an online google sheet document
- [Twitter4j](http://twitter4j.org/en/) : used to consume live streaming twitter API

