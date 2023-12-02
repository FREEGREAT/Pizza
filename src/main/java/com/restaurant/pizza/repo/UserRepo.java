package com.restaurant.pizza.repo;

import com.restaurant.pizza.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class UserRepo {
    public List<User> userList = new ArrayList<>();
}
