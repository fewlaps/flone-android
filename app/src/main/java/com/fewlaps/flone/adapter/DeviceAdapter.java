package com.fewlaps.flone.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fewlaps.flone.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DeviceAdapter extends ArrayAdapter<BluetoothDevice> {

    /**
     * ViewHolder class for layout.<br />
     * <br />
     * Auto-created on 2015-02-19 01:18:55 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private static class ViewHolder {
        public final LinearLayout rootView;
        public final TextView tvName;
        public final TextView tvAddress;

        private ViewHolder(LinearLayout rootView, TextView tvName, TextView tvAddress) {
            this.rootView = rootView;
            this.tvName = tvName;
            this.tvAddress = tvAddress;
        }

        public static ViewHolder create(LinearLayout rootView) {
            TextView tvName = (TextView) rootView.findViewById(R.id.tv_name);
            TextView tvMac = (TextView) rootView.findViewById(R.id.tv_address);
            return new ViewHolder(rootView, tvName, tvMac);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.item_device, parent, false);
            vh = ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        BluetoothDevice item = getItem(position);

        vh.tvName.setText(item.getName());
        vh.tvAddress.setText(item.getAddress());

        return vh.rootView;
    }

    private LayoutInflater mInflater;

    // Constructors
    public DeviceAdapter(Context context, List<BluetoothDevice> objects) {
        super(context, 0, objects);
        this.mInflater = LayoutInflater.from(context);
    }

    public DeviceAdapter(Context context, BluetoothDevice[] objects) {
        super(context, 0, objects);
        this.mInflater = LayoutInflater.from(context);
    }
}
