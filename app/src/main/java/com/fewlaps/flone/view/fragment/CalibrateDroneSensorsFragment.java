package com.fewlaps.flone.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.fewlaps.flone.R;
import com.fewlaps.flone.io.bean.DroneSensorData;

import de.greenrobot.event.EventBus;

public class CalibrateDroneSensorsFragment extends Fragment {

    public static CalibrateDroneSensorsFragment newInstance() {
        return new CalibrateDroneSensorsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    SeekBar rollSB;
    SeekBar pitchSB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calibrate_drone_sensors, container, false);

        rollSB = (SeekBar) view.findViewById(R.id.sb_roll);
        pitchSB = (SeekBar) view.findViewById(R.id.sb_pitch);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    public void onEventMainThread(DroneSensorData data) {
        updateScreen(data.getPitch(), data.getRoll());
    }

    private void updateScreen(double pitch, double roll) {
        rollSB.setProgress((int) (roll + 100));
        pitchSB.setProgress((int) (pitch + 100));
    }
} 