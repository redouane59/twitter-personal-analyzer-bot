package com.socialmediaraiser.twitterbot.impl.followingBot;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.socialmediaraiser.twitter.TwitterClient;
import com.socialmediaraiser.twitter.helpers.RequestHelper;
import com.socialmediaraiser.twitter.dto.tweet.ITweet;
import com.socialmediaraiser.twitter.dto.user.IUser;
import com.socialmediaraiser.twitterbot.AbstractTwitterFollowBot;
import com.socialmediaraiser.twitterbot.FollowProperties;
import com.socialmediaraiser.twitterbot.GoogleSheetHelper;
import com.socialmediaraiser.twitterbot.impl.User;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Client;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.OAuth1;
import lombok.Getter;
import lombok.Setter;
import lombok.CustomLog;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


@Setter
@Getter
@CustomLog
public class TwitterBotByLiveKeyWords extends AbstractTwitterFollowBot {

    private int maxFriendship = 390;
    private int queueSize = 100;
    private int iterations = 0;
    private boolean saveResults;
    private Client client;

    public TwitterBotByLiveKeyWords(String ownerName, boolean follow, boolean saveResults) {
        super(ownerName, follow, saveResults);
    }

    @Override
    public List<IUser> getPotentialFollowers(String ownerId, int count){
        this.saveResults = saveResults;

        if(count>maxFriendship){
            count = maxFriendship;
        }

        this.setOwnerFollowingIds(this.getTwitterClient().getFollowingIds(ownerId));

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
                    .authentication(new OAuth1(
                            RequestHelper.TWITTER_CREDENTIALS.getApiKey(),
                            RequestHelper.TWITTER_CREDENTIALS.getApiSecretKey(),
                            RequestHelper.TWITTER_CREDENTIALS.getAccessToken(),
                            RequestHelper.TWITTER_CREDENTIALS.getAccessTokenSecret()))
                    .processor(new StringDelimitedProcessor(queue))
                    .build();
            client.connect();
        }

        TwitterClient.OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        int nbFollows = new GoogleSheetHelper(this.getOwnerName()).getPreviouslyFollowedIds(true, true, new Date()).size();

        while (!client.isDone() && (nbFollows+this.getPotentialFollowers().size())<count) {
            if(!queue.isEmpty()){
                LOGGER.info(()->"SMR - queue > 0");
                try{
                    String queueString = queue.take();
                    ITweet foundedTweet = TwitterClient.OBJECT_MAPPER.readValue(queueString, ITweet.class);
                    LOGGER.info(()->"SMR - analysing tweet from " + foundedTweet.getUser().getName() + " : "
                            + foundedTweet.getText() + " ("+foundedTweet.getLang()+")");
                    if(!this.matchWords(foundedTweet,Arrays.asList(FollowProperties.getTargetProperties().getUnwantedKeywords()))){
                        this.doActions(foundedTweet);
                    }
                } catch(Exception e){
                    LOGGER.severe(e.getMessage());
                }

            }
        }

        client.stop();
    }

    private void doActions(ITweet tweet){
        User user = new User(tweet.getUser());
        iterations++;
        if(this.shouldFollow(user)){
            LOGGER.info(()->"SMR - checking language...");
            if(this.isLanguageOK(user)){
                // this.likeTweet(tweet.getId());
                if(this.isFollow()) {
                    getTwitterClient().follow(user.getId());
                    LOGGER.info(user.getName() + " followed ");
                }
                if (!this.isFollow()) {
                    user.setDateOfFollowNow();
                    this.getPotentialFollowers().add(user);
                    if(this.saveResults){
                        this.getIoHelper().addNewFollowerLine(user);
                    }
                } else {
                    LOGGER.severe(()->"error following " + user.getName());
                }
                LOGGER.info(tweet.getText());
                LOGGER.info(()->"\n-------------");
            }
        }
    }



}
