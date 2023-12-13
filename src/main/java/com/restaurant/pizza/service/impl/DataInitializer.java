package com.restaurant.pizza.service.impl;

import com.restaurant.pizza.entity.Kebab;
import com.restaurant.pizza.resource.KebabController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final KebabController kebabController;

    @Autowired
    public DataInitializer(KebabController kebabController) {
        this.kebabController = kebabController;
    }

    @Override
    public void run(String... args) {
        List<Kebab> initialKebabs = new ArrayList<>();
        initialKebabs.add(new Kebab("Шаурма", 5.99, List.of("Салат", "Томати", "Огірки")));
        initialKebabs.add(new Kebab("Класичний кебаб", 7.99, List.of("Цибуля", "Перець", "Соус")));

        kebabController.setKebabs(initialKebabs);
    }
}
