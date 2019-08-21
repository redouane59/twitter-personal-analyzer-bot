package com.socialMediaRaiser.twitter;

import com.socialMediaRaiser.twitter.helpers.dto.IUser;
import com.socialMediaRaiser.twitter.helpers.dto.getUser.TweetDTO;
import com.socialMediaRaiser.twitter.scoring.UserScoringEngine;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class User implements IUser {

    private String id;
    private String username;
    private int followersCount;
    private int followingCount;
    private String lang;
    private int tweetCount;
    private Date dateOfCreation;
    private int commonFollowers; // nb of occurrences in followers search
    private Date dateOfFollow;
    private Date dateOfFollowBack;
    private String description;
    @Deprecated
    private int favouritesCount;
    private Date lastUpdate;
    private String location;
    private UserScoringEngine scoringEngine = new UserScoringEngine(FollowProperties.targetProperties.getMinimumPercentMatch());
    private List<TweetDTO> mostRecentTweet;
    private boolean protectedAccount;

    @Builder
    User(String id, String userName, int followersCout, int followingCount, String lang, int statusesCount, Date dateOfCreation,
         int commonFollowers, Date dateOfFollow, Date dateOfFollowBack, String description, int favouritesCount,
         Date lastUpdate, String location, List<TweetDTO> mostRecentTweet, boolean protectedAccount){
        this.id = id;
        this.username = userName;
        this.followersCount = followersCout;
        this.followingCount = followingCount;
        this.lang = lang;
        this.tweetCount = statusesCount;
        this.dateOfCreation = dateOfCreation;
        this.commonFollowers = commonFollowers;
        this.dateOfFollow = dateOfFollow;
        this.dateOfFollowBack = dateOfFollowBack;
        this.description = description;
        this.favouritesCount = favouritesCount;
        this.lastUpdate = lastUpdate;
        this.location = location;
        this.mostRecentTweet = mostRecentTweet;
        this.protectedAccount = protectedAccount;
    }

    // @odo remove argument ?
    public boolean shouldBeFollowed(String ownerName){
        if(this.getUsername()!=null && this.getUsername().equals(ownerName)){
            return false;
        }
        return this.scoringEngine.shouldBeFollowed(this);
    }

    private long getNbDaysSinceLastTweet(){
        Date now = new Date();
        Date lastUpdate = this.getLastUpdate();
        return (now.getTime()-lastUpdate.getTime()) / (24 * 60 * 60 * 1000);
    }

    public boolean shouldBeUnfollowed() {
        throw new UnsupportedOperationException();
    }

    public boolean shouldBeTakenForItsFollowers(){

        if(this.getFollowersCount()> FollowProperties.targetProperties.getMinNbFollowers()
                && this.getFollowersRatio()> FollowProperties.influencerProperties.getMinRatio()
                && this.lang.equals(FollowProperties.targetProperties.getLanguage())){
            return true;
        } else{
            return false;
        }
    }

    public boolean isInfluencer(){
        String descriptionWords = FollowProperties.targetProperties.getDescription();
        String[] descriptionWordsSplitted = descriptionWords.split(FollowProperties.ARRAY_SEPARATOR);
        String[] userDescriptionSplitted = this.getDescription().split(" ");

        String locationWords = FollowProperties.targetProperties.getLocation();
        String[] locationWordsSplitted = locationWords.split(FollowProperties.ARRAY_SEPARATOR);
        String[] userLocationSplitted = this.getLocation().split(" ");

        boolean matchDescription = false;
        boolean matchLocation = false;

        for(String s :userDescriptionSplitted){
            if(Arrays.stream(descriptionWordsSplitted).anyMatch(s::contains)){
                matchDescription = true;
            }
        }
        for(String s :userLocationSplitted){
            if(Arrays.stream(locationWordsSplitted).anyMatch(s::contains)){
                matchLocation = true;
            }
        }
        return (matchDescription&&matchLocation);

       /* if(this.getFollowersRatio()> followConfiguration.getMinRatio()
                && this.getFollowersCount()> followConfiguration.getInfluencerMinNbFollowers()
                && this.description.contains(followConfiguration.getDescription()[0]) */
                /*&& this.lang!=null && this.lang.equals(followConfiguration.getLanguage())*//*){
            return true;
        } else{
            return false;
        } */
    }

    public void incrementCommonFollowers(){
        this.commonFollowers++;
    }

    public void setDateOfFollowNow(){
        this.setDateOfFollow(new Date());
    }

    public long getDaysBetweenFollowAndLastUpdate(){
        if(dateOfFollow==null || lastUpdate==null){
            return Long.MAX_VALUE;
        }
        return (dateOfFollow.getTime()-lastUpdate.getTime()) / (24 * 60 * 60 * 1000);
    }

    public long getYearsBetweenFollowAndCreation(){
        return (dateOfFollow.getTime()-dateOfCreation.getTime()) / (365 * 24 * 60 * 60 * 1000);
    }

    public void addLanguageFromLastTweet(List<Tweet> userLastTweets){
        if(userLastTweets!=null){
            for(Tweet tweet : userLastTweets){
                if(!tweet.getLang().equals("und")){
                    this.setLang(tweet.getLang());
                    if(this.lang.equals(FollowProperties.targetProperties.getLanguage())){
                        break;
                    }
                }
            }
        }
    }

    public boolean getRandomForestPrediction(){
        this.setDateOfFollowNow();
        return RandomForestAlgoritm.getPrediction(this);
    }

    public double getFollowersRatio() {
        return (double) this.followersCount / (double) this.followingCount;
    }

    @Override
    public boolean equals(Object o) {
        User otherUser = (User) o;
        return (otherUser).getId() == this.getId();
    }
}
