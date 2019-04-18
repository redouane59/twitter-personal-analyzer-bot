package twitter.helpers;

import lombok.Data;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
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
    private final String version = "1.0";
    private final String algorithm = "HmacSHA1";
    private final String signatureMethod = "HMAC-SHA1";
    public static final String OAUTH_TOKEN = "oauth_token";
    public static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
    public static final String OAUTH_SIGNATURE = "oauth_signature";
    public static final String OAUTH_NONCE = "oauth_nonce";
    public static final String OAUTH_TIMESTAMP = "oauth_timestamp";
    public static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
    public static final String OAUTH_VERSION = "oauth_version";
    private final String ENC = "UTF-8";
    private Base64 base64 = new Base64();

    public abstract String getSignature(String url, String method, String params)
            throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException;

    public String getBaseSignature(String url, String method, String params) {
        return method+"&"+this.encode(url) + "&" + params;
    }

    public String getSigningKey(){
        return this.consumerSecret + "&" + this.secretToken;
    }

    public List<NameValuePair> getParametersToEncrypt(){
        return this.getParametersToEncrypt(this.getNonce(), this.getTimestamp());
    }

 /*   public String getStringParametersToEncrypt() throws UnsupportedEncodingException {
        return URLEncoder.encode(URLEncodedUtils.format(
                this.getParametersToEncrypt(this.getNonce(), this.getTimestamp()), ENC), ENC);
    } */

    public String getStringParametersToEncrypt(String nonce, String timestamp) throws UnsupportedEncodingException {
      //  return URLEncoder.encode(URLEncodedUtils.format(this.getParametersToEncrypt(nonce, timestamp), ENC), ENC);
       return String.valueOf(this.getParametersToEncrypt(nonce, timestamp))
               .replace(", ", "&")
               .replace("[","")
               .replace("]","");
    }

    public List<NameValuePair> getParametersToEncrypt(String nonce, String timestamp){
        List<NameValuePair> qparams = new ArrayList<>();
        // These params should ordered in key
        qparams.add(new BasicNameValuePair(OAUTH_CONSUMER_KEY, this.consumerKey));
        qparams.add(new BasicNameValuePair(OAUTH_NONCE, nonce));
        qparams.add(new BasicNameValuePair(OAUTH_SIGNATURE_METHOD, "HMAC-SHA1"));
        qparams.add(new BasicNameValuePair(OAUTH_TIMESTAMP, timestamp));
        qparams.add(new BasicNameValuePair(OAUTH_TOKEN, this.accessToken));
        qparams.add(new BasicNameValuePair(OAUTH_VERSION, this.version));
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
        return stringBuilder.toString();
    }

    public String encode(String value) {
        String encoded = "";
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String sb = "";
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                sb += "%2A";
            } else if (focus == '+') {
                sb += "%20";
            } else if (focus == '%' && i + 1 < encoded.length()
                    && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
                sb += '~';
                i += 2;
            } else {
                sb += focus;
            }
        }
        return sb.toString();
    }

    public String getAuthorizationHeader(String method, String url, String nonce,String timestamp) throws IOException, InvalidKeyException, NoSuchAlgorithmException {

        String oauthSignature = this.getSignature(url, method, this.getStringParametersToEncrypt(nonce, timestamp));
        String result = "OAuth oauth_consumer_key" +
                "=\""+ this.getConsumerKey() + "\"" +
                "," + OAUTH_TOKEN + "=\""+this.getAccessToken()+"\"" +
                ","+ OAUTH_SIGNATURE_METHOD + "=\"" + this.getSignatureMethod() + "\"" +
                "," + OAUTH_TIMESTAMP + "=\"" + timestamp + "\"" +
                "," + OAUTH_NONCE + "=\"" + nonce + "\"" +
                "," + OAUTH_VERSION + "=\"" +this.getVersion()+ "\"" +
                "," + OAUTH_SIGNATURE + "=\"" + oauthSignature  + "\"";
        return result;
    }


}
