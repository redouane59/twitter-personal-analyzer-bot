package com.socialmediaraiser.twitterbot;

import com.fasterxml.jackson.databind.JsonNode;
import com.socialmediaraiser.core.RelationType;
import com.socialmediaraiser.twitterbot.FollowProperties;
import com.socialmediaraiser.core.twitter.Tweet;
import com.socialmediaraiser.core.twitter.TwitterHelper;
import com.socialmediaraiser.core.twitter.User;
import com.socialmediaraiser.core.twitter.helpers.*;
import com.socialmediaraiser.core.twitter.helpers.dto.ConverterHelper;
import com.socialmediaraiser.core.twitter.helpers.dto.getuser.AbstractUser;
import com.socialmediaraiser.core.twitter.helpers.dto.getuser.TweetDataDTO;
import com.socialmediaraiser.twitterbot.scoring.Criterion;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import io.vavr.control.Option;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Data
public abstract class AbstractTwitterBot {

    private static final Logger LOGGER = Logger.getLogger(com.socialmediaraiser.twitterbot.AbstractTwitterBot.class.getName());
    private AbstractIOHelper ioHelper;

    public abstract List<AbstractUser> getPotentialFollowers(String ownerId, int count);
    private String ownerName;
    private List<AbstractUser> potentialFollowers = new ArrayList<>();
    List<String> followedRecently;
    List<String> ownerFollowingIds;
    private boolean follow; // if try will follow users
    private boolean saveResults;
    private URLHelper urlHelper = new URLHelper();
    private RequestHelper requestHelper = new RequestHelper();
    private JsonHelper jsonHelper = new JsonHelper();
    private TwitterHelper twitterHelper;
    private static final String IDS = "ids";
    private static final String USERS = "users";
    private static final String CURSOR = "cursor";
    private static final String NEXT = "next";
    private static final String RETWEET_COUNT = "retweet_count";
    private static final String RELATIONSHIP = "relationship";
    private static final String FOLLOWING = "following";
    private static final String FOLLOWED_BY = "followed_by";
    private static final String SOURCE = "source";
    //   private static final int MAX_GET_F_CALLS = 30;

    public AbstractTwitterBot(String ownerName, boolean follow, boolean saveResults) {
        this.ioHelper = new GoogleSheetHelper(ownerName);
        this.ownerName = ownerName;
        this.twitterHelper = new TwitterHelper(ownerName);
        this.follow = follow;
        this.saveResults = saveResults;
        this.followedRecently = this.getIoHelper().getPreviouslyFollowedIds();
        this.ownerFollowingIds = getTwitterHelper().getFollowingIds(getTwitterHelper().getUserFromUserName(ownerName).getId());
    }

    public void checkNotFollowBack(String ownerName, Date date, boolean override) {
        List<String> followedPreviously = this.getIoHelper().getPreviouslyFollowedIds(override, override, date);
        if(followedPreviously!=null && !followedPreviously.isEmpty()){
            AbstractUser user = getTwitterHelper().getUserFromUserName(ownerName);
            this.areFriends(user.getId(), followedPreviously, this.isFollow(), this.isSaveResults());
        } else{
            LOGGER.severe(()->"no followers found at this date");
        }
    }

    public AbstractUser followNewUser(AbstractUser potentialFollower){
        boolean result = getTwitterHelper().follow(potentialFollower.getId());
        if (result) {
            potentialFollower.setDateOfFollowNow();
            if (this.saveResults) {
                this.getIoHelper().addNewFollowerLine(potentialFollower);
            }
            return potentialFollower;
        }
        return null;
    }

    // @todo remove count
    // date with yyyyMMddHHmm format
    public List<Tweet> searchForTweets(String query, int count, String fromDate, String toDate){

        if(count<10){
            count = 10;
            LOGGER.severe(()->"count minimum = 10");
        }
        if(count>100){
            count = 100;
            LOGGER.severe(()->"count maximum = 100");
        }
        String url = this.getUrlHelper().getSearchTweets30daysUrl();
        Map<String, String> parameters = new HashMap<>();
        parameters.put("query",query);
        parameters.put("maxResults",String.valueOf(count));
        parameters.put("fromDate",fromDate);
        parameters.put("toDate",toDate);

        String next;
        List<Tweet> result = new ArrayList<>();
        do {
            JsonNode response = this.getRequestHelper().executePostRequest(url,parameters);
            JsonNode responseArray = null;
            try {
                responseArray = JsonHelper.OBJECT_MAPPER.readTree(response.get("results").toString());
            } catch (IOException e) {
                LOGGER.severe(e.getMessage());
            }

            if(response!=null && response.size()>0){
                result.addAll(this.getJsonHelper().jsonResponseToTweetListV2(responseArray));
            } else{
                LOGGER.severe(()->"response null or ids not found !");
            }

            if(!response.has(NEXT)){
                break;
            }
            next = response.get(NEXT).toString();
            parameters.put(NEXT, next);
        }
        while (next!= null && result.size()<count);
        return result;
    }

