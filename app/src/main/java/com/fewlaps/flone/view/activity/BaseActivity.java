package com.fewlaps.flone.view.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.fewlaps.flone.util.BluetoothUtil;

/**
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 19/02/2015
 */
public class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BluetoothUtil.startBluetooth();
    }
}
