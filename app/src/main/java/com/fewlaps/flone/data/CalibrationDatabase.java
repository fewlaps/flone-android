package com.fewlaps.flone.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fewlaps.flone.data.bean.PhoneCalibrationData;
import com.google.gson.Gson;

/**
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 19/02/2015
 */
public class CalibrationDatabase {

    private static final Gson GSON = new Gson();

    private static final String PREF_PHONE_CALIBRATION = "prefPhoneCalibration";

    private static PhoneCalibrationData cachedData = null;

    public static PhoneCalibrationData getPhoneCalibrationData(Context context) {
        if (cachedData == null) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            String value = preferences.getString(PREF_PHONE_CALIBRATION, null);
            if (value != null) {
                cachedData = GSON.fromJson(value, PhoneCalibrationData.class);
            } else {
                cachedData = new PhoneCalibrationData();
            }
        }
        return cachedData;
    }

    public static void setPhoneCalibrationData(Context context, PhoneCalibrationData data) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(PREF_PHONE_CALIBRATION, GSON.toJson(data)).commit();
        cachedData = data;
    }
}
