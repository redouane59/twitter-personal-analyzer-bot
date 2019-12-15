package com.socialmediaraiser.twittersocialgraph;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmediaraiser.core.twitter.TwitterHelper;
import com.socialmediaraiser.core.twitter.helpers.dto.getuser.AbstractUser;
import com.socialmediaraiser.twittersocialgraph.impl.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.logging.Logger;

public class FollowerAnalyzer extends TwitterHelper {

    private static final Logger LOGGER = Logger.getLogger(FollowerAnalyzer.class.getName());
    private int minMatching = 20 ;

    public FollowerAnalyzer(String userName) {
        super(userName);
    }

    public int countCommonUsers(Set<String> users1, Set<String> users2){
        Set<String> common = new HashSet<>(users1);
        common.retainAll(users2);
        return common.size();
    }

    public String getJsonGraph(HashSet<UserGraph> users) throws IOException, URISyntaxException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGraph graph = new JsonGraph();
        graph.setNodes(users);

        Set<Link> studiedLinks = new HashSet<>();
        for(UserGraph user1 : users){
            LOGGER.info("analyzing " + user1.getId() + " ... ");
            Set<String> followers1 = loadFollowers(user1.getId());
            for (Iterator<UserGraph> it = graph.getNodes().iterator(); it.hasNext(); ) {
                UserGraph f = it.next();
                if (f.getId() == user1.getId())
                    f.setSize(this.computeSize(followers1.size()));
            }
            for(UserGraph user2 : users) {
                if (user1!=user2 && !studiedLinks.contains(new Link(user1.getId(), user2.getId(),0))){
                    AbstractUser user = this.getUserFromUserName(user2.getId());
                    if(user!=null){
                        Set<String> followers2 = loadFollowers(user2.getId());
                        if(followers2==null) continue;
                        int value = computeValue(followers1, followers2);
                        if (value >= minMatching) {
                            LOGGER.info("*** links added between "
                                    + user1.getId() + " ("+followers1.size()+" followers) & "
                                    + user2.getId() + " ("+followers2.size() + " followers)"
                                    + " --> " + (value) + "% ***");
                            graph.getLinks().add(new Link(user1.getId(), user2.getId(), this.computeLinkValue(value, minMatching)));
                        } else{
                           /* LOGGER.info("links NOT added between "
                                    + user1.getId() + " ("+followers1.size()+" followers) & "
                                    + user2.getId() + " ("+followers2.size() + " followers)"
                                    + " (" + (value) + "%)");*/
                        }
                    } else{
                        LOGGER.severe("user null");
                    }
                }
                studiedLinks.add(new Link(user1.getId(), user2.getId(), 0));
            }
        }
        LOGGER.info("\n"+mapper.writeValueAsString(graph));

        String result = mapper.writeValueAsString(graph);
        try {
            mapper.writeValue(new File("twitterGraph.json"), result);
        } catch (IOException e) {
            LOGGER.severe(e.toString());
        }
        return result;
    }

    private Set<String> loadFollowers(String userName) throws IOException, URISyntaxException {
        String fileName = userName+".json";
        File file = new File("src/main/resources/users/"+fileName);
        ObjectMapper mapper = new ObjectMapper();
        if (file.exists()) {
            try {
                return mapper.readValue(file, HashSet.class);
            } catch (Exception e) {
                LOGGER.severe(userName + " KO!!");
            }
        }
        AbstractUser user = this.getUserFromUserName(userName);
        if(user==null){
            LOGGER.severe(userName + "not found");
            return null;
        }
        Set<String> followers = this.getUserFollowersIds(user.getId());
        File resourcesDirectory = new File("src/main/resources/users/"+fileName);
        mapper.writeValue(resourcesDirectory, followers);
        return followers;
    }

    private int computeValue(Set<String> followers1, Set<String> followers2){
        int ratio1 = 100 * countCommonUsers(followers1, followers2) / followers1.size();
        int ratio2 = 100 * countCommonUsers(followers1, followers2) / followers2.size();
        return (ratio1+ratio2)/2;
   //     int nbTotalFollowers = followers1.size() + followers2.size() - countCommonUsers(followers1, followers2);
   //     return 100*countCommonUsers(followers1, followers2)/nbTotalFollowers;
    }

    private int computeLinkValue(int value, int minMatching){
        return 1+(value - minMatching)/5;
    }

    private int computeSize(int nbFollowers){
        if(nbFollowers<50000){
            return 5;
        } else if (nbFollowers<100000){
            return 6;
        } else if (nbFollowers<150000){
            return 8;
        }else if (nbFollowers<200000){
            return 7;
        } else if (nbFollowers<250000){
            return 8;
        } else if (nbFollowers<500000){
            return 9;
        } else{
            return 10;
        }
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
        int[][] result = new int[users.size()][others.size()];
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
        LOGGER.info(arrayToCsvString(result, users, others,";",""));
    }

    public Map<GroupEnum, Integer> analyzeUser(String user, List<UserGraph> users){
        Map<GroupEnum, MatchingNumber> map = new HashMap<>();
        // init map
        map.put(GroupEnum.EX_GAUCHE, new MatchingNumber(0,0)); // first = sum of %, second = sum of profiles
        map.put(GroupEnum.PS, new MatchingNumber(0,0));
        map.put(GroupEnum.LREM, new MatchingNumber(0,0));
        map.put(GroupEnum.PR, new MatchingNumber(0,0));
        map.put(GroupEnum.LR, new MatchingNumber(0,0));
        map.put(GroupEnum.EX_DROITE, new MatchingNumber(0,0));
        Set<String> userFollowersIds = this.getUserFollowersIds(this.getUserFromUserName(user).getId());
        for(UserGraph userGraph : users){
            Set<String> followers = this.getUserFollowersIds(this.getUserFromUserName(userGraph.getId()).getId());
            int value = this.computeValue(userFollowersIds, followers);
            map.get(userGraph.getGroupEnum()).incrementMatchingSum(value);
            map.get(userGraph.getGroupEnum()).incrementNbElements();
        }

        Map<GroupEnum, Integer> result = new HashMap<>();
        for(Map.Entry<GroupEnum, MatchingNumber> element : map.entrySet()){
            result.put(element.getKey(), element.getValue().getAverage());
        }
        return result;
    }

    public static String arrayToCsvString(int[][] s, List<String> names, String separator, String quote) {

        int len = s.length;
        StringBuffer sb = new StringBuffer(len * 16);
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

    public static String arrayToCsvString(int[][] s, List<String> names1, List<String> names2, String separator,
                                          String quote) {

        int len = s.length;
        StringBuffer sb = new StringBuffer(len * 16);
        sb.append(separator);
        for(int i=0;i<names2.size();i++){
            sb.append(quote);
            sb.append(names2.get(i));
            sb.append(quote);
            sb.append(separator);
        }
        sb.append("\n");

        for (int i = 0; i < len; i++) {
            sb.append(names1.get(i));
            sb.append(separator);
            for(int j=0; j<s[i].length; j++){
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
