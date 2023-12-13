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


    public List<KebabOrder> getCartItems() {
        return cart.getItems().stream()
                .map(kebab -> new KebabOrder(kebab, new ArrayList<>()))
                .collect(Collectors.toList());
    }


    public void clearCart() {
        cart.clear();
    }

    public void updateCartItem(Long orderId, String type, Double price, List<String> additionalIngredients) {
        Optional<KebabOrder> orderToUpdate = orders.stream()
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
        return orders;
    }


    }
