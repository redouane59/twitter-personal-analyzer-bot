package com.github.redouane59.twitterbot.impl;


import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.HashMap;
import io.vavr.collection.HashSet;
import io.vavr.collection.Map;
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
public class TweetInteraction {

  @With
  private Set<String> answererIds  = HashSet.empty();
  @With
  private Set<String> retweeterIds = HashSet.empty();
  @With
  private Set<String> quotersIds   = HashSet.empty();
  @With
  private Set<String> likersIds    = HashSet.empty();
  @With
  private Set<String> answeredIds  = HashSet.empty();
  @With
  private Set<String> retweetedIds = HashSet.empty();
  @With
  private Set<String> quotedIds    = HashSet.empty();
  @With
  private Set<String> likedIds     = HashSet.empty();

  public TweetInteraction addAnswerer(String answerer) {
    Set<String> withNewId = HashSet.ofAll(answererIds);
    withNewId = withNewId.add(answerer);
    return this.withAnswererIds(withNewId);
  }

  public TweetInteraction addRetweeter(String retweeter) {
    Set<String> withNewId = HashSet.ofAll(retweeterIds);
    withNewId = withNewId.add(retweeter);
    return this.withRetweeterIds(withNewId);
  }

  public TweetInteraction addQuoter(String quoter) {
    Set<String> withNewId = HashSet.ofAll(quotersIds);
    withNewId = withNewId.add(quoter);
    return this.withQuotersIds(withNewId);
  }

  public TweetInteraction addLikers(String likers) {
    Set<String> withNewId = HashSet.ofAll(likersIds);
    withNewId = withNewId.add(likers);
    return this.withLikersIds(withNewId);
  }

  public TweetInteraction addAnswered(String answered) {
    Set<String> withNewId = HashSet.ofAll(answeredIds);
    withNewId = withNewId.add(answered);
    return this.withAnsweredIds(withNewId);
  }

  public TweetInteraction addRetweeted(String retweeted) {
    Set<String> withNewId = HashSet.ofAll(retweetedIds);
    withNewId = withNewId.add(retweeted);
    return this.withRetweetedIds(withNewId);
  }

  public TweetInteraction addQuoted(String quoted) {
    Set<String> withNewId = HashSet.ofAll(quotedIds);
    withNewId = withNewId.add(quoted);
    return this.withQuotedIds(withNewId);
  }

  public TweetInteraction addLiked(String liked) {
    Set<String> withNewId = HashSet.ofAll(likedIds);
    withNewId = withNewId.add(liked);
    return this.withLikedIds(withNewId);
  }

  // Using VAVR collections everywhere would be much less noisy
  public TweetInteraction merge(TweetInteraction other) {
    return this.mergeAnswerers(other)
               .mergeRetweeters(other)
               .mergeQuoters(other)
               .mergeLikers(other)
               .mergeAnswered(other)
               .mergeRetweeted(other)
               .mergeQuoted(other)
               .mergeLiked(other);
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

  private TweetInteraction mergeQuoters(TweetInteraction other) {
    Set<String> mergedQuoters = io.vavr.collection.HashSet.ofAll(this.getQuotersIds())
                                                          .addAll(other.getQuotersIds());
    return this.withQuotersIds(mergedQuoters);
  }

  private TweetInteraction mergeLikers(TweetInteraction other) {
    Set<String> mergedLikers = io.vavr.collection.HashSet.ofAll(this.getLikersIds())
                                                         .addAll(other.getLikersIds());
    return this.withLikersIds(mergedLikers);
  }

  private TweetInteraction mergeAnswered(TweetInteraction other) {
    Set<String> mergedAnswered = io.vavr.collection.HashSet.ofAll(this.getAnsweredIds())
                                                           .addAll(other.getAnsweredIds());
    return this.withAnsweredIds(mergedAnswered);
  }

  private TweetInteraction mergeRetweeted(TweetInteraction other) {
    Set<String> mergedRetweeted = io.vavr.collection.HashSet.ofAll(this.getRetweetedIds())
                                                            .addAll(other.getRetweetedIds());
    return this.withRetweetedIds(mergedRetweeted);
  }

  private TweetInteraction mergeQuoted(TweetInteraction other) {
    Set<String> mergedQuoted = io.vavr.collection.HashSet.ofAll(this.getQuotedIds())
                                                         .addAll(other.getQuotedIds());
    return this.withQuotedIds(mergedQuoted);
  }

  private TweetInteraction mergeLiked(TweetInteraction other) {
    Set<String> mergedLiked = io.vavr.collection.HashSet.ofAll(this.getLikedIds())
                                                        .addAll(other.getLikedIds());
    return this.withLikedIds(mergedLiked);
  }

  /**
   * Get UserStats from TweetInteraction object
   *
   * @return a map with userId as key and UserStats as value
   */
  public Map<String, UserStats> toUserStatsMap() {
    Set<String> allUsers = this.answererIds.addAll(this.retweeterIds).addAll(this.quotersIds).addAll(this.likersIds)
                                           .addAll(answeredIds).addAll(retweetedIds).addAll(quotedIds).addAll(likedIds);
    return HashMap.ofEntries(allUsers.toStream().map(this::buildTuple));
  }

  private Tuple2<String, UserStats> buildTuple(String userId) {
    return Tuple.of(userId, new UserStats().updateFromTweetInteraction(userId, this));
  }

}
