package com.socialMediaRaiser.twitter.helpers.dto.getUser;

import lombok.Data;

import java.util.List;

@Data
public class UserObjectResponseDTO {
    private List<UserDTO> data;
    private IncludesDTO includes;
}
