package com.fewlaps.flone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

import com.fewlaps.flone.R;
import com.fewlaps.flone.io.bean.DroneSensorData;
import com.fewlaps.flone.io.input.phone.PhoneInputData;
import com.fewlaps.flone.io.input.phone.PhoneOutputData;
import com.fewlaps.flone.io.input.phone.ScreenThrottleData;
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

    private View throttleV;

    private SeekBar phoneHeading;
    private SeekBar phonePitch;
    private SeekBar phoneRoll;
    private SeekBar droneHeading;
    private SeekBar dronePitch;
    private SeekBar droneRoll;
    private SeekBar dataSentYaw;
    private SeekBar dataSentPitch;
    private SeekBar dataSentRoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fly);

        throttleV = findViewById(R.id.rl_throttle);

        phoneHeading = (SeekBar) findViewById(R.id.sb_phone_heading);
        phonePitch = (SeekBar) findViewById(R.id.sb_phone_pitch);
        phoneRoll = (SeekBar) findViewById(R.id.sb_phone_roll);
        droneHeading = (SeekBar) findViewById(R.id.sb_drone_heading);
        dronePitch = (SeekBar) findViewById(R.id.sb_drone_pitch);
        droneRoll = (SeekBar) findViewById(R.id.sb_drone_roll);
        dataSentYaw = (SeekBar) findViewById(R.id.sb_data_sent_yaw);
        dataSentPitch = (SeekBar) findViewById(R.id.sb_data_sent_pitch);
        dataSentRoll = (SeekBar) findViewById(R.id.sb_data_sent_roll);

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

        throttleV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ScreenThrottleData.instance.setThrottle((int) event.getY());
                return true;
            }
        });
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                ScreenThrottleData.instance.setScreenHeight(throttleV.getHeight());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fly_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_calibration:
                startActivity(new Intent(this, CalibrationActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onEventMainThread(DroneSensorData data) {
        droneHeading.setProgress((int) data.getHeading() + 180);
        dronePitch.setProgress((int) data.getPitch() + 180);
        droneRoll.setProgress((int) data.getRoll() + 180);
    }

    public void onEventMainThread(PhoneInputData data) {
        phoneHeading.setProgress((int) data.getHeading() + 180);
        phonePitch.setProgress((int) data.getPitch() + 180);
        phoneRoll.setProgress((int) data.getRoll() + 180);

//        phoneSensorsSB.append(getString(R.string.throttle) + ": " + ScreenThrottleData.instance.getThrottle() + "\n");
    }

    public void onEventMainThread(PhoneOutputData data) {
        dataSentYaw.setProgress((int) data.getHeading() + 180);
        dataSentPitch.setProgress((int) data.getPitch() - 1000);
        dataSentRoll.setProgress((int) data.getRoll() - 1000);
    }
}
