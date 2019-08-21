package com.socialMediaRaiser.twitter.impl;

import com.socialMediaRaiser.twitter.AbstractTwitterBot;
import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.User;
import com.socialMediaRaiser.twitter.helpers.dto.IUser;
import com.socialMediaRaiser.twitter.scoring.UserScoringEngine;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
public class TwitterBotByInfluencers extends AbstractTwitterBot {

    private List<IUser> potentialFollowers = new ArrayList<>();
    private int maxFriendship = 390;

    public TwitterBotByInfluencers(String ownerName) {
        super(ownerName);
    }

    @Override
    public List<IUser> getPotentialFollowers(String ownerId, int count, boolean follow, boolean saveResults){
        if(count>maxFriendship) count = maxFriendship;
        int minOccurence = 0;
        List<IUser> ownerFollowers = this.getFollowerUsers(ownerId);
        List<IUser> influencers = this.getInfluencersFromUsers(ownerFollowers, 150);
        Collections.shuffle(influencers);

        Map<String, Long> sortedPotentialFollowersMap =
                this.getAllFollowerIdsFromUsersSortedByOccurence(ownerId, influencers,
                        FollowProperties.targetProperties.getNbBaseFollowers(), minOccurence);

        Iterator<Map.Entry<String, Long>> it = sortedPotentialFollowersMap.entrySet().iterator();
        int iteration = 0;
        int pointLimit = FollowProperties.scoringProperties.getTotalMaxPoints()*FollowProperties.targetProperties.getMinimumPercentMatch()/100;
        int score = 0;
        long startTime = System.currentTimeMillis();
        long time1;
        long time2;
        while (it.hasNext() && potentialFollowers.size() < count) {
            if(iteration%50==0 && iteration>0){
                System.out.println(potentialFollowers.size() + " followers found / " + count + " in "
                        +  (System.currentTimeMillis()-startTime)/(long)1000 + "s" + " | i="+iteration);
            }

            Map.Entry<String, Long> entry = it.next();
            if(entry.getKey()!=null && entry.getValue()!=null){
                String userId = entry.getKey();
                time1 = System.currentTimeMillis();
                /* retrieving the user information */
                User potentialFollower = (User)this.getUserFromUserId(userId); // criticity here (900/15min)
                time1 = (System.currentTimeMillis()-time1)/(long)1000;
                time2 = System.currentTimeMillis();
                /* checking if user exist and language is ok */
                if(potentialFollower!=null
                        && !potentialFollower.getUsername().equals(this.getOwnerName())
                        && potentialFollower.getLang().equals(FollowProperties.targetProperties.getLanguage())){
                    potentialFollower.setCommonFollowers(Math.toIntExact(entry.getValue()));
                    /* general scoring */
                    score = potentialFollower.getScoringEngine().getUserScore(potentialFollower);
                    if(score >= pointLimit){
                        /* RFA */
                        if(!FollowProperties.ioProperties.isUseRFA() || potentialFollower.getRandomForestPrediction()) {
                            if (follow) {
                                boolean result = this.follow(potentialFollower.getId());
                                if (result) {
                                    potentialFollower.setDateOfFollowNow();
                                    potentialFollowers.add(potentialFollower);
                                    if (saveResults) {
                                        this.getIOHelper().addNewFollowerLine(potentialFollower);
                                    }
                                }
                            } else {
                                System.out.println("potentialFollowers added : " + potentialFollower.getUsername());
                                potentialFollowers.add(potentialFollower);
                            }
                        }
                    }
                }
                time2 = (System.currentTimeMillis()-time2)/(long)1000;
                System.out.println("score of " + score + "/"+pointLimit+ " for " + potentialFollower.getUsername()
                        + " | times : " + time1 + "s - " + time2 + "s");
            }
            iteration++;
        }
        System.out.println("********************************");
        System.out.println(potentialFollowers.size() + " followers followed / "
                + iteration + " users analyzed (" + (potentialFollowers.size()*100)/(double)iteration + "%)");
        System.out.println("********************************");

        return potentialFollowers;
    }

    private List<IUser> getInfluencersFromUsers(List<IUser> users, int count){
        List<IUser> followersInfluencers = new ArrayList<>();
        User user;
        int i=0;
        // building influencers list
        while(i< users.size() && followersInfluencers.size() < count){
            user = (User)users.get(i);
            if(user.isInfluencer() && this.isLanguageOK(user)){
                followersInfluencers.add(user);
            }
            i++;
        }

        return followersInfluencers;
    }


    // id, occurencies
    private Map<String, Long> getAllFollowerIdsFromUsersSortedByOccurence(String ownerId, List<IUser> followers, int nbFollowersMaxtoWatch, int minOccurence){
        List<String> ownerFollowingIds = this.getFollowingIds(ownerId);
        ownerFollowingIds.add(ownerId);
        List<String> followedRecently = this.getIOHelper().getPreviouslyFollowedIds();
        // building influencers followers list
        List<String> influencersFollowersIds = new ArrayList<>();
        IUser user;
        int i=0;
        while(i<followers.size() && i<nbFollowersMaxtoWatch){
            user = followers.get(i);
            List<String> currentFollowersInfluencersFollowersId = this.getFollowerIds(user.getId()); // criticity here -> cache
            //  influencersFollowersIds.addAll(currentFollowersInfluencersFollowersId);
            for(String userId : currentFollowersInfluencersFollowersId){
                if(ownerFollowingIds.indexOf(userId)==-1 && followedRecently.indexOf(userId)==-1) {
                    influencersFollowersIds.add(userId);
                }
            }
            System.out.println(user.getUsername() + " (" + currentFollowersInfluencersFollowersId.size() + " followers)");
            i++;
        }
        Map<String, Long> sortedPotentialFollowersMap = influencersFollowersIds.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))// create a map with item, occurence
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .filter(x -> x.getValue()>=minOccurence)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        System.out.println(sortedPotentialFollowersMap.size() + " followers found \n");
        return sortedPotentialFollowersMap;
    }
}
