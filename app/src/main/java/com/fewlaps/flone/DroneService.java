package com.fewlaps.flone;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import de.greenrobot.event.EventBus;

/**
 * This Sercive is the responsable of maintaing a connection with the Drone
 *
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 20150215
 */
public class DroneService extends Service {

    public boolean connected; //it's connected or trying to connect the Drone

    private final IBinder serviceBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    public class LocalBinder extends Binder {
        public DroneService getService() {
            return DroneService.this;
        }
    }

    public static String ACTION_CONNECT = "connect";
    public static String ACTION_DISCONNECT = "disconnect";

    private EventBus bus;

    @Override
    public void onCreate() {
        Log.i("SERVICE", "onCreate");
        bus = EventBus.getDefault();
        bus.register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("SERVICE", "onStartCommand");
        startForeground(NotificationUtil.KEY_FOREGROUND_NOTIFICATION, NotificationUtil.getForegroundServiceNotification(this));
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
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
