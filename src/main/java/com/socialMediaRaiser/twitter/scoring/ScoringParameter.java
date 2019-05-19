package com.socialMediaRaiser.twitter.scoring;

import com.socialMediaRaiser.twitter.scoring.Criterion;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScoringParameter {
    private Criterion criterion;
    private Object value; // @todo dirty
}
