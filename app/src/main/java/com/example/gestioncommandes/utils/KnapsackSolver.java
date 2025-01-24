package com.example.gestioncommandes.utils;

import com.example.gestioncommandes.models.Order;

import java.util.ArrayList;
import java.util.List;

public class KnapsackSolver {

    /**
     * Trouve la meilleure combinaison d'ordres qui maximise le prix total
     * tout en respectant les contraintes de poids et de volume.
     *
     * @param orders     Liste de tous les ordres disponibles.
     * @param maxWeight  Poids maximum du conteneur.
     * @param maxVolume  Volume maximum du conteneur.
     * @return Une liste d'ordres représentant la meilleure combinaison.
     */
    public static List<Order> findBestCombination(List<Order> orders, double maxWeight, double maxVolume) {
        int n = orders.size();
        double[][][] dp = new double[n + 1][(int) maxWeight + 1][(int) maxVolume + 1];

        // Remplir la table dp
        for (int i = 1; i <= n; i++) {
            Order order = orders.get(i - 1);
            for (int w = 0; w <= maxWeight; w++) {
                for (int v = 0; v <= maxVolume; v++) {
                    if (order.getWeight() <= w && order.getVolume() <= v) {
                        dp[i][w][v] = Math.max(
                                dp[i - 1][w][v], // Ne pas inclure l'ordre
                                dp[i - 1][w - (int) order.getWeight()][v - (int) order.getVolume()] + order.getPrice() // Inclure l'ordre
                        );
                    } else {
                        dp[i][w][v] = dp[i - 1][w][v]; // Ne pas inclure l'ordre
                    }
                }
            }
        }

        // Récupérer les ordres sélectionnés
        List<Order> selectedOrders = new ArrayList<>();
        int w = (int) maxWeight;
        int v = (int) maxVolume;
        for (int i = n; i > 0; i--) {
            if (dp[i][w][v] != dp[i - 1][w][v]) {
                selectedOrders.add(orders.get(i - 1));
                w -= (int) orders.get(i - 1).getWeight();
                v -= (int) orders.get(i - 1).getVolume();
            }
        }

        return selectedOrders;
    }
}
