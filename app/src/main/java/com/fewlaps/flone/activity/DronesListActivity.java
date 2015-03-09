package com.fewlaps.flone.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fewlaps.flone.R;
import com.fewlaps.flone.adapter.DroneAdapter;
import com.fewlaps.flone.data.Database;
import com.fewlaps.flone.data.bean.Drone;

import java.util.List;

/**
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 19/02/2015
 */
public class DronesListActivity extends BaseActivity {

    private static final int ADD_DRONE_REQUEST = 42;

    private ListView listView = null;
    private View listContainer = null;
    private View zeroCase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drones_list);

        listView = (ListView) findViewById(R.id.lv_drones);
        listContainer = findViewById(R.id.l_container);
        zeroCase = findViewById(R.id.z_drones);

        findViewById(R.id.bt_add_drone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(DronesListActivity.this, AddDroneActivity.class), ADD_DRONE_REQUEST);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Drone> drones = Database.getDrones(DronesListActivity.this);
                Drone selectedDrone = drones.get(position);
                Database.setSelectedDrone(DronesListActivity.this, selectedDrone);
                startActivity(new Intent(DronesListActivity.this, FlyActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<Drone> drones = Database.getDrones(this);
        if (drones.isEmpty()) {
            listView.setVisibility(View.INVISIBLE);
            zeroCase.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            zeroCase.setVisibility(View.INVISIBLE);

            listView.setAdapter(new DroneAdapter(this, drones));
        }
    }
}
