package com.socialmediaraiser.twittersocialgraph.impl;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MatchingNumber {
    private double matchingSum;
    private int nbElements;

    public void incrementMatchingSum(double value){
        matchingSum+=value;
    }

    public void incrementNbElements(){
        nbElements++;
    }

    public double getAverage(){
        if(nbElements==0){
            return 0;
        }
        return matchingSum/nbElements;
    }
}
