package ru.praktikum.stellarburgers.model.order;

public class Order {

    Integer number;

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Order{" +
                "number=" + number +
                '}';
    }
}
