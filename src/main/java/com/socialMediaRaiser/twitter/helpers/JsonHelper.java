package com.socialMediaRaiser.twitter.helpers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.json.Json;
import com.socialMediaRaiser.twitter.Tweet;
import com.socialMediaRaiser.twitter.User;
import com.socialMediaRaiser.twitter.helpers.dto.getUser.*;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

public class JsonHelper {

    public static ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final Logger LOGGER = Logger.getLogger(Json.class.getName());

    @Deprecated
    private static final String STATUSES_COUNT = "statuses_count";
    private static final String CREATED_AT = "created_at";
    private final String SCREEN_NAME = "screen_name";
    private final String USER = "user";
    private final String FOLLOWER_COUNT = "followers_count";
    @Deprecated
    private final String FRIENDS_COUNT = "friends_count";
    private final String FAVOURITES_COUNT = "favourites_count";
    private final String FAVORITE_COUNT = "favorite_count";
    private final String RETWEET_COUNT = "retweet_count";
    private final String DESCRIPTION = "description";
    private final String TEXT = "text";
    private final String STATUS = "status";
    private final String LOCATION = "location";
    private final String ID = "id";
    private final String IDS = "ids";
    private final String LANG = "lang";
    private final String NEXT_CURSOR = "next_cursor";
    public static final String FOLLOWING = "following";

    public List<String> jsonLongArrayToList(JsonNode jsonObject){
        ArrayList<String> listdata = new ArrayList<>();
        for(JsonNode n : jsonObject.get(IDS)){
            listdata.add(n.asText());
        }
        return listdata;
    }

    public List<AbstractUser> jsonUserArrayToList(JsonNode jsonObject){
        ArrayList<AbstractUser> users = new ArrayList<>();
        for(JsonNode node : jsonObject){
            users.add(this.jsonResponseToUser(node));
        }
        return users;
    }

    public User jsonResponseToUser(JsonNode jsonObject){
        if(jsonObject==null) return null;

        String id = jsonObject.get(ID).asText();
        String screenName = Option.of(jsonObject.get(SCREEN_NAME)).map(s -> jsonObject.get(SCREEN_NAME).asText()).getOrNull();
        int followersCount = Option.of(jsonObject.get(FOLLOWER_COUNT)).map(s -> jsonObject.get(FOLLOWER_COUNT).asInt()).getOrElse(0);
        int friendsCount = Option.of(jsonObject.get(FRIENDS_COUNT)).map(s -> jsonObject.get(FRIENDS_COUNT).asInt()).getOrElse(0);
        int statuses_count = Option.of(jsonObject.get(STATUSES_COUNT)).map(s -> jsonObject.get(STATUSES_COUNT).asInt()).getOrElse(0);
        String created_at = Option.of(jsonObject.get(CREATED_AT)).map(s -> jsonObject.get(CREATED_AT).asText()).getOrNull();
        String description = Option.of(jsonObject.get(DESCRIPTION)).map(s -> jsonObject.get(DESCRIPTION).asText()).getOrNull();
        String location = Option.of(jsonObject.get(LOCATION)).map(s -> jsonObject.get(LOCATION).asText()).getOrElse("");
        String lastUpdate = Option.of(jsonObject.get(STATUS)).map(s -> (jsonObject.get(STATUS)).get(CREATED_AT).asText()).getOrNull();
        return User.builder()
                .id(id)
                .userName(screenName)
                .followersCout(followersCount)
                .followingCount(friendsCount)
                .statusesCount(statuses_count)
                .dateOfCreation(getDateFromTwitterString(created_at))
                .description(description)
                .dateOfFollow(null)
                .lastUpdate(getDateFromTwitterString(lastUpdate))
                .location(location)
                .build();
    }

    public AbstractUser jsonResponseToUserV2(String response) throws IOException {
        ObjectMapper objectMapper = JsonHelper.OBJECT_MAPPER;
        UserObjectResponseDTO obj = objectMapper.readValue(response, UserObjectResponseDTO.class);
        UserDTO data = obj.getData().get(0);
        IncludesDTO includes = obj.getIncludes();
        List<TweetDTO> mostRecentTweet = null;
        String lang = null;
        Date lastUpdate = null;
        if(!data.isProtectedAccount() && includes!=null){
            mostRecentTweet = includes.getTweets();
            lang = includes.getTweets().get(0).getLang();
            lastUpdate = getDateFromTwitterDateV2(includes.getTweets().get(0).getCreated_at());
        }
        return User.builder()
                .id(data.getId())
                .userName(data.getUsername())
                .followersCout(data.getStats().getFollowers_count())
                .followingCount(data.getStats().getFollowing_count())
                .statusesCount(data.getStats().getTweet_count())
                .dateOfCreation(getDateFromTwitterDateV2(data.getCreated_at()))
                .description(data.getDescription())
                .dateOfFollow(null)
                .location(data.getLocation())
                .mostRecentTweet(mostRecentTweet)
                .lang(lang)
                .lastUpdate(lastUpdate) // @todo manage null value in scoring ?
                .protectedAccount(data.isProtectedAccount())
                .build();
    }

    @Deprecated
    public Long getLongFromCursorObject(JsonNode response){
        if(response==null){
            LOGGER.severe(()->"result null");
            return null;
        }
        return response.get(NEXT_CURSOR).asLong();
    }

    public static Date getDateFromTwitterString(String date)
    {
        if(date==null) return null;
        final String TWITTER = "EEE MMM dd HH:mm:ss Z yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(TWITTER, Locale.ENGLISH);
        sf.setLenient(true);
        return Try.of(() -> sf.parse(date)).getOrNull();
    }

    public static Date getDateFromTwitterDateV2(String date)
    {
        return Option.of(date).map(d -> Date.from(Instant.parse(date))).getOrNull();
    }

    public List<Tweet> jsonResponseToTweetList(JsonNode jsonArray) {
        List<Tweet> tweets = new ArrayList<>();
        if(jsonArray!=null){
            for(JsonNode node : jsonArray){
                String id = node.get(ID).asText();
                int retweetsCount = node.get(RETWEET_COUNT).asInt();
                int favourites_count = node.get(FAVORITE_COUNT).asInt();
                String text = node.get(TEXT).asText();
                String lang = node.get(LANG).asText();
                Date createdAtDate = getDateFromTwitterString(node.get(CREATED_AT).asText());
                User user = null;
                try{ // @todo to test
                    user = jsonResponseToUser(node.get(USER));
                    user.setLastUpdate(createdAtDate);
                } catch (Exception e){
                    e.printStackTrace();
                }
                Tweet tweet = Tweet.builder()
                        .id(id)
                        .retweet_count(retweetsCount)
                        .favorite_count(favourites_count)
                        .text(text)
                        .lang(lang)
                        .created_at(createdAtDate)
                        .user(user)
                        .build();
                tweets.add(tweet);
            }
        }
        return tweets;
    }

}
