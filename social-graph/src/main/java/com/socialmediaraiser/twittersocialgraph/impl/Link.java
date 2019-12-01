package com.socialmediaraiser.twittersocialgraph.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Link implements Comparable<Link>{
    private String source;
    private String target;
    private int value;

    @Override
    public boolean equals(Object o){
        if (o==null || this.getClass() != o.getClass()) return false;
        Link other = (Link)o;
        return source.equals(other.getSource()) && target.equals(other.getTarget())
                || source.equals(other.getTarget()) && target.equals(other.getSource());
    }

    @Override
    public int hashCode() {
        return source.hashCode() + target.hashCode();
    }

    @Override
    public int compareTo(Link link) {
        return Integer.compare(value, link.getValue());
    }
}
