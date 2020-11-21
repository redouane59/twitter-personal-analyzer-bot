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

  //  @With
//  private String      userId;
  @With
  private Set<String> answersIds   = HashSet.empty();
  @With
  private Set<String> retweetsIds  = HashSet.empty();
  @With
  private Set<String> quotesIds    = HashSet.empty();
  @With
  private Set<String> likesIds     = HashSet.empty();
  @With
  private Set<String> answeredIds  = HashSet.empty();
  @With
  private Set<String> retweetedIds = HashSet.empty();
  @With
  private Set<String> quotedIds    = HashSet.empty();
  @With
  private Set<String> likedIds     = HashSet.empty();

 /* public UserInteraction addUserId(String userId) {
    return this.withUserId(userId);
  } */

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

  public UserInteraction addQuote(String quoteId) {
    Set<String> withNewId = HashSet.ofAll(quotesIds);
    withNewId = withNewId.add(quoteId);
    return this.withQuotesIds(withNewId);
  }

  public UserInteraction addLiked(String likedId) {
    Set<String> withNewId = HashSet.ofAll(likedIds);
    withNewId = withNewId.add(likedId);
    return this.withLikedIds(withNewId);
  }

  public UserInteraction addAnswered(String answeredId) {
    Set<String> withNewId = HashSet.ofAll(answeredIds);
    withNewId = withNewId.add(answeredId);
    return this.withAnsweredIds(withNewId);
  }

  public UserInteraction addRetweeted(String retweetedId) {
    Set<String> withNewId = HashSet.ofAll(retweetedIds);
    withNewId = withNewId.add(retweetedId);
    return this.withRetweetedIds(withNewId);
  }

  public UserInteraction addQuoted(String quotedId) {
    Set<String> withNewId = HashSet.ofAll(quotedIds);
    withNewId = withNewId.add(quotedId);
    return this.withQuotedIds(withNewId);
  }

  // Using VAVR collections everywhere would be much less noisy
  public UserInteraction merge(UserInteraction other) {
    return this.mergeAnswers(other)
               .mergeRetweets(other)
               .mergeQuotes(other)
               .mergeLikes(other)
               .mergeAnswered(other)
               .mergeRetweeted(other)
               .mergeQuoted(other)
               .mergeLiked(other);
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

  private UserInteraction mergeQuotes(UserInteraction other) {
    Set<String> mergedQuoters = HashSet.ofAll(this.getQuotesIds())
                                       .addAll(other.getQuotesIds());
    return this.withQuotesIds(mergedQuoters);
  }

  private UserInteraction mergeLikes(UserInteraction other) {
    Set<String> mergedLikers = HashSet.ofAll(this.getLikesIds())
                                      .addAll(other.getLikesIds());
    return this.withLikesIds(mergedLikers);
  }

  private UserInteraction mergeAnswered(UserInteraction other) {
    Set<String> mergedAnswered = HashSet.ofAll(this.getAnsweredIds())
                                        .addAll(other.getAnsweredIds());
    return this.withAnsweredIds(mergedAnswered);
  }

  private UserInteraction mergeRetweeted(UserInteraction other) {
    Set<String> mergedRetweeted = HashSet.ofAll(this.getRetweetedIds())
                                         .addAll(other.getRetweetedIds());
    return this.withRetweetedIds(mergedRetweeted);
  }

  private UserInteraction mergeQuoted(UserInteraction other) {
    Set<String> mergedQuoted = HashSet.ofAll(this.getQuotedIds())
                                      .addAll(other.getQuotedIds());
    return this.withQuotedIds(mergedQuoted);
  }

  private UserInteraction mergeLiked(UserInteraction other) {
    Set<String> mergedLiked = HashSet.ofAll(this.getLikedIds())
                                     .addAll(other.getLikedIds());
    return this.withLikedIds(mergedLiked);
  }
}
