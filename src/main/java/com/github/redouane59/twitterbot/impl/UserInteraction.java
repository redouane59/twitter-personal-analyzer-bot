package com.github.redouane59.twitterbot.impl;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInteraction {

  @With
  private String      userId;
  @With
  private Set<String> answersIds  = HashSet.empty();
  @With
  private Set<String> retweetsIds = HashSet.empty();
  @With
  private Set<String> likesIds    = HashSet.empty();

  public UserInteraction addUserId(String userId) {
    return this.withUserId(userId);
  }

  public UserInteraction addAnswer(String answerId) {
    Set<String> withNewId = HashSet.ofAll(answersIds);
    withNewId = withNewId.add(answerId);
    return this.withAnswersIds(withNewId);
  }

  public UserInteraction addRetweet(String retweetId) {
    Set<String> withNewId = HashSet.ofAll(retweetsIds);
    withNewId = withNewId.add(retweetId);
    return this.withRetweetsIds(withNewId);
  }

  public UserInteraction addLike(String likeId) {
    Set<String> withNewId = HashSet.ofAll(likesIds);
    withNewId = withNewId.add(likeId);
    return this.withLikesIds(withNewId);
  }

  // Using VAVR collections everywhere would be much less noisy
  public UserInteraction merge(UserInteraction other) {
    return this.mergeAnswers(other)
               .mergeRetweets(other)
               .mergeLikes(other);
  }

  private UserInteraction mergeAnswers(UserInteraction other) {
    Set<String> mergedAnswerers = HashSet.ofAll(this.getAnswersIds())
                                                    .addAll(other.getAnswersIds());
    return this.withAnswersIds(mergedAnswerers);
  }

  private UserInteraction mergeRetweets(UserInteraction other) {
    Set<String> mergedRetweeters = HashSet.ofAll(this.getRetweetsIds())
                                                     .addAll(other.getRetweetsIds());
    return this.withRetweetsIds(mergedRetweeters);
  }

  private UserInteraction mergeLikes(UserInteraction other) {
    Set<String> mergedLikers = HashSet.ofAll(this.getLikesIds())
                                                 .addAll(other.getLikesIds());
    return this.withLikesIds(mergedLikers);
  }
}
