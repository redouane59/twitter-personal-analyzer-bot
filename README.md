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
Then launch `com.socialmediaraiser.twittersocialgraph.Launcher` with your userName as argument, editing inside the class the accounts you want to analyse.

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
  description: "JavaScript,Python,Java,j2e,C++,PHP,C#,Shell,TypeScript,Ruby,Jupyter,Objective-C,Powershell,react,vuejs,angular,sql, api ,json,
                #ai,#ia,intelligence artificielle,machine learning,réseau de neurones,réseau neuronal,neural network,data,
                software,informatique,network,tech,dev ,developer,developpeur,development,innovation,#esn,ssii,digital,cyber,algorithm,social media
                seo,#sem,ceo ,cto ,entrepreneur,founder,startup,devops,réalité augmenté,front-end,front end,back-end,back end,fullstack
                github,aws,google,linux,programmation,bot ,jenkins,intellij,docker,
                ecommerce,e-commerce,omnicanal,project man,chef de projet,
                decathlon,ibm,microsoft,ISEN,supinfo,cgi,sopra,capgemini"
  keywords: "JavaScript,Python,Java,j2e,C++,PHP,C#,Shell,TypeScript,Ruby,Jupyter,Objective-C,Powershell,react,vuejs,angular,sql, api ,json,
             #ai,#ia,intelligence artificielle,machine learning,réseau de neurones,réseau neuronal,neural network,data,
             software,informatique,network,tech,dev ,developer,developpeur,development,innovation,#esn,ssii,digital,cyber,algorithm,social media
             seo,#sem,ceo ,cto ,entrepreneur,founder,startup,devops,réalité augmenté,front-end,front end,back-end,back end,fullstack
             github,aws,google,linux,programmation,bot ,jenkins,intellij,docker,
             ecommerce,e-commerce,omnicanal,
             ibm,microsoft,ISEN,supinfo"
  unwantedKeywords: xxx, RT, patriote
  minRatio: 0.5
  maxRatio: 2
  maxDaysSinceLastTweet: 5
  nbBaseFollowers: 20
  terms:
  location: "France,Paris,Lyon,Marseille,Lille,Nice,Toulouse,Bordeaux,Rouen,Strasbourg,Nantes,Metz,Grenoble,Toulon,Montpellier,Nancy,Saint-Étienne,Melun,LeHavre,Tours,Clermont-Ferrand,Orléans,Mulhouse,Rennes,Reims,Caen,Angers,Dijon,Nîmes,Limoges,Aix-en-Provence,Perpignan,Biarritz,Brest,LeMans,Amiens,Besançon,Annecy,Calais,Poitiers,Versailles,Kerbrient,Béziers,LaRochelle,Roanne,Bourges,Arras,Troyes,Cherbourg,Agen,Tarbes,Ajaccio,Saint-Brieuc,Nevers,Vichy,Dieppe,Auxerre,Bastia,Châlons-en-Champagne            Belgique,Brussels,Bruxelles,Antwerp,Liège,Gent,Charleroi,Brugge,Namur,Mons,Hasselt,Arlon,Mouscron,Vilvoorde,Virton,Tournai,Leuven,Ieper,Roeselare,Dendermonde,Tielt,Veurne,Oudenaarde,Philippeville,Waremme,Oostende,Wavre,Thuin,Ath,Hannut,Marche-en-Famenne,Tongeren,Maaseik,Mechelen,Lincent,Huy,Nivelles,Aalst,Enghien,Soignies,Dinant,Eeklo,Neufchâteau,Turnhout,Verviers,Kortrijk,Bastogne,Sint-Niklaas,Diksmuide            Canada,Toronto,Montréal,Vancouver,Ottawa,Calgary,Edmonton,Hamilton,Winnipeg,Québec,Oshawa,Kitchener,Halifax,London,Windsor,Victoria,Saskatoon,Barrie,Regina,Sudbury,Abbotsford,Sarnia,Sherbrooke,SaintJohn’s,Kelowna,Trois-Rivières,Kingston,ThunderBay,Moncton,SaintJohn,Nanaimo,Peterborough,Saint-Jérôme,RedDeer,Lethbridge,Kamloops,PrinceGeorge,MedicineHat,Drummondville,Chicoutimi,Fredericton,Chilliwack,NorthBay,Shawinigan-Sud,Cornwall,Joliette,Belleville,           Suisse,Geneva,Zürich,Basel,Bern,Lausanne,Lucerne,Lugano,SanktFiden,Chur,Schaffhausen,Fribourg,Neuchâtel,Tripon,Zug,Frauenfeld,Bellinzona,Aarau,Herisau,Solothurn,Schwyz,Liestal,Delémont,Sarnen,Altdorf,Stansstad,Glarus,Appenzell,Saignelégier,AffolternamAlbis,Cully,Romont,Aarberg,Scuol,Fleurier,Unterkulm,Stans,Lichtensteig,Yverdon-les-Bains,Boudry,Balsthal,Dornach,Lachen,Payerne,Baden,BadZurzach,Tafers,Haslen,Echallens,Rapperswil-Jona,Bulle,Bülach,SanktGallen,Wil,Zofingen,Vevey,Renens,Brugg,Laufenburg,LaChaux-de-Fonds,Andelfingen,Dietikon,Winterthur,Thun,LeLocle,Bremgarten,Tiefencastel,Saint-Maurice,Cernier,Ostermundigen,Estavayer-le-Lac,Frutigen,Muri,Murten,Rheinfelden,Gersau,Schüpfheim,Saanen,Olten,Domat/Ems,Münchwilen,Horgen,Willisau,Rorschach,Morges,Interlaken,Sursee,Küssnacht,Weinfelden,Pfäffikon,Meilen,Langnau,Kreuzlingen,Nidau,Igis,Ilanz,Einsiedeln,Wangen,Hinwil,Hochdorf,Thusis,Lenzburg,Dielsdorf,Mörel-Filet,Münster-Geschinen,Martigny,Brig-Glis,Davos,Uster,Altstätten,Courtelary,Porrentruy"
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

