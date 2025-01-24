package com.example.gestioncommandes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

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
import com.example.gestioncommandes.utils.OrderGenerator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CONTAINER_CONFIG = 1; // Définir la constante
    private List<Order> orders;
    private OrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Générer des commandes aléatoires
        orders = OrderGenerator.generateRandomOrders(20);

        // Configurer le RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new OrderAdapter(orders, order -> {
            // Naviguer vers l'écran de détails
            Intent intent = new Intent(MainActivity.this, OrderDetailActivity.class);
            intent.putExtra("ORDER", order);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        // Bouton pour régénérer les commandes
        findViewById(R.id.regenerateButton).setOnClickListener(v -> {
            orders = OrderGenerator.generateRandomOrders(20);
            adapter = new OrderAdapter(orders, order -> {
                Intent intent = new Intent(MainActivity.this, OrderDetailActivity.class);
                intent.putExtra("ORDER", order);
                startActivity(intent);
            });
            recyclerView.setAdapter(adapter);
        });

        Button buttonConfigureContainer = findViewById(R.id.buttonConfigureContainer);
        buttonConfigureContainer.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ContainerConfigActivity.class);
            intent.putExtra("ORDERS_LIST", new ArrayList<>(orders));
            startActivityForResult(intent, REQUEST_CODE_CONTAINER_CONFIG);
        });
    }
}