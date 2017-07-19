package api;

import okhttp3.Response;
import org.apache.commons.httpclient.util.URIUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import helpers.AccessData;
import org.testng.Assert;

import java.io.IOException;
import java.util.ArrayList;

public class Requests extends Helpers implements AccessData {

    public static JSONObject returnErrorMessage(Response response) throws IOException{
        return new JSONObject(response.body().string() + "\n" + response.code() + "\n" + response.message());
    }

    public static JSONObject updateStatus(String text) throws  IOException{
        System.out.println(">>> Create status with defined text");

        String textConverted = URIUtil.encodeQuery(text);
        Response response = Helpers.request.post(apiURL + "/1.1/statuses/update.json?status=" +  textConverted, "");
        String responseBody = response.body().string();
        Assert.assertEquals(response.code(),200);

        if (response.code() == 200) {
            JSONObject result = new JSONObject(responseBody);
            System.out.println(response.code() + " " + response.message());
            System.out.println("-----------------------");
            System.out.println("id_str=" + result.getString("id_str"));
            System.out.println("created_at=" + result.getString("created_at"));
            System.out.println("text=" + result.getString("text"));
            System.out.println("-----------------------");
            return result;
        } else {
            failTest(responseBody, response.code(), response.message());
            return returnErrorMessage(response);
        }
    }

    public static ArrayList<String> getUserStatusesIdCollection() throws IOException {
        System.out.println(">>> Getting All statuses id's");

        ArrayList<String> result = new ArrayList<String>();

        Response response = Helpers.request.get(apiURL + "/1.1/" + "statuses/user_timeline.json");
        String responseBody = response.body().string();
        JSONArray responseJSON = new JSONArray(responseBody);

        int size = responseJSON.length();
        String tempId = null;

        for (int i = 0; i < size; i++){
            tempId = responseJSON.getJSONObject(i).getString("id_str");
            result.add(tempId);
        }

        return result;
    }
    public static JSONArray getUserStatuses() throws IOException {
        System.out.println(">>> Getting All statuses id's");

        ArrayList result = new ArrayList();
        Response response = Helpers.request.get(apiURL + "/1.1/" + "statuses/user_timeline.json");
        String responseBody = response.body().string();

        return new JSONArray(responseBody);
    }

    public static JSONArray getUserMessages() throws IOException {
        System.out.println(">>> Getting All messages id's");

        ArrayList result = new ArrayList();
        Response response = Helpers.request.get(apiURL + "/1.1/direct_messages.json");
        String responseBody = response.body().string();

        return new JSONArray(responseBody);
    }

    public static ArrayList<String> getUserAllMessagesCollection() throws IOException {
        System.out.println(">>> Getting All Received messages id's");

        ArrayList<String> result = new ArrayList<String>();
        Response response = Helpers.request.get(apiURL + "/1.1/direct_messages/events/list.json");
        String responseBody = response.body().string();
        JSONObject responseJSON = new JSONObject(responseBody);

        int size = responseJSON.getJSONArray("events").length();
        String tempId = null;

        for (int i = 0; i < size; i++){
            tempId = responseJSON.getJSONArray("events").getJSONObject(i).getString("id");
            result.add(tempId);
        }
        System.out.println(result);
        return result;
    }

    public static JSONObject getUserStatuses(int index) throws IOException {
        System.out.println(">>> Getting USER statuses");

        Response response = Helpers.request.get(apiURL + "/1.1/" + "statuses/user_timeline.json");
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("-----------------------");
            JSONArray responseJSON = new JSONArray(responseBody);
            JSONObject result = responseJSON.getJSONObject(index);

            System.out.println("id_str=" + result.getString("id_str"));
            System.out.println("created_at=" + result.getString("created_at"));
            System.out.println("text=" + result.getString("text"));
            System.out.println("-----------------------");

            return result;
        } else {
            failTest(responseBody, response.code(), response.message());
            return returnErrorMessage(response);
        }
    }

    public static JSONObject sendDirectMessage(String text, String receiver) throws IOException {
        System.out.println(">>> Send direct message: " + text + " to :" + receiver);

        String textConverted = URIUtil.encodeQuery(text);
        String receiverConverted = URIUtil.encodeQuery(receiver);
        Response response = Helpers.request.post(apiURL + "/1.1/" + "direct_messages/new.json?" + "" +
                "text=" + textConverted + "&screen_name=" + receiverConverted, "");
        String responseBody = response.body().string();

        if (response.code() == 200) {
            System.out.println(response.code() + " " + response.message());
            System.out.println("-----------------------");
            JSONObject result = new JSONObject(responseBody);

            System.out.println("id_str=" + result.getString("id_str"));
            System.out.println("recipient_screen_name=" + result.getJSONObject("recipient").getString("screen_name"));
            System.out.println("text=" + result.getString("text"));
            System.out.println("-----------------------");

            return result;
        } else {
            failTest(responseBody, response.code(), response.message());
            return returnErrorMessage(response);
        }
    }

    public static JSONObject removeDirectMessage(String id_str) throws IOException {
        System.out.println(">>> Remove message with id=" + id_str);

        Response response = Helpers.request.post(apiURL + "/1.1/direct_messages/destroy.json"
                + "?id=" + id_str, "");
        String responseBody = response.body().string();
        JSONObject result = new JSONObject(responseBody);
        if (response.code() != 200) {
            result = returnErrorMessage(response);
        }
        return result;
    }

    public static JSONObject removeStatus(String id_str) throws IOException {
        System.out.println(">>> Remove status with id=" + id_str);

        Response response = Helpers.request.post(apiURL + "/1.1/statuses/destroy/" + id_str + ".json", "");
        String responseBody = response.body().string();
        JSONObject result = new JSONObject(responseBody);
        if (response.code() != 200) {
            result = returnErrorMessage(response);
        }
        return result;
    }

    public static JSONObject updateProfile(String parameterLine) throws IOException {
        System.out.println(">>> Updating profile name to : " + parameterLine);
        //        https://api.twitter.com/1.1/account/update_profile.json?name=qa_yakov
        Response response = Helpers.request.post(apiURL + "/1.1/account/update_profile.json?" + parameterLine, "");
        String responseBody = response.body().string();
        JSONObject result = new JSONObject(responseBody);
        if (response.code() != 200) {
            result = returnErrorMessage(response);
        }
        return result;
    }
}
