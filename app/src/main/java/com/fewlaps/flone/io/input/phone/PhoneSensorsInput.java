package com.fewlaps.flone.io.input.phone;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.fewlaps.flone.data.CalibrationDatabase;
import com.fewlaps.flone.data.bean.PhoneSensorsData;
import com.fewlaps.flone.io.bean.MultiWiiValues;
import com.fewlaps.flone.io.communication.RCSignals;
import com.fewlaps.flone.io.input.UserInstructionsInput;

import de.greenrobot.event.EventBus;


/**
 * This implementation gets the user data from the sensors of the phone
 * <p/>
 * Created by Roc on 14/05/2015.
 */
public class PhoneSensorsInput extends MultiWiiValues implements SensorEventListener, UserInstructionsInput {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetic;

    private Context context;

    public PhoneSensorsInput(Context context) {
        this.context = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagnetic, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterListeners() {
        mSensorManager.unregisterListener(this);
    }


    float[] mGravity;
    float[] mGeomagnetic;
    float R[] = new float[9];
    float I[] = new float[9];
    float orientation[] = new float[3];

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            mGravity = event.values;
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            mGeomagnetic = event.values;
        if (mGravity != null && mGeomagnetic != null) {
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                SensorManager.getOrientation(R, orientation);
            }
        }

        setHeading(restrictAngle((int) Math.toDegrees((double) orientation[0])));
        setPitch(restrictAngle((int) Math.toDegrees((double) orientation[2])));
        setRoll(restrictAngle((int) Math.toDegrees((double) orientation[1])));

        EventBus.getDefault().post(new PhoneSensorsData(heading, pitch, roll));
    }


    private int restrictAngle(int tmpAngle) {
        while (tmpAngle >= 180) tmpAngle -= 360;
        while (tmpAngle < -180) tmpAngle += 360;
        return tmpAngle;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public double getThrottle() {
        return ScreenThrottleData.instance.getThrottle();
    }

    @Override
    public double getHeading() {
        return heading;
    }

    @Override
    public double getPitch() {
        double average = CalibrationDatabase.getPhoneCalibrationData(context).getAverageMaxPitch();
        double relative = pitch * RCSignals.RC_MID_GAP / average;
        double absolute = relative + RCSignals.RC_MID;
        return absolute;
    }

    @Override
    public double getRoll() {
        double average = CalibrationDatabase.getPhoneCalibrationData(context).getAverageMaxRoll();
        double relative = roll * RCSignals.RC_MID_GAP / average;
        double absolute = relative + RCSignals.RC_MID;
        return absolute;
    }
}
