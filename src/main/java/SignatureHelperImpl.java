import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SignatureHelperImpl extends AbstractSignatureHelper {

    public String getSignature(String url, String method, String params)
            throws UnsupportedEncodingException, NoSuchAlgorithmException,
            InvalidKeyException {

        StringBuilder base = new StringBuilder();
        base.append(method);
        base.append("&");
        base.append(url);
        base.append("&");
        base.append(params);
        System.out.println("Stirng for oauth_signature generation:" + base);
        // secretToken key.
        byte[] keyBytes = (this.getConsumerKey() + "&" + this.getSecretToken()).getBytes(this.getENC());

        SecretKey key = new SecretKeySpec(keyBytes, this.getHMAC_SHA1());

        Mac mac = Mac.getInstance(this.getHMAC_SHA1());
        mac.init(key);

        return new String(this.getBase64().encode(mac.doFinal(base.toString().getBytes(
                this.getENC()))), this.getENC()).trim();
    }




}