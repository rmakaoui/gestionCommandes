package com.example.gestioncommandes.utils;

import com.example.gestioncommandes.models.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OrderGenerator {
    public static List<Order> generateRandomOrders(int count) {
        List<Order> orders = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= count; i++) {
            Order order = new Order(
                    i,
                    random.nextInt(100) + 1,       // Poids entre 1 et 100
                    random.nextInt(50) + 1,        // Volume entre 1 et 50
                    random.nextInt(491) + 10,      // Prix entre 10 et 500
                    random.nextInt(3) + 1,         // Priorité entre 1 et 3
                    random.nextBoolean()           // Fragilité aléatoire
            );
            orders.add(order);
        }
        return orders;
    }
}
