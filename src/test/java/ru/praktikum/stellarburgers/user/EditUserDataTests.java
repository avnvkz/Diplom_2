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

public class EditUserDataTests {

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
    public void editUserEmailWithAuthorizationToNewEmailOk200() {
        User user = UserGenerator.getRandom();

        accessToken = userClient.create(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", is(true))
                .extract().path("accessToken");

        String email = UserGenerator.emailGetRandom();

        user.setEmail(email);

        userClient.editUser(accessToken, user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", is(true));
    }

    @Test
    public void editUserNameWithAuthorizationToNewNameOk200() {
        User user = UserGenerator.getRandom();

        accessToken = userClient.create(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", is(true))
                .extract().path("accessToken");

        String name = UserGenerator.nameGetRandom();

        user.setName(name);

        userClient.editUser(accessToken, user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", is(true));
    }

    @Test
    public void editUserEmailWithAuthorizationToDuplicatedEmailError403() {
        User user1 = UserGenerator.getRandom();
        User user2 = UserGenerator.getRandom();

        accessToken = userClient.create(user1)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", is(true))
                .extract().path("accessToken");

        String accessToken2 = userClient.create(user2)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", is(true))
                .extract().path("accessToken");

        userClient.editUser(accessToken, user2)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .assertThat()
                .body("success", is(false))
                .body("message", is("User with such email already exists"));;

        userClient.delete(accessToken2);
    }


    @Test
    public void editUserEmailWithoutAuthorizationToNewEmailError401() {
        User user = UserGenerator.getRandom();

        accessToken = userClient.create(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", is(true))
                .extract().path("accessToken");

        String email = UserGenerator.emailGetRandom();

        user.setEmail(email);

        userClient.editUser("", user)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat()
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @Test
    public void editUserNameWithoutAuthorizationToNewNameError401() {
        User user = UserGenerator.getRandom();

        accessToken = userClient.create(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .assertThat()
                .body("success", is(true))
                .extract().path("accessToken");

        String name = UserGenerator.nameGetRandom();

        user.setName(name);

        userClient.editUser("", user)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .assertThat()
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }
}
