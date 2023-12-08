package com.restaurant.pizza.resource;

import com.restaurant.pizza.entity.Client;
import com.restaurant.pizza.entity.Kebab;
import com.restaurant.pizza.entity.KebabOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/kebab")
public class KebabController {

    private List<Kebab> kebabs = new ArrayList<>();
    private List<KebabOrder> orders = new ArrayList<>();
    private List<Client> clients = new ArrayList<>();


    @GetMapping("/menu")
    public List<Kebab> getMenu() {
        // Повертаємо список доступних кебабів
        return kebabs;
    }

    @PostMapping("/order/{clientName}")
    public String orderKebabForClient(
            @PathVariable String clientName,
            @RequestParam String type,
            @RequestParam List<String> additionalIngredients) {
        Kebab orderedKebab = kebabs.stream()
                .filter(kebab -> kebab.getType().equalsIgnoreCase(type))
                .findFirst()
                .orElse(null);

        if (orderedKebab != null) {
            Client client = clients.stream()
                    .filter(c -> c.getName().equalsIgnoreCase(clientName))
                    .findFirst()
                    .orElseGet(() -> {
                        Client newClient = new Client(clientName);
                        clients.add(newClient);
                        return newClient;
                    });

            KebabOrder order = new KebabOrder(orderedKebab, additionalIngredients);
            client.placeOrder(order);

            return "Замовлення для клієнта " + clientName + " прийнято: " + orderedKebab.getType()
                    + " з додатковими інгредієнтами " + additionalIngredients;
        } else {
            return "Кебаб з типом " + type + " не знайдено в меню.";
        }
    }

    @GetMapping("orders/{clientName}")
    public List<KebabOrder> getClientOrders(@PathVariable String clientName) {
        Client client = clients.stream()
                .filter(c -> c.getName().equalsIgnoreCase(clientName))
                .findFirst()
                .orElse(null);

        return client != null ? client.getOrders() : Collections.emptyList();
    }


    @PutMapping("/update/{clientName}/orders/{orderId}")
    public ResponseEntity<String> updateClientOrder(
            @PathVariable String clientName,
            @PathVariable Long orderId,
            @RequestParam String type,
            @RequestParam Double price,
            @RequestParam List<String> additionalIngredients) {
        KebabOrder updatedOrder = new KebabOrder(new Kebab(type, price,null), additionalIngredients);

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
                orders.remove(orderToUpdate.get());
                orders.add(updatedOrder);
                return ResponseEntity.ok("Замовлення клієнта " + clientName + " оновлено: " + updatedOrder);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Замовлення з ID " + orderId + " не знайдено.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Клієнта з ім'ям " + clientName + " не знайдено.");
        }
    }


    @GetMapping("/previous-orders")
    public List<Client> getAllClients() {
        List<Client> clientInfoList = new ArrayList<>();

        for (Client client : clients) {
            List<KebabOrder> orders = client.getOrders();
            Client clientInfo = new Client(client.getName(), orders);
            clientInfoList.add(clientInfo);
        }

        return clientInfoList;
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
                // Remove the kebab order from the list
                orders.remove(orderToDelete.get());

                // Decrement the IDs of orders with IDs greater than the deleted order's ID
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
    public void setKebabs(List<Kebab> kebabs) {
        this.kebabs = kebabs;
    }

}
