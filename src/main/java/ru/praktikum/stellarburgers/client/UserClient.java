package ru.praktikum.stellarburgers.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.praktikum.stellarburgers.client.base.StellarBurgerRestClient;
import ru.praktikum.stellarburgers.model.user.User;
import ru.praktikum.stellarburgers.model.user.UserCredentials;

import static io.restassured.RestAssured.given;


public class UserClient extends StellarBurgerRestClient{

    private static final String USER_URI = BASE_URI + "/auth";

    @Step("Create user {user}")
    public ValidatableResponse create(User user) {
        return given()
                .spec(getBaseReqSpec())
                .body(user)
                .when()
                .post(USER_URI + "/register")
                .then();
    }

    @Step("Login user {userCredentials}")
    public ValidatableResponse login(UserCredentials userCredentials) {
        return given()
                .spec(getBaseReqSpec())
                .body(userCredentials)
                .when()
                .post(USER_URI + "/login")
                .then();
    }

    @Step("Edit user email")
    public ValidatableResponse editUser(String accessToken, User user) {
        return given()
                .spec(getBaseReqSpec())
                .header("authorization", accessToken)
                .body(user)
                .when()
                .patch(USER_URI + "/user")
                .then();
    }

    @Step("Delete user")
    public ValidatableResponse delete(String accessToken) {
        return given()
                .spec(getBaseReqSpec())
                .header("authorization", accessToken)
                .when()
                .delete(USER_URI + "/user")
                .then();
    }


}
