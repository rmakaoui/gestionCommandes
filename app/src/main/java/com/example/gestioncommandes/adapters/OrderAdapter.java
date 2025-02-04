package com.example.gestioncommandes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gestioncommandes.R;
import com.example.gestioncommandes.models.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<Order> orders;
    private OnItemClickListener listener;

    public OrderAdapter(List<Order> orders, OnItemClickListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.bind(order);
        holder.itemView.setOnClickListener(v -> listener.onItemClick(order));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        private TextView orderId, orderWeight, orderVolume, orderPrice;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            orderWeight = itemView.findViewById(R.id.orderWeight);
            orderVolume = itemView.findViewById(R.id.orderVolume);
            orderPrice = itemView.findViewById(R.id.orderPrice);
        }

        public void bind(Order order) {
            Context context = itemView.getContext();  // On récupère le contexte ici
            orderId.setText(context.getString(R.string.order_number) + order.getOrderId());
            orderWeight.setText(context.getString(R.string.weight) + ": " + order.getWeight() + " kg");
            orderVolume.setText(context.getString(R.string.volume) + ": " + order.getVolume() + " m³");
            orderPrice.setText(order.getPrice() + " " + context.getString(R.string.price));

        }
    }

    public interface OnItemClickListener {
        void onItemClick(Order order);
    }
}
