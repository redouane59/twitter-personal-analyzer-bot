import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmediaraiser.twittersocialgraph.FollowerAnalyzer;
import com.socialmediaraiser.twittersocialgraph.impl.GroupEnum;
import com.socialmediaraiser.twittersocialgraph.impl.MatchingNumber;
import com.socialmediaraiser.twittersocialgraph.impl.UserGraph;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserGraphTest {

    @Test
    public void testCustomSerialization() throws JsonProcessingException {
        UserGraph userGraph = new UserGraph("test", GroupEnum.JOURNALISTES_GAUCHE);
        String json = new ObjectMapper().writeValueAsString(userGraph);
        assertTrue(json.contains(String.valueOf(GroupEnum.JOURNALISTES_GAUCHE.getGroupId())));
    }

    @Test
    public void testAnalyzeUser(){
        FollowerAnalyzer analyzer = new FollowerAnalyzer("RedouaneBali");
        List<UserGraph> toCompare = new ArrayList<>();
        toCompare.add(new UserGraph("fabien_gay", GroupEnum.EX_GAUCHE));
        toCompare.add(new UserGraph("rglucks1", GroupEnum.PS));
        toCompare.add(new UserGraph("BalasGuillaume", GroupEnum.PS));
        toCompare.add(new UserGraph("vpecresse", GroupEnum.LR));
        toCompare.add(new UserGraph("GabrielAttal", GroupEnum.LREM));
        toCompare.add(new UserGraph("EPhilippePM", GroupEnum.LREM));
        toCompare.add(new UserGraph("J_Bardella", GroupEnum.EX_DROITE));
        Map<GroupEnum, Double> result = analyzer.analyzeUser("LaurentBouvet",toCompare);
        assertNotNull(result);
    }
}
