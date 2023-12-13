package com.restaurant.pizza.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    @NotBlank(message = "Ім'я клієнта не може бути порожнім")
    private String name;

    @Email(message = "Невірний формат електронної пошти")
    @NotBlank(message = "Електронна пошта клієнта не може бути порожньою")
    private String email;

    private List<KebabOrder> orders;
    private Cart cart;


    public Client(String name, String email) {
        this.name = name;
        this.email = email;
        this.orders = new ArrayList<>();
        this.cart = new Cart();

    }

    public void addToCart(Kebab kebab, List<String> additionalIngredients) {
        if (cart == null) {
            cart = new Cart();
        }
        kebab.setAdditionalIngredients(additionalIngredients);
        cart.addItem(kebab);
    }

    public List<Kebab> getCartItems() {
        return cart.getItems();
    }

    public void clearCart() {
        cart.clear();
    }

    public List<KebabOrder> getOrders() {
        return orders;
    }

    public void placeOrder(KebabOrder order) {
        orders.add(order);
    }
}