package com.socialmediaraiser.core.twitter.signature;

import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import lombok.Data;
import okhttp3.*;
import okio.Buffer;
import okio.ByteString;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Logger;

@Data
public final class Oauth1SigningInterceptor implements Interceptor {
    private static final Logger LOGGER = Logger.getLogger(Oauth1SigningInterceptor.class.getName());
    private static final Escaper ESCAPER = UrlEscapers.urlFormParameterEscaper();
    private static final String OAUTH_CONSUMER_KEY = "oauth_consumer_key";
    private static final String OAUTH_NONCE = "oauth_nonce";
    private static final String OAUTH_SIGNATURE = "oauth_signature";
    private static final String OAUTH_SIGNATURE_METHOD = "oauth_signature_method";
    private static final String OAUTH_SIGNATURE_METHOD_VALUE = "HMAC-SHA1";
    private static final String OAUTH_TIMESTAMP = "oauth_timestamp";
    private static final String OAUTH_ACCESS_TOKEN = "oauth_token";
    private static final String OAUTH_VERSION = "oauth_version";
    private static final String OAUTH_VERSION_VALUE = "1.0";

    private final String oauthNonce;
    private String oauthTimestamp;
    private final String consumerKey;
    private final String consumerSecret;
    private final String accessToken;
    private final String accessSecret;

    private Oauth1SigningInterceptor(String consumerKey, String consumerSecret, String accessToken,
                                     String accessSecret, String oauthNonce, String oauthTimestamp) {
        this.consumerKey = consumerKey;
        this.consumerSecret = consumerSecret;
        this.accessToken = accessToken;
        this.accessSecret = accessSecret;
        this.oauthNonce = oauthNonce;
        this.oauthTimestamp = oauthTimestamp;
    }

    @Override public Response intercept(Interceptor.Chain chain) throws IOException {
        return chain.proceed(signRequest(chain.request()));
    }

    public Request signRequest(Request request) {
        String consumerKeyValue = ESCAPER.escape(consumerKey);
        String accessTokenValue = ESCAPER.escape(accessToken);

        SortedMap<String, String> parameters = new TreeMap<>();
        parameters.put(OAUTH_CONSUMER_KEY, consumerKeyValue);
        parameters.put(OAUTH_ACCESS_TOKEN, accessTokenValue);
        parameters.put(OAUTH_NONCE, oauthNonce);
        parameters.put(OAUTH_TIMESTAMP, oauthTimestamp);
        parameters.put(OAUTH_SIGNATURE_METHOD, OAUTH_SIGNATURE_METHOD_VALUE);
        parameters.put(OAUTH_VERSION, OAUTH_VERSION_VALUE);

        HttpUrl url = request.url();
        for (int i = 0; i < url.querySize(); i++) {
            parameters.put(ESCAPER.escape(url.queryParameterName(i)),
                    ESCAPER.escape(url.queryParameterValue(i)));
        }

        if(request.body()!=null) {
            RequestBody requestBody = request.body();
            Buffer body = new Buffer();
            try {
                requestBody.writeTo(body);
            } catch (IOException e) {
                LOGGER.severe(e.getMessage());
                body.close();
            }

     /*       while (!body.exhausted()) {
                long keyEnd = body.indexOf((byte) '=');
                if (keyEnd == -1) throw new IllegalStateException("Key with no parameterValue: " + body.readUtf8());
                String key = body.readUtf8(keyEnd);
                body.skip(1); // Equals.

                long valueEnd = body.indexOf((byte) '&');
                String value = valueEnd == -1 ? body.readUtf8() : body.readUtf8(valueEnd);
                if (valueEnd != -1) body.skip(1); // Ampersand.

                parameters.put(key, value);
            }*/
        }

        try (Buffer base = new Buffer()){

        String method = request.method();
        base.writeUtf8(method);
        base.writeByte('&');
        base.writeUtf8(ESCAPER.escape(request.url().newBuilder().query(null).build().toString()));
        base.writeByte('&');

        boolean first = true;
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            if (!first) base.writeUtf8(ESCAPER.escape("&"));
            first = false;
            base.writeUtf8(ESCAPER.escape(entry.getKey()));
            base.writeUtf8(ESCAPER.escape("="));
            base.writeUtf8(ESCAPER.escape(entry.getValue()));
        }

        String signingKey =
                ESCAPER.escape(consumerSecret) + "&" + ESCAPER.escape(accessSecret);

        SecretKeySpec keySpec = new SecretKeySpec(signingKey.getBytes(), "HmacSHA1");
        Mac mac;
        try {
            mac = Mac.getInstance("HmacSHA1");
            mac.init(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException(e);
        }
        byte[] result = mac.doFinal(base.readByteArray());
        String signature = ByteString.of(result).base64();

        String authorization = "OAuth "
                + OAUTH_CONSUMER_KEY + "=\"" + consumerKeyValue + "\", "
                + OAUTH_NONCE + "=\"" + oauthNonce + "\", "
                + OAUTH_SIGNATURE + "=\"" + ESCAPER.escape(signature) + "\", "
                + OAUTH_SIGNATURE_METHOD + "=\"" + OAUTH_SIGNATURE_METHOD_VALUE + "\", "
                + OAUTH_TIMESTAMP + "=\"" + oauthTimestamp + "\", "
                + OAUTH_ACCESS_TOKEN + "=\"" + accessTokenValue + "\", "
                + OAUTH_VERSION + "=\"" + OAUTH_VERSION_VALUE + "\"";

        return request.newBuilder()
                .addHeader("Authorization", authorization)
                .build();

        }

    }

