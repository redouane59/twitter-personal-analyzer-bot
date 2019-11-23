package com.socialmediaraiser.twitter.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.socialmediaraiser.twitter.AbstractTwitterBot;
import com.socialmediaraiser.twitter.User;
import com.socialmediaraiser.twitter.helpers.dto.getuser.AbstractUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sun.security.tools.keytool.Main;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class FollowerAnalyzer extends AbstractTwitterBot {

    private List<String> unwantedWords = Arrays.asList("aaa", "les", "de", "des", "and", "www","http",
            "https","pour","est","que","par","sur","for","own","chez","com","compte","qui","une","are","dans","pas",
            "the","vous","mes","moi","with","about", "tweets","votre","suis","mon","tout","you","vie","mais","plus","comme",
            "nous","ans","your","not","non","one","avec","rien","sont","but","tous","bien","ici","son","fais","parle","toi",
            "quand","aux","sans","être","ses","ils","depuis","avant","ceux","aime","engagent","faire","autres","twitter",
            "nos","notre","même","entre");
    public FollowerAnalyzer(String ownerName, boolean follow, boolean saveResults) {
        super(ownerName, follow, saveResults);
    }

    public Map<String, Integer> analyzeBios(String userName, int nbResults){
        List<AbstractUser> users = this.getFollowerUsers(this.getUserFromUserName(userName).getId());
        HashMap<String, Integer> map = new HashMap<>();

        for(AbstractUser user : users){
            User u = (User)user;
            String[] words  = u.getDescription().replace("-","").replaceAll("[^\\p{L}]", " ")
                    .toLowerCase().split("\\s+");
            for(String word : words){
                if(word.length()>2 && !unwantedWords.contains(word)) {
                    map.put(word, map.getOrDefault(word, 0) + 1);
                }
            }
        }

        Object[] a = map.entrySet().toArray();
        Arrays.sort(a, (Comparator) (o1, o2) -> ((Map.Entry<String, Integer>) o2).getValue()
                .compareTo(((Map.Entry<String, Integer>) o1).getValue()));
        int currentResults = 0;

        Map<String, Integer> result = new HashMap<>();

        for (Object e : a) {
            Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) e;
            System.out.println(entry.getKey() + " : " + entry.getValue());
            result.put(entry.getKey(), entry.getValue());
            currentResults++;
            if(currentResults>=nbResults) break;
        }

        return result;
    }

    public int countCommonUsers(String userName1, String userName2){
        List<String> followers1 = this.getFollowerIds(this.getUserFromUserName(userName1).getId());
        List<String> followers2 = this.getFollowerIds(this.getUserFromUserName(userName2).getId());

        List<String> common = new ArrayList<>(followers1);
        common.retainAll(followers2);

        return common.size();
    }

    public void countCommonUsers(String userName1, List<String> userNames){
        List<String> followers1 = this.getFollowerIds(this.getUserFromUserName(userName1).getId());

        for(String userName : userNames){
            List<String> followers2 = this.getFollowerIds(this.getUserFromUserName(userName).getId());
            List<String> common = new ArrayList<>(followers1);
            common.retainAll(followers2);
            System.out.println(userName1 + " + " + userName + " = " + common.size());
        }
    }


    public String getJsonGraph(HashSet<UserGraph> users) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonGraph graph = new JsonGraph();
        graph.setNodes(users);

        Map<String, String> edges = new HashMap<>();
        for(UserGraph user1 : users){
            int minMatching = 25;
            for(UserGraph user2 : users) {
                if (user1!=user2
                        && !(edges.containsKey(user1.getId()) && edges.get(user1.getId()).equals(user2.getId()))
                        && !(edges.containsKey(user2.getId()) && edges.get(user2.getId()).equals(user1.getId()))){
                    HashSet followers1 = new HashSet(Optional.of(this.getFollowerIds(this.getUserFromUserName(user1.getId()).getId()))
                            .orElse(new ArrayList<>()));
                    HashSet followers2 = new HashSet(Optional.of(this.getFollowerIds(this.getUserFromUserName(user2.getId()).getId()))
                            .orElse(new ArrayList<>()));
                    HashSet<String> common = new HashSet<>(followers1);
                    common.retainAll(followers2);
                    int value = 100 * common.size() / Math.min(followers1.size(), followers2.size());
                    if (value > minMatching) {
                        edges.put(user1.getId(), user2.getId());
                        System.out.println("***\nlinks added between " + user1.getId() + " & " + user2.getId()
                                + " (" + (value) + "%)\n***");
                        graph.getLinks().add(new Link(user1.getId(), user2.getId(), 1+(value - minMatching)/5));
                    } else{
                        System.out.println("links NOT added between " + user1.getId() + " & " + user2.getId()
                                + " (" + (value) + "%)");
                    }
                }
            }
            System.out.println(mapper.writeValueAsString(graph));
        }
        String result = mapper.writeValueAsString(graph);
        System.out.println(result);
        try {
            mapper.writeValue(new File("twitterGraph.json"), result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<AbstractUser> getPotentialFollowers(String ownerId, int count) {
        return null;
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

    @Data
    @AllArgsConstructor
    public class Link{
        private String source;
        private String target;
        private int value;
    }
}
