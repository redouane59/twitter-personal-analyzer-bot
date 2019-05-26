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
    @Override
    public Tweet deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException
    {
        JsonNode tweetNode = parser.readValueAsTree();
        JsonNode userNode = tweetNode.get("user");

        User user = User.builder()
                .id(userNode.get("id").asLong())
                .userName(userNode.get("screen_name").asText())
                .followingCount(userNode.get("friends_count").asInt())
                .followerCout(userNode.get("followers_count").asInt())
                .statusesCount(userNode.get("statuses_count").asInt())
                .favouritesCount(userNode.get("favourites_count").asInt())
                .location(userNode.get("location").asText())
                .description(userNode.get("description").asText())
                .dateOfCreation(JsonHelper.getTwitterDate(userNode.get("created_at").asText()))
                .lastUpdate(JsonHelper.getTwitterDate(tweetNode.get("created_at").asText()))
                .lang(tweetNode.get("lang").asText())
                .build();

        Tweet tweet = Tweet.builder()
                .id(tweetNode.get("id").asLong())
                .text(tweetNode.get("text").asText())
                .lang(tweetNode.get("lang").asText())
                .favorite_count(tweetNode.get("favorite_count").asInt())
                .retweet_count(tweetNode.get("retweet_count").asInt())
                .reply_count(tweetNode.get("reply_count").asInt())
                .user(user)
                .created_at(JsonHelper.getTwitterDate(tweetNode.get("created_at").asText()))
                .build();

        return tweet;
    }
}
