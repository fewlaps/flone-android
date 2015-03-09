package com.fewlaps.flone.service;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.fewlaps.flone.bluetooth.BluetoothCommunication;
import com.fewlaps.flone.data.Database;
import com.fewlaps.flone.data.bean.Drone;
import com.fewlaps.flone.util.NotificationUtil;

/**
 * This Sercive is the responsable of maintaing a connection with the Drone
 *
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 15/02/2015
 */
public class DroneService extends BaseService {
    private static final int DELAY_RECONNECT = 500;
    private static final int DELAY_READ = 100;

    BluetoothCommunication communication = null;

    public boolean connected; //it's connected or trying to connect the Drone

    public static String ACTION_CONNECT = "connect";
    public static String ACTION_DISCONNECT = "disconnect";

    private Handler connectTask = new Handler();
    private Handler readTask = new Handler();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("SERVICE", "onStartCommand");
        communication = new BluetoothCommunication(this);
        startForeground(NotificationUtil.KEY_FOREGROUND_NOTIFICATION, NotificationUtil.getForegroundServiceNotification(this));

        connectTask.postDelayed(reconnectRunnable, DELAY_RECONNECT);
        readTask.postDelayed(readRunnable, DELAY_READ);

        return START_NOT_STICKY;
    }

    public void onEventMainThread(String action) {
        Log.d("TEST", "banana");

        if (action.equals(ACTION_CONNECT)) {
            connected = true;
        } else if (action.equals(ACTION_DISCONNECT)) {
            connected = false;
            stopForeground(true);
            stopSelf();
        }
    }

    private final Runnable reconnectRunnable = new Runnable() {
        public void run() {
            if (!communication.connected) {
                Drone selectedDrone = Database.getSelectedDrone(DroneService.this);
                if (selectedDrone != null) {
                    communication.connect(selectedDrone.address);
                }
            }
            connectTask.postDelayed(reconnectRunnable, DELAY_RECONNECT);
        }

        ;
    };
    private final Runnable readRunnable = new Runnable() {
        public void run() {
            if (communication.connected) {
                communication.read();
            }
            connectTask.postDelayed(readRunnable, DELAY_READ);
        }

        ;
    };
}
