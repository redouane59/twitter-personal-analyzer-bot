import twitter.Application;

import java.util.List;

public class Main {

    public static void main(String[] args) throws IllegalAccessException {

        Application application = new Application();
        String tweetName = "RedTheOne";
        // List<String> result = application.followAllPotentialFollowersFromCommon(tweetName);

        List<String> result = application.getUsersNotFollowingBack(tweetName);

        System.out.println(result.size() + " results : " + result.toArray());

    }

}