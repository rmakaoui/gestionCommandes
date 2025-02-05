package com.example.gestioncommandes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestioncommandes.R;
import com.example.gestioncommandes.adapters.OrderAdapter;
import com.example.gestioncommandes.models.Container;
import com.example.gestioncommandes.models.Order;
import com.example.gestioncommandes.utils.OrderGenerator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_CONTAINER_CONFIG = 1;
    private List<Order> orders;
    private OrderAdapter adapter;
    private Button buttonUseContainer;

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

        // Configuration du conteneur
        Button buttonConfigureContainer = findViewById(R.id.buttonConfigureContainer);
        buttonUseContainer = findViewById(R.id.buttonUseContainer); // Récupérer le bouton ajouté

        buttonConfigureContainer.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ContainerConfigActivity.class);
            intent.putExtra("ORDERS_LIST", new ArrayList<>(orders));
            startActivityForResult(intent, REQUEST_CODE_CONTAINER_CONFIG);
        });

        checkContainer();
    }

    private void checkContainer() {
        Container container = Container.getInstance(this);

        if (container.getMaxWeight() > 0 && container.getMaxVolume() > 0) {
            Toast.makeText(this, "Container is configured: Max Weight = "
                    + container.getMaxWeight() + " kg, Max Volume = "
                    + container.getMaxVolume() + " m³", Toast.LENGTH_LONG).show();

            // Rendre visible le bouton pour réutiliser le conteneur existant
            buttonUseContainer.setVisibility(View.VISIBLE);

            // Gérer le clic sur le bouton "Use Existing Container"
            buttonUseContainer.setOnClickListener(v -> {
                useExistingContainer();
            });
        } else {
            buttonUseContainer.setVisibility(View.GONE);
        }
    }

    private void useExistingContainer() {
        Container container = Container.getInstance(this);

        Intent intent = new Intent(MainActivity.this, ShippingResultActivity.class);
        intent.putExtra("MAX_WEIGHT", container.getMaxWeight());
        intent.putExtra("MAX_VOLUME", container.getMaxVolume());
        intent.putExtra("ORDERS_LIST", new ArrayList<>(orders));
        startActivityForResult(intent, REQUEST_CODE_CONTAINER_CONFIG);
    }
}