    public static final class Builder {
        private String consumerKey;
        private String consumerSecret;
        private String accessToken;
        private String accessSecret;
        private String oauthNonce;
        private String oauthTimeStamp;

        public Builder consumerKey(String consumerKey) {
            if (consumerKey == null) throw new NullPointerException("CONSUMER_KEY = null");
            this.consumerKey = consumerKey;
            return this;
        }

        public Builder consumerSecret(String consumerSecret) {
            if (consumerSecret == null) throw new NullPointerException("CONSUMER_SECRET = null");
            this.consumerSecret = consumerSecret;
            return this;
        }

        public Builder accessToken(String accessToken) {
            if (accessToken == null) throw new NullPointerException("ACCESS_TOKEN == null");
            this.accessToken = accessToken;
            return this;
        }

        public Builder accessSecret(String accessSecret) {
            if (accessSecret == null) throw new NullPointerException("accessSecret == null");
            this.accessSecret = accessSecret;
            return this;
        }

        public Builder oauthNonce(String oauthNonce) {
            if (oauthNonce == null) throw new NullPointerException("random == null");
            this.oauthNonce = oauthNonce;
            return this;
        }

        public Builder oauthTimeStamp(String oauthTimeStamp) {
            if (oauthTimeStamp == null) throw new NullPointerException("clock == null");
            this.oauthTimeStamp = oauthTimeStamp;
            return this;
        }

        public Oauth1SigningInterceptor build() {
            if (consumerKey == null) throw new IllegalStateException("CONSUMER_KEY not set");
            if (consumerSecret == null) throw new IllegalStateException("CONSUMER_SECRET not set");
            if (accessToken == null) throw new IllegalStateException("ACCESS_TOKEN not set");
            if (accessSecret == null) throw new IllegalStateException("accessSecret not set");
            if (oauthNonce == null) throw new IllegalStateException("oauthNonce not set");
            if (oauthTimeStamp == null) throw new IllegalStateException("oauthTimeStamp not set");
            return new Oauth1SigningInterceptor(consumerKey, consumerSecret, accessToken, accessSecret, oauthNonce, oauthTimeStamp);
        }
    }
}