    protected Authentication getAuthentication(){
        return new OAuth1(
                FollowProperties.getTwitterCredentials().getConsumerKey(),
                FollowProperties.getTwitterCredentials().getConsumerSecret(),
                FollowProperties.getTwitterCredentials().getAccessToken(),
                FollowProperties.getTwitterCredentials().getSecretToken());
    }

    private void logError(Exception e, String response){
        LOGGER.severe(() -> e.getMessage() + " response = " + response);
    }

    public void unfollowAllUsersFromCriterion(Criterion criterion, int value, boolean unfollow){
        int maxUnfollows = 400;
        int nbUnfollows = 0;
        for(String id : this.ownerFollowingIds){
            AbstractUser user = getTwitterHelper().getUserFromUserId(id);
            if(nbUnfollows>=maxUnfollows) break;
            if(unfollow && this.shouldBeUnfollowed(user, criterion, value)){
                boolean result = getTwitterHelper().unfollow(user.getId());
                nbUnfollows++;
                LOGGER.info(()-> user.getUsername() + " -> unfollowed");
                if(!result){
                    LOGGER.severe(() -> "error unfollowing " + user.getUsername());
                }
            }
        }
        LOGGER.info(nbUnfollows + " users unfollowed");
    }

    public boolean shouldFollow(AbstractUser user){
        return (ownerFollowingIds.indexOf(user.getId())==-1
                && followedRecently.indexOf(user.getId())==-1
                && potentialFollowers.indexOf(user)==-1
                && this.shouldBeFollowed(user, this.getOwnerName()));
    }

    protected LinkedHashMap<String, Boolean> areFriends(String userId, List<String> otherIds, boolean unfollow, boolean writeOnSheet){
        LinkedHashMap<String, Boolean> result = new LinkedHashMap<>();
        int nbUnfollows = 0;
        for(String otherId : otherIds){
            boolean userShouldBeUnfollowed = shouldBeUnfollowed(userId, otherId, writeOnSheet);
            if(unfollow && userShouldBeUnfollowed){
                boolean unfollowResult = this.getTwitterHelper().unfollow(otherId);
                if(unfollowResult) {
                    nbUnfollows++;
                    this.temporiseUnfollows(nbUnfollows);
                }
            }
            result.put(otherId, !userShouldBeUnfollowed);
        }
        LOGGER.info("Follow back : " + ((otherIds.size()-nbUnfollows)*100)/otherIds.size() + "% (nb unfollows : " + nbUnfollows+ ")");
        return result;
    }

    private boolean shouldBeUnfollowed(String userId, String otherId, boolean writeOnSheet){
        RelationType relation = this.getTwitterHelper().getRelationType(userId, otherId);
        if(relation==null) LOGGER.severe(() -> "null value for" + otherId);

        Boolean userFollowsBack = false;
        if(relation == RelationType.FRIENDS || relation == RelationType.FOLLOWER){
            userFollowsBack = true;
        }

        if(writeOnSheet){
            this.getIoHelper().updateFollowBackInformation(otherId, userFollowsBack);
        }

        return !userFollowsBack && relation == RelationType.FOLLOWING;
    }

    private void temporiseUnfollows(int nbUnfollows){
        if(nbUnfollows%5==0){
            try {
                LOGGER.info(()->"Sleeping 5sec");
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                LOGGER.severe(e.getMessage());
                Thread.currentThread().interrupt();
            }
        }
    }

    public Map<String, Integer> getNbInterractions(Date limitDate, String userName) throws IOException, ParseException {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("tweet.json").getFile());
        TweetDataDTO[] tweets = JsonHelper.OBJECT_MAPPER.readValue(file, TweetDataDTO[].class);


