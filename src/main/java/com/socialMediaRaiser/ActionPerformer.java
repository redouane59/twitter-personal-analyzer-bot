package com.socialMediaRaiser;

public interface ActionPerformer {

    boolean follow(Long userId);
    boolean unfollow(Long userId);
    boolean follow(String userName);
    boolean unfollow(String userName);

}
