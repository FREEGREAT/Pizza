package com.restaurant.pizza.entity;

import lombok.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Data
@Getter
@Setter
@AllArgsConstructor
public class KebabOrder {
    private static final AtomicLong idCounter = new AtomicLong(1);

    private Kebab kebab;
    private Long id;
    private boolean confirmed;
    private List<String> additionalIngredients;
    public KebabOrder(Kebab kebab, List<String> additionalIngredients) {
        this.id = generateUniqueId();
        this.kebab = kebab;
        this.additionalIngredients = additionalIngredients;
        this.confirmed = false;
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

    private Long generateUniqueId() {
        return idCounter.getAndIncrement();
    }



    public void confirmOrder() {
        this.confirmed = true;
    }
}
