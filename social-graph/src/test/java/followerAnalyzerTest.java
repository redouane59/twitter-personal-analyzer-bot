import com.socialmediaraiser.twittersocialgraph.FollowerAnalyzer;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class followerAnalyzerTest {

    @Test
    public void testRelativeRatioWithSameSize(){
        FollowerAnalyzer followerAnalyzer = new FollowerAnalyzer("RedouaneBali");
        Set<String> followers1 = new HashSet<>();
        followers1.add("1");
        followers1.add("2");
        followers1.add("3");
        followers1.add("4");
        Set<String> followers2 = new HashSet<>();
        followers2.add("1");
        followers2.add("2");
        followers2.add("5");
        followers2.add("6");
        int commonUsers = followerAnalyzer.countCommonUsers(followers1, followers2);
        assertEquals(0.5,followerAnalyzer.getRatioRelativeValue(followers1, followers2,commonUsers));
        assertEquals(0.5,followerAnalyzer.getRatioRelativeValue(followers2, followers1,commonUsers));
    }

    @Test
    public void testRelativeRatioWithDifferentSize(){
        FollowerAnalyzer followerAnalyzer = new FollowerAnalyzer("RedouaneBali");
        Set<String> followers1 = new HashSet<>();
        followers1.add("1");
        followers1.add("2");
        followers1.add("3");
        followers1.add("4");
        followers1.add("5");
        followers1.add("6");
        followers1.add("7");
        followers1.add("8");
        Set<String> followers2 = new HashSet<>();
        followers2.add("1");
        followers2.add("2");
        followers2.add("10");
        int commonUsers = followerAnalyzer.countCommonUsers(followers1, followers2);
        double ratio1 = commonUsers / (double)followers1.size();
        double ratio2 = commonUsers / (double)followers2.size();

        assertTrue(followerAnalyzer.getRatioRelativeValue(followers1, followers2,commonUsers)>ratio1);
        assertTrue(followerAnalyzer.getRatioRelativeValue(followers2, followers1,commonUsers)<ratio2);
    }
}
