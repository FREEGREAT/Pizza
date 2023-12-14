package com.restaurant.pizza.resource;

import com.restaurant.pizza.entity.Client;
import com.restaurant.pizza.entity.Kebab;
import com.restaurant.pizza.entity.KebabOrder;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@WebMvcTest(KebabController.class)
public class KebabControllerIntegrationTest {

    @MockBean
    private KebabController kebabController;

    @Test
    public void testAddToCart()  {
        String clientName = "Іван";
        Long kebabId = 1L;
        List<KebabOrder> cartItems = new ArrayList<>();
        cartItems.add(new KebabOrder(new Kebab("Шаурма", 5.99, List.of("Салат", "Томати", "Огірки")), List.of("Салат", "Томати", "Огірки")));
        cartItems.add(new KebabOrder(new Kebab("Класичний кебаб", 7.99, List.of("Цибуля", "Перець", "Соус")), List.of("Цибуля", "Перець", "Соус")));
        when(kebabController.addToCart(anyString(), anyLong())).thenReturn(ResponseEntity.ok("Кебаб з ID " + kebabId + " додано до корзини клієнта " + clientName));
    }
    @Test
    public void testGetCart() {
        String clientName = "Іван";
        List<KebabOrder> cartItems = new ArrayList<>();
        cartItems.add(new KebabOrder(new Kebab("Шаурма", 5.99, List.of("Салат", "Томати", "Огірки")), List.of("Салат", "Томати", "Огірки")));
        cartItems.add(new KebabOrder(new Kebab("Класичний кебаб", 7.99, List.of("Цибуля", "Перець", "Соус")), List.of("Цибуля", "Перець", "Соус")));
        when(kebabController.getCart(anyString())).thenReturn(ResponseEntity.ok(cartItems));
    }
    @Test
    public void testRemoveFromCart()  {
        String clientName = "Іван";
        Long kebabId = 1L;
        List<KebabOrder> cartItems = new ArrayList<>();
        cartItems.add(new KebabOrder(new Kebab("Шаурма", 5.99, List.of("Салат", "Томати", "Огірки")), List.of("Салат", "Томати", "Огірки")));
        cartItems.add(new KebabOrder(new Kebab("Класичний кебаб", 7.99, List.of("Цибуля", "Перець", "Соус")), List.of("Цибуля", "Перець", "Соус")));
        when(kebabController.removeFromCart(anyString(), anyLong())).thenReturn(ResponseEntity.ok("Кебаб з ID " + kebabId + " видалено з корзини клієнта " + clientName));
    }
    @Test
    public void testGetKebabs()  {
        List<Kebab> kebabs = new ArrayList<>();
        kebabs.add(new Kebab("Шаурма", 5.99, List.of("Салат", "Томати", "Огірки")));
        kebabs.add(new Kebab("Класичний кебаб", 7.99, List.of("Цибуля", "Перець", "Соус")));
        when(kebabController.getKebabs()).thenReturn(ResponseEntity.ok(kebabs));
    }
    @Test
    public void testGetKebab()  {
        Long kebabId = 1L;
        Kebab kebab = new Kebab("Шаурма", 5.99, List.of("Салат", "Томати", "Огірки"));
        when(kebabController.getKebab(anyLong())).thenReturn(ResponseEntity.ok(kebab));
    }
    @Test
    public void testGetClients()  {
        List<Client> clients = new ArrayList<>();
        Client client = new Client("Назар", "nazarkuryshchuk8@gmail.com");
        clients.add(client);
        when(kebabController.getClients()).thenReturn(ResponseEntity.ok(clients));
    }

    @Test
    public void isValidEmail()  {
        String email = "nazarkuryshchuk8@gmail.com";
        when(kebabController.isValidEmail(anyString())).thenReturn(false);
    }

    @Test
    public void updateCartItem ()  {
        String clientName = "Іван";
        Long kebabId = 1L;
        List<KebabOrder> cartItems = new ArrayList<>();
        cartItems.add(new KebabOrder(new Kebab("Шаурма", 5.99, List.of("Салат", "Томати", "Огірки")), List.of("Салат", "Томати", "Огірки")));
        cartItems.add(new KebabOrder(new Kebab("Класичний кебаб", 7.99, List.of("Цибуля", "Перець", "Соус")), List.of("Цибуля", "Перець", "Соус")));
        when(kebabController.updateCartItem(anyString(), anyLong(), anyInt())).thenReturn(ResponseEntity.ok("Кількість кебабу з ID " + kebabId + " в корзині клієнта " + clientName + " змінено на " + 1));
    }

    @Test
    public void testSetKebabs()  {
        List<Kebab> kebabs = new ArrayList<>();
        kebabs.add(new Kebab("Шаурма", 5.99, List.of("Салат", "Томати", "Огірки")));
        kebabs.add(new Kebab("Класичний кебаб", 7.99, List.of("Цибуля", "Перець", "Соус")));
        doNothing().when(kebabController).setKebabs(anyList());
    }
    @Test
    public void testUpdateKebab()  {
        Long kebabId = 1L;
        Kebab kebab = new Kebab("Шаурма", 5.99, List.of("Салат", "Томати", "Огірки"));
        doNothing().when(kebabController).updateKebab(anyLong(), any());
    }

    @Test
    public void createClient ()  {
        String clientName = "Андрій";
        String email = "andree@gmail.com";
        when(kebabController.createClient(anyString(), anyString())).thenReturn(ResponseEntity.ok("Клієнта " + clientName + " з email " + email + " створено"));
    }


}