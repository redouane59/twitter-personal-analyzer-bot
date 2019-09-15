package com.socialmediaraiser;

public interface ActionPerformer {

    boolean follow(String userId);
    boolean unfollow(String userId);

}
