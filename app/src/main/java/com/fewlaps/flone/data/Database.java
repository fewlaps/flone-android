package com.fewlaps.flone.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fewlaps.flone.data.bean.Drone;
import com.fewlaps.flone.data.bean.DroneList;
import com.google.gson.Gson;

import java.util.List;

/**
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 19/02/2015
 */
public class Database {

    private static final Gson GSON = new Gson();

    private static final String PREF_DRONES_LIST = "prefDronesList";

    private static List<Drone> drones = null;

    public static List<Drone> getDrones(Context context) {
        if (drones == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String value = preferences.getString(PREF_DRONES_LIST, null);
            if (value != null) {
                drones = GSON.fromJson(value, DroneList.class);
            } else {
                drones = new DroneList();
            }
        }
        return drones;
    }

    public static void addDrone(Context context, Drone drone) {
        List<Drone> drones = getDrones(context);
        drones.add(drone);
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREF_DRONES_LIST, GSON.toJson(drones)).apply();
    }
}
