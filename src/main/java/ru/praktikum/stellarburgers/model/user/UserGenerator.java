package ru.praktikum.stellarburgers.model.user;

import com.github.javafaker.Faker;
import java.util.Locale;

public class UserGenerator {

    public static User getRandom() {
        Faker faker = Faker.instance(new Locale("en-GB"));
        String email = faker.internet().emailAddress();
        String password = faker.internet().password();
        String name = faker.name().username();
        return new User(email, password, name);
    }

    public static String emailGetRandom() {
        Faker faker = Faker.instance(new Locale("en-GB"));
        String email = faker.internet().emailAddress();
        return email;
    }

    public static String nameGetRandom() {
        Faker faker = Faker.instance(new Locale("en-GB"));
        String name = faker.name().username();
        return name;
    }
}
