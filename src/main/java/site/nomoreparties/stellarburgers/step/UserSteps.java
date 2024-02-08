package site.nomoreparties.stellarburgers.step;


import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.client.UserClient;
import site.nomoreparties.stellarburgers.dto.UserCreateRequest;

public class UserSteps {
    private final UserClient userClient;

    public UserSteps(UserClient userClient) {
        this.userClient = userClient;
    }

    @Step("Send POST request /api/auth/register")
    public ValidatableResponse create(String email, String password, String name) {
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setEmail(email);
        userCreateRequest.setPassword(password);
        userCreateRequest.setName(name);
        return userClient.create(userCreateRequest).then();
    }

    @Step("Send POST request /api/auth/login")
    public ValidatableResponse login(String email, String password, String name) {
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setEmail(email);
        userCreateRequest.setPassword(password);
        userCreateRequest.setName(name);
        return userClient.login(userCreateRequest).then();
    }

    @Step("получить токен пользователя")
    public String token(String email, String password, String name) {
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setEmail(email);
        userCreateRequest.setPassword(password);
        userCreateRequest.setName(name);
        return userClient.login(userCreateRequest).then().extract().body().path("accessToken");

    }

    @Step("удалить тестового пользователя")
    public void delete (String accessToken) {
        userClient.delete(accessToken).then();
    }


    @Step("Send PATCH request /api/auth/user")
    public ValidatableResponse update(String email, String password, String name, String accessToken) {
        UserCreateRequest userCreateRequest = new UserCreateRequest();
        userCreateRequest.setEmail(email);
        userCreateRequest.setPassword(password);
        userCreateRequest.setName(name);
        return userClient.update(userCreateRequest, accessToken).then();
    }
}






