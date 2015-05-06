package com.fewlaps.flone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fewlaps.flone.R;
import com.fewlaps.flone.communication.bean.DroneSensorInformation;
import com.fewlaps.flone.service.DroneService;

import de.greenrobot.event.EventBus;

/**
 * About the flow: When the user is in this Activity, it means that it is (or wants to be)
 * connected to a Drone. So, this Activity is the responsable of launching the Service to
 * connect to the Drone when the user is in it.
 * <p/>
 * Also, it is the responsable of saying: "are you sure you want to disconnect of the Drone?"
 * when the user taps the back button.
 *
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 20150215
 */
public class FlyActivity extends BaseActivity {

    private TextView droneSensorsTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fly);

        droneSensorsTV = (TextView) findViewById(R.id.tv_drone_sensors);

        findViewById(R.id.bt_connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FlyActivity.this.startService(new Intent(FlyActivity.this, DroneService.class));
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

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    public void onEventMainThread(DroneSensorInformation droneSensorInformation) {
        Log.d("SENSORS", droneSensorInformation.toString());
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.axis_yaw) + ": " + droneSensorInformation.getYaw() + "\n");
        sb.append(getString(R.string.axis_pitch) + ": " + droneSensorInformation.getPitch() + "\n");
        sb.append(getString(R.string.axis_roll) + ": " + droneSensorInformation.getRoll() + "\n");
        droneSensorsTV.setText(sb.toString());
    }
}
