package com.socialMediaRaiser.twitter.helpers.dto.getUser;

import lombok.Data;

import java.util.List;

@Data
public class IncludesDTO {
    private List<TweetDTO> tweets;
}
