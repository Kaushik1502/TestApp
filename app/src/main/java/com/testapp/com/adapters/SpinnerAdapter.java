package com.testapp.com.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.testapp.R;

/**
 * Created by cws on 19/10/16.
 */
public class SpinnerAdapter extends BaseAdapter {
    Activity context;
    LayoutInflater inflter;
    ViewHolder holder = null;
    private int[] cities;

    public SpinnerAdapter(Activity context, int[] cities) {
        this.context = context;
        this.cities = cities;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return cities.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spinner_custom, null);
            holder = new ViewHolder();
            holder.names = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(holder);
        } else holder = (ViewHolder) convertView.getTag();
        holder.names.setText(cities[position]);
        return convertView;
    }

    public class ViewHolder {
        public TextView names;
    }
}