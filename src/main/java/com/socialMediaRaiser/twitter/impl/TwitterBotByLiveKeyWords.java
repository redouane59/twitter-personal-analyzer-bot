package com.socialMediaRaiser.twitter.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.socialMediaRaiser.twitter.AbstractTwitterBot;
import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.Tweet;
import com.socialMediaRaiser.twitter.User;
import com.socialMediaRaiser.twitter.helpers.GoogleSheetHelper;
import com.socialMediaRaiser.twitter.helpers.JsonHelper;
import com.socialMediaRaiser.twitter.helpers.dto.getUser.AbstractUser;
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

@Setter
@Getter
public class TwitterBotByLiveKeyWords extends AbstractTwitterBot {

    private List<AbstractUser> potentialFollowers = new ArrayList<>();
    List<String> followedRecently;
    List<String> ownerFollowingIds;
    private int maxFriendship = 390;
    private int QUEUE_SIZE = 100;
    private int iterations = 0;
    private boolean saveResults;
    private Client client;

    public TwitterBotByLiveKeyWords(String ownerName) {
        super(ownerName);
    }

    @Override
    public List<AbstractUser> getPotentialFollowers(String ownerId, int count, boolean follow, boolean saveResults){
        this.setFollow(follow);
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

        System.out.println("SMR - tracking terms : ");
        Arrays.asList(FollowProperties.targetProperties.getKeywords()).forEach(System.out::println);

        System.out.println("SMR - tracking languages : ");
        Arrays.asList(FollowProperties.targetProperties.getLanguage()).forEach(System.out::println);

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

        while (!client.isDone() && (nbFollows+potentialFollowers.size())<count) {
            if(queue.size()>0){
                System.out.println("SMR - queue > 0");
                try{
                    String queueString = queue.take();
                    Tweet foundedTweet = JsonHelper.OBJECT_MAPPER.readValue(queueString, Tweet.class);
                    System.out.println("SMR - analysing tweet from " + foundedTweet.getUser().getUsername() + " : "
                            + foundedTweet.getText() + " ("+foundedTweet.getLang()+")");
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
        AbstractUser user = tweet.getUser();
        iterations++;
        if(ownerFollowingIds.indexOf(user.getId())==-1
                && followedRecently.indexOf(user.getId())==-1
                && potentialFollowers.indexOf(user)==-1
                && user.shouldBeFollowed(this.getOwnerName())){
            System.out.println("SMR - checking language...");
            if(user.isLanguageOK()){
                // this.likeTweet(tweet.getId());
                boolean result = false;
                if(this.isFollow()) {
                    result = this.follow(user.getId());
                    System.out.println(user.getUsername() + " followed " + result);
                }
                if (result || !this.isFollow()) {
                    user.setDateOfFollowNow();
                    potentialFollowers.add(user);
                    if(this.saveResults){
                        this.getIOHelper().addNewFollowerLine(user);
                    }
                } else {
                    System.err.println("error following " + user.getUsername());
                }
                System.out.println(tweet.getText());
                System.out.println("\n-------------");
            } else{

            }
        }
    }

}
