package com.socialMediaRaiser.twitter.helpers.dto.getUser;

import lombok.Data;

import java.util.List;

@Data
public class DescriptionDTO {
    private List<HashtagDTO> hashtags;
    private List<MentionDTO> mentions;
}
