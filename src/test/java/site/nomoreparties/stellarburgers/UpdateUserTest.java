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

public class UpdateUserTest {
    private UserSteps userSteps;
    String[] domenName=new String[]{"@mail.ru","@yandex.ru","@gmail.com"};
    String email = RandomStringUtils.random(10, "qwertyyuiopasdfghjklzxcvbnm") + domenName[RandomUtils.nextInt(0,domenName.length)];
    String password = RandomStringUtils.random(8, "qwertyyuiopasdfghjklzxcvbnm1234567890");
    String name = RandomStringUtils.random(10, "qwertyyuiopasdfghjklzxcvbnm");
    String generate = RandomStringUtils.random(5, "qwerty");
    String accessToken ="";

    @Before
    public void setUp() {
        userSteps = new UserSteps(new UserClient());
        userSteps.create(email, password, name);
        this.accessToken=userSteps.token(email, password, name);
    }

    @Test
    @DisplayName("Изменение почты с авторизацией")
    public void userUpdateEmail() {
        userSteps.update(generate+email, password, name,accessToken)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(generate+email))
                .body("user.name",equalTo(name));
    }

    @Test
    @DisplayName("Изменение пароля с авторизацией")
    public void userUpdatePassword() {
        userSteps.update(email, generate+password, name,accessToken)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(email))
                .body("user.name",equalTo(name));
    }

    @Test
    @DisplayName("Изменение имени с авторизацией")
    public void userUpdateName() {
        userSteps.update(email, password, generate+name,accessToken)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(email))
                .body("user.name",equalTo(generate+name));
    }

    @Test
    @DisplayName("Нельзя измененить почту без авторизации")
    public void userUpdateEmailWithOutAuth() {
        userSteps.update(generate+email, password, name,"")
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Нельзя измененить пароль без авторизации")
    public void userUpdatePasswordWithOutAuth() {
        userSteps.update(email, generate+password, name,"")
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Нельзя измененить имя без авторизации")
    public void userUpdateNameWithOutAuth() {
        userSteps.update(email, password, generate+name,"")
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @After
    public void delete(){
        userSteps.delete(accessToken);

    }
}