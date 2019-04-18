package twitter.helpers;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SignatureHelperImpl extends AbstractSignatureHelper {

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    public String getSignature(String url, String method, String params)
            throws UnsupportedEncodingException, NoSuchAlgorithmException,
            InvalidKeyException {

        byte[] keyBytes = this.getSigningKey().getBytes(this.getENC());

        SecretKey key = new SecretKeySpec(keyBytes, this.getAlgorithm());

        Mac mac = Mac.getInstance(this.getAlgorithm());
        mac.init(key);

        String baseString = this.getBaseSignature(url, method, params);
        byte[] byteEncodedData = mac.doFinal(baseString.getBytes());
        String stringEncodedData = new String(this.getBase64().encode(byteEncodedData));
        return this.encode(stringEncodedData);
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static byte[] hexToByes(String str){
        byte[] val = new byte[str.length() / 2];
        for (int i = 0; i < val.length; i++) {
            int index = i * 2;
            int j = Integer.parseInt(str.substring(index, index + 2), 16);
            val[i] = (byte) j;
        }
        return val;
    }


}