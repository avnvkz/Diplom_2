package ru.praktikum.stellarburgers.model.order;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OrderGenerator {


    public static OrderIngredients getRandom(AccessIngredients accessIngredients) {
        Random random = new Random();
        List<String> hashIngredients = new ArrayList<>();
        Integer number = random.nextInt(5) + 1;
        Integer bound = accessIngredients.size();
        for (int i = 0; i < number; i++) {
            int index = random.nextInt(bound);
            String hash = accessIngredients.getData().get(index).getId().toString();
            hashIngredients.add(hash);
        }
        return new OrderIngredients(hashIngredients);
    }

    public static OrderIngredients getEmptylist() {
        List<String> hashIngredients = new ArrayList<>();
        return new OrderIngredients(hashIngredients);
    }

    public static OrderIngredients getWrongHashList() {
        Random random = new Random();
        List<String> hashIngredients = new ArrayList<>();
        Integer number = random.nextInt(3) + 1;
        for (int i = 0; i < number; i++) {
            String hash = RandomStringUtils.randomAlphanumeric(24).toString();
            hashIngredients.add(hash);
        }
        return new OrderIngredients(hashIngredients);
    }
}
