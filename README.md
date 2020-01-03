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

# core
Core functions used to call Twitter API.

## Resources :
- [Twitter API](https://developer.twitter.com/en/docs) : used to get users or tweets infos, follow & unfollow users, etc.
- [Google Sheets API](https://developers.google.com/sheets/api/) : used to write followed users information & statistics on an online google sheet document
- [Twitter4j](http://twitter4j.org/en/) : used to consume live streaming twitter API

# social-graph
Demo available here : https://twitter-social-graph.firebaseapp.com/index.html

To use it with your own [twitter developper account](https://developer.twitter.com/en/apply-for-access), add a .yaml file in `social-graph/src/main/resources/<YourUserName>.yaml` of this type 
```$xslt
twitter-credentials:
  consumerKey: xxx
  consumerSecret: xxx
  accessToken: xxx
  secretToken: xxx
```
Then launch `com.socialmediaraiser.twittersocialgraph.Launcher` with your userName as argument.
## Resources :
- [D3JS (Force Directed Graph with Labels)](https://bl.ocks.org/heybignick/3faf257bbbbc7743bb72310d03b86ee8) : Graph representation.
