package com.example.topza.piggy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Chayenjr on 28/6/2559.
 */
public class DeviceAdapter extends BaseAdapter {

    Context mContext;
    String[] save_name;
    String[] device_name;
    public static int device_position;

    public DeviceAdapter(Context context, String[] save_name, String[] device_name) {
        this.mContext= context;
        this.save_name = save_name;
        this.device_name = device_name;
    }

    public int getCount() {
        return save_name.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = mInflater.inflate(R.layout.list_device, parent, false);

        TextView textView = (TextView)view.findViewById(R.id.CustomListSaveName);
        textView.setText(save_name[position]);

        TextView textView2 = (TextView)view.findViewById(R.id.CustomListTextDeviceName);
        textView2.setText(device_name[position]);

        device_position = position;
        return view;
    }
}
