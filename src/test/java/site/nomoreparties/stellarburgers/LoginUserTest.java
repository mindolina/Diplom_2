package site.nomoreparties.stellarburgers;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.client.UserClient;
import site.nomoreparties.stellarburgers.step.UserSteps;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginUserTest {
    private UserSteps userSteps;
    String[] domenName=new String[]{"@mail.ru","@yandex.ru","@gmail.com"};
    String email = RandomStringUtils.random(10, "qwertyyuiopasdfghjklzxcvbnm") + domenName[RandomUtils.nextInt(0,domenName.length)];
    String password = RandomStringUtils.random(8, "qwertyyuiopasdfghjklzxcvbnm1234567890");
    String name = RandomStringUtils.random(10, "qwertyyuiopasdfghjklzxcvbnm");
    String generateRandomText = RandomStringUtils.random(5,"qwerty");
    String accessToken ="";

    @Before
    public void setUp() {
        userSteps = new UserSteps(new UserClient());
    }

    @Test
    @DisplayName("успешная авторизация")
    public void userLogin() {
        userSteps.create(email, password, name);
        userSteps.login(email, password, name)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(email))
                .body("user.name",equalTo(name))
                .body("accessToken", notNullValue())
                .body("refreshToken",notNullValue());
    }

    @Test
    @DisplayName("не авторизоваться, если неверный логин")
    public void userLoginWithErrorEmail() {
        userSteps.create(email, password, name);
        userSteps.login(generateRandomText+email, password, name)
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("не авторизоваться, если неверный пароль")
    public void userLoginWithErrorPassword() {
        userSteps.create(email, password, name);
        userSteps.login(email, password+generateRandomText, name)
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    public void delete(){
        this.accessToken=userSteps.token(email, password, name);
        userSteps.delete(accessToken);
    }
}
