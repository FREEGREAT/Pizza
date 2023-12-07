package com.restaurant.pizza.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Kebab {
    private String type;
    private double price;
    private List<String> additionalIngredients;
}
