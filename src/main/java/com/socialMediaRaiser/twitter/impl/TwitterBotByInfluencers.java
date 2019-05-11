package com.socialMediaRaiser.twitter.impl;

import com.socialMediaRaiser.twitter.AbstractTwitterBot;
import com.socialMediaRaiser.twitter.User;
import com.socialMediaRaiser.twitter.helpers.GoogleSheetHelper;
import com.socialMediaRaiser.twitter.helpers.IOHelper;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class TwitterBotByInfluencers extends AbstractTwitterBot {

    private List<User> potentialFollowers = new ArrayList<>();
    private GoogleSheetHelper sheetHelper;
    {
        try {
            sheetHelper = new GoogleSheetHelper();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getPotentialFollowers(Long ownerId, int count, boolean follow){
        int nbFollowersMaxToWatch = 20;
        int minOccurence = 2;
        List<User> ownerFollowers = this.getFollowerUsers(ownerId); // criticity here (15/15min)
        List<Long> ownerFollowingIds = this.getFollowingIds(ownerId);
        List<Long> followedRecently = this.getFollowedRecently();
        List<User> influencerFollowers = this.getInfluencersFromFollowers(ownerFollowers);
        Collections.shuffle(influencerFollowers);

        Map<Long, Long> sortedPotentialFollowersMap =
                this.getAllFollowerIdsFromUsersSortedByOccurence(influencerFollowers, nbFollowersMaxToWatch, minOccurence);

        Iterator<Map.Entry<Long, Long>> it = sortedPotentialFollowersMap.entrySet().iterator();
        while (it.hasNext() && potentialFollowers.size() < count) {
            try{
                Map.Entry<Long, Long> entry = it.next();
                if(entry.getKey()!=null && entry.getValue()!=null){
                    Long userId = entry.getKey();
                    if(ownerFollowingIds.indexOf(userId)==-1 && followedRecently.indexOf(userId)==-1){
                        User potentialFollower = this.getUserFromUserId(userId); // criticity here (900/15min)
                        potentialFollower.setCommonFollowers(Math.toIntExact(entry.getValue()));
                        if (potentialFollower.shouldBeFollowed()) {
                            if (follow) {
                                potentialFollower.setDateOfFollowNow();
                                System.out.print(potentialFollowers.size()+1 + "/"+count + " ");
                                boolean result = this.follow(potentialFollower.getId());
                                if (result) {
                                    potentialFollowers.add(potentialFollower);
                                    sheetHelper.addNewFollowerLine(potentialFollower);
                                }
                            } else {
                                System.out.println("potentialFollowers added : " + potentialFollower.getUserName());
                                potentialFollowers.add(potentialFollower);
                            }
                        }
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();// @todo understand why this happend
            }
        }
        System.out.println("********************************");
        System.out.println(potentialFollowers.size() + " followers followed / "
        + sortedPotentialFollowersMap.size() + " users analyzed");
        System.out.println("********************************");

        return potentialFollowers;
    }

    private List<User> getInfluencersFromFollowers(List<User> followers){
        List<User> followersInfluencers = new ArrayList<>();
        User user;
        int i=0;
        // building influencers list
        while(i< followers.size()){
            user = followers.get(i);
            if(user.isInfluencer()){
                followersInfluencers.add(user);
            }
            i++;
        }

        return followersInfluencers;
    }

    // id, occurencies
    private Map<Long, Long> getAllFollowerIdsFromUsersSortedByOccurence(List<User> followers, int nbFollowersMaxtoWatch, int minOccurence){
        // building influencers followers list
        List<Long> influencersFollowersIds = new ArrayList<>();
        User user;
        int i=0;
        while(i<followers.size() && i<nbFollowersMaxtoWatch){
            user = followers.get(i);
            List<Long> currentFollowersInfluencersFollowersId = this.getFollowerIds(user.getId()); // criticity here -> cache
            influencersFollowersIds.addAll(currentFollowersInfluencersFollowersId);
            System.out.println(user.getUserName() + " (" + currentFollowersInfluencersFollowersId.size() + " followers)");
            i++;
        }
        Map<Long, Long> sortedPotentialFollowersMap = influencersFollowersIds.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))// create a map with item, occurence
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .filter(x -> x.getValue()>=minOccurence)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        System.out.println(sortedPotentialFollowersMap.size() + " followers found \n");
        return sortedPotentialFollowersMap;
    }

    @Override
    public List<Long> getFollowedRecently() {
        List<Long> result = new ArrayList<>();
        String filePath = System.getProperty("user.home") + File.separatorChar
                + "Documents" + File.separatorChar
                + "all_followed"
                +".csv";
        IOHelper ioHelper = new IOHelper();
        try {
            List<String[]> file = ioHelper.readData(filePath);
            for(String[] s : file){
                result.add(Long.valueOf(s[0]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
