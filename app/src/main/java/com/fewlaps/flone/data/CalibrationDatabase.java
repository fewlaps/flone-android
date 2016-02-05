package com.fewlaps.flone.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fewlaps.flone.data.bean.DroneCalibrationData;
import com.fewlaps.flone.data.bean.PhoneCalibrationData;
import com.google.gson.Gson;

public class CalibrationDatabase {

    private static final Gson GSON = new Gson();

    private static final String PREF_PHONE_CALIBRATION = "prefPhoneCalibration";
    private static final String PREF_DRONE_CALIBRATION = "prefDroneCalibration";

    private static PhoneCalibrationData phoneCacheData = null;
    private static DroneCalibrationData droneCacheData = null;

    public static PhoneCalibrationData getPhoneCalibrationData(Context context) {
        if (phoneCacheData == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String value = preferences.getString(PREF_PHONE_CALIBRATION, null);
            if (value != null) {
                phoneCacheData = GSON.fromJson(value, PhoneCalibrationData.class);
            } else {
                phoneCacheData = new PhoneCalibrationData();
            }
        }
        return phoneCacheData;
    }

    public static void setPhoneCalibrationData(Context context, PhoneCalibrationData data) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(PREF_PHONE_CALIBRATION, GSON.toJson(data)).commit();
        phoneCacheData = data;
    }

    public static DroneCalibrationData getDroneCalibrationData(Context context, String droneNickName) {
        if (droneCacheData == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String value = preferences.getString(PREF_DRONE_CALIBRATION + droneNickName, null);
            if (value != null) {
                droneCacheData = GSON.fromJson(value, DroneCalibrationData.class);
            } else {
                droneCacheData = new DroneCalibrationData();
            }
        }
        return droneCacheData;
    }

    public static void setDroneCalibrationData(Context context, String droneNickName, DroneCalibrationData data) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(PREF_DRONE_CALIBRATION + droneNickName, GSON.toJson(data)).commit();
        droneCacheData = data;
    }
}
