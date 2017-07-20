package tests.api;

import api.Helpers.request;
import helpers.AccessData;
import io.restassured.RestAssured;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;

public class AssuredApiTest extends BaseApiTest implements AccessData{

    @BeforeClass
    public static void setup(){
        RestAssured.baseURI = apiURL;
        RestAssured.basePath = "/1.1/";
    }

    //POST https://dev.twitter.com/rest/reference/post/statuses/update
    @Test(priority = 1, enabled = true)
    public void updateStatus_Test() throws IOException {
        request.loadAuth("User1");
        String textFor = "Mars should be colonized till 2045";
        setSpecification().when().post("statuses/update.json?status=" + textFor)
                .then()
                    .statusCode(200)
                    .body("text", equalTo(textFor));

        cleanAllStatuses();
    }

    //GET https://dev.twitter.com/rest/reference/get/statuses/user_timeline
    @Test(priority = 2, enabled = true)
    public void userTimeLine_Test() throws IOException{
        request.loadAuth("User1");
        String textFor1 = "Mars should be colonized till 2065";
        String textFor2 = "Mars should be colonized till 2085";

        setSpecification().when().post("statuses/update.json?status=" + textFor1);
        setSpecification().when().post("statuses/update.json?status=" + textFor2);

        setSpecification().when().get("statuses/user_timeline.json")
                .then()
                    .statusCode(200)
                    .body("size()", is(2))
                    .body("text[0]", equalTo(textFor2))
                    .body("text[1]", equalTo(textFor1));

        cleanAllStatuses();
    }

    //POST https://dev.twitter.com/rest/reference/post/direct_messages/new (optional)
    @Test(priority = 3, enabled = true)
    public void directMessagesNew_Test() throws IOException{
        request.loadAuth("User1");
        String messageText = "Hey From rest";
        String recipient = "qa_yakov";

        setSpecification().when().post("direct_messages/new.json?" + "text=" + messageText + "&screen_name=" + recipient)
                .then()
                    .statusCode(200)
                    .body("text", equalTo(messageText))
                    .body("recipient.name", equalTo(recipient));
    }

    //GET https://dev.twitter.com/rest/reference/get/direct_messages (optional)
    @Test(priority = 4, enabled = true)
    public void directMessages_Test() throws IOException{
        request.loadAuth("User2");
        String messageText1 = "Hey from Rest and User2";
        String messageText2 = "Buy from User2";
        String recipient = "AutoqaYakov";

        setSpecification().when().post("direct_messages/new.json?" + "text=" + messageText1 + "&screen_name=" + recipient);
        setSpecification().when().post("direct_messages/new.json?" + "text=" + messageText2 + "&screen_name=" + recipient);

        request.loadAuth("User1");
        setSpecification().when().get("direct_messages.json")
                .then()
                    .body("size()", is(2))
                    .body("text[0]", equalTo(messageText2))
                    .body("text[1]", equalTo(messageText1))
                    .body("recipient_screen_name[0]", equalTo(recipient))
                    .body("recipient_screen_name[1]", equalTo(recipient));
    }

    //POST https://dev.twitter.com/rest/reference/post/account/update_profile (optional)
    @Test(priority = 5, enabled = true)
    public void updateProfile_Test() throws IOException{
        request.loadAuth("User2");
        String newName = "qa_yakov_updated";
        String oldName = "qa_yakov";

        setSpecification().when().post("account/update_profile.json?" + "name=" + newName)
                .then()
                    .body("name", equalTo(newName));

        setSpecification().when().post("account/update_profile.json?" + "name=" + oldName);
    }

    @Test(priority = 5, enabled = true)
    public void debug() throws IOException{
    }

    @AfterClass(enabled = true)
    public void clean() throws IOException, InterruptedException {
        request.loadAuth("User1");
        cleanMessages(getUserAllMessagesCollection());

        request.loadAuth("User2");
        cleanMessages(getUserAllMessagesCollection());
    }

}
