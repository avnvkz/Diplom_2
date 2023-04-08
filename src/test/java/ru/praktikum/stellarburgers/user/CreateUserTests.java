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
import ru.praktikum.stellarburgers.model.user.UserGenerator;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;

public class CreateUserTests {

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
    public void userCanBeCreatedWithValidDataOk200() {
        User user = UserGenerator.getRandom();

        accessToken = userClient.create(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", is(true))
                .extract().path("accessToken");
    }

    @Test
    public void userCannotBeCreatedWithDuplicatedDataError403() {
        User user = UserGenerator.getRandom();

        accessToken = userClient.create(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", is(true))
                .extract().path("accessToken");

        userClient.create(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat()
                .body("success", is(false))
                .body("message", is("User already exists"));
    }

    @Test
    public void userCannotBeCreatedWithoutEmailError403() {
        User user = UserGenerator.getRandom();
        user.setEmail("");

        userClient.create(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat()
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }

    @Test
    public void userCannotBeCreatedWithoutPasswordError403() {
        User user = UserGenerator.getRandom();
        user.setPassword("");

        userClient.create(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat()
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }

    @Test
    public void userCannotBeCreatedWithoutNamedError403() {
        User user = UserGenerator.getRandom();
        user.setName("");

        userClient.create(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat()
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }
}