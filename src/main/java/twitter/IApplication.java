package twitter;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface IApplication {
    int getNbFollowers(Long userId) throws IOException, InterruptedException, InvalidKeyException, NoSuchAlgorithmException;
    int getNbFollowings(Long userId) throws IOException, InterruptedException, InvalidKeyException, NoSuchAlgorithmException;
    int getRetweeters(Long tweetId) throws IOException, InterruptedException, InvalidKeyException, NoSuchAlgorithmException;

}

