package com.restaurant.pizza.resource;

import com.restaurant.pizza.entity.Kebab;
import com.restaurant.pizza.entity.KebabOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/kebab")
public class KebabController {

    private List<Kebab> kebabs = new ArrayList<>();
    private List<KebabOrder> orders = new ArrayList<>();


    @GetMapping("/menu")
    public List<Kebab> getMenu() {
        // Повертаємо список доступних кебабів
        return kebabs;
    }

    @PostMapping("/order")
    public String orderKebab(@RequestParam String type, @RequestParam List<String> additionalIngredients) {
        // Знаходимо кебаб за типом у меню
        Kebab orderedKebab = kebabs.stream()
                .filter(kebab -> kebab.getType().equalsIgnoreCase(type))
                .findFirst()
                .orElse(null);

        if (orderedKebab != null) {
            // Створюємо нове замовлення
            KebabOrder kebabOrder = new KebabOrder(orderedKebab, additionalIngredients);

            // Додаємо замовлення до списку
            orders.add(kebabOrder);

            // Повертаємо підтвердження замовлення
            return "Замовлення прийнято: " + kebabOrder.toString();
        } else {
            return "Кебаб з типом " + type + " не знайдено в меню.";
        }
    }
    @GetMapping("/previous-orders")
    public List<KebabOrder> getPreviousOrders() {
        return orders;
    }
    @GetMapping("/previous-orders/{orderId}")
    public ResponseEntity<KebabOrder> getPreviousOrderById(@PathVariable Long orderId) {
        Optional<KebabOrder> order = orders.stream()
                .filter(o -> o.getId().equals(orderId))
                .findFirst();

        return order.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    public void setKebabs(List<Kebab> kebabs) {
        this.kebabs = kebabs;
    }

}
