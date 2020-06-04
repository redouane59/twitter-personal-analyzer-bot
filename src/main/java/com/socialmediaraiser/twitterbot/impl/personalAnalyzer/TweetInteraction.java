package com.socialmediaraiser.twitterbot.impl.personalAnalyzer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TweetInteraction {
    private Set<String> answererIds = new HashSet<>();
    private Set<String> retweeterIds = new HashSet<>();
    private Set<String> likersIds = new HashSet<>();
}
