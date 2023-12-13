package com.restaurant.pizza.resource;

import com.restaurant.pizza.entity.Client;
import com.restaurant.pizza.entity.ConfirmedOrder;
import com.restaurant.pizza.entity.Kebab;
import com.restaurant.pizza.entity.KebabOrder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;
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

        Client existingClient = clients.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);

        if (existingClient != null) {
            return ResponseEntity.ok("Клієнт " + existingClient.getName() + " вже зареєстрований");
        }

        Client client = new Client(name, email);

        if (isValidEmail(client.getEmail())) {
            clients.add(client);
            return ResponseEntity.ok("Клієнт " + client.getName() + " створено");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Невірний формат електронної пошти");
        }
    }

//    private ResponseEntity<String> sendEmail(String to, String subject, String messageText) {
//        Properties properties = new Properties();
//        properties.put("mail.smtp.auth", "true");
//        properties.put("mail.smtp.starttls.enable", "true");
//        properties.put("mail.smtp.host", "sandbox.smtp.mailtrap.io");
//        properties.put("mail.smtp.port", "2525");
//
//        Session session = Session.getInstance(properties, new Authenticator() {
//            @Override
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication("4c2b6babb6f257", "8cfe1f58dccf35");
//            }
//        });
//
//        try {
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress("nazarkuryshchuk8@gmail.com")); // Замініть на вашу електронну адресу
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
//            message.setSubject(subject);
//            message.setText(messageText);
//
//            Transport.send(message);
//
//            System.out.println("Email sent successfully.");
//
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Помилка відправлення електронної пошти: " + e.getMessage());
//        }
//        return null;
//    }
//    public boolean isRegistered(String clientName) {
//        return clients.stream()
//                .anyMatch(client -> client.getName().equalsIgnoreCase(clientName));
//    }


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

        // Перевірка, чи клієнт вже зареєстрований
//        if (!client.isRegistered()) {
//            sendEmail(client.getEmail(), "Реєстрація в системі",
//                    "Ви зареєстровані в системі. Очікуйте на наші смачні кебаби!");
//
//            // Позначте клієнта як зареєстрованого
//            client.setRegistered(true);
//        }

        Kebab orderedKebab = kebabs.stream()
                .filter(kebab -> kebab.getType().equalsIgnoreCase(type))
                .findFirst()
                .orElse(null);

        if (orderedKebab != null) {
            client.addToCart(orderedKebab, additionalIngredients);

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

    @PutMapping("/update/{clientName}/cart/{cartId}")
    public ResponseEntity<String> updateClientOrder(
            @PathVariable String clientName,
            @PathVariable Long cartId,
            @RequestParam String type,
            @RequestParam Double price,
            @RequestParam List<String> additionalIngredients) {

        Client client = clients.stream()
                .filter(c -> c.getName().equalsIgnoreCase(clientName))
                .findFirst()
                .orElse(null);

        if (client != null) {
            List<KebabOrder> orders = client.getOrders();
            Optional<KebabOrder> orderToUpdate = orders.stream()
                    .filter(o -> o.getId().equals(cartId))
                    .findFirst();

            if (orderToUpdate.isPresent()) {
                KebabOrder order = orderToUpdate.get();

                // Перевірка, чи замовлення ще не підтверджено
                if (!order.isConfirmed()) {
                    // Оновлення полів замовлення
                    order.getKebab().setType(type);
                    order.getKebab().setPrice(price);
                    order.setAdditionalIngredients(additionalIngredients);

                    return ResponseEntity.ok("Замовлення клієнта " + clientName + " оновлено: " + order);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Підтверджене замовлення не можна редагувати.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Замовлення з ID " + cartId + " не знайдено.");
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
    public List<Kebab> getClientCart(@PathVariable String clientName) {
        Client client = clients.stream()
                .filter(c -> c.getName().equalsIgnoreCase(clientName))
                .findFirst()
                .orElse(null);

        return client != null ? client.getCartItems() : new ArrayList<>();
    }

    @PostMapping("/client/{clientName}/confirm-order")
    public ResponseEntity<String> confirmOrder(@PathVariable String clientName) {
        Client client = clients.stream()
                .filter(c -> c.getName().equalsIgnoreCase(clientName))
                .findFirst()
                .orElse(null);

        if (client != null) {
            List<Kebab> cartItems = client.getCartItems();
            if (!cartItems.isEmpty()) {
                List<KebabOrder> orders = cartItems.stream()
                        .map(kebab -> new KebabOrder(kebab, new ArrayList<>()))
                        .collect(Collectors.toList());

                client.getOrders().addAll(orders);
                client.clearCart();

                return ResponseEntity.ok("Замовлення клієнта " + clientName + " підтверджено");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Корзина клієнта " + clientName + " порожня");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Клієнта з ім'ям " + clientName + " не знайдено.");
        }
    }

    public void setKebabs(List<Kebab> kebabs) {
        this.kebabs = kebabs;
    }

}
