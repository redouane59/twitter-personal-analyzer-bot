package com.socialMediaRaiser.twitter.helpers.dto.getRelationship;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class RelationshipDTO {
    private SourceDTO source;
    private TargetDTO target;
}
