package com.fewlaps.flone.service;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.fewlaps.flone.DesiredYawCalculator;
import com.fewlaps.flone.data.KnownDronesDatabase;
import com.fewlaps.flone.data.bean.Drone;
import com.fewlaps.flone.io.bean.ActualArmedData;
import com.fewlaps.flone.io.bean.ArmedDataChangeRequest;
import com.fewlaps.flone.io.bean.DroneConnectionStatusChanged;
import com.fewlaps.flone.io.bean.DroneSensorData;
import com.fewlaps.flone.io.bean.MultiWiiValues;
import com.fewlaps.flone.io.bean.UsingRawDataChangeRequest;
import com.fewlaps.flone.io.communication.Bluetooth;
import com.fewlaps.flone.io.communication.Communication;
import com.fewlaps.flone.io.communication.RCSignals;
import com.fewlaps.flone.io.communication.protocol.MultiWii230;
import com.fewlaps.flone.io.communication.protocol.MultirotorData;
import com.fewlaps.flone.io.input.phone.PhoneOutputData;
import com.fewlaps.flone.io.input.phone.PhoneSensorsInput;
import com.fewlaps.flone.io.input.phone.RawDataInput;
import com.fewlaps.flone.util.NotificationUtil;

import de.greenrobot.event.EventBus;

/**
 * This Sercive is the responsable of maintaining a connection with the Drone, asking for data, and sending data
 *
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 15/02/2015
 */
public class DroneService extends BaseService {
    public static final boolean ARMED_DEFAULT = false;
    public static final boolean USING_RAW_DATA_DEFAULT = false;

    public static final Object ACTION_GET_ARMED = "getArmed";
    public static final String ACTION_CONNECT = "connect";
    public static final String ACTION_DISCONNECT = "disconnect";

    private boolean armed = ARMED_DEFAULT;
    private boolean usingRawData = USING_RAW_DATA_DEFAULT;

    private static final int BAUD_RATE = 115200; //The baud rate where the BT works

    private static final int DELAY_RECONNECT = 2000; //The time the reconnect task will wait between launches
    private static final int COMMAND_TIMEOUT = 1000; //The time we consider that was "too time ago for being connected"

    public Communication communication;
    public MultirotorData protocol;

    public boolean running = false;

    private Handler connectTask = new Handler();

    private long lastDroneAnswerReceived = 0;

    private PhoneSensorsInput phoneSensorsInput;
    private DroneSensorData droneInput;

    private PhoneOutputData phoneOutputData = new PhoneOutputData();

    public static MultiWiiValues valuesSent = new MultiWiiValues(); //Created at startup, never changed, never destroyed, totally reused at every request
    public static final RCSignals rc = new RCSignals(); //Created at startup, never changed, never destroyed, totally reused at every request
    DesiredYawCalculator yawCalculator = new DesiredYawCalculator();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        communication = new Bluetooth(getApplicationContext());
        protocol = new MultiWii230(communication);

        startForeground(NotificationUtil.KEY_FOREGROUND_NOTIFICATION, NotificationUtil.getForegroundServiceNotification(this));

        onEventMainThread(ACTION_CONNECT);

        if (phoneSensorsInput == null) {
            phoneSensorsInput = new PhoneSensorsInput(this);
        }

        return START_NOT_STICKY;
    }

    public void onEventMainThread(String action) {
        Log.d("SERVICE", "Action received: " + action);

        if (action.equals(ACTION_CONNECT)) {
            running = true;
            reconnectRunnable.run();
        } else if (action.equals(ACTION_DISCONNECT)) {
            phoneSensorsInput.unregisterListeners();
            communication.close();
            running = false;
            stopForeground(true);
            stopSelf();
        } else if (action.equals(ACTION_GET_ARMED)) {
            EventBus.getDefault().post(new ActualArmedData(armed));
        }
    }

    public void onEventMainThread(DroneConnectionStatusChanged status) {
        if (status.isConnected()) {
            protocol.SendRequestMSP_ATTITUDE();
        }
    }

    public void onEventMainThread(DroneSensorData droneSensorData) {
        droneInput = droneSensorData;

        protocol.SendRequestMSP_ATTITUDE();
        lastDroneAnswerReceived = System.currentTimeMillis();

        updateRcWithInputData();
        EventBus.getDefault().post(phoneOutputData);

        if (valuesSent.isDifferentThanRcValues(rc)) {
            Log.d("DATASENT", rc.toString());
            protocol.sendRequestMSP_SET_RAW_RC(rc.get());
            valuesSent.update(rc);
        }
    }

    public void onEventMainThread(ArmedDataChangeRequest request) {
        armed = request.isArmed();
        EventBus.getDefault().post(new ActualArmedData(armed));
    }

    public void onEventMainThread(UsingRawDataChangeRequest request) {
        usingRawData = request.isUsingRawData();
    }

    private final Runnable reconnectRunnable = new Runnable() {
        public void run() {
            if (running) {
                if (!communication.Connected) {
                    Drone selectedDrone = KnownDronesDatabase.getSelectedDrone(DroneService.this);
                    if (selectedDrone != null) {
                        protocol.Connect(selectedDrone.address, BAUD_RATE, 0);
                    }
                } else {
                    if (lastDroneAnswerReceived < System.currentTimeMillis() - COMMAND_TIMEOUT) {
                        protocol.SendRequestMSP_ATTITUDE(); //Requesting the attitude, in order to make the connection fail
                    }
                }
                connectTask.postDelayed(reconnectRunnable, DELAY_RECONNECT);
            }
        }
    };

    /**
     * Sets the RC data, using the userInput and the droneInput. It's a common task
     * to do before sending the RC to the drone, to make it fly as the user excepts
     */
    private void updateRcWithInputData() {
        if (usingRawData) {
            rc.setThrottle((int) RawDataInput.instance.getThrottle());
            rc.setRoll((int) RawDataInput.instance.getRoll());
            rc.setPitch((int) RawDataInput.instance.getPitch());
            rc.setYaw((int) RawDataInput.instance.getHeading());
            rc.set(RCSignals.AUX1, (int) RawDataInput.instance.getAux1());
            rc.set(RCSignals.AUX1, (int) RawDataInput.instance.getAux2());
            rc.set(RCSignals.AUX1, (int) RawDataInput.instance.getAux3());
            rc.set(RCSignals.AUX1, (int) RawDataInput.instance.getAux4());
        } else {
            int yaw;
            int pitch;
            int roll;

            if (armed) {
                rc.set(RCSignals.AUX1, RCSignals.RC_MAX);
                rc.setThrottle((int) phoneSensorsInput.getThrottle());

                //yaw = (int) yawCalculator.getYaw(droneInput.getHeading(), phoneSensorsInput.getHeading()); //while having the compass issue, sending 1500 to the board
                yaw = RCSignals.RC_MID;
                pitch = (int) phoneSensorsInput.getPitch();
                roll = (int) phoneSensorsInput.getRoll();
            } else {
                rc.set(RCSignals.AUX1, RCSignals.RC_MIN);
                rc.setThrottle(RCSignals.RC_MIN);

                yaw = RCSignals.RC_MID;
                pitch = RCSignals.RC_MID;
                roll = RCSignals.RC_MID;
            }

            rc.setYaw(yaw);
            rc.setRoll(pitch);
            rc.setPitch(roll);

            phoneOutputData.update(yaw, pitch, roll);
        }
    }
}
