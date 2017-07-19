package tests.api;

import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import java.io.IOException;
import static api.Requests.*;

public class MainApiTest extends BaseApiTest {

    //POST https://dev.twitter.com/rest/reference/post/statuses/update
    @Test(priority = 1, enabled = true)
    public void updateStatus_Test() throws IOException{
        //create status
        String textFor = "Mars should be colonized till 2040";
        request.loadAuth("User1");
        JSONObject result = updateStatus(textFor);

        Assert.assertTrue(result.has("text"));
        Assert.assertEquals(result.getString("text"), textFor);
        request.loadAuth("User1");
        cleanAllStatuses();
    }

    //GET https://dev.twitter.com/rest/reference/get/statuses/user_timeline
    @Test(priority = 2, enabled = true)
    public void userTimeLine_Test() throws IOException{
        //create two nes statuses
        request.loadAuth("User1");
        String textFor1 = "Mars should be colonized till 2060";
        String textFor2 = "Mars should be colonized till 2080";
        updateStatus(textFor1);
        updateStatus(textFor2);

        Assert.assertTrue(getUserStatuses().length() == 2);
        Assert.assertEquals(getUserStatuses().getJSONObject(0).getString("text"), textFor2);
        Assert.assertEquals(getUserStatuses().getJSONObject(1).getString("text"), textFor1);
        request.loadAuth("User1");
        cleanAllStatuses();
    }

    //POST https://dev.twitter.com/rest/reference/post/direct_messages/new (optional)
    @Test(priority = 3, enabled = true)
    public void directMessagesNew_Test() throws IOException{
        request.loadAuth("User1");
        String messageText = "Hey From rest";
        String recipient = "qa_yakov";
        JSONObject result = sendDirectMessage(messageText, recipient);

        Assert.assertTrue(result.has("text"));
        Assert.assertEquals(result.getString("text"), messageText);
        Assert.assertEquals(result.getJSONObject("recipient").getString("name"), recipient);
    }

    //GET https://dev.twitter.com/rest/reference/get/direct_messages (optional)
    @Test(priority = 4, enabled = true)
    public void directMessages_Test() throws IOException{
        request.loadAuth("User2");
        String messageText1 = "Hey from Rest and User2";
        String messageText2 = "Buy from User2";
        String recipient = "AutoqaYakov";
        sendDirectMessage(messageText1, recipient);
        sendDirectMessage(messageText2, recipient);

        request.loadAuth("User1");
        JSONArray result = getUserMessages();

        Assert.assertTrue(result.length() == 2);
        Assert.assertEquals(result.getJSONObject(0).getString("recipient_screen_name"), recipient);
        Assert.assertEquals(result.getJSONObject(1).getString("recipient_screen_name"), recipient);
        Assert.assertEquals(result.getJSONObject(1).getString("text"), messageText1);
        Assert.assertEquals(result.getJSONObject(0).getString("text"), messageText2);
    }

    //POST https://dev.twitter.com/rest/reference/post/account/update_profile (optional)
    @Test(priority = 5, enabled = true)
    public void updateProfile_Test() throws IOException{
        request.loadAuth("User2");
        String newName = "qa_yakov_updated";
        String oldName = "qa_yakov";
        JSONObject result = updateProfile("name=" + newName);

        Assert.assertEquals(result.getString("name"), newName);
        updateProfile("name=" + oldName);
    }

    @AfterClass(enabled = true)
    public void clean() throws IOException, InterruptedException {
        request.loadAuth("User1");
        cleanMessages(getUserAllMessagesCollection());

        request.loadAuth("User2");
        cleanMessages(getUserAllMessagesCollection());
    }
}
