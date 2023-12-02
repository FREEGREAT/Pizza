package com.restaurant.pizza.resource;

import com.restaurant.pizza.entity.User;
import com.restaurant.pizza.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/user")
public class UserResource {
    @Autowired
    private UserRepo userRepository;

    @GetMapping
    public User getUser(){
        return userRepository.userList.get(0);
    }

    @PostMapping
    public String createUser() {
        User user = new User();
        user.setName("Vlad");
        user.setSurname("Dmytrenko");
        userRepository.userList.add(user);
        return "already";
    }

}
