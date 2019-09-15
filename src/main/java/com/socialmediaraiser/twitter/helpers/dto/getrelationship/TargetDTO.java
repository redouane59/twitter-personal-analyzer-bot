package com.socialmediaraiser.twitter.helpers.dto.getrelationship;

import lombok.Data;

@Data
public class TargetDTO {
    private boolean followed_by;
    private String screen_name;
    private String id_str;
    private boolean following;
    private boolean following_requested;
    private Long id;
    private boolean following_received;
}
