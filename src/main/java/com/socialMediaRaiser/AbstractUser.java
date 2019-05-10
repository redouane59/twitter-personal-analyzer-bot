package com.socialMediaRaiser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractUser {
    private long id;
    private String userName;
    private int followersCount;
    private int followingCount;

    public double getFollowersRatio() {
        return (double) this.followersCount / (double) this.followingCount;
    }

    public abstract boolean shouldBeFollowed();

    public abstract boolean shouldBeUnfollowed();

    @Override
    public boolean equals(Object o) {
        AbstractUser otherUser = (AbstractUser) o;
        if (otherUser.getId() == this.getId()) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(this.id);
    }
}