        Map<String, Integer> result = new HashMap<>();
        Date tweetDate;
        for(TweetDataDTO tweetDataDTO : tweets){
            // checking the reply I gave to other users
            String inReplyUserId = tweetDataDTO.getTweet().getInReplyToUserId();
            tweetDate = JsonHelper.getDateFromTwitterString(tweetDataDTO.getTweet().getCreatedAt());
            if(inReplyUserId!=null){
                if(tweetDate!=null && tweetDate.compareTo(limitDate)>0) {
                    result.put(inReplyUserId, 1+result.getOrDefault(inReplyUserId, 0));
                }
            }
            if(tweetDate!=null && tweetDate.compareTo(limitDate)>0){
                // checking the user who retweeted me
                if(tweetDataDTO.getTweet().getRetweetCount()>0){
                    List<String> retweeterIds = this.getTwitterHelper().getRetweetersId(tweetDataDTO.getTweet().getId());
                    for(String retweeterId : retweeterIds){
                        result.put(retweeterId, 1+result.getOrDefault(retweeterId, 0));
                    }
                }
            }
        }

        Date currentDate = ConverterHelper.minutesBefore(75);
        // 10 requests for 10 weeks
        for(int i=1; i<=10;i++){
            // checking the reploy other gave me (40 days)
            List<Tweet> tweetWithReplies = this.getTwitterHelper().searchForLast100Tweets30days("@"+userName,
                    ConverterHelper.getStringFromDate(currentDate)
            );
            for(Tweet tweet : tweetWithReplies){
                if(!tweet.getText().contains("RT")){
                    result.put(tweet.getUser().getId(), 1+result.getOrDefault(tweet.getUser().getId(), 0));
                }
                currentDate = tweet.getCreatedAt();
            }
        }
        return result;
    }

    public boolean shouldBeFollowed(AbstractUser user, String ownerName){
        if(user.getUsername()!=null && user.getUsername().equals(ownerName)){
            return false;
        }
        return false; // @todo to fix
        //return this.scoringEngine.shouldBeFollowed(user);
    }

    public boolean shouldBeUnfollowed(AbstractUser user, Criterion criterion, int value){
        switch (criterion){
            case LAST_UPDATE:
                if(user.getLastUpdate()!=null) {
                    long nbDaysSinceLastUpdate = (new Date().getTime() - user.getLastUpdate().getTime()) / (24 * 60 * 60 * 1000);
                    if(nbDaysSinceLastUpdate>value){
                        return true;
                    }
                }
                return false;
            case RATIO:
                if(user.getFollowersRatio()<value){
                    return true;
                }
                return false;
            case NB_FOLLOWERS:
                if(user.getFollowersCount()<value){
                    return true;
                }
                return false;
            default:
                LOGGER.severe("Criterion " + criterion.name() + " not found");
                return false;
        }
    }

    public boolean isLanguageOK(AbstractUser user){
        return Option.of(user.getLang())
                .map(b -> user.getLang().equals(FollowProperties.getTargetProperties().getLanguage())).getOrElse(false);
    }

    public boolean getRandomForestPrediction(AbstractUser user){
        user.setDateOfFollowNow();
        return RandomForestAlgoritm.getPrediction(user);
    }

    public boolean isInfluencer(AbstractUser user){
        String descriptionWords = FollowProperties.getTargetProperties().getDescription();
        String[] descriptionWordsSplitted = descriptionWords.split(FollowProperties.getArraySeparator());
        String[] userDescriptionSplitted = user.getDescription().split(" ");

        String locationWords = FollowProperties.getTargetProperties().getLocation();
        String[] locationWordsSplitted = Option.of(locationWords.split(FollowProperties.getArraySeparator())).getOrElse(new String[0]);

        String[] userLocationSplitted = user.getLocation().split(" ");

        boolean matchDescription = false;
        boolean matchLocation = false;

        for(String s :userDescriptionSplitted){
            if(Arrays.stream(descriptionWordsSplitted).anyMatch(s::contains)){
                matchDescription = true;
            }
        }
        for(String s :userLocationSplitted){
            if(Arrays.stream(locationWordsSplitted).anyMatch(s::contains)){
                matchLocation = true;
            }
        }
        return (matchDescription
                && matchLocation
                && user.getFollowersRatio() > FollowProperties.getInfluencerProperties().getMinRatio()
                && user.getFollowersCount()> FollowProperties.getInfluencerProperties().getMinNbFollowers());
    }

}
