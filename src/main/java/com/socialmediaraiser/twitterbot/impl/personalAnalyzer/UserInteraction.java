package com.socialmediaraiser.twitterbot.impl.personalAnalyzer;

import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.val;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInteraction {

  @With
  private Set<String> answersIds  = new HashSet<>();
  @With
  private Set<String> retweetsIds = new HashSet<>();
  @With
  private Set<String> likesIds    = new HashSet<>();

  // Using VAVR collections everywhere would be much less noisy
  public UserInteraction addAnswer(String answerId) {
    val withNewId = new HashSet<>(answersIds);
    withNewId.add(answerId);
    return this.withAnswersIds(withNewId);
  }

  public UserInteraction addRetweet(String retweetId) {
    val withNewId = new HashSet<>(retweetsIds);
    withNewId.add(retweetId);
    return this.withRetweetsIds(withNewId);
  }

  public UserInteraction addLike(String likeId) {
    val withNewId = new HashSet<>(likesIds);
    withNewId.add(likeId);
    return this.withLikesIds(withNewId);
  }

  // Using VAVR collections everywhere would be much less noisy
  public UserInteraction merge(UserInteraction that) {
    return this.mergeAnswers(that)
               .mergeRetweets(that)
               .mergeLikes(that);
  }

  private UserInteraction mergeAnswers(UserInteraction other) {
    Set<String> mergedAnswerers = io.vavr.collection.HashSet.ofAll(this.getAnswersIds())
                                                    .addAll(other.getAnswersIds())
                                                    .toJavaSet();
    return this.withAnswersIds(mergedAnswerers);
  }

  private UserInteraction mergeRetweets(UserInteraction other) {
    Set<String> mergedRetweeters = io.vavr.collection.HashSet.ofAll(this.getRetweetsIds())
                                                     .addAll(other.getRetweetsIds())
                                                     .toJavaSet();
    return this.withRetweetsIds(mergedRetweeters);
  }

  private UserInteraction mergeLikes(UserInteraction other) {
    Set<String> mergedLikers = io.vavr.collection.HashSet.ofAll(this.getLikesIds())
                                                 .addAll(other.getLikesIds())
                                                 .toJavaSet();
    return this.withLikesIds(mergedLikers);
  }
}
