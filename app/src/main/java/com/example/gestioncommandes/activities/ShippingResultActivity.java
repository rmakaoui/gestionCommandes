package com.example.gestioncommandes.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestioncommandes.R;
import com.example.gestioncommandes.adapters.OrderAdapter;
import com.example.gestioncommandes.models.Order;
import com.example.gestioncommandes.utils.KnapsackSolver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShippingResultActivity extends AppCompatActivity {

    private List<Order> orders;

    private List<Order> bestCombination;

    private double maxWeight,maxVolume;

    private OrderAdapter adapter;

    private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 100;

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

        bestCombination = KnapsackSolver.findBestCombination(orders, maxWeight, maxVolume);

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

        Button buttonShowOrders = findViewById(R.id.buttonShowOrders);
        buttonShowOrders.setOnClickListener(v -> {
            // Afficher la boîte de dialogue avec tous les ordres
            showOrdersDialog(orders, bestCombination);
        });

        Button comparisonButton = findViewById(R.id.comparisonButton);

        // Définir un écouteur de clic sur le bouton
        comparisonButton.setOnClickListener(v -> {
            // Afficher un modal de confirmation
            showConfirmationDialog();
        });
    }

    private void showConfirmationDialog() {
        // Créer un AlertDialog
        new AlertDialog.Builder(this)
                .setTitle("Confirmation")
                .setMessage("Êtes-vous sûr de vouloir quitter et revenir à l'écran principal ?")
                .setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Revenir à MainActivity
                        Intent intent = new Intent(ShippingResultActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Fermer l'activité actuelle
                    }
                })
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ne rien faire, simplement fermer le dialogue
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
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
    private void showOrdersDialog(List<Order> allOrders, List<Order> selectedOrders) {
        // Créer une chaîne de caractères pour afficher les détails des ordres
        StringBuilder ordersDetails = new StringBuilder();

        for (Order order : allOrders) {
            // Vérifier si l'ordre est dans la liste des ordres sélectionnés
            boolean isSelected = selectedOrders.contains(order);

            // Ajouter les détails de l'ordre
            String orderText = "Order #" + order.getOrderId() +
                    " - Weight: " + order.getWeight() + " kg" +
                    ", Volume: " + order.getVolume() + " m³" +
                    ", Price: $" + order.getPrice() +
                    ", Priority: " + order.getPriority() +
                    ", Fragile: " + (order.isFragile() ? "Yes" : "No") +
                    "\n\n";

            ordersDetails.append(orderText);
        }

        // Créer un SpannableString pour appliquer des styles
        SpannableString spannableString = new SpannableString(ordersDetails.toString());

        // Appliquer des styles aux ordres sélectionnés
        int startIndex = 0;
        for (Order order : allOrders) {
            String orderText = "Order #" + order.getOrderId() +
                    " - Weight: " + order.getWeight() + " kg" +
                    ", Volume: " + order.getVolume() + " m³" +
                    ", Price: $" + order.getPrice() +
                    ", Priority: " + order.getPriority() +
                    ", Fragile: " + (order.isFragile() ? "Yes" : "No") +
                    "\n\n";

            if (selectedOrders.contains(order)) {
                // Appliquer le style bleu et gras
                spannableString.setSpan(
                        new ForegroundColorSpan(Color.BLUE), // Couleur bleue
                        startIndex,
                        startIndex + orderText.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
                spannableString.setSpan(
                        new StyleSpan(Typeface.BOLD), // Texte en gras
                        startIndex,
                        startIndex + orderText.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }

            startIndex += orderText.length();
        }

        // Créer une boîte de dialogue modale
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("All Orders")
                .setMessage(spannableString) // Utiliser le SpannableString pour afficher les styles
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss()) // Bouton pour fermer la boîte de dialogue
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }
}