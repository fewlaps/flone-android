package com.fewlaps.flone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.IntentCompat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.SeekBar;
import android.widget.TextView;

import com.fewlaps.flone.R;
import com.fewlaps.flone.data.CalibrationDatabase;
import com.fewlaps.flone.data.KnownDronesDatabase;
import com.fewlaps.flone.data.bean.Drone;
import com.fewlaps.flone.data.bean.DroneCalibrationData;
import com.fewlaps.flone.data.bean.PhoneSensorsData;
import com.fewlaps.flone.io.bean.ActualArmedData;
import com.fewlaps.flone.io.bean.ArmedDataChangeRequest;
import com.fewlaps.flone.io.bean.DelayData;
import com.fewlaps.flone.io.bean.DroneSensorData;
import com.fewlaps.flone.io.bean.UserTouchModeChangedEvent;
import com.fewlaps.flone.io.communication.RCSignals;
import com.fewlaps.flone.io.input.phone.PhoneOutputData;
import com.fewlaps.flone.io.input.phone.ScreenThrottleData;
import com.fewlaps.flone.service.DroneService;
import com.fewlaps.flone.view.dialog.SendRawDataDialog;
import com.squareup.phrase.Phrase;

import de.greenrobot.event.EventBus;

/**
 * About the flow: When the user is in this Activity, it means that it is (or wants to be)
 * connected to a Drone. So, this Activity is the responsable of launching the Service to
 * connect to the Drone when the user is in it.
 * <p/>
 * Also, it is the responsable of saying: "are you sure you want to disconnect of the Drone?"
 * when the user taps the back button.
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

    private SeekBar delaySeekBar;
    private TextView delayTV;

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

        delaySeekBar = (SeekBar) findViewById(R.id.sb_delay);
        delayTV = (TextView) findViewById(R.id.tv_delay);

        throttleBackgroundV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    ScreenThrottleData.instance.setThrottle((int) event.getY());
                    EventBus.getDefault().post(new UserTouchModeChangedEvent(true));
                } else if (action == MotionEvent.ACTION_MOVE) {
                    ScreenThrottleData.instance.setThrottle((int) event.getY());
                } else if (action == MotionEvent.ACTION_UP) {
                    if (ScreenThrottleData.instance.getThrottlePorcentage() < 90) {
                        if (ScreenThrottleData.instance.getThrottlePorcentage() < 10) {
                            ScreenThrottleData.instance.setThrottleAtZero();
                        } else {
                            ScreenThrottleData.instance.setThrottleAtMid();
                        }
                    }
                    EventBus.getDefault().post(new UserTouchModeChangedEvent(false));
                }

                updateThrottleLabel();

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

        updateThrottleLabel();

        EventBus.getDefault().post(DroneService.ACTION_GET_ARMED);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);

        updateArmedLayer();

        if (!armed) {
            setThrottleToZero();
        }
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
            case R.id.action_preferences:
                startActivity(new Intent(this, PreferenceActivity.class));
                return true;
            case R.id.action_send_raw_data:
                SendRawDataDialog.showDialog(this);
                return true;
            case R.id.action_disconnect:
                shutDownAndQuitActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onEventMainThread(DroneSensorData data) {
        Drone drone = KnownDronesDatabase.getSelectedDrone(this);
        DroneCalibrationData dcd = CalibrationDatabase.getDroneCalibrationData(this, drone.nickName);

        droneHeading.setProgress(((int) data.getHeading() + 180 + 360 + (int) dcd.getHeadingDifference()) % 360);
        dronePitch.setProgress((int) data.getPitch() + 180);
        droneRoll.setProgress((int) data.getRoll() + 180);
    }

    public void onEventMainThread(PhoneSensorsData data) {
        phoneHeading.setProgress((int) data.getHeading() + 180);
        phonePitch.setProgress((int) data.getPitch() + 180);
        phoneRoll.setProgress((int) data.getRoll() + 180);
    }

    public void onEventMainThread(PhoneOutputData data) {
        dataSentYaw.setProgress((int) data.getHeading() - 1000);
        dataSentPitch.setProgress((int) data.getPitch() - 1000);
        dataSentRoll.setProgress((int) data.getRoll() - 1000);
    }

    public void onEventMainThread(ActualArmedData armed) {
        this.armed = armed.isArmed();
        updateArmedLayer();
    }

    public void onEventMainThread(DelayData delay) {
        delaySeekBar.setProgress(delay.delay);
        delayTV.setText(delay.delay + " ms");
    }

    private void updateThrottleLabel() {
        double y = ScreenThrottleData.instance.getThrottleScreenPosition() - throttleControlLL.getHeight() + throttleTouchableRL.getPaddingTop();
        throttleControlLL.setY((int) y);

        CharSequence formatted = Phrase.from(getString(R.string.trottle_now)).put("value", ScreenThrottleData.instance.getThrottlePorcentage()).format();
        throttleControlPercentageTV.setText(formatted);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((keyCode == KeyEvent.KEYCODE_VOLUME_UP)) {
                EventBus.getDefault().post(new ArmedDataChangeRequest(true));
                setThrottleToZero();
            } else if ((keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)) {
                EventBus.getDefault().post(new ArmedDataChangeRequest(false));
                setThrottleToZero();
            }
        }
        return true;
    }

    private void shutDownAndQuitActivity() {
        EventBus.getDefault().post(new ArmedDataChangeRequest(false));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(DroneService.ACTION_DISCONNECT);
                Intent i = new Intent(FlyActivity.this, DronesListActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(i);
            }
        }, 1000);
    }

    private void setThrottleToZero() {
        ScreenThrottleData.instance.setThrottle(RCSignals.RC_MIN);
        updateThrottleLabel();
    }
}
