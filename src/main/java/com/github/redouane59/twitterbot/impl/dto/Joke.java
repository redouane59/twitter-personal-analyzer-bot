package com.github.redouane59.twitterbot.impl.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Joke {

  private int         status;
  private String      response;
  private String      error;
  private JokeContent joke;

  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  @Getter
  public static class JokeContent {

    private String question;
    private String answer;
    private int    id;
  }
}
