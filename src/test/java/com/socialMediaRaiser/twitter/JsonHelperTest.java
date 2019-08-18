package com.socialMediaRaiser.twitter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialMediaRaiser.twitter.helpers.dto.getUser.UserObjectResponseDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonHelperTest {

    private static String ownerName = "RedTheOne";

    @BeforeAll
    public static void init(){
        FollowProperties.load(ownerName);
    }

    @Test
    public void testCastTweetFromString() throws IOException {
        String tweet = "{\"created_at\":\"Sun May 26 16:37:48 +0000 2019\",\"id\":1132687286708178945,\"id_str\":\"1132687286708178945\",\"text\":\"Right @stewartdonald3 , so can you let JR know that we've been here before but our expectations now are 100+ points\\u2026 https:\\/\\/t.co\\/pzuPKcttBk\",\"source\":\"\\u003ca href=\\\"http:\\/\\/twitter.com\\/download\\/android\\\" rel=\\\"nofollow\\\"\\u003eTwitter for Android\\u003c\\/a\\u003e\",\"truncated\":true,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":2897854199,\"id_str\":\"2897854199\",\"name\":\"Tracy\",\"screen_name\":\"tracysweettweet\",\"location\":\"The Edge of Reality\",\"url\":null,\"description\":\"Mam to 2 kids and 1 labrador, pretty happy with life\",\"translator_type\":\"none\",\"protected\":false,\"verified\":false,\"followers_count\":29,\"friends_count\":283,\"listed_count\":0,\"favourites_count\":1589,\"statuses_count\":347,\"created_at\":\"Sat Nov 29 18:42:44 +0000 2014\",\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":false,\"lang\":null,\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"C0DEED\",\"profile_background_image_url\":\"http:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_image_url_https\":\"https:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_tile\":false,\"profile_link_color\":\"1DA1F2\",\"profile_sidebar_border_color\":\"C0DEED\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"profile_image_url\":\"http:\\/\\/pbs.twimg.com\\/profile_images\\/771135820199854083\\/BPszH9Z9_normal.jpg\",\"profile_image_url_https\":\"https:\\/\\/pbs.twimg.com\\/profile_images\\/771135820199854083\\/BPszH9Z9_normal.jpg\",\"profile_banner_url\":\"https:\\/\\/pbs.twimg.com\\/profile_banners\\/2897854199\\/1462485811\",\"default_profile\":true,\"default_profile_image\":false,\"following\":null,\"follow_request_sent\":null,\"notifications\":null},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"is_quote_status\":false,\"extended_tweet\":{\"full_text\":\"Right @stewartdonald3 , so can you let JR know that we've been here before but our expectations now are 100+ points next season and some entertaining bloody football! #STID\",\"display_text_range\":[0,172],\"entities\":{\"hashtags\":[{\"text\":\"STID\",\"indices\":[167,172]}],\"urls\":[],\"user_mentions\":[{\"screen_name\":\"stewartdonald3\",\"name\":\"stewart donald\",\"id\":577978827,\"id_str\":\"577978827\",\"indices\":[6,21]}],\"symbols\":[]}},\"quote_count\":0,\"reply_count\":3,\"retweet_count\":1,\"favorite_count\":2,\"entities\":{\"hashtags\":[],\"urls\":[{\"url\":\"https:\\/\\/t.co\\/pzuPKcttBk\",\"expanded_url\":\"https:\\/\\/twitter.com\\/i\\/web\\/status\\/1132687286708178945\",\"display_url\":\"twitter.com\\/i\\/web\\/status\\/1\\u2026\",\"indices\":[117,140]}],\"user_mentions\":[{\"screen_name\":\"stewartdonald3\",\"name\":\"stewart donald\",\"id\":577978827,\"id_str\":\"577978827\",\"indices\":[6,21]}],\"symbols\":[]},\"favorited\":false,\"retweeted\":false,\"filter_level\":\"low\",\"lang\":\"en\",\"timestamp_ms\":\"1558888668120\"}\n";
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Tweet result = objectMapper.readValue(tweet, Tweet.class);
        assertNotNull(result);
        assertNotNull(result.getLang());
        assertNotNull(result.getId());
        assertNotNull(result.getUser());
        assertNotNull(result.getCreated_at());
        assertNotNull(result.getFavorite_count());
        assertNotNull(result.getRetweet_count());
        assertNotNull(result.getReply_count());
        assertNotNull(result.getText());
    }

    @Test
    public void testParseGetUserResponse() throws IOException {
        String stringResponse = "{\n" +
                "   \"data\":[\n" +
                "      {\n" +
                "         \"id\":\"92073489\",\n" +
                "         \"created_at\":\"2009-11-23T17:53:15.000Z\",\n" +
                "         \"name\":\"Red'1\",\n" +
                "         \"username\":\"RedTheOne\",\n" +
                "         \"protected\":false,\n" +
                "         \"location\":\"Madrid, Espagne\",\n" +
                "         \"url\":\"\",\n" +
                "         \"description\":\"En vérité, j'suis à ma place quand je les dérange. Jamais dans la tendance, toujours dans la bonne direction... #Lille #Montréal #Madrid @Decathlon\uD83D\uDC8D\uD83C\uDDE9\uD83C\uDDFF\uD83C\uDDEE\uD83C\uDDF9\",\n" +
                "         \"verified\":false,\n" +
                "         \"entities\":{\n" +
                "            \"description\":{\n" +
                "               \"hashtags\":[\n" +
                "                  {\n" +
                "                     \"start\":112,\n" +
                "                     \"end\":118,\n" +
                "                     \"tag\":\"Lille\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"start\":119,\n" +
                "                     \"end\":128,\n" +
                "                     \"tag\":\"Montréal\"\n" +
                "                  },\n" +
                "                  {\n" +
                "                     \"start\":129,\n" +
                "                     \"end\":136,\n" +
                "                     \"tag\":\"Madrid\"\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"mentions\":[\n" +
                "                  {\n" +
                "                     \"start\":137,\n" +
                "                     \"end\":147,\n" +
                "                     \"username\":\"Decathlon\"\n" +
                "                  }\n" +
                "               ]\n" +
                "            }\n" +
                "         },\n" +
                "         \"profile_image_url\":\"https://pbs.twimg.com/profile_images/1162352850661445633/c1Rw5BI0_normal.jpg\",\n" +
                "         \"stats\":{\n" +
                "            \"followers_count\":6279,\n" +
                "            \"following_count\":4153,\n" +
                "            \"tweet_count\":35399,\n" +
                "            \"listed_count\":73\n" +
                "         },\n" +
                "         \"most_recent_tweet_id\":\"1162756711595282432\",\n" +
                "         \"pinned_tweet_id\":\"1035192987008020480\",\n" +
                "         \"format\":\"detailed\"\n" +
                "      }\n" +
                "   ],\n" +
                "   \"includes\":{\n" +
                "      \"tweets\":[\n" +
                "         {\n" +
                "            \"id\":\"1162756711595282432\",\n" +
                "            \"created_at\":\"2019-08-17T16:02:57.000Z\",\n" +
                "            \"text\":\"@fbelmadi dédicace spéciale pour toi\",\n" +
                "            \"author_id\":\"92073489\",\n" +
                "            \"in_reply_to_user_id\":\"92073489\",\n" +
                "            \"referenced_tweets\":[\n" +
                "               {\n" +
                "                  \"type\":\"replied_to\",\n" +
                "                  \"id\":\"1162756655945306113\"\n" +
                "               }\n" +
                "            ],\n" +
                "            \"entities\":{\n" +
                "               \"mentions\":[\n" +
                "                  {\n" +
                "                     \"start\":0,\n" +
                "                     \"end\":9,\n" +
                "                     \"username\":\"fbelmadi\"\n" +
                "                  }\n" +
                "               ]\n" +
                "            },\n" +
                "            \"stats\":{\n" +
                "               \"retweet_count\":0,\n" +
                "               \"reply_count\":1,\n" +
                "               \"like_count\":1,\n" +
                "               \"quote_count\":0\n" +
                "            },\n" +
                "            \"possibly_sensitive\":false,\n" +
                "            \"lang\":\"fr\",\n" +
                "            \"source\":\"<a href=\\\"https://mobile.twitter.com\\\" rel=\\\"nofollow\\\">Twitter Web App</a>\",\n" +
                "            \"format\":\"detailed\"\n" +
                "         }\n" +
                "      ]\n" +
                "   }\n" +
                "}";

        UserObjectResponseDTO objectInterpretation = new ObjectMapper().readValue(stringResponse, UserObjectResponseDTO.class);
        assertNotNull(objectInterpretation);
        assertNotNull(objectInterpretation.getData());
        assertNotNull(objectInterpretation.getIncludes());
        assertEquals(112, objectInterpretation.getData().get(0).getEntities().getDescription().getHashtags().get(0).getStart());
        assertTrue("Decathlon".equals(objectInterpretation.getData().get(0).getEntities().getDescription().getMentions().get(0).getUsername()));
        assertEquals(6279, objectInterpretation.getData().get(0).getStats().getFollowers_count());
        assertTrue("92073489".equals(objectInterpretation.getIncludes().getTweets().get(0).getAuthor_id()));
    }

    @Test
    public void testParseGetTweetResponse() throws IOException{

    }
}
