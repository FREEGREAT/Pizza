package com.restaurant.pizza.entity;

import lombok.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Data
@Getter
@Setter
@AllArgsConstructor
public class KebabOrder {
    private Kebab kebab;
    private static final AtomicLong counter = new AtomicLong();
    private Long id;
    private List<String> additionalIngredients;
    public KebabOrder(Kebab kebab, List<String> additionalIngredients) {
        this.id = counter.incrementAndGet();
        this.kebab = kebab;
        this.additionalIngredients = additionalIngredients;
    }

    public void decrementId() {
        if (this.id > 0) {
            this.id--;
        }
    }
    @Override
    public String toString() {
        return "Замовлення: " + kebab.getType() +
                ", Ціна: " + kebab.getPrice() +
                ", Додаткові інгредієнти: " + additionalIngredients;
    }
}
