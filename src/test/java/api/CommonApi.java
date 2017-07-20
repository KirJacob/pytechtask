package api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import static tests.api.BaseApiTest.setSpecification;

public class CommonApi {

    public static ArrayList<String> getUserStatusesIdCollection() throws IOException {
        System.out.println(">>> Getting All statuses id's");
        ArrayList<String> result = new ArrayList<String>();

        String responseBody = setSpecification().when().get("statuses/user_timeline.json")
                .then().contentType(ContentType.JSON).extract().response().asString();
        JSONArray responseJSON = new JSONArray(responseBody);

        int size = responseJSON.length();
        String tempId = null;

        for (int i = 0; i < size; i++){
            tempId = responseJSON.getJSONObject(i).getString("id_str");
            result.add(tempId);
        }

        return result;
    }

    public static JSONObject removeStatus(String id_str) throws IOException {
        System.out.println(">>> Remove status with id=" + id_str);

        String responseBody = setSpecification().when().post("statuses/destroy/" + id_str + ".json")
                .then().contentType(ContentType.JSON).extract().response().asString();
        return new JSONObject(responseBody);
    }

    public void cleanAllStatuses() throws IOException {
        System.out.println("Cleaning statuses after all tests");
        ArrayList idList = getUserStatusesIdCollection();
        String tempId = null;
        for (Object obj : idList){
            tempId = obj.toString();
            removeStatus(tempId);
        }
    }

    public static ArrayList<String> getUserAllMessagesCollection() throws IOException {
        System.out.println(">>> Getting All Received messages id's");

        ArrayList<String> result = new ArrayList<String>();
        String responseBody = setSpecification().when().get("direct_messages/events/list.json")
                .then().contentType(ContentType.JSON).extract().response().asString();
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

    public static JSONObject removeDirectMessage(String id_str) throws IOException {
        System.out.println(">>> Remove message with id=" + id_str);

        Response options = setSpecification().when().post("direct_messages/destroy.json" + "?id=" + id_str);
        String responseBody = options.then().contentType(ContentType.JSON).extract().response().asString();
        JSONObject result = new JSONObject(responseBody);

        return result;
    }

    public void cleanMessages(ArrayList<String> listOfIds) throws IOException {
        String tempId = null;
        for (Object obj : listOfIds){
            tempId = obj.toString();
            System.out.println("Removing message: " + tempId);
            removeDirectMessage(tempId);
        }
    }


}
