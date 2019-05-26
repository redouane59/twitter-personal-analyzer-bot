package com.socialMediaRaiser.twitter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.socialMediaRaiser.twitter.constants.SignatureConstants;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TwitterStreamCollector {
    static int QUEUE_SIZE = 100;

    public void collect() throws IOException, InterruptedException {

        final BlockingQueue<String> queue = new LinkedBlockingQueue<>(QUEUE_SIZE);
        final StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();

        // add some track terms with this code:
         endpoint.trackTerms(Lists.newArrayList("redtheone", "@redtheone", "lille"));
         List<Long> followings = new ArrayList();
         followings.add(92073489L);
         endpoint.followings(followings);

        ObjectMapper mapper = new ObjectMapper();
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        File from = new File(classLoader.getResource("twitter-client-secret.json").getFile());
        TypeReference<HashMap<String,Object>> typeRef
                = new TypeReference<>() {};

        HashMap<String,Object> o = mapper.readValue(from, typeRef);

        final Authentication auth = new OAuth1(
                o.get(SignatureConstants.CONSUMER_KEY).toString(),
                o.get(SignatureConstants.CONSUMER_SECRET).toString(),
                o.get(SignatureConstants.ACCESS_TOKEN).toString(),
                o.get(SignatureConstants.SECRET_TOKEN).toString());

        // Create a new BasicClient. By default gzip is enabled.
        final Client client = new ClientBuilder()
                .hosts(Constants.STREAM_HOST)
                .endpoint(endpoint)
                .authentication(auth)
                .processor(new StringDelimitedProcessor(queue))
                .build();

        // Establish a connection
        client.connect();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        while (!client.isDone()) {
            if(queue.size()>0){
                System.out.println("size : " + queue.size());
                String s = queue.take();
                try{
                    Tweet tweet = objectMapper.readValue(s, Tweet.class);
                    System.out.println(tweet);
                } catch (Exception e){
                    System.err.println(e);
                }
            }
        }

        client.stop();
        System.out.println("Client stopped, restart needed");
    }
}
