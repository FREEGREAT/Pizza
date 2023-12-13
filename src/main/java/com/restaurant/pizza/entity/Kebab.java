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

}
