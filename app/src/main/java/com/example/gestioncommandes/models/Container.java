package com.example.gestioncommandes.models;

import android.content.Context;
import android.content.SharedPreferences;

public class Container {
    private static final String PREF_NAME = "ContainerPrefs";
    private static final String KEY_MAX_WEIGHT = "max_weight";
    private static final String KEY_MAX_VOLUME = "max_volume";

    private static Container instance;
    private double maxWeight;
    private double maxVolume;
    private SharedPreferences prefs;

    private Container(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        maxWeight = prefs.getFloat(KEY_MAX_WEIGHT, 0);
        maxVolume = prefs.getFloat(KEY_MAX_VOLUME, 0);
    }

    public static Container getInstance(Context context) {
        if (instance == null) {
            instance = new Container(context);
        }
        return instance;
    }

    public double getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(double maxWeight) {
        this.maxWeight = maxWeight;
        prefs.edit().putFloat(KEY_MAX_WEIGHT, (float) maxWeight).apply();
    }

    public double getMaxVolume() {
        return maxVolume;
    }

    public void setMaxVolume(double maxVolume) {
        this.maxVolume = maxVolume;
        prefs.edit().putFloat(KEY_MAX_VOLUME, (float) maxVolume).apply();
    }
}
