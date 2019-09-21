package com.socialmediaraiser.twitter.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.socialmediaraiser.twitter.AbstractTwitterBot;
import com.socialmediaraiser.twitter.FollowProperties;
import com.socialmediaraiser.twitter.Tweet;
import com.socialmediaraiser.twitter.helpers.GoogleSheetHelper;
import com.socialmediaraiser.twitter.helpers.JsonHelper;
import com.socialmediaraiser.twitter.helpers.dto.getuser.AbstractUser;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

@Setter
@Getter
public class TwitterBotByLiveKeyWords extends AbstractTwitterBot {

    private static final Logger LOGGER = Logger.getLogger(TwitterBotByLiveKeyWords.class.getName());
    private int maxFriendship = 390;
    private int queueSize = 100;
    private int iterations = 0;
    private boolean saveResults;
    private Client client;

    public TwitterBotByLiveKeyWords(String ownerName, boolean follow, boolean saveResults) {
        super(ownerName, follow, saveResults);
    }

    @Override
    public List<AbstractUser> getPotentialFollowers(String ownerId, int count){
        this.saveResults = saveResults;

        if(count>maxFriendship){
            count = maxFriendship;
        }

        this.setOwnerFollowingIds(this.getFollowingIds(ownerId));

        try {
            this.collect(count);
        } catch (Exception e) {
            LOGGER.severe(e.getMessage());
        }

        LOGGER.info(()->"********************************");
        LOGGER.info(()->this.getPotentialFollowers().size() + " followers followed / " + iterations + " ("+(this.getPotentialFollowers().size()*100)/iterations + "%)");
        LOGGER.info(()->"********************************");

        return this.getPotentialFollowers();
    }


    public void collect(int count){

        final BlockingQueue<String> queue = new LinkedBlockingQueue<>(queueSize);
        final StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();

        endpoint.trackTerms(Arrays.asList(FollowProperties.getTargetProperties().getKeywords()));
        endpoint.languages(Arrays.asList(FollowProperties.getTargetProperties().getLanguage()));

        LOGGER.info(()->"SMR - tracking terms : ");
        Arrays.asList(FollowProperties.getTargetProperties().getKeywords()).forEach(LOGGER::info);

        LOGGER.info(()->"SMR - tracking languages : ");
        Arrays.asList(FollowProperties.getTargetProperties().getLanguage()).forEach(LOGGER::info);

        if(client==null || client.isDone()){

            client = new ClientBuilder()
                    .hosts(Constants.STREAM_HOST)
                    .endpoint(endpoint)
                    .authentication(this.getAuthentication())
                    .processor(new StringDelimitedProcessor(queue))
                    .build();
            client.connect();
        }

        JsonHelper.OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        int nbFollows = new GoogleSheetHelper(this.getOwnerName()).getPreviouslyFollowedIds(true, true, new Date()).size();

        while (!client.isDone() && (nbFollows+this.getPotentialFollowers().size())<count) {
            if(!queue.isEmpty()){
                LOGGER.info(()->"SMR - queue > 0");
                try{
                    String queueString = queue.take();
                    Tweet foundedTweet = JsonHelper.OBJECT_MAPPER.readValue(queueString, Tweet.class);
                    LOGGER.info(()->"SMR - analysing tweet from " + foundedTweet.getUser().getUsername() + " : "
                            + foundedTweet.getText() + " ("+foundedTweet.getLang()+")");
                    if(!foundedTweet.matchWords(Arrays.asList(FollowProperties.getTargetProperties().getUnwantedKeywords()))){
                        this.doActions(foundedTweet);
                    }
                } catch(Exception e){
                    LOGGER.severe(e.getMessage());
                }

            }
        }

        client.stop();
    }

    private void doActions(Tweet tweet){
        AbstractUser user = tweet.getUser();
        iterations++;
        if(this.shouldFollow(user)){
            LOGGER.info(()->"SMR - checking language...");
            if(user.isLanguageOK()){
                // this.likeTweet(tweet.getId());
                boolean result = false;
                if(this.isFollow()) {
                    result = this.follow(user.getId());
                    LOGGER.info(user.getUsername() + " followed " + result);
                }
                if (result || !this.isFollow()) {
                    user.setDateOfFollowNow();
                    this.getPotentialFollowers().add(user);
                    if(this.saveResults){
                        this.getIoHelper().addNewFollowerLine(user);
                    }
                } else {
                    LOGGER.severe(()->"error following " + user.getUsername());
                }
                LOGGER.info(tweet.getText());
                LOGGER.info(()->"\n-------------");
            }
        }
    }



}
