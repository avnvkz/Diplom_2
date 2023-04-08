package ru.praktikum.stellarburgers.user;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.praktikum.stellarburgers.client.UserClient;
import ru.praktikum.stellarburgers.model.user.User;
import ru.praktikum.stellarburgers.model.user.UserCredentials;
import ru.praktikum.stellarburgers.model.user.UserGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginUserTests {

    private UserClient userClient;
    private String accessToken;

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
    public void userCanLoginWithValidDateOk200() {
        User user = UserGenerator.getRandom();

        accessToken = userClient.create(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", is(true))
                .extract().path("accessToken");

        userClient.login(UserCredentials.from(user))
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", is(true))
                .body("accessToken", is(notNullValue()))
                .body("refreshToken", is(notNullValue()));
    }

    @Test
    public void userCannotLoginWithoutEmailError401() {
        User user = UserGenerator.getRandom();

        accessToken = userClient.create(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", is(true))
                .extract().path("accessToken");

        user.setEmail("");

        userClient.login(UserCredentials.from(user))
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat()
                .body("success", is(false))
                .body("message",is("email or password are incorrect"));
    }

    @Test
    public void userCannotLoginWithoutPasswordError401() {
        User user = UserGenerator.getRandom();

        accessToken = userClient.create(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", is(true))
                .extract().path("accessToken");

        user.setPassword("");

        userClient.login(UserCredentials.from(user))
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat()
                .body("success", is(false))
                .body("message",is("email or password are incorrect"));
    }

    @Test
    public void userCannotLoginWithInvalidDataError401() {
        User user = UserGenerator.getRandom();

        userClient.login(UserCredentials.from(user))
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat()
                .body("success", is(false))
                .body("message",is("email or password are incorrect"));
    }
}
