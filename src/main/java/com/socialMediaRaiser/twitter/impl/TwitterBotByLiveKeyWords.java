package com.socialMediaRaiser.twitter.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialMediaRaiser.twitter.*;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Data
public class TwitterBotByLiveKeyWords extends AbstractTwitterBot {

    private List<User> potentialFollowers = new ArrayList<>();
    List<Long> followedRecently;
    List<Long> ownerFollowingIds;
    private int maxFriendship = 390;
    private int QUEUE_SIZE = 100;
    private int iterations = 0;
    private boolean follow; // @todo in abstract ?
    private boolean saveResults;

    @Override
    public List<User> getPotentialFollowers(Long ownerId, int count, boolean follow, boolean saveResults){
        this.follow = follow;
        this.saveResults = saveResults;

        if(count>maxFriendship){
            count = maxFriendship;
        }

        this.followedRecently = this.getIOHelper().getPreviouslyFollowedIds();
        this.ownerFollowingIds = this.getFollowingIds(ownerId);

        try {
            this.collect(count);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("********************************");
        System.out.println(potentialFollowers.size() + " followers followed / " + iterations + " ("+(potentialFollowers.size()*100)/iterations + "%)");
        System.out.println("********************************");

        return potentialFollowers;
    }


    public void collect(int count){

        final BlockingQueue<String> queue = new LinkedBlockingQueue<>(QUEUE_SIZE);
        final StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();

        endpoint.trackTerms(Arrays.asList(FollowProperties.targetProperties.getKeywords()));
        endpoint.languages(Arrays.asList(FollowProperties.targetProperties.getLanguage()));

        final Client client = new ClientBuilder()
                .hosts(Constants.STREAM_HOST)
                .endpoint(endpoint)
                .authentication(this.getAuthentication())
                .processor(new StringDelimitedProcessor(queue))
                .build();

        client.connect();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        while (!client.isDone() && potentialFollowers.size()<count) {
            if(queue.size()>0){
                try{
                    String queueString = queue.take();
                    Tweet foundedTweet = objectMapper.readValue(queueString, Tweet.class);
                    if(!foundedTweet.matchWords(Arrays.asList(FollowProperties.targetProperties.getUnwantedKeywords()))){
                        this.doActions(foundedTweet);
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }

            }
        }

        client.stop();
    }

    private void doActions(Tweet tweet){
        User user = tweet.getUser();
        iterations++;

        if(ownerFollowingIds.indexOf(user.getId())==-1
                && followedRecently.indexOf(user.getId())==-1
                && potentialFollowers.indexOf(user)==-1
                && user.shouldBeFollowed()){
            if(this.isLanguageOK(user)){
                // this.likeTweet(tweet.getId());
                boolean result = false;
                if(this.follow) {
                    result = this.follow(user.getId());
                }
                if (result || !this.follow) {
                    user.setDateOfFollowNow();
                    potentialFollowers.add(user);
                    if(this.saveResults){
                        this.getIOHelper().addNewFollowerLine(user);
                    }
                } else {
                    System.err.println("error following " + user.getUserName());
                }
                System.out.println(tweet.getText());
                System.out.println("\n-------------");
            }
        }
    }

}
