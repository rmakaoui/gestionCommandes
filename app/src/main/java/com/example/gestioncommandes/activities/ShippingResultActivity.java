package com.example.gestioncommandes.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

    private double maxWeight,maxVolume;

    private OrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_result);

        // Récupérer les valeurs configurées


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ORDERS_LIST")) {
            maxWeight = getIntent().getDoubleExtra("MAX_WEIGHT", 100);
            maxVolume = getIntent().getDoubleExtra("MAX_VOLUME", 50);
            orders = (List<Order>) intent.getSerializableExtra("ORDERS_LIST");
        } else {
            orders = new ArrayList<>(); // Liste vide si aucune donnée n'est passée
        }

        List<Order> bestCombination = KnapsackSolver.findBestCombination(orders, maxWeight, maxVolume);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new OrderAdapter(bestCombination, order -> {
            // Naviguer vers l'écran de détails
            Intent intent2 = new Intent(ShippingResultActivity.this, OrderDetailActivity.class);
            intent2.putExtra("ORDER", order);
            startActivity(intent2);
        });
        recyclerView.setAdapter(adapter);

        // Afficher les ordres dans la console (pour vérification)
        displayOrdersInConsole(bestCombination);
        System.out.println("VS");
        displayOrdersInConsole(orders);
    }
    private void displayOrdersInConsole(List<Order> bestCombination ) {
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