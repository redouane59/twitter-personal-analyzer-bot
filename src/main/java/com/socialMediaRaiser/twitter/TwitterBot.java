package com.socialMediaRaiser.twitter;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class TwitterBot extends AbstractTwitterBot{

    private List<User> potentialFollowers = new ArrayList<>();

    public List<User> searchPotentialFollowersFromTargetedFollowerFollowers(String userName, int count, boolean follow){
        User user = this.getUserFromUserName(userName);
        return this.searchPotentialFollowersFromTargetedFollowerFollowers(user.getId(), count, follow);
    }

    public List<User> searchPotentialFollowersFromTargetedFollowerFollowers(Long ownerId, int count, boolean follow){
        List<User> ownerFollowers = this.getFollowerUsers(ownerId); // @Todo criticity here (15/15min)
        List<User> followersInfluencers = new ArrayList<>();
        List<Long> ownerFollowingIds = this.getFollowingIds(ownerId);
        int i=0;
        User user;

        // building influencers list
        while(i< ownerFollowers.size()){
            user = ownerFollowers.get(i);
            if(user.isInfluencer()){
                followersInfluencers.add(user);
            }
            i++;
        }

        Collections.shuffle(followersInfluencers);

        int nbInfluencersToWatch = 20;
        // building influencers followers list
        List<Long> influencersFollowersIds = new ArrayList<>();
        i=0;
        while(i<followersInfluencers.size() && i<nbInfluencersToWatch){
            user = followersInfluencers.get(i);
            Set<Long> currentFollowersInfluencersFollowersId = new HashSet<Long>(this.getFollowerIds(user.getId()));
            System.out.println(user.getScreen_name() + " (" + currentFollowersInfluencersFollowersId.size() + " followers)");
            influencersFollowersIds.addAll(currentFollowersInfluencersFollowersId);
            i++;
        }


        Map<Long, Long> sortedPotentialFollowersMap = influencersFollowersIds.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))// create a map with item, occurence
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .filter(x -> x.getValue()>1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));


        Iterator<Map.Entry<Long, Long>> it = sortedPotentialFollowersMap.entrySet().iterator();
        while (it.hasNext() && potentialFollowers.size() < count) {
            Map.Entry<Long, Long> entry = it.next();

            User potentialFollower = this.getUserFromUserId(entry.getKey());
            potentialFollower.setCommonFollowers(Math.toIntExact(entry.getValue()));
            if (potentialFollower.shouldBeFollowed()
                    && ownerFollowingIds.indexOf(potentialFollower.getId())==-1) { // useless
                if (follow) {
                    LocalDateTime now = LocalDateTime.now();
                    potentialFollower.setDateOfFollow(now.getDayOfMonth() + "/" + now.getMonthValue()
                            + " " + now.getHour() + ":" + now.getMinute());
                    boolean result = this.follow(potentialFollower.getScreen_name());
                    if (result) {
                        potentialFollowers.add(potentialFollower);
                    }
                } else {
                    System.out.println("potentialFollowers added : " + potentialFollower.getScreen_name());
                    potentialFollowers.add(potentialFollower);
                }
            }

            i += entry.getKey() + entry.getValue();
        }


        return potentialFollowers;
    }

    public List<User> searchPotentialFollowersFromRandomFollowerFollowers(String userName, int count, boolean follow){
        User user = this.getUserFromUserName(userName);
        return this.searchPotentialFollowersFromRandomFollowerFollowers(user.getId(), count, follow);
    }

    public List<User> searchPotentialFollowersFromRandomFollowerFollowers(Long ownerId, int count, boolean follow){
        List<User> ownerFollowers = this.getFollowerUsers(ownerId); // @Todo criticity here (15/15min)
        List<Long> ownerFollowingIds = this.getFollowingIds(ownerId);
        Collections.shuffle(ownerFollowers);

        int i=0;
        User user;
        while(i< ownerFollowers.size() && potentialFollowers.size()<count){
            user = ownerFollowers.get(i);
            if(user.shouldBeTakenForItsFollowers()){
                this.addPotentialFollowersFromUserFollowers(user.getScreen_name(), ownerFollowingIds, count, follow);
            }
            i++;
        }
        potentialFollowers.sort(Comparator.comparing(User::getCommonFollowers).reversed());
        return potentialFollowers;
    }

    private void addPotentialFollowersFromUserFollowers(String userName, List<Long> ownerFollowingIds, int count, boolean follow) {
        long startWorkingTime = System.currentTimeMillis();
        List<User> followerFollowers = this.getFollowerUsers(userName, potentialFollowers.size()); //@TODO criticity here (15/15min)
        // @TODO count how many common followers ?
        System.out.println("----- Watching followers of " + userName + "(" + followerFollowers.size() + ") -----");
        int nbUsersAdded = 0;
        int i = 0;
        while (i < followerFollowers.size() && potentialFollowers.size()<count) {
            User potentialFollower = followerFollowers.get(i);
            if (potentialFollower.shouldBeFollowed()
            && ownerFollowingIds.indexOf(potentialFollower.getId())==-1) { // user not already followed
                    int indexInPotentialFollowersList = potentialFollowers.indexOf(potentialFollower);
                    if(indexInPotentialFollowersList==-1){ // not already found
                        nbUsersAdded++;
                        if(follow){
                            LocalDateTime now = LocalDateTime.now();
                            potentialFollower.setDateOfFollow(now.getDayOfMonth()+"/"+now.getMonthValue()
                                            +" "+now.getHour()+":"+now.getMinute());
                            boolean result = this.follow(potentialFollower.getScreen_name());
                            if(result){
                                potentialFollowers.add(potentialFollower);
                            }
                        } else{
                            System.out.println("potentialFollowers.add : " + potentialFollower.getScreen_name());
                            potentialFollowers.add(potentialFollower);
                        }
                    } else{
                        potentialFollowers.get(indexInPotentialFollowersList).incrementCommonFollowers();
                    }
            }
            i++;
        }

        long stopWorkingTime = System.currentTimeMillis();
        System.out.println("***********************************");
        System.out.println(nbUsersAdded + " users added from "
                + userName + " followers in " + (stopWorkingTime-startWorkingTime)/(float)1000 + " s");
        System.out.println("Total = " + potentialFollowers.size());
        System.out.println("***********************************");
    }

    public List<String> getUsersNotFollowingBack(String userName, Boolean unfollow) {
        List<String> notFollowingsBackUsers = new ArrayList<>();
        User user = this.getUserFromUserName(userName);
        List<Long> followingsId = this.getFollowingIds(user.getId());
        int i=0;

        while(i<followingsId.size()){
            Long followingId = followingsId.get(i);
            User following = this.getUserFromUserId(followingId);
            if(following.shouldBeFollowed()){
                Boolean areFriend = this.areFriends(userName, following.getScreen_name());
                if(areFriend!=null && !areFriend) {
                    notFollowingsBackUsers.add(following.getScreen_name());
                    if(unfollow){
                        this.unfollow(following.getScreen_name());
                    }
                }
            };
            i++;
        }

        return notFollowingsBackUsers;
    }

    public List<String> getPotentialFollowersFromRetweet(String userName, Long retweetId) {

        List<User> retweeters = this.getRetweetersUsers(retweetId);
        List<String> potentialFollowers = new ArrayList<>();

        int i=0;
        while(i<retweeters.size() && potentialFollowers.size()<this.getUrlHelper().FOLLOW_MAX_CALLS){
            User retweeter = retweeters.get(i);
            if(retweeter.shouldBeFollowed()
                    &&!this.userIsFollowed(userName, retweeter.getScreen_name())){
                potentialFollowers.add(retweeter.getScreen_name());
            }

            i++;
        }

        return potentialFollowers;
    }


}
