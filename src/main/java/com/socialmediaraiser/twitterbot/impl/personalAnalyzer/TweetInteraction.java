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
public class TweetInteraction {

  @With
  private Set<String> answererIds  = new HashSet<>();
  @With
  private Set<String> retweeterIds = new HashSet<>();
  @With
  private Set<String> likersIds    = new HashSet<>();

  // Using VAVR collections everywhere would be much less noisy
  public TweetInteraction addAnswerer(String answerer) {
    Set<String> withNewId = new HashSet<>(answererIds);
    withNewId.add(answerer);
    return this.withAnswererIds(withNewId);
  }

  public TweetInteraction addRetweeted(String retweeter) {
    Set<String> withNewId = new HashSet<>(retweeterIds);
    withNewId.add(retweeter);
    return this.withRetweeterIds(withNewId);
  }

  public TweetInteraction addLiked(String liked) {
    Set<String> withNewId = new HashSet<>(likersIds);
    withNewId.add(liked);
    return this.withLikersIds(withNewId);
  }

  // Using VAVR collections everywhere would be much less noisy
  public TweetInteraction merge(TweetInteraction other) {
    return this.mergeAnswerers(other)
               .mergeRetweeters(other)
               .mergeLikers(other);
  }

  private TweetInteraction mergeAnswerers(TweetInteraction other) {
    Set<String> mergedAnswerers = io.vavr.collection.HashSet.ofAll(this.getAnswererIds())
                                                    .addAll(other.getAnswererIds())
                                                    .toJavaSet();
    return this.withAnswererIds(mergedAnswerers);
  }

  private TweetInteraction mergeRetweeters(TweetInteraction other) {
    Set<String> mergedRetweeters = io.vavr.collection.HashSet.ofAll(this.getRetweeterIds())
                                                     .addAll(other.getRetweeterIds())
                                                     .toJavaSet();
    return this.withRetweeterIds(mergedRetweeters);
  }

  private TweetInteraction mergeLikers(TweetInteraction other) {
    Set<String> mergedLikers = io.vavr.collection.HashSet.ofAll(this.getLikersIds())
                                                 .addAll(other.getLikersIds())
                                                 .toJavaSet();
    return this.withLikersIds(mergedLikers);
  }
}
