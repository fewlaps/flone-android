package com.fewlaps.flone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.fewlaps.flone.R;
import com.fewlaps.flone.io.bean.DroneSensorData;
import com.fewlaps.flone.io.input.phone.PhoneInputData;
import com.fewlaps.flone.io.input.phone.ScreenThrottleData;
import com.fewlaps.flone.service.DroneService;

import de.greenrobot.event.EventBus;

/**
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 20150523
 */
public class CalibratePhoneActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibrate_phone);
    }
}
