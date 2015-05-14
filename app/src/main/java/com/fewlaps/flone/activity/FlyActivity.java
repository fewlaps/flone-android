package com.fewlaps.flone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fewlaps.flone.R;
import com.fewlaps.flone.communication.bean.DroneSensorInformation;
import com.fewlaps.flone.communication.bean.PhoneSensorInformation;
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
    private TextView phoneSensorsTV;

    StringBuilder droneSensorsSB = new StringBuilder();
    StringBuilder phoneSensorsSB = new StringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fly);

        droneSensorsTV = (TextView) findViewById(R.id.tv_drone_sensors);
        phoneSensorsTV = (TextView) findViewById(R.id.tv_phone_sensors);

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
        droneSensorsSB.setLength(0);
        droneSensorsSB.append(getString(R.string.axis_heading) + ": " + droneSensorInformation.getHeading() + "\n");
        droneSensorsSB.append(getString(R.string.axis_pitch) + ": " + droneSensorInformation.getPitch() + "\n");
        droneSensorsSB.append(getString(R.string.axis_roll) + ": " + droneSensorInformation.getRoll() + "\n");
        droneSensorsTV.setText(droneSensorsSB.toString());
    }

    public void onEventMainThread(PhoneSensorInformation phoneSensorInformation) {
        phoneSensorsSB.setLength(0);
        phoneSensorsSB.append("Phone:\n");
        phoneSensorsSB.append(getString(R.string.axis_heading) + ": " + phoneSensorInformation.getHeading() + "\n");
        phoneSensorsSB.append(getString(R.string.axis_pitch) + ": " + phoneSensorInformation.getPitch() + "\n");
        phoneSensorsSB.append(getString(R.string.axis_roll) + ": " + phoneSensorInformation.getRoll() + "\n");
        phoneSensorsTV.setText(phoneSensorsSB.toString());
    }
}
