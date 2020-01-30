package unit;

import com.socialmediaraiser.core.twitter.helpers.dto.ConverterHelper;
import com.socialmediaraiser.twitterbot.AbstractTwitterFollowBot;
import com.socialmediaraiser.twitterbot.FollowProperties;
import com.socialmediaraiser.twitterbot.impl.TwitterBotByInfluencers;
import com.socialmediaraiser.twitterbot.scoring.Criterion;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.ParseException;

public class AbstractTwitterBotUnitTest {

    private static String ownerName = "RedTheOne";
    private AbstractTwitterFollowBot twitterBot = new TwitterBotByInfluencers(ownerName, false, false);

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
