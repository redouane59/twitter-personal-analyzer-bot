package com.socialMediaRaiser.twitter.helpers.dto.getRelationship;

import lombok.Data;

@Data
public class SourceDTO {
    private boolean can_dm;
    private boolean all_replies;
    private boolean following_requested;
    private boolean marked_spam;
    private boolean notifications_enabled;
    private boolean live_following;
    private boolean followed_by;
    private boolean muting;
    private String screen_name;
    private boolean blocking;
    private boolean blocked_by;
    private boolean want_retweets;
    private String id_str;
    private boolean following;
    private Long id;
    private boolean following_received;
}
