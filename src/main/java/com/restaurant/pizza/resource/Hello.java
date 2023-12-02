package com.restaurant.pizza.resource;

import  org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class Hello {
    @GetMapping("/")
    public String home(){
        return "Hello World!";
    }
}
