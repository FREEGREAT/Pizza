package com.restaurant.pizza.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurant.pizza.entity.Client;
import com.restaurant.pizza.entity.Kebab;
import com.restaurant.pizza.entity.KebabOrder;
import com.restaurant.pizza.resource.KebabController;

import lombok.ToString;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(KebabController.class)
public class KebabControllerIntegrationTest {

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

   @MockBean
   private KebabController kebabController;

   @Test
          public void testAddToCart() throws Exception {
          String clientName = "Іван";
          Long kebabId = 1L;
          List<KebabOrder> cartItems = new ArrayList<>();
          cartItems.add(new KebabOrder(new Kebab("Шаурма", 5.99, List.of("Салат", "Томати", "Огірки")), List.of("Салат", "Томати", "Огірки")));
          cartItems.add(new KebabOrder(new Kebab("Класичний кебаб", 7.99, List.of("Цибуля", "Перець", "Соус")), List.of("Цибуля", "Перець", "Соус")));
          when(kebabController.addToCart(anyString(), anyLong())).thenReturn(ResponseEntity.ok("Кебаб з ID " + kebabId + " додано до корзини клієнта " + clientName));
          }
    @Test
    public void testGetCart() throws Exception {
        String clientName = "Іван";
        List<KebabOrder> cartItems = new ArrayList<>();
        cartItems.add(new KebabOrder(new Kebab("Шаурма", 5.99, List.of("Салат", "Томати", "Огірки")), List.of("Салат", "Томати", "Огірки")));
        cartItems.add(new KebabOrder(new Kebab("Класичний кебаб", 7.99, List.of("Цибуля", "Перець", "Соус")), List.of("Цибуля", "Перець", "Соус")));
        when(kebabController.getCart(anyString())).thenReturn(ResponseEntity.ok(cartItems));
    }
    @Test
    public void testRemoveFromCart() throws Exception {
        String clientName = "Іван";
        Long kebabId = 1L;
        List<KebabOrder> cartItems = new ArrayList<>();
        cartItems.add(new KebabOrder(new Kebab("Шаурма", 5.99, List.of("Салат", "Томати", "Огірки")), List.of("Салат", "Томати", "Огірки")));
        cartItems.add(new KebabOrder(new Kebab("Класичний кебаб", 7.99, List.of("Цибуля", "Перець", "Соус")), List.of("Цибуля", "Перець", "Соус")));
        when(kebabController.removeFromCart(anyString(), anyLong())).thenReturn(ResponseEntity.ok("Кебаб з ID " + kebabId + " видалено з корзини клієнта " + clientName));
    }
    @Test
    public void testGetKebabs() throws Exception {
        List<Kebab> kebabs = new ArrayList<>();
        kebabs.add(new Kebab("Шаурма", 5.99, List.of("Салат", "Томати", "Огірки")));
        kebabs.add(new Kebab("Класичний кебаб", 7.99, List.of("Цибуля", "Перець", "Соус")));
        when(kebabController.getKebabs()).thenReturn(ResponseEntity.ok(kebabs));
    }
    @Test
    public void testGetKebab() throws Exception {
        Long kebabId = 1L;
        Kebab kebab = new Kebab("Шаурма", 5.99, List.of("Салат", "Томати", "Огірки"));
        when(kebabController.getKebab(anyLong())).thenReturn(ResponseEntity.ok(kebab));
    }
    @Test
    public void testGetClients() throws Exception {
        List<Client> clients = new ArrayList<>();
        Client client = new Client("Назар", "nazarkuryshchuk8@gmail.com");
        clients.add(client);
        when(kebabController.getClients()).thenReturn(ResponseEntity.ok(clients));
    }

    /**
     * @throws Exception
     */
    @Test
    public void isValidEmail() throws Exception {
        String email = "nazarkuryshchuk8@gmail.com";
        when(kebabController.isValidEmail(anyString())).thenReturn(false);
    }

    @Test 
    public void updateCartItem () throws Exception {
        String clientName = "Іван";
        Long kebabId = 1L;
        List<KebabOrder> cartItems = new ArrayList<>();
        cartItems.add(new KebabOrder(new Kebab("Шаурма", 5.99, List.of("Салат", "Томати", "Огірки")), List.of("Салат", "Томати", "Огірки")));
        cartItems.add(new KebabOrder(new Kebab("Класичний кебаб", 7.99, List.of("Цибуля", "Перець", "Соус")), List.of("Цибуля", "Перець", "Соус")));
        when(kebabController.updateCartItem(anyString(), anyLong(), anyInt())).thenReturn(ResponseEntity.ok("Кількість кебабу з ID " + kebabId + " в корзині клієнта " + clientName + " змінено на " + 1));
    }

    @Test
    public void testSetKebabs() throws Exception {
        List<Kebab> kebabs = new ArrayList<>();
        kebabs.add(new Kebab("Шаурма", 5.99, List.of("Салат", "Томати", "Огірки")));
        kebabs.add(new Kebab("Класичний кебаб", 7.99, List.of("Цибуля", "Перець", "Соус")));
        doNothing().when(kebabController).setKebabs(anyList());
    }
    @Test
    public void testUpdateKebab() throws Exception {
        Long kebabId = 1L;
        Kebab kebab = new Kebab("Шаурма", 5.99, List.of("Салат", "Томати", "Огірки"));
        doNothing().when(kebabController).updateKebab(anyLong(), any());
    }

    @Test
    public void createClient () throws Exception {
        String clientName = "Анлдрій";
        String email = "andree@gmail.com";
        when(kebabController.createClient(anyString(), anyString())).thenReturn(ResponseEntity.ok("Клієнта " + clientName + " з email " + email + " створено"));
    }

    
}
