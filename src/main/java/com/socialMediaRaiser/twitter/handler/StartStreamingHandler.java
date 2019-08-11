package com.socialMediaRaiser.twitter.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialMediaRaiser.twitter.impl.TwitterBotByLiveKeyWords;
import lombok.Getter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class StartStreamingHandler implements RequestHandler<String, String> {

    @Getter
    private LambdaLogger logger;

    private final String OWNER_ID = "owner_id";
    private final String COUNT = "count";


    @Override
    public String handleRequest(String inputString, Context context) {
        this.logger = context.getLogger();
        this.logger.log("\ninputStream : " + inputString);
        Map<String, String> map = null;
    /*    try {
            map = new ObjectMapper().readValue(inputString, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(e.getMessage());
            return "Failed";
        } */
        this.logger.log("\ninputObject : " + map);
        TwitterBotByLiveKeyWords bot = new TwitterBotByLiveKeyWords("RedouaneBali");
       // Long ownerId = Long.valueOf(map.get(OWNER_ID));
        Long ownerId = 1120050519182016513L;
      //  int count = Integer.valueOf(map.get(COUNT));
        int count = 300;
        System.out.println("start working with " + ownerId + " for " + count + " followers...");
        bot.getPotentialFollowers(ownerId, count,true,true);
        return "Succeeded";
    }
}
