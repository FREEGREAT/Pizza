package com.restaurant.pizza.entity;
import lombok.*;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Kebab {
    private String type;
    private Double price;
    private List<String> additionalIngredients;

    public void decrementId() {
    }

    public void setName(String string) {
    }

    public void setId(long l) {
    }

    public void setAdditionalIngredients(String string) {
    }

    public void setIngredients(String string) {
    }

    public Object getId() {
        return null;
    }

    public Object getName() {
        return null;
    }

    public Object getIngredients() {
        return null;
    }

}
