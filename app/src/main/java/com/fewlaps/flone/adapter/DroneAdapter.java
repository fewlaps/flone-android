package com.fewlaps.flone.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fewlaps.flone.R;
import com.fewlaps.flone.data.bean.Drone;

import java.util.List;

public class DroneAdapter extends ArrayAdapter<Drone> {

    /**
     * ViewHolder class for layout.<br />
     * <br />
     * Auto-created on 2015-02-19 01:54:23 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private static class ViewHolder {
        public final LinearLayout rootView;
        public final TextView tvNickname;
        public final TextView tvName;
        public final TextView tvAddress;

        private ViewHolder(LinearLayout rootView, TextView tvNickname, TextView tvName, TextView tvAddress) {
            this.rootView = rootView;
            this.tvNickname = tvNickname;
            this.tvName = tvName;
            this.tvAddress = tvAddress;
        }

        public static ViewHolder create(LinearLayout rootView) {
            TextView tvNickname = (TextView) rootView.findViewById(R.id.tv_nickname);
            TextView tvName = (TextView) rootView.findViewById(R.id.tv_name);
            TextView tvAddress = (TextView) rootView.findViewById(R.id.tv_address);
            return new ViewHolder(rootView, tvNickname, tvName, tvAddress);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.item_drone, parent, false);
            vh = ViewHolder.create((LinearLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Drone item = getItem(position);

        vh.tvNickname.setText(item.nickName);
        vh.tvName.setText(item.deviceName);
        vh.tvAddress.setText(item.address);

        return vh.rootView;
    }

    private LayoutInflater mInflater;

    // Constructors
    public DroneAdapter(Context context, List<Drone> objects) {
        super(context, 0, objects);
        this.mInflater = LayoutInflater.from(context);
    }

    public DroneAdapter(Context context, Drone[] objects) {
        super(context, 0, objects);
        this.mInflater = LayoutInflater.from(context);
    }
}
