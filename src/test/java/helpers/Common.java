package helpers;

import java.io.IOException;
import java.util.ArrayList;

import static api.Requests.*;
import static com.codeborne.selenide.Selenide.open;

public class Common {

    public void cleanMessages(ArrayList<String> listOfIds) throws IOException {
        String tempId = null;
        for (Object obj : listOfIds){
            tempId = obj.toString();
            System.out.println("Removing message: " + tempId);
            removeDirectMessage(tempId);
        }
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
}
