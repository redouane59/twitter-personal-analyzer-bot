package com.socialmediaraiser.twittersocialgraph;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmediaraiser.core.twitter.TwitterHelper;
import lombok.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

public class FollowerAnalyzer extends TwitterHelper {

    private static final Logger LOGGER = Logger.getLogger(FollowerAnalyzer.class.getName());

    public FollowerAnalyzer(String userName) {
        super(userName);
    }

    public int countCommonUsers(Set<String> users1, Set<String> users2){
        Set<String> common = new HashSet<>(users1);
        common.retainAll(users2);
        return common.size();
    }

    public String getJsonGraph(HashSet<UserGraph> users) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGraph graph = new JsonGraph();
        graph.setNodes(users);

        Set<Link> studiedLinks = new HashSet<>();
        for(UserGraph user1 : users){
            int minMatching = 20 ;
            Set<String> followers1 = this.getUserFollowersIds(this.getUserFromUserName(user1.getId()).getId());
            for(UserGraph user2 : users) {
                if (user1!=user2 && !studiedLinks.contains(new Link(user1.getId(), user2.getId(),0))){
                    Set<String> followers2 = this.getUserFollowersIds(this.getUserFromUserName(user2.getId()).getId());
                    if(followers2==null) continue;
                    int value = computeValue(followers1, followers2);
                    if (value >= minMatching) {
                        LOGGER.info("*** links added between "
                                + user1.getId() + " ("+followers1.size()+" followers) & "
                                + user2.getId() + " ("+followers2.size() + " followers)"
                                + " --> " + (value) + "% ***");
                        graph.getLinks().add(new Link(user1.getId(), user2.getId(), 1+(value - minMatching)/5));
                    } else{
                        LOGGER.info("links NOT added between "
                                + user1.getId() + " ("+followers1.size()+" followers) & "
                                + user2.getId() + " ("+followers2.size() + " followers)"
                                + " (" + (value) + "%)");
                    }
                }
                studiedLinks.add(new Link(user1.getId(), user2.getId(), 0));
            }
            LOGGER.info(mapper.writeValueAsString(graph));
        }
        String result = mapper.writeValueAsString(graph);
        try {
            mapper.writeValue(new File("twitterGraph.json"), result);
        } catch (IOException e) {
            LOGGER.severe(e.toString());
        }
        return result;
    }

    private int computeValue(Set<String> followers1, Set<String> followers2){
        int ratio1 = 100 * countCommonUsers(followers1, followers2) / followers1.size();
        int ratio2 = 100 * countCommonUsers(followers1, followers2) / followers2.size();
        return (ratio1+ratio2)/2;
    }

    public void getCsvArray(List<String> users) {
        int[][] result = new int[users.size()][users.size()];
        for(int i=0; i<users.size(); i++){
            String user1 = users.get(i);
            Set<String> followers1 = this.getUserFollowersIds(this.getUserFromUserName(user1).getId());
            for(int j=0;j<users.size(); j++) {
                String user2 = users.get(j);
                if (!user1.equals(user2)){
                    Set<String> followers2 = this.getUserFollowersIds(this.getUserFromUserName(user2).getId());
                    int value = computeValue(followers1, followers2);
                    LOGGER.info("*** links added between " + user1 + " ("+followers1.size()+") & " + user2
                            + "(" + followers2.size() +") -> " + (value) + "% ***");
                    result[i][j] = value;
                }
            }
            LOGGER.info(Arrays.toString(result));
        }
        LOGGER.info(arrayToCsvString(result, users,";",""));
    }

    public void getCsvArray(List<String> users, List<String> others){
        int[][] result = new int[users.size()][users.size()];
        for(int i=0; i<users.size(); i++){
            String user1 = users.get(i);
            Set<String> followers1 = this.getUserFollowersIds(this.getUserFromUserName(user1).getId());
            for(int j=0;j<others.size(); j++) {
                String user2 = others.get(j);
                if (!user1.equals(user2)){
                    Set<String> followers2 = this.getUserFollowersIds(this.getUserFromUserName(user2).getId());
                    int value = computeValue(followers1, followers2);
                    LOGGER.info("*** links added between " + user1 + " ("+followers1.size()+") & " + user2
                            + "(" + followers2.size() +") -> " + (value) + "% ***");
                    result[i][j] = value;
                }
            }
            LOGGER.info(Arrays.toString(result));
        }
        LOGGER.info(arrayToCsvString(result, users,";",""));
    }

    @Data
    @AllArgsConstructor
    public static class UserGraph{
        private String id;
        private int group;
    }

    @Data
    @NoArgsConstructor
    public class JsonGraph{
        private HashSet<UserGraph> nodes = new HashSet<>();
        private HashSet<Link> links = new HashSet<>();
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Link implements Comparable<Link>{
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

    public static String arrayToCsvString(int[][] s, List<String> names, String separator,
                                          String quote) {

        int len = s.length;
        StringBuffer sb   = new StringBuffer(len * 16);
        sb.append(separator);
        for(int i=0;i<names.size();i++){
            sb.append(quote);
            sb.append(names.get(i));
            sb.append(quote);
            sb.append(separator);
        }
        sb.append("\n");

        for (int i = 0; i < len; i++) {
            sb.append(names.get(i));
            sb.append(separator);
            for(int j=0; j<len; j++){
                sb.append(quote);
                sb.append(s[i][j]);
                sb.append(quote);
                sb.append(separator);
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
