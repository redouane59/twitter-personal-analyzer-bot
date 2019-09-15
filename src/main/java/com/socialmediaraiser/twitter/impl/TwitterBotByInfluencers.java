package com.socialmediaraiser.twitter.impl;

import com.socialmediaraiser.twitter.AbstractTwitterBot;
import com.socialmediaraiser.twitter.FollowProperties;
import com.socialmediaraiser.twitter.helpers.dto.getuser.AbstractUser;
import io.vavr.control.Option;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Getter
@Setter
public class TwitterBotByInfluencers extends AbstractTwitterBot {

    private List<AbstractUser> potentialFollowers = new ArrayList<>();
    private int maxFriendship = 390;
    private static final Logger LOGGER = Logger.getLogger(TwitterBotByInfluencers.class.getName());

    public TwitterBotByInfluencers(String ownerName) {
        super(ownerName);
    }

    @Override
    public List<AbstractUser> getPotentialFollowers(String ownerId, int count, boolean follow, boolean saveResults){
        this.setFollow(follow);
        if(count>maxFriendship) count = maxFriendship;
        int minOccurence = 0;
        List<AbstractUser> ownerFollowers = this.getFollowerUsers(ownerId);
        // @todo add raw list of incluencers in config
        List<AbstractUser> influencers = new ArrayList<>();
        influencers.addAll(this.getInfluencersFromUsers(ownerFollowers, 150));
        Collections.shuffle(influencers);

        Map<String, Long> sortedPotentialFollowersMap =
                this.getAllFollowerIdsFromUsersSortedByOccurence(ownerId, influencers,
                        FollowProperties.getTargetProperties().getNbBaseFollowers(), minOccurence);

        Iterator<Map.Entry<String, Long>> it = sortedPotentialFollowersMap.entrySet().iterator();
        int iteration = 0;
        int pointLimit = FollowProperties.getScoringProperties().getTotalMaxPoints()*FollowProperties.getTargetProperties().getMinimumPercentMatch()/100;
        int score;
        long startTime = System.currentTimeMillis();
        long time1;
        long time2;
        List<Long> totalTimes = new ArrayList<>();
        String message = "";
        while (it.hasNext() && potentialFollowers.size() < count) {
            this.logCurrentState(iteration, count, startTime, totalTimes);
            Map.Entry<String, Long> entry = it.next();
            if(entry.getKey()!=null && entry.getValue()!=null){
                String userId = entry.getKey();
                time1 = System.currentTimeMillis();
                /* retrieving the user information */
                AbstractUser potentialFollower = this.getUserFromUserId(userId); // criticity here (900/15min)
                time1 = (System.currentTimeMillis()-time1);
                time2 = System.currentTimeMillis();
                /* checking if user exist and language is ok */
                message = Option.of(potentialFollower).map(s -> potentialFollower.getUsername()).getOrElse("unknown user");
                if(this.canStudyAccount(potentialFollower)){
                    potentialFollower.setCommonFollowers(Math.toIntExact(entry.getValue()));
                    /* general scoring */
                    score = potentialFollower.getScoringEngine().getUserScore(potentialFollower);
                    message += " : " + score + "/"+pointLimit+ " | ";
                    if(shouldBeFollowed(potentialFollower, score, pointLimit)) {
                        if (this.isFollow()) {
                            AbstractUser user = this.followNewUser(potentialFollower, saveResults);
                            if(user!=null){
                                potentialFollowers.add(user);
                                message += " followed ! ";
                            }
                        } else {
                            message += "potentialFollowers added : " + potentialFollower.getUsername();
                            potentialFollowers.add(potentialFollower);
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
        LOGGER.info(potentialFollowers.size() + " followers followed / "
                + iteration + " users analyzed (" + (potentialFollowers.size()*100)/(double)iteration + " %) in "
                + totalTimes.stream().mapToLong(l -> l).sum()/(long)(1000*60) + " min");
        LOGGER.info(()->"********************************");

        return potentialFollowers;
    }

    private boolean shouldBeFollowed(AbstractUser potentialFollower, int score, int pointLimit){
        return score >= pointLimit
                && potentialFollower.isLanguageOK()
                &&(!FollowProperties.getIoProperties().isUseRFA() || potentialFollower.getRandomForestPrediction());
    }

    private boolean canStudyAccount(AbstractUser potentialFollower){
        return (potentialFollower!=null
                && !potentialFollower.isProtectedAccount()
                && !potentialFollower.getUsername().equals(this.getOwnerName()));
    }
    private void logCurrentState(int iteration, int count, long startTime, List<Long> totalTimes){
        if(iteration%50==0 && iteration>0){
            LOGGER.info("*** " + potentialFollowers.size() + "/" + count + " followers found  in "
                    +  (System.currentTimeMillis()-startTime)/(long)(1000*60) + "min" + " | i="+iteration
                    + " averrage time : " + totalTimes.stream().mapToLong(l -> l).average().getAsDouble()+" ***");
        }
    }

    private List<AbstractUser> getInfluencersFromUsers(List<AbstractUser> users, int count){
        List<AbstractUser> followersInfluencers = new ArrayList<>();
        AbstractUser user;
        int i=0;
        // building influencers list
        while(i< users.size() && followersInfluencers.size() < count){
            user = users.get(i);
            if(user.isInfluencer() && user.isLanguageOK()){
                followersInfluencers.add(user);
            }
            i++;
        }

        return followersInfluencers;
    }


    // id, occurencies
    private Map<String, Long> getAllFollowerIdsFromUsersSortedByOccurence(String ownerId, List<AbstractUser> followers, int nbFollowersMaxtoWatch, int minOccurence){
        List<String> ownerFollowingIds = this.getFollowingIds(ownerId);
        ownerFollowingIds.add(ownerId);
        List<String> followedRecently = this.getIoHelper().getPreviouslyFollowedIds();
        // building influencers followers list
        List<String> influencersFollowersIds = new ArrayList<>();
        AbstractUser user;
        int i=0;
        while(i<followers.size() && i<nbFollowersMaxtoWatch){
            user = followers.get(i);
            List<String> currentFollowersInfluencersFollowersId = this.getFollowerIds(user.getId());
            for(String userId : currentFollowersInfluencersFollowersId){
                if(ownerFollowingIds.indexOf(userId)==-1 && followedRecently.indexOf(userId)==-1) {
                    influencersFollowersIds.add(userId);
                }
            }
            LOGGER.info(user.getUsername() + " (" + currentFollowersInfluencersFollowersId.size() + " followers)");
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
}
