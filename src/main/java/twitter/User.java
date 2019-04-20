package twitter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private long id;
    private String screen_name;
    private int followers_count;
    private int friends_count;


    public double getFollowersRatio(){
        return (double)this.followers_count/(double)this.friends_count;
    }

    public boolean shouldBeFollowed(){
        int minNbFollowers = 300;
        int maxNbFollowers = 30000;
        int minRatio = 1;
        int maxRatio = 3;

        if(this.followers_count>minNbFollowers
                && this.followers_count<maxNbFollowers
                && this.getFollowersRatio()>minRatio
                && this.getFollowersRatio()<maxRatio){
            return true;
        } else{
            return false;
        }
    }

}
