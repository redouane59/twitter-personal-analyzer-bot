package com.socialmediaraiser.twitter.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.socialmediaraiser.twitter.impl.TwitterBotByLiveKeyWords;
import lombok.Getter;

import java.util.Map;

public class StartStreamingHandler implements RequestHandler<String, String> {

    @Getter
    private LambdaLogger logger;

    @Override
    public String handleRequest(String inputString, Context context) {
        this.logger = context.getLogger();
        this.logger.log("\ninputStream : " + inputString);
        Map<String, String> map = null;
    /*    try {
            map = JsonHelper.OBJECT_MAPPER.readValue(inputString, Map.class);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
            logger.log(e.getMessage());
            return "Failed";
        } */
        this.logger.log("\ninputObject : " + map);
        TwitterBotByLiveKeyWords bot = new TwitterBotByLiveKeyWords("RedouaneBali", true, true);
       // String ownerId = Long.valueOf(map.get(OWNER_ID));
        String ownerId = "1120050519182016513";
      //  int count = Integer.parseInt(map.get(COUNT));
        int count = 300;
        this.logger.log("start working with " + ownerId + " for " + count + " followers...");
        bot.getPotentialFollowers(ownerId, count);
        return "Succeeded";
    }
}
