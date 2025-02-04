package com.example.gestioncommandes.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.gestioncommandes.R;
import com.example.gestioncommandes.models.Order;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class OrderDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        // Récupérer la commande passée en paramètre
        Order order = (Order) getIntent().getSerializableExtra("ORDER");

        // Afficher les détails de la commande
        if (order != null) {
            TextView detailOrderId = findViewById(R.id.detailOrderId);
            TextView detailOrderWeight = findViewById(R.id.detailOrderWeight);
            TextView detailOrderVolume = findViewById(R.id.detailOrderVolume);
            TextView detailOrderPrice = findViewById(R.id.detailOrderPrice);
            TextView detailOrderPriority = findViewById(R.id.detailOrderPriority);
            TextView detailOrderFragile = findViewById(R.id.detailOrderFragile);

            detailOrderId.setText(getString(R.string.order_detail) + " : " + order.getOrderId());
            detailOrderWeight.setText(getString(R.string.weight) + " : " + order.getWeight() + " " + getString(R.string.kg));
            detailOrderVolume.setText(getString(R.string.volume) + " : " + order.getVolume() + " " + getString(R.string.m3));
            detailOrderPrice.setText(getString(R.string.price) + " : " + getString(R.string.usd) + order.getPrice());

            // Afficher la priorité sous forme de texte
            String priorityText;
            switch (order.getPriority()) {
                case 1:
                    priorityText = "High";
                    break;
                case 2:
                    priorityText = "Medium";
                    break;
                case 3:
                    priorityText = "Low";
                    break;
                default:
                    priorityText = "Unknown";
            }
            detailOrderPriority.setText(getString(R.string.priority) + " " + priorityText);

            // Afficher la fragilité sous forme de texte
            String fragileText = order.isFragile() ? getString(R.string.yes) : getString(R.string.no);
            detailOrderFragile.setText(getString(R.string.fragile) + " : " + fragileText);
        }
    }
}
