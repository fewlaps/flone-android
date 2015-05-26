package com.fewlaps.flone.io.input.phone;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.fewlaps.flone.data.CalibrationDatabase;
import com.fewlaps.flone.io.communication.RCSignals;
import com.fewlaps.flone.io.input.UserInstructionsInput;

import de.greenrobot.event.EventBus;
import hugo.weaving.DebugLog;


/**
 * This implementation gets the user data from the sensors of the phone
 * <p/>
 * Created by Roc on 14/05/2015.
 */
public class PhoneInput implements SensorEventListener, UserInstructionsInput {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private Sensor mMagnetic;

    private float[] mValuesMagnet = new float[3];
    private float[] mValuesAccel = new float[3];
    private float[] mValuesOrientation = new float[3];
    private float[] mRotationMatrix = new float[9];

    PhoneInputData inputData = new PhoneInputData();
    private Context context;

    public PhoneInput(Context context) {
        this.context = context;
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagnetic = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mMagnetic, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // Handle the events for which we registered
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                System.arraycopy(sensorEvent.values, 0, mValuesAccel, 0, 3);
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                System.arraycopy(sensorEvent.values, 0, mValuesMagnet, 0, 3);
                break;
        }
        SensorManager.getRotationMatrix(mRotationMatrix, null, mValuesAccel, mValuesMagnet);
        SensorManager.getOrientation(mRotationMatrix, mValuesOrientation);

        inputData.setHeading(restrictAngle((int) Math.toDegrees((double) mValuesOrientation[0])));
        inputData.setPitch(restrictAngle((int) Math.toDegrees((double) mValuesOrientation[1])));
        inputData.setRoll(restrictAngle((int) Math.toDegrees((double) mValuesOrientation[2])));
        EventBus.getDefault().post(inputData);
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
    public int getThrottle() {
        return ScreenThrottleData.instance.getThrottle();
    }

    @Override
    public double getHeading() {
        return inputData.getHeading();
    }

    @DebugLog
    @Override
    public double getPitch() {
        double value = inputData.getPitch();
        double average = CalibrationDatabase.getPhoneCalibrationData(context).getAverageMaxPitch();
        double relative = value * RCSignals.RC_MID_GAP / average;
        double absolute = relative + RCSignals.RC_MID;
        return absolute;
    }

    @DebugLog
    @Override
    public double getRoll() {
        double value = inputData.getRoll();
        double average = CalibrationDatabase.getPhoneCalibrationData(context).getAverageMaxRoll();
        double relative = value * RCSignals.RC_MID_GAP / average;
        double absolute = relative + RCSignals.RC_MID;
        return absolute;
    }
}
