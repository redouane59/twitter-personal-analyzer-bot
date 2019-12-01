package com.socialmediaraiser.twittersocialgraph.impl;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchingNumber {
    private int matchingSum;
    private int nbElements;

    public void incrementMatchingSum(int value){
        matchingSum+=value;
    }

    public void incrementNbElements(){
        nbElements++;
    }

    public int getAverage(){
        if(nbElements==0){
            return 0;
        }
        return matchingSum/nbElements;
    }
}
