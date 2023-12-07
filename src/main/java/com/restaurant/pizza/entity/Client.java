package com.restaurant.pizza.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Client {
    private String name;
    private List<KebabOrder> orders;

    public Client(String name) {
        this.name = name;
        this.orders = new ArrayList<>();
    }


    public List<KebabOrder> getOrders() {
        return orders;
    }

    public void placeOrder(KebabOrder order) {
        orders.add(order);
    }
}