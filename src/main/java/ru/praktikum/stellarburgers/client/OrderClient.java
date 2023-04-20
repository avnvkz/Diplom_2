package ru.praktikum.stellarburgers.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.praktikum.stellarburgers.client.base.StellarBurgerRestClient;
import ru.praktikum.stellarburgers.model.order.AccessIngredients;
import ru.praktikum.stellarburgers.model.order.OrderIngredients;
import static io.restassured.RestAssured.given;

public class OrderClient extends StellarBurgerRestClient {

    private static final String ORDER_URI = BASE_URI + "/orders";

    @Step("Get a list of ingredients as POJO")
    public AccessIngredients getListOfIngredients() {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(BASE_URI + "/ingredients")
                .body().as(AccessIngredients.class);
    }

    @Step("Create order with ingredients {orderIngredients}")
    public ValidatableResponse create(OrderIngredients orderIngredients, String accessToken) {
        return given()
                .spec(getBaseReqSpec())
                .header("authorization", accessToken)
                .body(orderIngredients)
                .when()
                .post(ORDER_URI)
                .then();
    }

    @Step("Get orders For User")
    public ValidatableResponse getUserOrders(String accessToken) {
        return given()
                .spec(getBaseReqSpec())
                .header("authorization", accessToken)
                .when()
                .get(ORDER_URI)
                .then();
    }

    @Step("Get all orders")
    public ValidatableResponse getAllOrders() {
        return given()
                .spec(getBaseReqSpec())
                .when()
                .get(ORDER_URI + "/all")
                .then();
    }

}
