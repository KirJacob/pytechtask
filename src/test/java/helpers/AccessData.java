package helpers;

public interface AccessData {
    String login = LoadProperties.load("login");
    String password = LoadProperties.load("password");
    String mainURL = LoadProperties.load("mainURL");
    String apiURL = LoadProperties.load("apiURL");
    String tweetURL = LoadProperties.load("tweetsURL");
}
