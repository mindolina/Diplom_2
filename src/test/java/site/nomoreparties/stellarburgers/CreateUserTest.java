package site.nomoreparties.stellarburgers;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.client.UserClient;
import site.nomoreparties.stellarburgers.step.UserSteps;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateUserTest {
    private UserSteps userSteps;
    String[] domenName=new String[]{"@mail.ru","@yandex.ru","@gmail.com"};
    String email = RandomStringUtils.random(10, "qwertyyuiopasdfghjklzxcvbnm") + domenName[RandomUtils.nextInt(0,domenName.length)];
    String password = RandomStringUtils.random(8, "qwertyyuiopasdfghjklzxcvbnm1234567890");
    String name = RandomStringUtils.random(10, "qwertyyuiopasdfghjklzxcvbnm");
    String accessToken ="";


    @Before
    public void setUp(){
        userSteps=new UserSteps(new UserClient());
    }

    @Test
    @DisplayName("создание пользователя")
    public void createUser(){
       userSteps.create(email,password,name)
               .statusCode(200)
               .body("success", equalTo(true))
               .body("user.email", equalTo(email))
               .body("user.name",equalTo(name))
               .body("accessToken", notNullValue())
               .body("refreshToken",notNullValue());

    }

    @Test
    @DisplayName("нельзя создать пользователя, который уже зарегистрирован")
    public void createUserDublicate(){
        userSteps.create(email,password,name);
        userSteps.create(email,password,name)
                .statusCode(403)
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("нельзя создать пользователя не заполнив email")
    public void createUserWithoutEmail(){
        userSteps.create(email,password,name);
        String email="";
        userSteps.create(email,password,name)
                .statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));

    }

    @Test
    @DisplayName("нельзя создать пользователя не заполнив password")
    public void createUserWithoutPassword(){
        userSteps.create(email,password,name);
        String password="";
        userSteps.create(email,password,name)
                .statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
    }
    @Test
    @DisplayName("нельзя создать пользователя не заполнив name")
    public void createUserWithoutName(){
        userSteps.create(email,password,name);
        String name="";
        userSteps.create(email,password,name)
                .statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
    }


    @After
    public void delete(){
        this.accessToken=userSteps.token(email, password, name);
        userSteps.delete(accessToken);

    }



}
