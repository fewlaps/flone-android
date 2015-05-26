package com.fewlaps.flone.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.fewlaps.flone.R;
import com.fewlaps.flone.view.fragment.CalibrateDroneSensorsFragment;
import com.fewlaps.flone.view.fragment.CalibratePhoneSensorsFragment;
import com.fewlaps.flone.view.fragment.CalibrateSyncPhoneAndDroneFragment;

/**
 * @author Roc Boronat (roc@fewlaps.com)
 * @date 20150523
 */
public class CalibrationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);

        ViewPager pager = (ViewPager) findViewById(R.id.vp_root);
        CalibratePagerAdapter adapter = new CalibratePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
    }

    public class CalibratePagerAdapter extends FragmentPagerAdapter {
        private int NUM_ITEMS = 3;

        public CalibratePagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return CalibratePhoneSensorsFragment.newInstance();
                case 1:
                    return CalibrateDroneSensorsFragment.newInstance();
                case 2:
                    return CalibrateSyncPhoneAndDroneFragment.newInstance();
                default:
                    return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return CalibrationActivity.this.getString(R.string.calibrate_phone_sensors);
                case 1:
                    return CalibrationActivity.this.getString(R.string.calibrate_drone_sensors);
                case 2:
                    return CalibrationActivity.this.getString(R.string.calibrate_sync_phone_and_drone);
                default:
                    return null;
            }
        }
    }
}
