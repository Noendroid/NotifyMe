package com.igal.notifyme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by igalh on 01-Aug-17.
 */

public class SettingsAdapter extends BaseAdapter {
    private LayoutInflater infltr;
    private ArrayList<View> items;
    public SettingsAdapter(Context c) {
        // this.list = list;
        infltr = LayoutInflater.from(c);
        items = Device.getIndividualsView(c);
    }

    @Override
    public int getCount() {
        return Device.getNumOfIndividuals();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = items.get(position);
        final View finalConvertView = convertView;
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switch toggle = (Switch) finalConvertView.findViewById(R.id.item_on_off);
                toggle.toggle();
            }
        });
        return convertView;
    }
}
