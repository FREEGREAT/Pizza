package com.restaurant.pizza.resource;

import com.restaurant.pizza.entity.Client;
import com.restaurant.pizza.entity.ConfirmedOrder;
import com.restaurant.pizza.entity.Kebab;
import com.restaurant.pizza.entity.KebabOrder;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Client client = new Client(name, email);

        if (isValidEmail(client.getEmail())) {
            clients.add(client);
            return ResponseEntity.ok("Клієнт " + client.getName() + " створено");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Невірний формат електронної пошти");
        }
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

        Kebab orderedKebab = kebabs.stream()
                .filter(kebab -> kebab.getType().equalsIgnoreCase(type))
                .findFirst()
                .orElse(null);

        if (orderedKebab != null) {
            client.addToCart(orderedKebab, additionalIngredients);
           // client.placeOrder(new KebabOrder(orderedKebab, additionalIngredients));

            return ResponseEntity.ok("Кебаб додано до корзини та замовлено для клієнта " + clientName);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Кебаб з типом " + type + " не знайдено в меню.");
        }
    }

    private boolean isValidEmail(String email) {
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
            List<ConfirmedOrder> confirmedOrders = new ArrayList<>();
            for (KebabOrder kebabOrder : client.getOrders()) {
                ConfirmedOrder confirmedOrder = new ConfirmedOrder(kebabOrder.getKebab(), client.getName());
                confirmedOrders.add(confirmedOrder);
            }
            return confirmedOrders;
        } else {
            return Collections.emptyList();
        }
    }

    @PutMapping("/update/{clientName}/orders/{orderId}")
    public ResponseEntity<String> updateClientOrder(
            @PathVariable String clientName,
            @PathVariable Long orderId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) List<String> additionalIngredients) {

        Client client = clients.stream()
                .filter(c -> c.getName().equalsIgnoreCase(clientName))
                .findFirst()
                .orElse(null);

        if (client != null) {
            List<KebabOrder> orders = client.getOrders();
            Optional<KebabOrder> orderToUpdate = orders.stream()
                    .filter(o -> o.getId().equals(orderId))
                    .findFirst();

            if (orderToUpdate.isPresent()) {
                KebabOrder order = orderToUpdate.get();

                // Перевірка, чи замовлення ще не підтверджено
                if (!order.isConfirmed()) {
                    // Оновлення полів замовлення
                    if (type != null) {
                        order.getKebab().setType(type);
                    }
                    if (price != null) {
                        order.getKebab().setPrice(price);
                    }
                    if (additionalIngredients != null) {
                        order.setAdditionalIngredients(additionalIngredients);
                    }

                    return ResponseEntity.ok("Замовлення клієнта " + clientName + " оновлено: " + order);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Підтверджене замовлення не можна редагувати.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Замовлення з ID " + orderId + " не знайдено.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Клієнта з ім'ям " + clientName + " не знайдено.");
        }
    }


    @GetMapping("/previous-orders")
    public List<Client> getAllClients() {
        return clients;
    }


    @DeleteMapping("/delete/{clientName}/orders/{orderId}")
    public ResponseEntity<String> deleteKebab(
            @PathVariable String clientName,
            @PathVariable Long orderId) {
        // Find the client by name
        Client client = clients.stream()
                .filter(c -> c.getName().equalsIgnoreCase(clientName))
                .findFirst()
                .orElse(null);

        if (client != null) {
            List<KebabOrder> orders = client.getOrders();
            Optional<KebabOrder> orderToDelete = orders.stream()
                    .filter(o -> o.getId().equals(orderId))
                    .findFirst();

            if (orderToDelete.isPresent()) {
                orders.remove(orderToDelete.get());

                orders.forEach(order -> {
                    if (order.getId() > orderToDelete.get().getId()) {
                        order.decrementId();
                    }
                });

                return ResponseEntity.ok("Замовлення клієнта " + clientName + " з ID " + orderId + " видалено.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Замовлення з ID " + orderId + " не знайдено.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Клієнта з ім'ям " + clientName + " не знайдено.");
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
