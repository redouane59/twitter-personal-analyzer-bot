package com.socialMediaRaiser.twitter.impl;

import com.socialMediaRaiser.twitter.AbstractTwitterBot;
import com.socialMediaRaiser.twitter.FollowProperties;
import com.socialMediaRaiser.twitter.User;
import com.socialMediaRaiser.twitter.helpers.dto.getUser.AbstractUser;
import io.vavr.control.Option;
import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Setter
public class TwitterBotByInfluencers extends AbstractTwitterBot {

    private List<AbstractUser> potentialFollowers = new ArrayList<>();
    private int maxFriendship = 390;

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
                        FollowProperties.targetProperties.getNbBaseFollowers(), minOccurence);

        Iterator<Map.Entry<String, Long>> it = sortedPotentialFollowersMap.entrySet().iterator();
        int iteration = 0;
        int pointLimit = FollowProperties.scoringProperties.getTotalMaxPoints()*FollowProperties.targetProperties.getMinimumPercentMatch()/100;
        int score;
        long startTime = System.currentTimeMillis();
        long time1;
        long time2;
        List<Long> totalTimes = new ArrayList<>();
        while (it.hasNext() && potentialFollowers.size() < count) {
            if(iteration%50==0 && iteration>0){
                System.out.println("*** " + potentialFollowers.size() + "/" + count + " followers found  in "
                        +  (System.currentTimeMillis()-startTime)/(long)(1000*60) + "min" + " | i="+iteration
                        + " averrage time : " + totalTimes.stream().mapToLong(l -> l).average().getAsDouble()+" ***");
            }

            Map.Entry<String, Long> entry = it.next();
            if(entry.getKey()!=null && entry.getValue()!=null){
                String userId = entry.getKey();
                time1 = System.currentTimeMillis();
                /* retrieving the user information */
                AbstractUser potentialFollower = this.getUserFromUserId(userId); // criticity here (900/15min)
                time1 = (System.currentTimeMillis()-time1);
                time2 = System.currentTimeMillis();
                /* checking if user exist and language is ok */
                System.out.print(Option.of(potentialFollower).map(s -> potentialFollower.getUsername()).getOrElse("unknown user"));
                if(potentialFollower!=null
                        && !potentialFollower.isProtectedAccount()
                        && !potentialFollower.getUsername().equals(this.getOwnerName())){
                    potentialFollower.setCommonFollowers(Math.toIntExact(entry.getValue()));
                    /* general scoring */
                    score = potentialFollower.getScoringEngine().getUserScore(potentialFollower);
                    System.out.print(" : " + score + "/"+pointLimit+ " | ");
                    if(score >= pointLimit){
                        if(potentialFollower.getLang()!=null // to put higher when V2 full imeplemented with lang inside user
                                && potentialFollower.getLang().equals(FollowProperties.targetProperties.getLanguage())){
                            /* RFA */
                            if(!FollowProperties.ioProperties.isUseRFA() || potentialFollower.getRandomForestPrediction()) {
                                if (this.isFollow()) {
                                    boolean result = this.follow(potentialFollower.getId());
                                    if (result) {
                                        System.out.print(" followed ! ");
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
                } else{
                    System.out.print(" : KO | ");
                }
                time2 = (System.currentTimeMillis()-time2);
                System.out.println(" times : " + time1 + "ms - " + time2 + "ms");
                totalTimes.add((time1+time2));
            }
            iteration++;
        }
        System.out.println("********************************");
        System.out.println(potentialFollowers.size() + " followers followed / "
                + iteration + " users analyzed (" + (potentialFollowers.size()*100)/(double)iteration + " %) in "
        + totalTimes.stream().mapToLong(l -> l).sum()/(long)(1000*60) + " min");
        System.out.println("********************************");

        return potentialFollowers;
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
        List<String> followedRecently = this.getIOHelper().getPreviouslyFollowedIds();
        // building influencers followers list
        List<String> influencersFollowersIds = new ArrayList<>();
        AbstractUser user;
        int i=0;
        while(i<followers.size() && i<nbFollowersMaxtoWatch){
            user = followers.get(i);
            List<String> currentFollowersInfluencersFollowersId = this.getFollowerIds(user.getId());
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
