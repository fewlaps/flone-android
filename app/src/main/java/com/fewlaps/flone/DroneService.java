package com.fewlaps.flone;

import android.content.Intent;
import android.util.Log;

/**
 * This Sercive is the responsable of maintaing a connection with the Drone
 *
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 20150215
 */
public class DroneService extends BaseService {

    public boolean connected; //it's connected or trying to connect the Drone

    public static String ACTION_CONNECT = "connect";
    public static String ACTION_DISCONNECT = "disconnect";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("SERVICE", "onStartCommand");
        startForeground(NotificationUtil.KEY_FOREGROUND_NOTIFICATION, NotificationUtil.getForegroundServiceNotification(this));
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
}
