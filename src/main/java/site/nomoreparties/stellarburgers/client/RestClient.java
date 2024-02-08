package site.nomoreparties.stellarburgers.client;


import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static site.nomoreparties.stellarburgers.config.Config.BASE_URL;


    public abstract class RestClient {
        protected RequestSpecification getDefaultRequestSpecification(){
            return  given()
                    .baseUri(BASE_URL)
                    .contentType(ContentType.JSON);
        }
}
