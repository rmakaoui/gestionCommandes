package com.example.gestioncommandes.models;

import java.io.Serializable;

public class Order implements Serializable {
    private int orderId;
    private double weight;
    private double volume;
    private double price;
    private int priority;
    private boolean isFragile;

    public Order(int orderId, double weight, double volume, double price, int priority, boolean isFragile) {
        this.orderId = orderId;
        this.weight = weight;
        this.volume = volume;
        this.price = price;
        this.priority = priority;
        this.isFragile = isFragile;
    }

    // Getters
    public int getOrderId() { return orderId; }
    public double getWeight() { return weight; }
    public double getVolume() { return volume; }
    public double getPrice() { return price; }
    public int getPriority() { return priority; }
    public boolean isFragile() { return isFragile; }
}
