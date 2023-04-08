package ru.praktikum.stellarburgers.order;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.praktikum.stellarburgers.client.OrderClient;
import ru.praktikum.stellarburgers.client.UserClient;
import ru.praktikum.stellarburgers.model.order.AccessIngredients;
import ru.praktikum.stellarburgers.model.order.OrderGenerator;
import ru.praktikum.stellarburgers.model.order.OrderIngredients;
import ru.praktikum.stellarburgers.model.user.User;
import ru.praktikum.stellarburgers.model.user.UserGenerator;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;

public class CreateOrderTests {

    private UserClient userClient;
    private String accessToken;
    private OrderClient orderClient;
    private AccessIngredients accessIngredientsPojo;

    @BeforeClass
    public static void globalSetUp() {
        RestAssured.filters(
                new RequestLoggingFilter(), new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
    }

    @After
    public void deleteUser() {
        if(accessToken != null) {
            userClient.delete(accessToken)
                    .assertThat()
                    .statusCode(SC_ACCEPTED)
                    .and()
                    .body("success", is(true))
                    .body("message", is("User successfully removed"));
        }
    }

    @Test
    public void createOrderWithIngredientsAndWithAuthUserOk200() {
        User user = UserGenerator.getRandom();

        accessToken = userClient.create(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", is(true))
                .extract().path("accessToken");

        accessIngredientsPojo = orderClient.getListOfIngredients();
        OrderIngredients orderIngredients = OrderGenerator.getRandom(accessIngredientsPojo);

        orderClient.create(orderIngredients, accessToken)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", is(true));
    }

    @Test
    public void createOrderWithIngredientsAndWithoutAuthUserOk200() {
        accessIngredientsPojo = orderClient.getListOfIngredients();
        OrderIngredients orderIngredients = OrderGenerator.getRandom(accessIngredientsPojo);

        orderClient.create(orderIngredients, "")
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", is(true));
    }

    @Test
    public void createOrderWithoutIngredientsAndWithAuthUserBadRequest400() {
        User user = UserGenerator.getRandom();

        accessToken = userClient.create(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", is(true))
                .extract().path("accessToken");

        OrderIngredients orderIngredients = OrderGenerator.getEmptylist();

        orderClient.create(orderIngredients, accessToken)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat()
                .body("success", is(false))
                .body("message", is("Ingredient ids must be provided"));
    }

    @Test
    public void createOrderWithoutIngredientsAndWithoutAuthUserBadRequest400() {
        OrderIngredients orderIngredients = OrderGenerator.getEmptylist();

        orderClient.create(orderIngredients, "")
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .assertThat()
                .body("success", is(false))
                .body("message", is("Ingredient ids must be provided"));
    }

    @Test
    public void createOrderWithWrongHashIngredientsAndWithAuthUserInternalServerError500() {
        User user = UserGenerator.getRandom();

        accessToken = userClient.create(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", is(true))
                .extract().path("accessToken");

        OrderIngredients orderIngredients = OrderGenerator.getWrongHashList();

        orderClient.create(orderIngredients, accessToken)
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    public void createOrderWithWrongHashIngredientsAndWithoutAuthUserInternalServerError500() {
        OrderIngredients orderIngredients = OrderGenerator.getWrongHashList();

        orderClient.create(orderIngredients, "")
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
