package com.socialMediaRaiser.twitter.helpers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.socialMediaRaiser.twitter.Tweet;
import com.socialMediaRaiser.twitter.User;

import java.io.IOException;

public class TweetDeserializer extends JsonDeserializer<Tweet>
{

    private final String createdAt = "created_at";
    @Override
    public Tweet deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException
    {
        JsonNode tweetNode = parser.readValueAsTree();
        JsonNode userNode = tweetNode.get("user");

        User user = User.builder()
                .id(userNode.get("id").asText())
                .userName(userNode.get("screen_name").asText())
                .followingCount(userNode.get("friends_count").asInt())
                .followersCout(userNode.get("followers_count").asInt())
                .statusesCount(userNode.get("statuses_count").asInt())
                .favouritesCount(userNode.get("favourites_count").asInt())
                .location(userNode.get("location").asText())
                .description(userNode.get("description").asText())
                .dateOfCreation(JsonHelper.getDateFromTwitterString(userNode.get(createdAt).asText()))
                .lastUpdate(JsonHelper.getDateFromTwitterString(tweetNode.get(createdAt).asText()))
                .lang(tweetNode.get("lang").asText())
                .build();

        Tweet tweet = Tweet.builder()
                .id(tweetNode.get("id").asText())
                .text(tweetNode.get("text").asText())
                .lang(tweetNode.get("lang").asText())
                .favorite_count(tweetNode.get("favorite_count").asInt())
                .retweet_count(tweetNode.get("retweet_count").asInt())
                .reply_count(tweetNode.get("reply_count").asInt())
                .user(user)
                .created_at(JsonHelper.getDateFromTwitterString(tweetNode.get(createdAt).asText()))
                .build();

        return tweet;
    }
}
