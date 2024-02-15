package site.nomoreparties.stellarburgers.client;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.dto.UserCreateRequest;

import static io.restassured.RestAssured.given;
import static site.nomoreparties.stellarburgers.config.Config.BASE_URL;

public class UserClient extends RestClient {
    public Response create (UserCreateRequest userCreateRequest){
        return getDefaultRequestSpecification()
                .body(userCreateRequest)
                .when()
                .post("/api/auth/register");
    }

    public Response login (UserCreateRequest userCreateRequest){
        return getDefaultRequestSpecification()
                .body(userCreateRequest)
                .when()
                .post("/api/auth/login");
    }
    public Response delete (String accessToken){
        return getDefaultRequestSpecification()
                .header("Authorization", accessToken)
                .delete("/api/auth/user");
    }

    public Response update (UserCreateRequest userCreateRequest, String accessToken ){
        return getDefaultRequestSpecification()
                .header("Authorization", accessToken)
                .body(userCreateRequest)
                .patch("/api/auth/user");
    }
}
