package com.socialmediaraiser.twitterbot.impl;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class UserInteractions {

    List<UserInteraction> values = new ArrayList<>();

    public UserInteraction get(String userId){
        for(UserInteraction userInteraction : values){
            if(userInteraction.getUserId().equals(userId)){
                return userInteraction;
            }
        }
        UserInteraction userInteraction = new UserInteraction(userId);
        this.values.add(userInteraction);
        return userInteraction;
    }

    @Getter
    @Setter
    public static class UserInteraction{
        private String userId;
        private int nbRepliesTo = 0;
        private int nbRepliesFrom = 0;
        private int nbRetweets = 0;

        public UserInteraction(String userId){
            this.userId = userId;
        }

        public void incrementNbRepliesTo(){
            nbRepliesTo++;
        }

        public void incrementNbRepliesFrom(){
            nbRepliesFrom++;
        }

        public void incrementNbRetweets(){
            nbRetweets++;
        }

        public int getTotalNbInteractions(){
            return this.nbRepliesTo+this.nbRepliesFrom+this.nbRetweets;
        }

        @Override
        public boolean equals(Object o){
            if (o==null || this.getClass() != o.getClass()) return false;
            UserInteraction other = (UserInteraction) o;
            return other.getUserId().equals(this.userId);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 17 * hash + (this.userId != null ? this.userId.hashCode() : 0);
            return hash;
        }
    }


}
