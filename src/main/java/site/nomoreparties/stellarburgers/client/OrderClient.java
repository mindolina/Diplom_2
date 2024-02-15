package site.nomoreparties.stellarburgers.client;

import io.restassured.response.Response;
import site.nomoreparties.stellarburgers.dto.OrderCreateRequest;

public class OrderClient extends RestClient {

    public Response create (OrderCreateRequest orderCreateRequest, String accessToken){
        return getDefaultRequestSpecification()
                .header("Authorization", accessToken)
                .body(orderCreateRequest)
                .when()
                .post("/api/orders");
    }

    public Response getIngredients (){
        return getDefaultRequestSpecification()
                .get("/api/ingredients");
    }

    public Response getOrders (String accessToken){
        return getDefaultRequestSpecification()
                .header("Authorization", accessToken)
                .get("/api/orders");
    }

}
