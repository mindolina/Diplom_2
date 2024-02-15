package site.nomoreparties.stellarburgers.step;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.client.OrderClient;
import site.nomoreparties.stellarburgers.dto.OrderCreateRequest;

import java.util.ArrayList;
import java.util.List;


public class OrderSteps {
    private final OrderClient orderClient;

    public OrderSteps(OrderClient orderClient) {
        this.orderClient = orderClient;
    }
    @Step("Send POST request /api/orders")
    public ValidatableResponse create(List<String> ingredients,String accessToken) {
        OrderCreateRequest orderCreateRequest=new OrderCreateRequest();
        orderCreateRequest.setIngredients(ingredients);
        return orderClient.create(orderCreateRequest, accessToken).then();
    }

    @Step("Send GET request /api/ingredients")
    public ArrayList<String> getAllIdIngredients() {
        return orderClient.getIngredients().then().extract().path("data._id");
    }

    @Step("Send GET request /api/orders")
    public ValidatableResponse getOrders(String accessToken) {
        return orderClient.getOrders(accessToken).then();

    }

}
