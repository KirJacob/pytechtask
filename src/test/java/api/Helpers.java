package api;

import helpers.LoadProperties;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;

import java.io.IOException;

public class Helpers {

    public static class request {

        static String CONSUMER_KEY;
        static String CONSUMER_SECRET;
        static String token;
        static String secret;

        public static void loadAuth(String user){
            if (user.equals("User1")){
                System.out.println("Authenticating ... " + user);
                CONSUMER_KEY = LoadProperties.load("CONSUMER_KEY");
                CONSUMER_SECRET = LoadProperties.load("CONSUMER_SECRET");
                token = LoadProperties.load("token");
                secret = LoadProperties.load("secret");
            }
            if (user.equals("User2")){
                System.out.println("Authenticating ... " + user);
                CONSUMER_KEY = LoadProperties.load("CONSUMER_KEY2");
                CONSUMER_SECRET = LoadProperties.load("CONSUMER_SECRET2");
                token = LoadProperties.load("token2");
                secret = LoadProperties.load("secret2");
            }
        }

        public static Response get(String url) throws IOException {
            OkHttpClient client = new OkHttpClient();
            OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
            consumer.setTokenWithSecret(token, secret);

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            Request signedRequest = null;
            try {
                signedRequest = (Request) consumer.sign(request).unwrap();
            } catch (OAuthExpectationFailedException | OAuthCommunicationException | OAuthMessageSignerException e) {
                e.printStackTrace();
            }

            System.out.println(signedRequest.url());

            return client.newCall(signedRequest).execute();
        }

        public static Response post(String url, String payload) throws IOException {
            OkHttpClient client = new OkHttpClient();
            OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
            consumer.setTokenWithSecret(token, secret);

            RequestBody body = RequestBody.create(null, new byte[]{});
            Request request = new Request.Builder()
                    .url(url)
                    .method("POST", body)
                    .build();

            Request signedRequest = null;
            try {
                signedRequest = (Request) consumer.sign(request).unwrap();
            } catch (OAuthExpectationFailedException | OAuthCommunicationException | OAuthMessageSignerException e) {
                e.printStackTrace();
            }

            System.out.println(signedRequest.url());

            return client.newCall(signedRequest).execute();
        }
    }

    protected static void failTest(String responseBody, int responseCode, String responseMsg) throws IOException {
        System.out.println("Response: " + responseCode + " " + responseMsg);
        System.out.println(responseBody);
        System.out.println("--------");
        if (!(responseCode == 200) || !(responseCode == 204)) {
            throw new RuntimeException(responseBody +
                    "\nExpected:[200], Actual:[" + responseCode + "]");
        }
    }
}
