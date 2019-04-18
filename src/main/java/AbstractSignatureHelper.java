import lombok.Data;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public abstract class AbstractSignatureHelper
{

    private final String consumerKey = "LiMccelygYuyueOKZLaVk9N1R";
    private final String consumerSecret = "xxx";
    private final String accessToken = "92073489-N6BLM48cIKk3X5ya5RiXNPYlShFF1Z1vYRug6rRiv";
    private String secretToken = "xxx";
    public static final String OAUTH_TOKEN = "oauth_token";
    public static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
    public static final String OAUTH_SIGNATURE = "oauth_signature";
    public static final String OAUTH_NONCE = "oauth_nonce";
    public static final String OAUTH_TIMESTAMP = "oauth_timestamp";
    public static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
    public static final String OAUTH_VERSION = "oauth_version";
    private final String HMAC_SHA1 = "HmacSHA1";
    private final String ENC = "UTF-8";
    private Base64 base64 = new Base64();

    public abstract String getSignature(String url, String method, String params)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException;

    public List<NameValuePair> getParametersToEncrypt(){
        return this.getParametersToEncrypt(this.getNonce(), this.getTimestamp());
    }

    public String getStringParametersToEncrypt() throws UnsupportedEncodingException {
        return URLEncoder.encode(URLEncodedUtils.format(
                this.getParametersToEncrypt(this.getNonce(), this.getTimestamp()), ENC), ENC);
    }

    public String getStringParametersToEncrypt(String nonce, String timestamp) throws UnsupportedEncodingException {
        return URLEncoder.encode(URLEncodedUtils.format(
                this.getParametersToEncrypt(nonce, timestamp), ENC), ENC);
    }

    public List<NameValuePair> getParametersToEncrypt(String nonce, String timestamp){
        List<NameValuePair> qparams = new ArrayList<>();
        // These params should ordered in key
        qparams.add(new BasicNameValuePair(OAUTH_CONSUMER_KEY, this.getConsumerKey()));
        qparams.add(new BasicNameValuePair(OAUTH_TOKEN, this.getAccessToken()));
        qparams.add(new BasicNameValuePair(OAUTH_SIGNATURE_METHOD, "HMAC-SHA1"));
        qparams.add(new BasicNameValuePair(OAUTH_TIMESTAMP, timestamp));
        qparams.add(new BasicNameValuePair(OAUTH_NONCE, nonce));
        qparams.add(new BasicNameValuePair(OAUTH_VERSION, "1.0"));
        return qparams;
    }

    private String getTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return String.valueOf(timestamp.getTime());
    }

    private String getNonce() {
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 15; i++) {
            stringBuilder.append(secureRandom.nextInt(10));
        }
        String randomNumber = stringBuilder.toString();
        return randomNumber;
    }
}
