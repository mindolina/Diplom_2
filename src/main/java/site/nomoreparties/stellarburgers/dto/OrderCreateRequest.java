package site.nomoreparties.stellarburgers.dto;

import java.util.List;

public class OrderCreateRequest {
    public List<String> ingredients;

    public OrderCreateRequest(List<String> ingredients) {
        this.ingredients = ingredients;
    }
    public OrderCreateRequest() {
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
