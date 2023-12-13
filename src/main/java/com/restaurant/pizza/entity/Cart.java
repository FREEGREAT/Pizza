package com.restaurant.pizza.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Getter
@Setter
public class Cart {

    private static Long cartIdCounter = 1L;
    private Long cartId;
    private List<Kebab> items;

    public Cart() {
        this.cartId = cartIdCounter++;
        this.items = new ArrayList<>();
    }

    public void addItem(Kebab kebab) {
        items.add(kebab);
    }

    public void removeItem(Kebab kebab) {
        items.remove(kebab);
    }

    public List<Kebab> getItems() {
        return items;
    }

    public void clear() {
        items.clear();
    }


}

