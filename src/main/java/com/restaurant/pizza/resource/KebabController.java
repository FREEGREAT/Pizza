package com.restaurant.pizza.resource;

import com.restaurant.pizza.entity.*;
import jakarta.validation.Valid;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
@Setter
@RestController
@RequestMapping("/kebab")
public class KebabController {

    private List<Kebab> kebabs = new ArrayList<>();
    private List<Client> clients = new ArrayList<>();

    @GetMapping("/menu")
    public List<Kebab> getMenu() {
        return kebabs;
    }

    @PostMapping("/client")
    public ResponseEntity<String> createClient(
            @RequestParam String name,
            @RequestParam String email) {
        if (!isValidEmail(email)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Невірний формат електронної пошти");
        }

        clients.add(new Client(name, email));
        return ResponseEntity.ok("Клієнт " + name + " створено");
    }

    @PostMapping("/client/{clientName}/order")
    public ResponseEntity<String> orderKebabForClient(
            @PathVariable String clientName,
            @RequestParam String type,
            @RequestParam List<String> additionalIngredients) {

        Client client = clients.stream()
                .filter(c -> c.getName().equalsIgnoreCase(clientName))
                .findFirst()
                .orElseGet(() -> {
                    Client newClient = new Client(clientName, "");
                    clients.add(newClient);
                    return newClient;
                });

        Optional<Kebab> orderedKebab = kebabs.stream()
                .filter(kebab -> kebab.getType().equalsIgnoreCase(type))
                .findFirst();

        if (orderedKebab.isPresent()) {
            KebabOrder kebabOrder = new KebabOrder(orderedKebab.get(), additionalIngredients);
            client.addToCart(kebabOrder);
            return ResponseEntity.ok("Кебаб додано до корзини " + clientName);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Кебаб з типом " + type + " не знайдено в меню.");
        }
    }


    boolean isValidEmail(String email) {
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(regex);
    }

    @GetMapping("confirmed-orders/{clientName}")
    public List<ConfirmedOrder> getConfirmedOrders(@PathVariable String clientName) {
        Client client = clients.stream()
                .filter(c -> c.getName().equalsIgnoreCase(clientName))
                .findFirst()
                .orElse(null);

        if (client != null) {
            return client.getOrders().stream()
                    .map(kebabOrder -> new ConfirmedOrder(kebabOrder.getKebab(), clientName))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @PutMapping("/client/{clientName}/cart/{cartItemId}")
    public ResponseEntity<String> updateCartItem(
            @PathVariable String clientName,
            @PathVariable Long cartItemId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) List<String> additionalIngredients) {

        Client client = clients.stream()
                .filter(c -> c.getName().equalsIgnoreCase(clientName))
                .findFirst()
                .orElse(null);

        if (client != null) {
            Optional<KebabOrder> cartItem = client.getCartItems().stream()
                    .filter(kebabOrder -> kebabOrder.getId().equals(cartItemId))
                    .findFirst();

            if (cartItem.isPresent()) {
                // Оновлюємо дані замовлення
                KebabOrder updatedKebabOrder = cartItem.get();
                if (type != null) {
                    updatedKebabOrder.getKebab().setType(type);
                }

                // Перевірка, чи передано значення ціни
                if (price != null) {
                    updatedKebabOrder.getKebab().setPrice(price);
                }

                updatedKebabOrder.setAdditionalIngredients(additionalIngredients);

                return ResponseEntity.ok("Замовлення в корзині клієнта " + clientName + " оновлено: " + updatedKebabOrder);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Замовлення в корзині з ID " + cartItemId + " не знайдено.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Клієнта з ім'ям " + clientName + " не знайдено.");
        }
    }

    @DeleteMapping("/client/{clientName}/cart/clear")
    public ResponseEntity<String> clearCart(@PathVariable String clientName) {
        Client client = clients.stream()
                  .filter(c -> c.getName().equalsIgnoreCase(clientName))
                  .findFirst()
                  .orElse(null);
        client.clearCart();
        return ResponseEntity.ok("Замовлення в корзині клієнта " + clientName + " очищена ");
    }

    @GetMapping("/previous-orders")
    public List<Client> getAllClients() {
        return clients;
    }
    @PostMapping("/client/{clientName}/confirm-order")
    public ResponseEntity<String> confirmOrder(@PathVariable String clientName) {
        Client client = clients.stream()
                .filter(c -> c.getName().equalsIgnoreCase(clientName))
                .findFirst()
                .orElse(null);

        if (client != null) {
            client.confirmOrder();

            // Встановлення confirmed в true для всіх підтверджених замовлень
            client.getConfirmedOrders().forEach(order -> order.setConfirmed(true));

            return ResponseEntity.ok("Замовлення клієнта " + clientName + " підтверджено.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Клієнта з ім'ям " + clientName + " не знайдено.");
        }
    }


    @DeleteMapping("/delete/{clientName}/cart/{cartId}")
    public ResponseEntity<String> deleteKebab(
            @PathVariable String clientName,
            @PathVariable Long cartId) {

        Client client = clients.stream()
                .filter(c -> c.getName().equalsIgnoreCase(clientName))
                .findFirst()
                .orElse(null);

        if (client != null) {
            List<KebabOrder> cartItems = client.getCartItems();
            Iterator<KebabOrder> iterator = cartItems.iterator();

            client.removeItemById(cartId);

            while (iterator.hasNext()) {
                KebabOrder order = iterator.next();

                if (order.getId().equals(cartId)) {
                    iterator.remove();
                    reassignOrderIds(cartItems);
                    return ResponseEntity.ok("Замовлення клієнта " + clientName + " з ID " + cartId + " видалено з корзини.");
                }
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Замовлення з ID " + cartId + " не знайдено в корзині.");
        } else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Клієнта з ім'ям " + clientName + " не знайдено.");
        }
    }

    private void reassignOrderIds(List<KebabOrder> orders) {
        for (int i = 0; i < orders.size(); i++) {
            orders.get(i).setId(Long.valueOf(i + 1));
        }
    }

    @GetMapping("/client/{clientName}/cart")
    public List<KebabOrder> getClientCart(@PathVariable String clientName) {
        Client client = clients.stream()
                .filter(c -> c.getName().equalsIgnoreCase(clientName))
                .findFirst()
                .orElse(null);

        return client != null ? client.getCartItems() : new ArrayList<>();
    }

    public void setKebabs(List<Kebab> kebabs) {
        this.kebabs = kebabs;
    }

}
