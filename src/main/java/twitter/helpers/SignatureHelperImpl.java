package twitter.helpers;

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

        // secretToken key.
        byte[] keyBytes = this.getSigningKey().getBytes(this.getENC());

        SecretKey key = new SecretKeySpec(keyBytes, this.getHMAC_SHA1());

        Mac mac = Mac.getInstance(this.getHMAC_SHA1());
        mac.init(key);

        return new String(this.getBase64().encode(mac.doFinal(this.getBaseSignature(url, method, params).getBytes(
                this.getENC()))), this.getENC()).trim();
    }

    @Override
    public String getBaseSignature(String url, String method, String params) {
        return this.encode(method + "&" + url + "&" + params);
    }


}