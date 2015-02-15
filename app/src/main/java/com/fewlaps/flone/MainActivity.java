package com.fewlaps.flone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import de.greenrobot.event.EventBus;

/**
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 20150215
 */
public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.bt_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.startService(new Intent(MainActivity.this, DroneService.class));
                EventBus.getDefault().post(DroneService.ACTION_CONNECT);
            }
        });
        findViewById(R.id.bt_disconnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(DroneService.ACTION_DISCONNECT);
            }
        });
    }
}
