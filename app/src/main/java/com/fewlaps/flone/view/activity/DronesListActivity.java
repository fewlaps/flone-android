package com.fewlaps.flone.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fewlaps.flone.R;
import com.fewlaps.flone.data.KnownDronesDatabase;
import com.fewlaps.flone.data.bean.Drone;
import com.fewlaps.flone.view.adapter.DroneAdapter;
import com.fewlaps.flone.view.dialog.OkCancelDialog;
import com.fewlaps.flone.view.listener.OnSelectedOkDialogListener;

import java.util.List;

public class DronesListActivity extends BaseActivity implements OnSelectedOkDialogListener {

    private static final int ADD_DRONE_REQUEST = 42;

    private ListView listView = null;
    private Drone droneToRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drones_list);

        listView = (ListView) findViewById(R.id.lv_drones);
        listView.setEmptyView(findViewById(R.id.z_drones));

        findViewById(R.id.bt_add_drone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(DronesListActivity.this, AddDroneActivity.class), ADD_DRONE_REQUEST);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<Drone> drones = KnownDronesDatabase.getDrones(DronesListActivity.this);
                Drone selectedDrone = drones.get(position);
                KnownDronesDatabase.setSelectedDrone(DronesListActivity.this, selectedDrone);
                startActivity(new Intent(DronesListActivity.this, ConnectingActivity.class));
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                droneToRemove = KnownDronesDatabase.getDrones(DronesListActivity.this).get(position);
                OkCancelDialog.showDialog(DronesListActivity.this, String.format(getString(R.string.remove_drone_confirmation), droneToRemove.nickName));
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        List<Drone> drones = KnownDronesDatabase.getDrones(this);

        listView.setAdapter(new DroneAdapter(this, drones));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drone_list_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_preferences:
                startActivity(new Intent(this, PreferenceActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void OnSelectedOkDialogListener() {
        KnownDronesDatabase.removeDrone(DronesListActivity.this, droneToRemove);

        List<Drone> drones = KnownDronesDatabase.getDrones(DronesListActivity.this);
        listView.setAdapter(new DroneAdapter(DronesListActivity.this, drones));
    }
}
