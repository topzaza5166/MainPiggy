package com.example.topza.piggy;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by topza on 6/6/2559.
 */
public class HistoryListAdapter extends BaseAdapter{


    @Override
    public int getCount() {
        return 10000;
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
        CustomViewGroupTemplate view;
        if(convertView != null)
            view =(CustomViewGroupTemplate) convertView;
        else
            view = new CustomViewGroupTemplate(parent.getContext());
        return view;
    }
}
