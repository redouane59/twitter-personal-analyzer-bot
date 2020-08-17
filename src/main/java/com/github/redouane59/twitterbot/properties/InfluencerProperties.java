package com.github.redouane59.twitterbot.properties;

import lombok.Data;

@Data
public class InfluencerProperties {

  private float  minRatio;
  private int    minNbFollowers;
  private String baseList;
}
