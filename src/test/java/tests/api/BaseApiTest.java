package tests.api;

import api.CommonApi;
import api.Helpers;
import helpers.AccessData;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class BaseApiTest extends CommonApi implements AccessData {

    public static RequestSpecification setSpecification(){
        RestAssured.baseURI = apiURL;
        RestAssured.basePath = "/1.1/";

        return RestAssured.given().auth()
                .oauth(Helpers.request.CONSUMER_KEY, Helpers.request.CONSUMER_SECRET, Helpers.request.token, Helpers.request.secret);
    }

}
