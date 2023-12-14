package com.restaurant.pizza.resource;

import com.restaurant.pizza.entity.Client;
import com.restaurant.pizza.entity.Kebab;
import com.restaurant.pizza.entity.KebabOrder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class KebabControllerTest {
    @Autowired
    private KebabController kebabController;
    @Test
    public void getMenuPassTest() {
        KebabController controller = new KebabController();
        List<Kebab> expectedKebabs = new ArrayList<>();
        expectedKebabs.add(new Kebab("Kebab 1", 10.0, Arrays.asList("Tomato", "Onion")));
        expectedKebabs.add(new Kebab("Kebab 2", 12.0, Arrays.asList("Lettuce", "Pickles")));
        controller.setKebabs(expectedKebabs);
        List<Kebab> actualKebabs = controller.getMenu();

        assertEquals(expectedKebabs.size(), actualKebabs.size());
        for (int i = 0; i < expectedKebabs.size(); i++) {
            Kebab expectedKebab = expectedKebabs.get(i);
            Kebab actualKebab = actualKebabs.get(i);
            assertEquals(expectedKebab.getType(), actualKebab.getType());
            assertEquals(expectedKebab.getPrice(), actualKebab.getPrice());
            assertArrayEquals(expectedKebab.getAdditionalIngredients().toArray(), actualKebab.getAdditionalIngredients().toArray());
        }
    }
    @Test
    public void createClientPassTest() {
        KebabController controller = new KebabController();
        String name = "John Doe";
        String email = "johndoe@example.com";
        ResponseEntity<String> response = controller.createClient(name, email);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Клієнт " + name + " створено", response.getBody());
    }

    @Test
    public void createClientErrBadRequest() {
        KebabController controller = new KebabController();
        String name = "John Doe";
        String email = "invalidEmail";
        ResponseEntity<String> response = controller.createClient(name, email);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Невірний формат електронної пошти", response.getBody());
    }
    @Test
    public void orderKebabForClientPassTest() {
        KebabController controller = new KebabController();
        String clientName = "John Doe";
        String type = "Шаурма";
        List<String> additionalIngredients = Arrays.asList("Extra cheese");
        Client client = new Client(clientName, "johndoe@example.com");
        controller.setClients(Arrays.asList(client));
        Kebab kebab = new Kebab(type, 10.0, Arrays.asList("Tomato", "Onion"));
        controller.setKebabs(Arrays.asList(kebab));
        ResponseEntity<String> response = controller.orderKebabForClient(clientName, type, additionalIngredients);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Кебаб додано до корзини " + clientName, response.getBody());
        assertFalse(client.getCartItems().contains(new KebabOrder(kebab, additionalIngredients)));
    }
    @Test
    public void orderKebabForClientErrNotFound() {
        KebabController controller = new KebabController();
        String clientName = "John Doe";
        String type = "Nonexistent Kebab";
        Client client = new Client(clientName, "johndoe@example.com");
        controller.setClients(Arrays.asList(client));
        ResponseEntity<String> response = controller.orderKebabForClient(clientName, type, new ArrayList<>());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Кебаб з типом " + type + " не знайдено в меню.", response.getBody());
    }
    @Test
    public void updateCartItemPassTest() {
        String clientName = "John Doe";
        Long cartItemId = 1L;
        Client client = new Client(clientName, "johndoe@example.com");
        kebabController.setClients(Arrays.asList(client));
        KebabOrder kebabOrder = new KebabOrder(new Kebab("Шаурма", 10.0, Arrays.asList("Tomato", "Onion")), Arrays.asList("Extra cheese"));
        client.addToCart(kebabOrder);

        ResponseEntity<String> response = kebabController.updateCartItem(clientName, cartItemId, "Кебаб класичний", 12.0, Arrays.asList("Lettuce", "Pickles"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Замовлення в корзині клієнта John Doe оновлено: " + kebabOrder, response.getBody());
        Optional<KebabOrder> updatedCartItem = client.getCartItems().stream()
                .filter(kebabOrder1 -> kebabOrder1.getId().equals(cartItemId))
                .findFirst();

        assertTrue(updatedCartItem.isPresent());
        KebabOrder updatedKebabOrder = updatedCartItem.get();
        assertEquals(kebabOrder.getId(), updatedKebabOrder.getId());
        assertEquals("Кебаб класичний", updatedKebabOrder.getKebab().getType());
        assertEquals(12.0, updatedKebabOrder.getKebab().getPrice());
        assertEquals(Arrays.asList("Lettuce", "Pickles"), updatedKebabOrder.getAdditionalIngredients());
    }
    @Test
    public void updateCartItemID404() {
        String clientName = "John Doe";
        Long cartItemId = 100L;

        ResponseEntity<String> response = kebabController.updateCartItem(clientName, cartItemId, null, null, Arrays.asList("Lettuce", "Pickles"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Замовлення в корзині з ID 100 не знайдено.", response.getBody());
    }
    @Test
    public void updateCartItemUser404() {
        String clientName = "Non-existing client";
        Long cartItemId = 1L;
        ResponseEntity<String> response = kebabController.updateCartItem(clientName, cartItemId, null, null, Arrays.asList("Lettuce", "Pickles"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Клієнта з ім'ям Non-existing client не знайдено.", response.getBody());
    }
    @Test
    public void clearCartPassTest() {
        String clientName = "John Doe";
        Client client = new Client(clientName, "johndoe@example.com");

        KebabOrder kebabOrder = new KebabOrder(new Kebab("Шаурма", 10.0, Arrays.asList("Tomato", "Onion")), Arrays.asList("Extra cheese"));
        client.addToCart(kebabOrder);
        kebabController.setClients(Arrays.asList(client));
        ResponseEntity<String> response = kebabController.clearCart(clientName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Замовлення в корзині клієнта John Doe очищена ", response.getBody());
        assertTrue(client.getCartItems().isEmpty());
    }
    @Test
    public void clearCartClient404() {
        String clientName = "Non-existing client";
        Client client = new Client("John", "client@gmail.com:)");
        client.addToCart(new KebabOrder(new Kebab("Шаурма", 10.0, Arrays.asList("Tomato", "Onion")), Arrays.asList("Extra cheese")));
        kebabController.setClients(Arrays.asList(client));
        ResponseEntity<String> response = kebabController.clearCart(clientName);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Клієнта з ім'ям Non-existing client не знайдено.", response.getBody());
    }
    @Test
    public void getAllClients_shouldReturnAllClients() {
        Client client1 = new Client("John Doe", "johndoe@example.com");
        Client client2 = new Client("Jane Doe", "janedoe@example.com");
        kebabController.setClients(Arrays.asList(client1, client2));
        List<Client> actualClients = kebabController.getAllClients();

        assertEquals(kebabController.getAllClients(), actualClients);
    }
    @Test
    public void getAllClientsEmpty() {
        kebabController.setClients(new ArrayList<>());
        List<Client> actualClients = kebabController.getAllClients();

        assertTrue(actualClients.isEmpty());
    }
    @Test
    public void confirmOrderTest() {

        String clientName = "John Doe";
        Client client = new Client(clientName, "johndoe@example.com");
        KebabOrder kebabOrder = new KebabOrder(new Kebab("Шаурма", 10.0, Arrays.asList("Tomato", "Onion")), Arrays.asList("Extra cheese"));
        kebabController.setClients(Arrays.asList(client));
        client.addToCart(kebabOrder);
        ResponseEntity<String> response = kebabController.confirmOrder(clientName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Замовлення клієнта John Doe підтверджено.", response.getBody());
        assertTrue(client.getConfirmedOrders().contains(kebabOrder));
    }
    @Test
    public void confirmOrder400() {
        String clientName = "John Doe";
        Client client = new Client(clientName, "johndoe@example.com");
        kebabController.setClients(Arrays.asList(client));
        ResponseEntity<String> response = kebabController.confirmOrder(client.getName());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Клієнт " + clientName + " не має підтверджених замовлень.", response.getBody());
    }
    @Test
    public void confirmOrderClient404() {
        String clientName = "Non-existing client";
        ResponseEntity<String> response = kebabController.confirmOrder(clientName);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Клієнта з ім'ям Non-existing client не знайдено.", response.getBody());
    }
    @Test
    public void deleteKebabPassTest() {

        String clientName = "John Doe";
        Long cartItemId = 1L;
        Client client = new Client(clientName, "johndoe@example.com");
        KebabOrder kebabOrder = new KebabOrder(new Kebab("Шаурма", 10.0, Arrays.asList("Tomato", "Onion")), Arrays.asList("Extra cheese"));
        kebabController.setClients(Arrays.asList(client));
        kebabOrder.setId(cartItemId);
        client.addToCart(kebabOrder);
        ResponseEntity<String> response = kebabController.deleteKebab(clientName, cartItemId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Замовлення клієнта John Doe з ID 1 видалено з корзини.", response.getBody());
        assertFalse(client.getCartItems().contains(kebabOrder));
    }
    @Test
    public void deleteKebabCartItem404() {
        String clientName = "John Doe";
        Long cartItemId = 1L;
        Client client = new Client(clientName, "johndoe@example.com");
        kebabController.setClients(Arrays.asList(client));
        ResponseEntity<String> response = kebabController.deleteKebab(clientName, cartItemId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Замовлення з ID " + cartItemId + " не знайдено в корзині.", response.getBody());
    }
    @Test
    public void deleteKebabClient404() {
        String clientName = "Non-existing client";
        Long cartItemId = 1L;
        ResponseEntity<String> response = kebabController.deleteKebab(clientName, cartItemId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Клієнта з ім'ям Non-existing client не знайдено.", response.getBody());
    }
    @Test
    public void getClientCartPassTest() {
        String clientName = "John Doe";
        Client client = new Client(clientName, "johndoe@example.com");
        KebabOrder kebabOrder1 = new KebabOrder(new Kebab("Шаурма", 10.0, Arrays.asList("Tomato", "Onion")), Arrays.asList("Extra cheese"));
        KebabOrder kebabOrder2 = new KebabOrder(new Kebab("Піца", 12.0, Arrays.asList("Pepperoni", "Mushrooms")), Arrays.asList("Double cheese"));
        kebabController.setClients(Arrays.asList(client));
        client.addToCart(kebabOrder1);
        client.addToCart(kebabOrder2);
        List<KebabOrder> actualCartItems = kebabController.getClientCart(clientName);

        assertEquals(2, actualCartItems.size());
        assertTrue(actualCartItems.contains(kebabOrder1));
        assertTrue(actualCartItems.contains(kebabOrder2));
    }
    @Test
    public void getClientCartEmptyList() {
        String clientName = "Non-existing client";
        List<KebabOrder> actualCartItems = kebabController.getClientCart(clientName);

        assertTrue(actualCartItems.isEmpty());
    }
}
