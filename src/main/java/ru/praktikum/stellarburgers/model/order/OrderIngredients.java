package ru.praktikum.stellarburgers.model.order;

import java.util.List;

public class OrderIngredients {

    private List<String> ingredients;

    public OrderIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

}