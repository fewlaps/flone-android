package com.fewlaps.flone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fewlaps.flone.R;
import com.fewlaps.flone.io.bean.ArmedDataChangeRequest;
import com.fewlaps.flone.io.bean.ActualArmedData;
import com.fewlaps.flone.io.bean.DroneSensorData;
import com.fewlaps.flone.io.input.phone.PhoneInputData;
import com.fewlaps.flone.io.input.phone.PhoneOutputData;
import com.fewlaps.flone.io.input.phone.ScreenThrottleData;
import com.fewlaps.flone.service.DroneService;
import com.squareup.phrase.Phrase;

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

    private View throttleBackgroundV;
    private View throttleRL;
    private View throttleTouchableRL;
    private View throttleControlLL;
    private TextView throttleControlPercentageTV;
    private View disarmedLayout;

    private SeekBar phoneHeading;
    private SeekBar phonePitch;
    private SeekBar phoneRoll;
    private SeekBar droneHeading;
    private SeekBar dronePitch;
    private SeekBar droneRoll;
    private SeekBar dataSentYaw;
    private SeekBar dataSentPitch;
    private SeekBar dataSentRoll;

    private boolean armed = DroneService.ARMED_DEFAULT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fly);

        throttleBackgroundV = findViewById(R.id.v_throttle_background);
        throttleRL = findViewById(R.id.rl_throttle);
        throttleTouchableRL = findViewById(R.id.rl_throttle_touchable);
        throttleControlLL = findViewById(R.id.ll_throttle_control);
        throttleControlPercentageTV = (TextView) findViewById(R.id.tv_throttle_control_percentage);
        disarmedLayout = findViewById(R.id.root_disarmed);

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

        throttleBackgroundV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ScreenThrottleData.instance.setThrottle((int) event.getY());

                double y = ScreenThrottleData.instance.getThrottleScreenPosition() - throttleControlLL.getHeight() + throttleTouchableRL.getPaddingTop();
                throttleControlLL.setY((int) y);

                updateThrottleLabel(ScreenThrottleData.instance.getThrottlePorcentage());

                return true;
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ScreenThrottleData.instance.setScreenHeight(throttleBackgroundV.getHeight());
                throttleRL.startAnimation(AnimationUtils.loadAnimation(FlyActivity.this, android.R.anim.fade_in));
                throttleRL.setVisibility(View.VISIBLE);
            }
        }, 100);

        updateThrottleLabel(0);

        EventBus.getDefault().post(DroneService.ACTION_GET_ARMED);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);

        updateArmedLayer();
    }

    private void updateArmedLayer() {
        if (armed) {
            disarmedLayout.setVisibility(View.INVISIBLE);
        } else {
            disarmedLayout.setVisibility(View.VISIBLE);
        }
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
    }

    public void onEventMainThread(PhoneOutputData data) {
        dataSentYaw.setProgress((int) data.getHeading() + 180);
        dataSentPitch.setProgress((int) data.getPitch() - 1000);
        dataSentRoll.setProgress((int) data.getRoll() - 1000);
    }

    public void onEventMainThread(ActualArmedData armed) {
        this.armed = armed.isArmed();
        updateArmedLayer();
    }

    private void updateThrottleLabel(int throttlePorcentage) {
        CharSequence formatted = Phrase.from(getString(R.string.trottle_now)).put("value", throttlePorcentage).format();
        throttleControlPercentageTV.setText(formatted);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
            EventBus.getDefault().post(new ArmedDataChangeRequest(true));
        } else if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
            EventBus.getDefault().post(new ArmedDataChangeRequest(false));
        }
        return true;
    }
}
