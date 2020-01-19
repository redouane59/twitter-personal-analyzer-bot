package com.socialmediaraiser.twittersocialgraph;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.socialmediaraiser.core.twitter.TwitterHelper;
import com.socialmediaraiser.core.twitter.helpers.dto.getuser.AbstractUser;
import com.socialmediaraiser.twittersocialgraph.impl.*;
import lombok.Data;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Logger;

@Data
public class FollowerAnalyzer extends TwitterHelper {

    private static final Logger LOGGER = Logger.getLogger(FollowerAnalyzer.class.getName());
    private double minRatioSimpleMatching = 0.2 ; // if one of the two user has this % of common, draw a 1 minimum link
    private double minRatioDoubleMatching = 0.1 ; // if the product of the two ratio > this, draw a wider link

    public FollowerAnalyzer() {
        super("twitter-credentials");
    }

    public int countCommonUsers(Set<String> users1, Set<String> users2){
        Set<String> common = new HashSet<>(users1);
        common.retainAll(users2);
        return common.size();
    }

    public String getJsonGraph(HashSet<UserGraph> users) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGraph graph = new JsonGraph();
        graph.setNodes(users);

        Set<Link> studiedLinks = new HashSet<>();
        for(UserGraph user1 : users){
            LOGGER.info("analyzing " + user1.getId() + " ... ");
            Set<String> followers1 = loadFollowers(user1.getId());
            // manage node size
            for (UserGraph f : graph.getNodes()) {
                if (f.getId().equals(user1.getId()))
                    f.setSize(this.computeSize(followers1.size()));
            }
            for(UserGraph user2 : users) {
                if (user1!=user2 && !studiedLinks.contains(new Link(user1.getId(), user2.getId(),0))){
                    AbstractUser user = this.getUserFromUserName(user2.getId());
                    if(user!=null){
                        Set<String> followers2 = loadFollowers(user2.getId());
                        if(followers2==null) continue;
                        int commonUsers = countCommonUsers(followers1, followers2);
                        double value1 = getRatioRelativeValue(followers1, followers2, commonUsers);
                        double value2 = getRatioRelativeValue(followers2, followers1, commonUsers);
                        if(shouldDrawLink(value1, value2)){
                            LOGGER.info("*** links added between "
                                    + user1.getId() + " ("+followers1.size()+" followers) & "
                                    + user2.getId() + " ("+followers2.size() + " followers) | "
                                    + value1 + " , " + value2 + " ***");
                            graph.getLinks().add(new Link(user1.getId(), user2.getId(),
                                    this.computeLinkValue(followers1, followers2)));
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

    public JsonGraph getJsonGraph(HashSet<UserGraph> usersToAnalyze, HashSet<UserGraph> users) throws IOException {
        this.minRatioDoubleMatching = 0.05;
        ObjectMapper mapper = new ObjectMapper();
        JsonGraph graph = new JsonGraph();
        if(usersToAnalyze.size()==1) graph.getNodes().add((UserGraph)usersToAnalyze.toArray()[0]);
            for(UserGraph user1 : usersToAnalyze){
            user1.setSize(20);
         //   graph.getNodes().add(user1);
            Set<String> followers1 = loadFollowers(user1.getId());
            for(UserGraph user2 : users) {
            //    LOGGER.info("analyzing " + user2.getId() + " ... ");
                Set<String> followers2 = loadFollowers(user2.getId());
                if(followers2==null || user1.getId().equals(user2.getId())) continue;
                int commonUsers = countCommonUsers(followers1, followers2);
                double value1 = getRatioRelativeValue(followers1, followers2, commonUsers);
                double value2 = getRatioRelativeValue(followers2, followers1, commonUsers);
                if(shouldDrawLink(value1, value2)){
                    if(!graph.getNodes().stream().anyMatch(o -> o.getId().equals(user2.getId()))){
                        graph.getNodes().add(user2);
                    }
                    LOGGER.info("*** links added between "
                            + user1.getId() + " ("+followers1.size()+" followers) & "
                            + user2.getId() + " ("+followers2.size() + " followers) | "
                            + value1 + " , " + value2 + " ***");
                    graph.getLinks().add(new Link(user1.getId(), user2.getId(),
                            this.computeLinkValue(followers1, followers2)));
                    for (UserGraph f : graph.getNodes()) {
                        if (f.getId().equals(user2.getId()))
                            f.setSize(this.computeSize(followers2.size()));
                    }
                }
            }
        }


        LOGGER.info("\n"+mapper.writeValueAsString(graph));

        String result = mapper.writeValueAsString(graph);
        try {
            mapper.writeValue(new File("twitterGraph.json"), result);
        } catch (IOException e) {
            LOGGER.severe(e.toString());
        }
        return graph;
    }


    private Set<String> loadFollowers(String userName) throws IOException {
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

    private boolean shouldDrawLink(Set<String> followers1, Set<String> followers2){
        int commonUsers = countCommonUsers(followers1, followers2);
        double value1 = getRatioRelativeValue(followers1, followers2, commonUsers);
        double value2 = getRatioRelativeValue(followers2, followers1, commonUsers);
        return shouldDrawLink(value1, value2);
    }

    private boolean shouldDrawLink(double value1, double value2){
        return (value1>= minRatioSimpleMatching || value2>= minRatioSimpleMatching);
    }

    public double getRatioRelativeValue(Set<String> followers1, Set<String> followers2, int commonUsers){
        double ratio1 = commonUsers / (double)followers1.size();
        double higherNbFollowers = Math.max(followers1.size(), followers2.size());
        double lowerNbFollowers = Math.min(followers1.size(), followers2.size());
        double diffSize = (higherNbFollowers-lowerNbFollowers)/higherNbFollowers;
        double coeff = 1.95;
        if(followers1.size()>followers2.size()){
            return ratio1*(1+coeff*diffSize);
        } else{
            return ratio1/(1+coeff*diffSize);
        }
    }

    private int computeLinkValue(Set<String> followers1, Set<String> followers2){
        int commonUsers = countCommonUsers(followers1, followers2);
        double ratio1 = commonUsers / (double)followers1.size();
        double ratio2 = commonUsers / (double)followers2.size();

        if ((ratio1*ratio2)>minRatioDoubleMatching){
            int nbTotalFollowers = followers1.size() + followers2.size() - commonUsers;
            double value = commonUsers/(double)nbTotalFollowers;
            return (int)(1+(value - minRatioDoubleMatching)*100);
        } else{
            return 1;
        }
    }

    private int computeSize(int nbFollowers){
        if(nbFollowers<25000){
            return 5;
        } else if (nbFollowers<50000){
            return 6;
        } else if (nbFollowers<75000){
            return 7;
        }else if (nbFollowers<100000){
            return 8;
        } else if (nbFollowers<150000){
            return 9;
        } else if (nbFollowers<200000){
            return 10;
        } else if (nbFollowers<300000){
            return 11;
        } else if (nbFollowers<400000){
            return 12;
        } else if (nbFollowers<500000){
            return 13;
        }else if (nbFollowers<750000){
            return 14;
        }  else if (nbFollowers<1000000){
            return 15;
        } else{
            return 17;
        }
    }

    public void getCsvArray(List<UserGraph> users) throws IOException, URISyntaxException {
        int[][] result = new int[users.size()][users.size()];
        for(int i=0; i<users.size(); i++){
            String userName1 = users.get(i).getId();
            LOGGER.info("*** analysing " +userName1);
            Set<String> followers1 = this.loadFollowers(userName1);
            for(int j=0;j<users.size(); j++) {
                String userName2 = users.get(j).getId();
                if (!userName1.equals(userName2)){
                    Set<String> followers2 = this.loadFollowers(userName2);
                    int commonFollowers = this.countCommonUsers(followers1, followers2);
                    int value = 100*commonFollowers/followers1.size();
                    /*LOGGER.info("*** links added between " + user1 + " ("+followers1.size()+") & " + user2
                            + "(" + followers2.size() +") -> " + (value) + "% ***");*/
                    result[i][j] = value;
                }
            }
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
                    int commonFollowers = this.countCommonUsers(followers1, followers2);
                    int value = 100*followers1.size()/commonFollowers;
                    LOGGER.info("*** links added between " + user1 + " ("+followers1.size()+") & " + user2
                            + "(" + followers2.size() +") -> " + (value) + "% ***");
                    result[i][j] = value;
                }
            }
            LOGGER.info(Arrays.toString(result));
        }
        LOGGER.info(arrayToCsvString(result, users, others,";",""));
    }

    public Map<GroupEnum, Double> analyzeUser(String user, List<UserGraph> users){
        Map<GroupEnum, MatchingNumber> map = new HashMap<>();
        map.put(GroupEnum.EX_GAUCHE, new MatchingNumber(0,0)); // first = sum of %, second = sum of profiles
        map.put(GroupEnum.PS, new MatchingNumber(0,0));
        map.put(GroupEnum.LREM, new MatchingNumber(0,0));
        map.put(GroupEnum.PR, new MatchingNumber(0,0));
        map.put(GroupEnum.LR, new MatchingNumber(0,0));
        map.put(GroupEnum.EX_DROITE, new MatchingNumber(0,0));
        Set<String> userFollowersIds = this.getUserFollowersIds(this.getUserFromUserName(user).getId());
        for(UserGraph userGraph : users){
            Set<String> followers = this.getUserFollowersIds(this.getUserFromUserName(userGraph.getId()).getId());
            double value = 1; //this.computeValue(userFollowersIds, followers);
            map.get(userGraph.getGroupEnum()).incrementMatchingSum(value);
            map.get(userGraph.getGroupEnum()).incrementNbElements();
        }

        Map<GroupEnum, Double> result = new HashMap<>();
        for(Map.Entry<GroupEnum, MatchingNumber> element : map.entrySet()){
            result.put(element.getKey(), element.getValue().getAverage());
        }
        return result;
    }

    public static String arrayToCsvString(int[][] s, List<UserGraph> names, String separator, String quote) {

        int len = s.length;
        StringBuffer sb = new StringBuffer(len * 16);
        sb.append(separator);
        for(int i=0;i<names.size();i++){
            sb.append(quote);
            sb.append(names.get(i).getId());
            sb.append(quote);
            sb.append(separator);
        }
        sb.append("\n");

        for (int i = 0; i < len; i++) {
            sb.append(names.get(i).getId());
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
