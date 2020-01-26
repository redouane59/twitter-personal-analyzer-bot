package integration;

import com.socialmediaraiser.core.twitter.User;
import com.socialmediaraiser.core.twitter.helpers.dto.getuser.AbstractUser;
import com.socialmediaraiser.twitterbot.FollowProperties;
import com.socialmediaraiser.twitterbot.RandomForestAlgoritm;
import com.socialmediaraiser.twitterbot.UnfollowLauncher;
import com.socialmediaraiser.twitterbot.impl.TwitterBotByInfluencers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RandomForestAlgorithmTest {

    String userName = "RedouaneBali";

    @BeforeEach
    void init() throws Exception {
        FollowProperties.load(userName);
        RandomForestAlgoritm.process();
    }
  /*  @Test
    void testFalse() {
        AbstractUser user = new TwitterBotByInfluencers(userName, false, false).getTwitterHelper().getUserFromUserName("blevy90");
        assertEquals(false, user.getRandomForestPrediction());
    } */

  /*  @Test
    void testTrue() {
        // Followers	Followings	NbDaySinceLastTweet	CommonFollowers	DateOfFollow	Tweets	YearsSinceCreation
        // 4837	2448	4	2	2019/05/28 21:32	8426	8
        Calendar cal = Calendar.getInstance();
        cal.set(2019, 4, 28, 21, 32);
        Date followDate = cal.getTime();
        User user = User.builder()
                .followersCout(4837)
                .followingCount(2448)
                .lastUpdate(UnfollowLauncher.yesterday(4))
                .commonFollowers(2)
                .dateOfFollow(followDate)
                .statusesCount(8426)
                .dateOfCreation(UnfollowLauncher.yesterday(8*365))
                .build();
        assertEquals(true, user.getRandomForestPrediction());
    } */
}
