package com.socialmediaraiser.twittersocialgraph.impl;

import com.socialmediaraiser.twittersocialgraph.impl.Link;
import com.socialmediaraiser.twittersocialgraph.impl.UserGraph;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;

@Data
@NoArgsConstructor
public class JsonGraph{
    private HashSet<UserGraph> nodes = new HashSet<>();
    private HashSet<Link> links = new HashSet<>();
}
