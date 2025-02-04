package com.example.gestioncommandes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestioncommandes.R;
import com.example.gestioncommandes.adapters.OrderAdapter;
import com.example.gestioncommandes.models.Order;
import com.example.gestioncommandes.utils.KnapsackSolver;

import java.util.ArrayList;
import java.util.List;

public class ShippingResultActivity extends AppCompatActivity {

    private List<Order> orders;
    private double maxWeight, maxVolume;
    private OrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_result);

        TextView emptyMessage = findViewById(R.id.emptyMessage);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Récupérer les valeurs de l'intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ORDERS_LIST")) {
            maxWeight = intent.getDoubleExtra("MAX_WEIGHT", 100);
            maxVolume = intent.getDoubleExtra("MAX_VOLUME", 50);

            Object receivedOrders = intent.getSerializableExtra("ORDERS_LIST");
            if (receivedOrders instanceof ArrayList<?>) {
                orders = (ArrayList<Order>) receivedOrders;
            } else {
                orders = new ArrayList<>();
            }
        } else {
            orders = new ArrayList<>();
        }

        // Appliquer l'algorithme de sélection
        List<Order> bestCombination = KnapsackSolver.findBestCombination(orders, maxWeight, maxVolume);

        // Vérifier si la liste est vide
        if (bestCombination.isEmpty()) {
            String message = getString(R.string.no_orders_message);
            emptyMessage.setText(message);
            emptyMessage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyMessage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            adapter = new OrderAdapter(bestCombination, order -> {
                Intent intent2 = new Intent(ShippingResultActivity.this, OrderDetailActivity.class);
                intent2.putExtra("ORDER", order);
                startActivity(intent2);
            });
            recyclerView.setAdapter(adapter);
        }

        // Afficher les ordres dans la console (pour vérification)
        displayOrdersInConsole(bestCombination);
        System.out.println("VS");
        displayOrdersInConsole(orders);
    }
    private void displayOrdersInConsole(List<Order> bestCombination) {
        System.out.println("ShippingResult activity");
        for (Order order : bestCombination) {
            System.out.println("Order #" + order.getOrderId() +
                    " - Weight: " + order.getWeight() + " kg" +
                    ", Volume: " + order.getVolume() + " m³" +
                    ", Price: $" + order.getPrice() +
                    ", Priority: " + order.getPriority() +
                    ", Fragile: " + (order.isFragile() ? "Yes" : "No"));
        }
    }
}