package com.fewlaps.flone.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fewlaps.flone.R;

public class CalibrateDroneSensorsFragment extends Fragment {

    public static CalibrateDroneSensorsFragment newInstance() {
        return new CalibrateDroneSensorsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calibrate_drone_sensors, container, false);
        return view;
    }
} 