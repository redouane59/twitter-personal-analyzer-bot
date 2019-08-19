package com.socialMediaRaiser.twitter.helpers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import com.socialMediaRaiser.twitter.helpers.dto.getUser.TweetDTO;
import com.socialMediaRaiser.twitter.helpers.dto.getUser.TwitterStatsDTO;
import com.socialMediaRaiser.twitter.helpers.dto.getUser.UserDTO;
import com.socialMediaRaiser.twitter.helpers.dto.getUser.UserStatsDTO;


import java.io.IOException;

public class TweetDeserializer extends JsonDeserializer<TweetDTO>
{
    @Override
    public TweetDTO deserialize(JsonParser parser, DeserializationContext context)
            throws IOException, JsonProcessingException
    {
        JsonNode tweetNode = parser.readValueAsTree();
        JsonNode userNode = tweetNode.get("user");

        UserDTO user = UserDTO.builder()
                .id(userNode.get("id").asText())
                .username(userNode.get("screen_name").asText())
                .stats(UserStatsDTO.builder()
                        .followersCount(userNode.get("followersCount").asInt())
                        .followingCount(userNode.get("friends_count").asInt())
                        .tweetCount(userNode.get("statuses_count").asInt())
                        .build())
                .location(userNode.get("location").asText())
                .description(userNode.get("description").asText())
                .dateOfCreation(userNode.get("dateOfCreation").asText())
                .build();

        TweetDTO tweet = TweetDTO.builder()
                .id(tweetNode.get("id").asText())
                .text(tweetNode.get("text").asText())
                .lang(tweetNode.get("lang").asText())
                .stats(TwitterStatsDTO.builder()
                        .like_count(tweetNode.get("favorite_count").asInt())
                        .retweet_count(tweetNode.get("retweet_count").asInt())
                        .reply_count(tweetNode.get("reply_count").asInt())
                        .build()
                )
                .author_id(user.getId())
                .created_at(tweetNode.get("dateOfCreation").asText())
                .build();

        return tweet;
    }
}
