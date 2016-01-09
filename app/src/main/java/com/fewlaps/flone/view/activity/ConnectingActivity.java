package com.fewlaps.flone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.fewlaps.flone.R;
import com.fewlaps.flone.io.bean.DroneSensorData;
import com.fewlaps.flone.service.DroneService;

import net.frakbot.jumpingbeans.JumpingBeans;

import de.greenrobot.event.EventBus;

/**
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 20150907
 */
public class ConnectingActivity extends BaseActivity {

    public static final int JUMPING_BEANS_ANIMATION_START_DELAY = 500;
    private JumpingBeans jumpingBeans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connecting);

        ConnectingActivity.this.startService(new Intent(ConnectingActivity.this, DroneService.class));
        EventBus.getDefault().post(DroneService.ACTION_CONNECT);

        new Handler().postDelayed(new Runnable() { //To make a loving animation we start it after the Activity startup cycle
            @Override
            public void run() {
                jumpingBeans = JumpingBeans.with((TextView) findViewById(R.id.tv_connecting)).appendJumpingDots().build();
            }
        }, JUMPING_BEANS_ANIMATION_START_DELAY);
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
    public void onBackPressed() {
        EventBus.getDefault().post(DroneService.ACTION_DISCONNECT);
        jumpingBeans.stopJumping();
        finish();
    }

    public void onEventMainThread(DroneSensorData data) {
        if (data != null) { //Connected to the drone!
            startActivity(new Intent(this, FlyActivity.class));
            jumpingBeans.stopJumping();
            finish();
        }
    }
}
