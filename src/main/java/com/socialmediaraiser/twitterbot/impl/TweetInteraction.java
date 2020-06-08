package com.socialmediaraiser.twitterbot.impl;


import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.collection.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.With;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TweetInteraction {

  @With
  private Set<String> answererIds  = HashSet.empty();
  @With
  private Set<String> retweeterIds = HashSet.empty();
  @With
  private Set<String> likersIds    = HashSet.empty();

  // Using VAVR collections everywhere would be much less noisy
  public TweetInteraction addAnswerer(String answerer) {
    Set<String> withNewId = HashSet.ofAll(answererIds);
    withNewId = withNewId.add(answerer);
    return this.withAnswererIds(withNewId);
  }

  public TweetInteraction addRetweeted(String retweeter) {
    Set<String> withNewId = HashSet.ofAll(retweeterIds);
    withNewId = withNewId.add(retweeter);
    return this.withRetweeterIds(withNewId);
  }

  public TweetInteraction addLiked(String liked) {
    Set<String> withNewId = HashSet.ofAll(likersIds);
    withNewId = withNewId.add(liked);
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
                                                            .addAll(other.getAnswererIds());
    return this.withAnswererIds(mergedAnswerers);
  }

  private TweetInteraction mergeRetweeters(TweetInteraction other) {
    Set<String> mergedRetweeters = io.vavr.collection.HashSet.ofAll(this.getRetweeterIds())
                                                             .addAll(other.getRetweeterIds());
    return this.withRetweeterIds(mergedRetweeters);
  }

  private TweetInteraction mergeLikers(TweetInteraction other) {
    Set<String> mergedLikers = io.vavr.collection.HashSet.ofAll(this.getLikersIds())
                                                         .addAll(other.getLikersIds());
    return this.withLikersIds(mergedLikers);
  }

  /**
   * Get UserStats from TweetInteraction object
   * @return a map with userId as key and UserStats as value
   */
  public Map<String, UserStats> toUserStatsList(){
    Set<String> allUsers = this.answererIds.addAll(this.retweeterIds).addAll(this.likersIds);
    return HashMap.ofEntries(allUsers.toStream().map(this::buildTurple));
  }

  private Tuple2<String, UserStats> buildTurple(String userId){
    return Tuple.of(userId, new UserStats().updateFromTweetInteraction(userId, this));
  }

}
