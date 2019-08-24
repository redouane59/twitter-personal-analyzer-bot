package com.socialMediaRaiser.twitter.helpers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialMediaRaiser.twitter.Tweet;
import com.socialMediaRaiser.twitter.User;
import com.socialMediaRaiser.twitter.helpers.dto.getUser.*;
import io.vavr.control.Option;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JsonHelper {

    public static ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

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
        if(jsonObject==null) {
            return null;
        }
        String id = jsonObject.get(ID).asText();
        String screenName = jsonObject.get(SCREEN_NAME).asText();
        int followersCount =jsonObject.get(FOLLOWER_COUNT).asInt();
        int friendsCount = jsonObject.get(FRIENDS_COUNT).asInt();
        int statuses_count = jsonObject.get(STATUSES_COUNT).asInt();
        String created_at = jsonObject.get(CREATED_AT).asText();
        String description = jsonObject.get(DESCRIPTION).asText();
        int favourites_count = jsonObject.get(FAVOURITES_COUNT).asInt();
        String location = Option.of(jsonObject.get(LOCATION)).map(s -> jsonObject.get(LOCATION).asText()).getOrElse("");
        String lastUpdate = Option.of(jsonObject.has(STATUS)).map(s -> (jsonObject.get(STATUS)).get(CREATED_AT).asText()).getOrNull();
        return User.builder()
                .id(id)
                .userName(screenName)
                .followersCout(followersCount)
                .followingCount(friendsCount)
                .statusesCount(statuses_count)
                .dateOfCreation(getDateFromTwitterString(created_at))
                .description(description)
                .favouritesCount(favourites_count)
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
                .lastUpdate(lastUpdate)
                .protectedAccount(data.isProtectedAccount())
                .build();
    }

    @Deprecated
    public Long getLongFromCursorObject(JsonNode response){
        if(response==null){
            System.err.println("result null");
            return null;
        }
        return response.get(NEXT_CURSOR).asLong();
    }

    public static Date getDateFromTwitterString(String date)
    {
        if(date==null){
            return null;
        }

        try {
            final String TWITTER = "EEE MMM dd HH:mm:ss Z yyyy";
            SimpleDateFormat sf = new SimpleDateFormat(TWITTER, Locale.ENGLISH);
            sf.setLenient(true);
            return sf.parse(date);
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Date getDateFromTwitterDateV2(String date)
    {
        if(date==null){
            return null;
        }

        try {
            return Date.from(Instant.parse(date));
        } catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Deprecated
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
                try{
                    user = jsonResponseToUser(JsonHelper.OBJECT_MAPPER.readTree(node.get(USER).asText()));
                    user.setLastUpdate(createdAtDate);
                } catch (Exception e){
                    System.err.println(e);
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
