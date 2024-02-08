package site.nomoreparties.stellarburgers;
import io.qameta.allure.junit4.DisplayName;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.client.OrderClient;
import site.nomoreparties.stellarburgers.client.UserClient;
import site.nomoreparties.stellarburgers.step.OrderSteps;
import site.nomoreparties.stellarburgers.step.UserSteps;
import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrdersUserTest {
    private OrderSteps orderSteps;

    private UserSteps userSteps;

    String[] domenName=new String[]{"@mail.ru","@yandex.ru","@gmail.com"};
    String email = RandomStringUtils.random(10, "qwertyyuiopasdfghjklzxcvbnm") + domenName[RandomUtils.nextInt(0,domenName.length)];
    String password = RandomStringUtils.random(8, "qwertyyuiopasdfghjklzxcvbnm1234567890");
    String name = RandomStringUtils.random(10, "qwertyyuiopasdfghjklzxcvbnm");
    String accessToken = "";
    List<String> idIngredients;


    @Before
    public void setUp() {
        orderSteps = new OrderSteps(new OrderClient());
        userSteps = new UserSteps(new UserClient());
        userSteps.create(email, password, name);
        this.accessToken=userSteps.token(email, password, name);
    }

    @Test
    @DisplayName("получить заказы по авторизованному пользователю")
    public void getOrdersWithAuth() {
        orderSteps.getAllIdIngredients();
        this.idIngredients = orderSteps.getAllIdIngredients();
        int generateNumberElementOne = RandomUtils.nextInt(0, idIngredients.size());
        int generateNumberElementTwo = RandomUtils.nextInt(0, idIngredients.size());
        List<String> ingredients = new ArrayList<>();
        ingredients.add(idIngredients.get(generateNumberElementOne));
        ingredients.add(idIngredients.get(generateNumberElementTwo));
        orderSteps.create(ingredients, accessToken);
        orderSteps.getOrders(accessToken)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("orders.ingredients",notNullValue());
    }

    @Test
    @DisplayName("не получить заказы без авторизации")
    public void getOrdersWithOutAuth() {
        orderSteps.getOrders("")
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message",equalTo("You should be authorised"));
    }

    @After
    public void delete(){
        userSteps.delete(accessToken);
    }

}
