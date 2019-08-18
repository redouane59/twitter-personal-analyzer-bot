package com.socialMediaRaiser.twitter.helpers.dto.getUser;

import lombok.Data;

@Data
public class MentionDTO {
    private int start;
    private int end;
    private String username;
}
