package ru.praktikum.stellarburgers.model.order;


import java.util.List;

public class AccessIngredients {
    private List<Ingredient> data;

    public List<Ingredient> getData() {
        return data;
    }

    public void setData(List<Ingredient> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Ingredients{" +
                "data=" + data +
                '}';
    }

    public int size() {
        return data.size();
    }
}
