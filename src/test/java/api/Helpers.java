package api;

import helpers.LoadProperties;

public class Helpers {

    public static class request {

        public static String CONSUMER_KEY;
        public static String CONSUMER_SECRET;
        public static String token;
        public static String secret;

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
    }
}
