package com.fewlaps.flone;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import de.greenrobot.event.EventBus;

/**
 * The boring part of common Services
 *
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 20150215
 */
public class BaseService extends Service {

    private final IBinder serviceBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return serviceBinder;
    }

    public class LocalBinder extends Binder {
        public BaseService getService() {
            return BaseService.this;
        }
    }

    private EventBus bus;

    @Override
    public void onCreate() {
        Log.i("SERVICE", "onCreate");
        bus = EventBus.getDefault();
        bus.register(this);
    }

    @Override
    public void onDestroy() {
        bus.unregister(this);
        super.onDestroy();
    }
}
