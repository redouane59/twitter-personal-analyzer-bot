package unit;

import com.socialmediaraiser.core.twitter.User;
import com.socialmediaraiser.core.twitter.helpers.dto.ConverterHelper;
import com.socialmediaraiser.core.twitter.helpers.dto.getuser.RequestTokenDTO;
import com.socialmediaraiser.twitterbot.AbstractTwitterBot;
import com.socialmediaraiser.twitterbot.FollowProperties;
import com.socialmediaraiser.twitterbot.impl.TwitterBotByInfluencers;
import com.socialmediaraiser.twitterbot.scoring.Criterion;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class AbstractTwitterBotUnitTest {

    private static String ownerName = "RedTheOne";
    private AbstractTwitterBot twitterBot = new TwitterBotByInfluencers(ownerName, false, false);

    @BeforeAll
    public static void init(){
        FollowProperties.load(ownerName);
    }

   /* @Test
    public void testShouldBeFollowBadLastUpdate2() {
        UserScoringEngine engine = new UserScoringEngine(100);
        User user = new User();
        user.setFollowersCount(1500);
        user.setFollowingCount(1000);
        user.setLang("fr");
        user.setLastUpdate(UnfollowLauncher.yesterday(50));
        user.setScoringEngine(engine);
        assertEquals(false, user.shouldBeFollowed(ownerName));
        assertFalse(engine.shouldBeFollowed(user));
    }

    @Test
    public void testIsUserInfluencer(){
        FollowProperties.getTargetProperties().setDescription("java");
        FollowProperties.getTargetProperties().setLocation("France");
        User user = User.builder().location("France").description("java")
                .followersCout(10000)
                .followingCount(100)
                .build();
        assertTrue(user.isInfluencer());
        user = User.builder().location("Senegal").description("java").build();
        assertFalse(user.isInfluencer());
        user = User.builder().location("Senegal").description("cool").build();
        assertFalse(user.isInfluencer());
        user = User.builder().location("France").description("cool").build();
        assertFalse(user.isInfluencer());
    } */

    @Test
    public void testUserDiffDate0() {
        User user = User.builder()
                .dateOfFollow(new Date())
                .lastUpdate(new Date())
                .build();
        assertEquals(0, user.getDaysBetweenFollowAndLastUpdate());
    }

    @Test
    public void testUserDiffDate7() {
        final Calendar lastUpdate = Calendar.getInstance();
        lastUpdate.add(Calendar.DATE, -7);
        User user = User.builder()
                .dateOfFollow(new Date())
                .lastUpdate(lastUpdate.getTime())
                .build();
        assertEquals(7, user.getDaysBetweenFollowAndLastUpdate());
    }

    @Test
    public void testHashCode() {
        User user = User.builder().id("12345").build();
        User user2 = User.builder().id("23456").build();
        assertNotEquals(user.hashCode(), user2.hashCode());
    }

    @Test
    public void testGetTokens(){
        FollowProperties.getTwitterCredentials().setAccessToken("");
        FollowProperties.getTwitterCredentials().setSecretToken("");
        RequestTokenDTO result = this.twitterBot.getRequestHelper().executeTokenRequest();
        assertTrue(result.getOauthToken().length()>1);
        assertTrue(result.getOauthTokenSecret().length()>1);
    }

    @Test
    @Disabled
    public void testunfollowFromLastUpdateDifference(){
        this.twitterBot.unfollowAllUsersFromCriterion(Criterion.LAST_UPDATE,30, false);
    }

    @Test
    @Disabled
    public void getNbInterractionsTest() throws IOException, ParseException {
        this.twitterBot.getNbInterractions(ConverterHelper.getDateFromString("20200101"), ownerName);
    }

}
