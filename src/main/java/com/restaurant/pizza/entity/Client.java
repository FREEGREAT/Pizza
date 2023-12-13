package com.restaurant.pizza.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    private List<KebabOrder> cartItems = new ArrayList<>();
    private List<KebabOrder> confirmedOrders = new ArrayList<>();

    public Client(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public void addToCart(KebabOrder kebabOrder) {
        cartItems.add(kebabOrder);
    }


    public List<KebabOrder> getCartItems() {
        return new ArrayList<>(cartItems);
    }

    public void clearCart() {
        cartItems.clear();
    }

    public void removeItemById(Long kebabId) {
        cartItems.removeIf(kebab -> kebab.getId().equals(kebabId));
    }

    public void confirmOrder() {
        for (KebabOrder order : cartItems) {
            order.setConfirmed(true);
        }
        confirmedOrders.addAll(cartItems);
        cartItems.clear();
    }

    public List<KebabOrder> getConfirmedOrders() {
        return new ArrayList<>(confirmedOrders);
    }

    public void updateCartItem(Long orderId, String type, Double price, List<String> additionalIngredients) {
        Optional<KebabOrder> orderToUpdate = cartItems.stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst();

        if (orderToUpdate.isPresent()) {
            KebabOrder order = orderToUpdate.get();
            order.getKebab().setType(type);
            order.getKebab().setPrice(price);
            order.setAdditionalIngredients(additionalIngredients);
        }
    }

    public List<KebabOrder> getOrders() {
        return new ArrayList<>(cartItems);
    }

    public Cart getCart() {
        return new Cart();
    }
}