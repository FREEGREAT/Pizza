//package com.restaurant.pizza.resource;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.restaurant.pizza.entity.Client;
//import com.restaurant.pizza.entity.Kebab;
//import com.restaurant.pizza.entity.KebabOrder;
//import com.restaurant.pizza.resource.KebabController;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@WebMvcTest(KebabController.class)
//public class KebabControllerIntegrationTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private KebabController kebabController;
//
////    @Test
////    public void testGetMenu() throws Exception {
////        List<Kebab> kebabs = new ArrayList<>();
////        kebabs.add(new Kebab("Шаурма", 5.99, List.of("Салат", "Томати", "Огірки")));
////        kebabs.add(new Kebab("Класичний кебаб", 7.99, List.of("Цибуля", "Перець", "Соус")));
////
////        when(kebabController.getMenu()).thenReturn(kebabs);
////
////        mockMvc.perform(get("/kebab/menu"))
////                .andExpect(status().isOk())
////                .andExpect(content().json(objectMapper.writeValueAsString(kebabs)));
////
////        verify(kebabController, times(1)).getMenu();
////    }
//    @Test
//public void testCreateClient() throws Exception {
//        String name = "Іван";
//        String email = "nazar@gmail.com";
//        when(kebabController.createClient(anyString(), anyString())).thenReturn(ResponseEntity.ok("Клієнт " + name + " створено"));
//
//        mockMvc.perform(post("/kebab/client")
//        .param("name", name)
//        .param("email", email))
//        .andExpect(status().isOk())
//        .andExpect(content().string("Клієнт " + name + " створено"));
//
//        verify(kebabController, times(1)).createClient(anyString(), anyString());
//        }
//        @Test
//        public void testCreateClientWithInvalidEmail() throws Exception {
//        String name = "Іван";
//        String email = "nazar";
//        when(kebabController.createClient(anyString(), anyString())).thenReturn(ResponseEntity.status(400).body("Невірний формат електронної пошти"));
//
//        mockMvc.perform(post("/kebab/client")
//        .param("name", name)
//        .param("email", email))
//        .andExpect(status().isBadRequest())
//        .andExpect(content().string("Невірний формат електронної пошти"));
//        }
//        @Test
//        public void testOrderKebabForClient() throws Exception {
//        String clientName = "Іван";
//        String type = "Шаурма";
//        List<String> additionalIngredients = new ArrayList<>();
//        additionalIngredients.add("Салат");
//        additionalIngredients.add("Томати");
//        additionalIngredients.add("Огірки");
//        when(kebabController.orderKebabForClient(anyString(), anyString(), anyList())).thenReturn(ResponseEntity.ok("Кебаб додано до корзини та замовлено для клієнта " + clientName));
//
//        mockMvc.perform(post("/kebab/client/{clientName}/order", clientName)
//        .param("type", type)
//        .param("additionalIngredients", additionalIngredients.get(0))
//        .param("additionalIngredients", additionalIngredients.get(1))
//        .param("additionalIngredients", additionalIngredients.get(2)))
//        .andExpect(status().isOk())
//        .andExpect(content().string("Кебаб додано до корзини та замовлено для клієнта " + clientName));
//
//        verify(kebabController, times(1)).orderKebabForClient(anyString(), anyString(), anyList());
//        }
//        @Test
//        public void testOrderKebabForClientWithInvalidType() throws Exception {
//        String clientName = "Іван";
//        String type = "Шаурма";
//        List<String> additionalIngredients = new ArrayList<>();
//        additionalIngredients.add("Салат");
//        additionalIngredients.add("Томати");
//        additionalIngredients.add("Огірки");
//        when(kebabController.orderKebabForClient(anyString(), anyString(), anyList())).thenReturn(ResponseEntity.status(404).body("Кебаб з типом " + type + " не знайдено в меню."));
//
//        mockMvc.perform(post("/kebab/client/{clientName}/order", clientName)
//        .param("type", type)
//        .param("additionalIngredients", additionalIngredients.get(0))
//        .param("additionalIngredients", additionalIngredients.get(1))
//        .param("additionalIngredients", additionalIngredients.get(2)))
//        .andExpect(status().isNotFound())
//
//        .andExpect(content().string("Кебаб з типом " + type + " не знайдено в меню."));
//        }
//
////        @Test
////        public void testGetConfirmedOrders() throws Exception {
////        String clientName = "Іван";
////        List<KebabOrder> confirmedOrders = new ArrayList<>();
////        confirmedOrders.add(new KebabOrder(new Kebab("Шаурма", 5.99, List.of("Салат", "Томати", "Огірки")), List.of("Салат", "Томати", "Огірки")));
////        confirmedOrders.add(new KebabOrder(new Kebab("Класичний кебаб", 7.99, List.of("Цибуля", "Перець", "Соус")), List.of("Цибуля", "Перець", "Соус")));
////        when(kebabController.getConfirmedOrders(anyString())).thenReturn(confirmedOrders);
////
////        mockMvc.perform(get("/kebab/confirmed-orders/{clientName}", clientName))
////        .andExpect(status().isOk())
////        .andExpect(content().json(objectMapper.writeValueAsString(confirmedOrders)));
////
////        verify(kebabController, times(1)).getConfirmedOrders(anyString());
////        }
////        @Test
////        public void testGetConfirmedOrdersWithInvalidClientName() throws Exception {
////        String clientName = "Іван";
////        List<KebabOrder> confirmedOrders = new ArrayList<>();
////
////        when(kebabController.getConfirmedOrders(anyString())).thenReturn(confirmedOrders);
////
////        mockMvc.perform(get("/kebab/confirmed-orders/{clientName}", clientName))
////        .andExpect(status().isOk())
////        .andExpect(content().json(objectMapper.writeValueAsString(confirmedOrders)));
////
////        verify(kebabController, times(1)).getConfirmedOrders(anyString());
////        }
//
////        @Test
////        public void testGetCart() throws Exception {
////        String clientName = "Іван";
////        List<KebabOrder> cartItems = new ArrayList<>();
////        cartItems.add(new KebabOrder(new Kebab("Шаурма", 5.99, List.of("Салат", "Томати", "Огірки")), List.of("Салат", "Томати", "Огірки")));
////        cartItems.add(new KebabOrder(new Kebab("Класичний кебаб", 7.99, List.of("Цибуля", "Перець", "Соус")), List.of("Цибуля", "Перець", "Соус")));
////        when(kebabController.getCart(anyString())).thenReturn(cartItems);
////
////        mockMvc.perform(get("/kebab/cart/{clientName}", clientName))
////        .andExpect(status().isOk())
////        .andExpect(content().json(objectMapper.writeValueAsString(cartItems)));
////
////        verify(kebabController, times(1)).getCart(anyString());
////        }
////        @Test
////        public void testGetCartWithInvalidClientName() throws Exception {
////        String clientName = "Іван";
////        List<KebabOrder> cartItems = new ArrayList<>();
////        when(kebabController.getCart(anyString())).thenReturn(cartItems);
////
////        mockMvc.perform(get("/kebab/cart/{clientName}", clientName))
////        .andExpect(status().isOk())
////        .andExpect(content().json(objectMapper.writeValueAsString(cartItems)));
////
////        verify(kebabController, times(1)).getCart(anyString());
////        }
//
////        @Test
////        public void testAddToCart() throws Exception {
////        String clientName = "Іван";
////        Long kebabId = 1L;
////        List<KebabOrder> cartItems = new ArrayList<>();
////        cartItems.add(new KebabOrder(new Kebab("Шаурма", 5.99, List.of("Салат", "Томати", "Огірки")), List.of("Салат", "Томати", "Огірки")));
////        cartItems.add(new KebabOrder(new Kebab("Класичний кебаб", 7.99, List.of("Цибуля", "Перець", "Соус")), List.of("Цибуля", "Перець", "Соус")));
////        when(kebabController.addToCart(anyString(), anyLong())).thenReturn(ResponseEntity.ok("Кебаб з ID " + kebabId + " додано до корзини клієнта " + clientName));
////        }
//
////        @Test
////        public void testAddToCartWithInvalidClientName() throws Exception {
////        String clientName = "Іван";
////        Long kebabId = 1L;
////        List<KebabOrder> cartItems = new ArrayList<>();
////        when(kebabController.addToCart(anyString(), anyLong())).thenReturn(ResponseEntity.status(404).body("Клієнта з ім'ям " + clientName + " не знайдено."));
////
////        mockMvc.perform(post("/kebab/cart/{clientName}/add/{kebabId}", clientName, kebabId))
////        .andExpect(status().isNotFound())
////        .andExpect(content().string("Клієнта з ім'ям " + clientName + " не знайдено."));
////
////        verify(kebabController, times(1)).addToCart(anyString(), anyLong());
////        }
//}
