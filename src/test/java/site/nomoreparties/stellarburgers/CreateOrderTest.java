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


public class CreateOrderTest {

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
        this.accessToken = userSteps.token(email, password, name);
    }

    @Test
    @DisplayName("Создание заказа без авторизации, c ингредиентами")
    public void createOrderWithOutAuth() {
        orderSteps.getAllIdIngredients();
        this.idIngredients = orderSteps.getAllIdIngredients();
        int generateNumberElement = RandomUtils.nextInt(0, idIngredients.size());

        List<String> ingredients = new ArrayList<>();
        ingredients.add(idIngredients.get(generateNumberElement));
        ingredients.add(idIngredients.get(generateNumberElement));

        orderSteps.create(ingredients, "")
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.number", notNullValue());

    }

    @Test
    @DisplayName("Создание заказа с авторизацией, c ингредиентами")
    public void createOrderWithAuth() {
        orderSteps.getAllIdIngredients();
        this.idIngredients = orderSteps.getAllIdIngredients();
        int generateNumberElementOne = RandomUtils.nextInt(0, idIngredients.size());
        int generateNumberElementTwo = RandomUtils.nextInt(0, idIngredients.size());
        List<String> ingredients = new ArrayList<>();
        ingredients.add(idIngredients.get(generateNumberElementOne));
        ingredients.add(idIngredients.get(generateNumberElementTwo));
        orderSteps.create(ingredients, accessToken)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("name", notNullValue())
                .body("order.ingredients._id[0]", equalTo(ingredients.get(0)))
                .body("order.ingredients._id[1]", equalTo(ingredients.get(1)))
                .body("order.status", equalTo("done"));

    }

    @Test
    @DisplayName("Создание заказа с авторизацией, без ингредиетов")
    public void createOrderWithAuthWithOutIngredients() {
        List<String> ingredients = new ArrayList<>();
        orderSteps.create(ingredients, accessToken)
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа без авторизации, без ингредиетов")
    public void createOrderWitOutAuroWithOutIngredients() {
        List<String> ingredients = new ArrayList<>();
        orderSteps.create(ingredients, "")
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithNotValidIdIngredients() {
        List<String> ingredients = new ArrayList<>();
        ingredients.add(RandomStringUtils.random(24, "qwertyuiopasdfghjklzxcvbnm123456789"));
        ingredients.add(RandomStringUtils.random(24, "qwertyuiopasdfghjklzxcvbnm123456789"));
        orderSteps.create(ingredients, accessToken)
                .statusCode(500)
                .body("html.body.pre", equalTo("Internal Server Error"));
    }

    @After
    public void delete() {
        userSteps.delete(accessToken);
    }
}

