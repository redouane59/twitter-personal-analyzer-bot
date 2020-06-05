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
    val withNewId = new HashSet<>(answererIds);
    withNewId.add(answerer);
    return this.withAnswererIds(withNewId);
  }

  // Using VAVR collections everywhere would be much less noisy
  public TweetInteraction merge(TweetInteraction that) {
    return this.mergeAnswerers(that)
               .mergeRetweeters(that)
               .mergeLikers(that);
  }

  private TweetInteraction mergeAnswerers(TweetInteraction that){
      val mergedAnswerers = io.vavr.collection.HashSet.ofAll(this.getAnswererIds())
                                                      .addAll(that.getAnswererIds())
                                                      .toJavaSet();
      return this.withAnswererIds(mergedAnswerers);
  }

  private TweetInteraction mergeRetweeters(TweetInteraction that){
      val mergedRetweeters = io.vavr.collection.HashSet.ofAll(this.getRetweeterIds())
                                                      .addAll(that.getRetweeterIds())
                                                      .toJavaSet();
      return this.withAnswererIds(mergedRetweeters);
  }

  private TweetInteraction mergeLikers(TweetInteraction that){
      val mergedLikers = io.vavr.collection.HashSet.ofAll(this.getLikersIds())
                                                      .addAll(that.getLikersIds())
                                                      .toJavaSet();
      return this.withAnswererIds(mergedLikers);
  }
}
