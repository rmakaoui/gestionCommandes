package com.example.gestioncommandes.activities;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.gestioncommandes.R;
import com.example.gestioncommandes.models.Order;

import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.app.AlertDialog;

public class ContainerConfigActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_CONTAINER_CONFIG = 1;
    private EditText editTextMaxWeight, editTextMaxVolume;
    private Button buttonSave,buttonReset;
    ;

    private List<Order> orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_config);

        // Récupérer la liste des ordres depuis l'Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("ORDERS_LIST")) {
            orders = (List<Order>) intent.getSerializableExtra("ORDERS_LIST");
        } else {
            orders = new ArrayList<>(); // Liste vide si aucune donnée n'est passée
        }

        // Afficher les ordres dans la console (pour vérification)
        displayOrdersInConsole();

        // Initialiser les vues
        editTextMaxWeight = findViewById(R.id.editTextMaxWeight);
        editTextMaxVolume = findViewById(R.id.editTextMaxVolume);
        buttonSave = findViewById(R.id.buttonSave);
        buttonReset = findViewById(R.id.buttonReset);

        // Gérer le clic sur le bouton "Save"
        buttonSave.setOnClickListener(v -> {
            // Récupérer les valeurs saisies
            String maxWeightStr = editTextMaxWeight.getText().toString();
            String maxVolumeStr = editTextMaxVolume.getText().toString();

            // Convertir les valeurs en nombres
            double maxWeight = Double.parseDouble(maxWeightStr);
            double maxVolume = Double.parseDouble(maxVolumeStr);

            // Afficher une boîte de dialogue modale avec les valeurs configurées
            showConfirmationDialog(maxWeight, maxVolume);
        });

        // Gérer le clic sur le bouton "Reset"
        buttonReset.setOnClickListener(v -> {
            // Réinitialiser les champs à des valeurs vides
            editTextMaxWeight.setText("");
            editTextMaxVolume.setText("");
        });
    }

    private void showConfirmationDialog(double maxWeight, double maxVolume) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Container Configuration")
                .setMessage("Max Weight: " + maxWeight + " kg\nMax Volume: " + maxVolume + " m³")
                .setPositiveButton("Confirm", (dialog, which) -> {
                    // Retourner les valeurs à l'activité principale
                    Intent resultIntent = new Intent(ContainerConfigActivity.this, ShippingResultActivity.class);
                    resultIntent.putExtra("MAX_WEIGHT", maxWeight);
                    resultIntent.putExtra("MAX_VOLUME", maxVolume);
                    resultIntent.putExtra("ORDERS_LIST", new ArrayList<>(orders));
                    setResult(RESULT_OK, resultIntent);
                    startActivityForResult(resultIntent, REQUEST_CODE_CONTAINER_CONFIG);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    // Ne rien faire, fermer la boîte de dialogue
                    dialog.dismiss();
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }

    private void displayOrdersInConsole() {
        System.out.println("Container config !");
        for (Order order : orders) {
            System.out.println("Order #" + order.getOrderId() +
                    " - Weight: " + order.getWeight() + " kg" +
                    ", Volume: " + order.getVolume() + " m³" +
                    ", Price: $" + order.getPrice() +
                    ", Priority: " + order.getPriority() +
                    ", Fragile: " + (order.isFragile() ? "Yes" : "No"));
        }
    }
}
