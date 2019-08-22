package com.socialMediaRaiser.twitter.helpers.dto.getUser;

import com.socialMediaRaiser.twitter.User;
import com.socialMediaRaiser.twitter.helpers.dto.IUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractTwitterUser implements IUser {

    private String id;
    private String username;
    private List<TweetDTO> mostRecentTweet;
    private String description;

    @Override
    public boolean equals(Object o) {
        AbstractTwitterUser otherUser = (AbstractTwitterUser) o;
        return (otherUser).getId() == this.getId();
    }
}
