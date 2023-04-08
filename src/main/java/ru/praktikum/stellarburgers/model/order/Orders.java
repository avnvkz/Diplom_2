package ru.praktikum.stellarburgers.model.order;

import java.util.ArrayList;
import java.util.Date;

public class Orders {

    ArrayList<String> ingredients;
    Integer id;
    String status;
    Integer number;
    Date createsAt;
    Date updateAt;

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public Integer getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public Integer getNumber() {
        return number;
    }

    public Date getCreatesAt() {
        return createsAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public void setCreatesAt(Date createsAt) {
        this.createsAt = createsAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "ingredients=" + ingredients +
                ", id=" + id +
                ", status='" + status + '\'' +
                ", number=" + number +
                ", createsAt=" + createsAt +
                ", updateAt=" + updateAt +
                '}';
    }
}
