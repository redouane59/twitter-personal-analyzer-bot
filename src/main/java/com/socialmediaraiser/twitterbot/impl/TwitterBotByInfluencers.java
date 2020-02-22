package com.socialmediaraiser.twitterbot.impl;

import com.socialmediaraiser.twitter.dto.user.IUser;
import com.socialmediaraiser.twitterbot.AbstractTwitterFollowBot;
import com.socialmediaraiser.twitterbot.FollowProperties;
import io.vavr.control.Option;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Getter
@Setter
public class TwitterBotByInfluencers extends AbstractTwitterFollowBot {

    private int maxFriendship = 390;
    private static final Logger LOGGER = Logger.getLogger(TwitterBotByInfluencers.class.getName());

    public TwitterBotByInfluencers(String ownerName, boolean follow, boolean saveResults) {
        super(ownerName, follow, saveResults);
    }

    @Override
    public List<IUser> getPotentialFollowers(String ownerId, int count){
        if(count>maxFriendship) count = maxFriendship;
        int minOccurence = 0;
        Map<String, Long> sortedPotentialFollowersMap =
                this.getAllFollowerIdsFromUsersSortedByOccurence(ownerId,
                        FollowProperties.getTargetProperties().getNbBaseFollowers(), minOccurence);
        int iteration = 0;
        int pointLimit = FollowProperties.getScoringProperties().getTotalMaxPoints()*FollowProperties.getTargetProperties().getMinimumPercentMatch()/100;
        int score;
        long startTime = System.currentTimeMillis();
        long time1;
        long time2;
        List<Long> totalTimes = new ArrayList<>();
        String message = "";
        Iterator<Map.Entry<String, Long>> it = sortedPotentialFollowersMap.entrySet().iterator();
        while (it.hasNext() && this.getPotentialFollowers().size() < count) {
            this.logCurrentState(iteration, count, startTime, totalTimes);
            Map.Entry<String, Long> entry = it.next();
            if(entry.getKey()!=null && entry.getValue()!=null){
                String userId = entry.getKey();
                time1 = System.currentTimeMillis();
                /* retrieving the user information */
                User potentialFollower = new User(getTwitterClient().getUserFromUserId(userId)); // criticity here (900/15min)
                time1 = (System.currentTimeMillis()-time1);
                time2 = System.currentTimeMillis();
                /* checking if user exist and language is ok */
                message = Option.of(potentialFollower).map(s -> potentialFollower.getName()).getOrElse("unknown user");
                if(this.canStudyAccount(potentialFollower)){
                    potentialFollower.setCommonFollowers(Math.toIntExact(entry.getValue()));
                    /* general scoring */
                    //score = potentialFollower.getScoringEngine().getUserScore(potentialFollower);
                    score = 0; // @TODO to fix
                    message += " : " + score + "/"+pointLimit+ " | ";
                    if(shouldBeFollowed(potentialFollower, score, pointLimit)) {
                        if (this.isFollow()) {
                            IUser user = this.followNewUser(potentialFollower);
                            if(user!=null){
                                this.getPotentialFollowers().add(user);
                                message += " followed ! ";
                            }
                        } else {
                            message += "potentialFollowers added : " + potentialFollower.getName();
                            this.getPotentialFollowers().add(potentialFollower);
                        }
                    }
                } else{
                    message += " : KO | ";
                }
                time2 = (System.currentTimeMillis()-time2);
                message += " times : " + time1 + "ms - " + time2 + "ms";
                totalTimes.add((time1+time2));
            }
            iteration++;
            LOGGER.info(message);
        }
        LOGGER.info(()->"********************************");
        LOGGER.info(this.getPotentialFollowers().size() + " followers followed / "
                + iteration + " users analyzed (" + (this.getPotentialFollowers().size()*100)/(double)iteration + " %) in "
                + totalTimes.stream().mapToLong(l -> l).sum()/(long)(1000*60) + " min");
        LOGGER.info(()->"********************************");

        return this.getPotentialFollowers();
    }

    private boolean shouldBeFollowed(IUser potentialFollower, int score, int pointLimit){
        return score >= pointLimit
                && this.isLanguageOK(potentialFollower)
                &&(!FollowProperties.getIoProperties().isUseRFA() || this.getRandomForestPrediction(potentialFollower));
    }

    private boolean canStudyAccount(IUser potentialFollower){
        return (potentialFollower!=null
                && !potentialFollower.isProtectedAccount()
                && !potentialFollower.getName().equals(this.getOwnerName()));
    }
    private void logCurrentState(int iteration, int count, long startTime, List<Long> totalTimes){
        if(iteration%50==0 && iteration>0){
            LOGGER.info("*** " + this.getPotentialFollowers().size() + "/" + count + " followers found  in "
                    +  (System.currentTimeMillis()-startTime)/(long)(1000*60) + "min" + " | i="+iteration
                    + " averrage time : " + totalTimes.stream().mapToLong(l -> l).average().getAsDouble()+" ***");
        }
    }

    private List<IUser> getInfluencersFromUsers(List<IUser> users, int count, boolean shuffle){
        List<IUser> followersInfluencers = new ArrayList<>();
        User user;
        int i=0;
        // building influencers list
        while(i< users.size() && followersInfluencers.size() < count){
            user = new User(users.get(i));
            if(this.isInfluencer(user) && this.isLanguageOK(user)){
                followersInfluencers.add(user);
            }
            i++;
        }
        if(shuffle) Collections.shuffle(followersInfluencers);
        return followersInfluencers;
    }


    // id, occurencies
    private Map<String, Long> getAllFollowerIdsFromUsersSortedByOccurence(String ownerId, int nbFollowersMaxtoWatch, int minOccurence){
        List<IUser> ownerFollowers = getTwitterClient().getFollowerUsers(ownerId);
        this.getOwnerFollowingIds().add(ownerId);
        List<String> followedRecently = this.getIoHelper().getPreviouslyFollowedIds();
        // building influencers followers list
        List<String> influencersFollowersIds = new ArrayList<>();
        IUser influencer;
        int i=0;
        List<IUser> influencers = this.getInfluencersFromConfig();
        influencers.addAll(this.getInfluencersFromUsers(ownerFollowers, nbFollowersMaxtoWatch*10, true));
        while(i<influencers.size() && i<nbFollowersMaxtoWatch){
            influencer = influencers.get(i);
            List<String> influencerFollowersId = getTwitterClient().getFollowerIds(influencer.getId());
            for(String userId : influencerFollowersId){
                if(this.getOwnerFollowingIds().indexOf(userId)==-1 && followedRecently.indexOf(userId)==-1) {
                    influencersFollowersIds.add(userId);
                }
            }
            LOGGER.info(influencer.getName() + " (" + influencerFollowersId.size() + " followers)");
            i++;
        }
        Map<String, Long> sortedPotentialFollowersMap = influencersFollowersIds.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))// create a map with item, occurence
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .filter(x -> x.getValue()>=minOccurence)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        LOGGER.info(()->sortedPotentialFollowersMap.size() + " followers found \n");
        return sortedPotentialFollowersMap;
    }

    public List<IUser> getInfluencersFromConfig(){
        String[] baseList = Option.of(FollowProperties.getInfluencerProperties().getBaseList())
                .map(x -> x.split(FollowProperties.getArraySeparator()))
                .getOrElse(new String[0]);
        List<IUser> influencers = new ArrayList<>();
        for(String s : baseList){
            IUser user = getTwitterClient().getUserFromUserName(s);
            if(user!=null){
                influencers.add(user);
            } else{
              LOGGER.severe("user " + s + " not found");
            }
        }
        return influencers ;
    }

}
