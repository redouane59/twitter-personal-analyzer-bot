import twitter.Application;
import twitter.RelationType;

import java.util.List;

public class Main {

    public static void main(String[] args) throws IllegalAccessException {

        Application application = new Application();
        String tweetName = "RedTheOne";
        List<String> potentialFollowersFromFollowerFollowers = application.getPotentialFollowers(tweetName, RelationType.FOLLOWER, RelationType.FOLLOWER);
        List<String> result = application.follow(potentialFollowersFromFollowerFollowers);
      //  List<String> result = application.getUsersNotFollowingBack(tweetName);
        System.out.println(result.size() + " results | " + result);

    }

}